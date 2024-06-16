package com.rizky.journeyonsolo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rizky.journeyonsolo.data.remote.response.ListDestinationItem
import com.rizky.journeyonsolo.databinding.ItemWisataBinding
import com.rizky.journeyonsolo.ui.home.HomeFragmentDirections

class NearbyDestinationAdapter : ListAdapter<ListDestinationItem, NearbyDestinationAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(private val binding: ItemWisataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(destination: ListDestinationItem) {
            binding.apply {
                tvItemName.text = destination.name
                tvItemLocation.text = destination.address
                tvItemRating.text = destination.rating
                Glide.with(imgItemPhoto.context)
                    .load(destination.imageUrl)
                    .into(imgItemPhoto)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemWisataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val destination = getItem(position)
        holder.bind(destination)

        holder.itemView.setOnClickListener {view ->
            val toDetailFragment = HomeFragmentDirections.actionNavigationHomeToDetailFragment()
            toDetailFragment.id = destination.placeId
            view.findNavController().navigate(toDetailFragment)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListDestinationItem>() {
            override fun areItemsTheSame(oldItem: ListDestinationItem, newItem: ListDestinationItem): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: ListDestinationItem, newItem: ListDestinationItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}