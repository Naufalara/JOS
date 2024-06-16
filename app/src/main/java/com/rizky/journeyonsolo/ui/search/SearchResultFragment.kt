package com.rizky.journeyonsolo.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView
import com.rizky.journeyonsolo.adapter.DestinationAdapter
import com.rizky.journeyonsolo.data.Result
import com.rizky.journeyonsolo.data.remote.response.ListDestinationItem
import com.rizky.journeyonsolo.databinding.FragmentSearchResultBinding
import com.rizky.journeyonsolo.ui.ViewModelFactory

class SearchResultFragment : Fragment() {

    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!

    private lateinit var searchBar: SearchBar
    private lateinit var searchView: SearchView

    private val viewModel by viewModels<SearchResultViewModel> {
        ViewModelFactory.getInstance()
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

        searchBar = binding.searchBar
        searchView = binding.searchView
        val dataName = SearchResultFragmentArgs.fromBundle(arguments as Bundle).name

        setupUI()
        searchBar.setText(dataName)
        setDataFirstWhenGetArgument(dataName)
        getSearch()
    }

    private fun setDataFirstWhenGetArgument(text: String) {

        viewModel.searchDestination(text)
        viewModel.searchResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }

                is Result.Success -> {
                    showLoading(false)
                    val newData = result.data
                    if (newData.isNotEmpty()) {
                        showDataNull(false)
                        setListDestination(newData)
                    } else {
                        setListDestination(emptyList())
                        showDataNull(true)
                    }
                }

                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(
                        requireContext(),
                        "Terjadi kesalahan " + result.error,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    private fun getSearch() {
        this.searchView.setupWithSearchBar(this.searchBar)
        searchView
            .editText
            .setOnEditorActionListener { textView, _, _ ->
                searchBar.setText(textView.text)
                viewModel.searchDestination(textView.text.toString())
                viewModel.searchResult.observe(viewLifecycleOwner) { result ->
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }

                        is Result.Success -> {
                            showLoading(false)
                            val newData = result.data
                            if (newData.isNotEmpty()) {
                                showDataNull(false)
                                setListDestination(newData)
                            } else {
                                setListDestination(emptyList())
                                showDataNull(true)
                            }
                        }

                        is Result.Error -> {
                            showLoading(false)
                            Toast.makeText(
                                requireContext(),
                                "Terjadi kesalahan " + result.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                searchView.hide()
                false
            }
    }

    private fun setupUI() {
        binding.apply {
            searchView.setupWithSearchBar(searchBar)
            rvResult.adapter = destinationAdapter
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showDataNull(isDataNull: Boolean) {
        binding.tvListKosong.visibility = if (isDataNull) View.VISIBLE else View.GONE
    }

    private fun setListDestination(newData: List<ListDestinationItem>) {
        destinationAdapter.submitList(newData)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}