package com.rizky.journeyonsolo.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rizky.journeyonsolo.adapter.FavDestinationAdapter
import com.rizky.journeyonsolo.data.local.entity.FavoriteDestination
import com.rizky.journeyonsolo.databinding.FragmentFavoriteBinding
import com.rizky.journeyonsolo.ui.ViewModelFactory

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<FavoriteViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private val favDestinationAdapter = FavDestinationAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDataFirst()
        setupUI()
    }

    private fun getDataFirst() {

        viewModel.getFavDestination().observe(viewLifecycleOwner){
            showLoading(true)
            if (it.isNotEmpty()){
                showLoading(false)
                setListDestination(it)
            } else{
                showLoading(false)
                setListDestination(it)
            }
        }
    }

    private fun setupUI() {
        binding.apply {
            rvResult.adapter = favDestinationAdapter
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showDataNull(isDataNull: Boolean) {
        binding.tvListKosong.visibility = if (isDataNull) View.VISIBLE else View.GONE
    }

    private fun setListDestination(listFavDestination: List<FavoriteDestination>) {
        favDestinationAdapter.submitList(listFavDestination)
        showDataNull(listFavDestination.isEmpty())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}