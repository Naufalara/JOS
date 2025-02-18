package com.jos.journeyonsolo.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.jos.journeyonsolo.adapter.DestinationAdapter
import com.jos.journeyonsolo.data.Result
import com.jos.journeyonsolo.data.remote.response.ListDestinationItem
import com.jos.journeyonsolo.databinding.FragmentSearchResultBinding
import com.jos.journeyonsolo.ui.ViewModelFactory

class SearchResultFragment : Fragment() {

    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<SearchResultViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private val destinationAdapter = DestinationAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataName = SearchResultFragmentArgs.fromBundle(arguments as Bundle).name

        setupUI()
        binding.searchBar.setText(dataName)
        performSearch(dataName)
        setupSearchListener()
    }

    private fun setupUI() {
        binding.rvResult.adapter = destinationAdapter
        binding.searchView.setupWithSearchBar(binding.searchBar)
    }

    private fun setupSearchListener() {
        binding.searchView.editText.setOnEditorActionListener { textView, _, _ ->
            val query = textView.text.toString().trim()
            binding.searchBar.setText(query)
            performSearch(query)
            binding.searchView.hide()
            false
        }
    }

    private fun performSearch(query: String) {
        viewModel.searchDestination(query, requireContext())
        observeSearchResult()
    }

    private fun observeSearchResult() {
        viewModel.searchResult.removeObservers(viewLifecycleOwner)
        viewModel.searchResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    showLoading(false)
                    updateDestinationList(result.data)
                }
                is Result.Error -> {
                    showLoading(false)
                    showToast(result.error)
                }
            }
        }
    }

    private fun updateDestinationList(data: List<ListDestinationItem>) {
        if (data.isEmpty()) {
            showDataNull(true)
        } else {
            showDataNull(false)
            destinationAdapter.submitList(data)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showDataNull(isDataNull: Boolean) {
        binding.tvListKosong.visibility = if (isDataNull) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), "Terjadi kesalahan: $message", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
