package com.jos.journeyonsolo.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.jos.journeyonsolo.R
import com.jos.journeyonsolo.data.Result
import com.jos.journeyonsolo.data.local.entity.FavoriteDestination
import com.jos.journeyonsolo.databinding.FragmentDetailBinding
import com.jos.journeyonsolo.ui.ViewModelFactory

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val dataEntity = FavoriteDestination()

    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mengambil ID dari argument yang dikirimkan
        val id = DetailFragmentArgs.fromBundle(arguments as Bundle).id

        // Tombol kembali
        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Mengamati data dari ViewModel
        viewModel.getData(id).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true) // Menampilkan loading indicator
                }
                is Result.Success -> {
                    showLoading(false) // Menghilangkan loading indicator
                    val newData = result.data


                    // Memastikan dataEntity diisi dengan benar
                    dataEntity.placeId = newData.placeId
                    dataEntity.name = newData.name
                    dataEntity.imageUrl = newData.imageUrl
                    dataEntity.address = newData.address
                    dataEntity.rating = newData.rating

                    // Mengisi UI dengan data yang diterima
                    binding.tvItemName.text = newData.name
                    Glide.with(requireContext())
                        .load(newData.imageUrl)
                        .into(binding.imgItemPhoto)
                    binding.tvItemLocation.text = newData.address
                    binding.tvItemRating.text = newData.rating
                    binding.tvItemDescription.text = newData.captionIdn
                    Log.d("DetailFragment", "Data received: $newData")

                    // Navigasi ke Maps Fragment
                    binding.btnBukaMaps.setOnClickListener {
                        val toDetailMapsFragment = DetailFragmentDirections.actionDetailFragmentToDetailMapsFragment()

                        if (newData.lat.isNotEmpty() && newData.lon.isNotEmpty()) {
                            toDetailMapsFragment.name = newData.name
                            toDetailMapsFragment.latitude = newData.lat.toFloat()
                            toDetailMapsFragment.longitude = newData.lon.toFloat()
                            Log.d("DetailFragment", "Latitude: ${newData.lat}, Longitude: ${newData.lon}")

                            view.findNavController().navigate(toDetailMapsFragment)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Koordinat tidak valid atau tidak tersedia.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                }
                is Result.Error -> {
                    showLoading(false) // Menghilangkan loading indicator
                    Toast.makeText(
                        requireContext(),
                        "Terjadi Kesalahan: ${result.error}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // Mengamati status favorit
        viewModel.getIsFavorite(id).observe(viewLifecycleOwner) { isFavorite ->
            if (isFavorite) {
                binding.btnFavorite.setImageResource(R.drawable.ic_favorite_full)
                binding.btnFavorite.setOnClickListener {
                    viewModel.deleteFavoriteDestination(dataEntity)
                }
            } else {
                binding.btnFavorite.setImageResource(R.drawable.ic_favorite_border)
                binding.btnFavorite.setOnClickListener {
                    viewModel.insertFavoriteDestination(dataEntity)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}