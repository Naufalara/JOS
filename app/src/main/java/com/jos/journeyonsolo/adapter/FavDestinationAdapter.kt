package com.jos.journeyonsolo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jos.journeyonsolo.R
import com.jos.journeyonsolo.data.local.entity.FavoriteDestination
import com.jos.journeyonsolo.databinding.ItemFavWisataBinding
import com.jos.journeyonsolo.ui.favorite.FavoriteFragmentDirections

class FavDestinationAdapter: ListAdapter<FavoriteDestination, FavDestinationAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(private val binding: ItemFavWisataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(destination: FavoriteDestination) {
            binding.apply {
                tvItemName.text = destination.name
                tvItemLocation.text = destination.address
                tvItemRating.text = destination.rating

                val imageResId = destination.imageUrl?.toIntOrNull() // Konversi string ke resource ID
                if (imageResId != null && imageResId != 0) {
                    // Jika valid, load gambar menggunakan Glide
                    Glide.with(imgItemPhoto.context)
                        .load(imageResId)
                        .into(imgItemPhoto)
                } else {
                    // Jika tidak valid, gunakan placeholder
                    Glide.with(imgItemPhoto.context)
                        .load(R.drawable.logojos)
                        .into(imgItemPhoto)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemFavWisataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val destination = getItem(position)
        holder.bind(destination)

        holder.itemView.setOnClickListener {view ->
            val toDetailFragment = FavoriteFragmentDirections.actionNavigationFavoriteToDetailFragment()
            toDetailFragment.id = destination.placeId
            view.findNavController().navigate(toDetailFragment)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteDestination>() {
            override fun areItemsTheSame(oldItem: FavoriteDestination, newItem: FavoriteDestination): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FavoriteDestination, newItem: FavoriteDestination): Boolean {
                return oldItem == newItem
            }
        }
    }
}