package com.rizky.journeyonsolo.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView
import com.rizky.journeyonsolo.data.Result
import com.rizky.journeyonsolo.adapter.NearbyDestinationAdapter
import com.rizky.journeyonsolo.adapter.RecommendDestinationAdapter
import com.rizky.journeyonsolo.data.remote.response.ListDestinationItem
import com.rizky.journeyonsolo.databinding.FragmentHomeBinding
import com.rizky.journeyonsolo.ui.ViewModelFactory

class HomeFragment : Fragment() {

    companion object {
        private const val TAG = "HomeFragment"
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var searchBar: SearchBar
    private lateinit var searchView: SearchView

    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private val recommendDestinationAdapter = RecommendDestinationAdapter()
    private val nearbyDestinationAdapter = NearbyDestinationAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the SearchBar and SearchView
        searchBar = binding.searchBar
        searchView = binding.searchView

        getDataFirst()
        setupUI()
        getSearch()
    }

    private fun getDataFirst() {
        viewModel.getData().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        val newData = result.data
                        Log.d(TAG, "Cek data, $newData")
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
                            "Terjadi Kesalahan " + result.error,
                            Toast.LENGTH_SHORT).
                        show()
                    }
                }
            }

        }
    }

    private fun getSearch() {
        this.searchView.setupWithSearchBar(this.searchBar)
        searchView
            .editText
            .setOnEditorActionListener { textView, _, _ ->
                searchView.hide()
                val toSearchResultFragment = HomeFragmentDirections.actionNavigationHomeToSearchResultFragment()
                toSearchResultFragment.name = textView.text.toString()
                view?.findNavController()?.navigate(toSearchResultFragment)
                false
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUI() {
        binding.apply {
            searchView.setupWithSearchBar(searchBar)
            rvRecommend.adapter = recommendDestinationAdapter
            rvNearby.adapter = nearbyDestinationAdapter
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showDataNull(isDataNull: Boolean) {
        binding.tvListKosong.visibility = if (isDataNull) View.VISIBLE else View.GONE
    }

    private fun setListDestination(newData: List<ListDestinationItem>) {
        recommendDestinationAdapter.submitList(newData)
        nearbyDestinationAdapter.submitList(newData)
    }

    override fun onResume() {
        super.onResume()
        searchView.hide()
    }
}