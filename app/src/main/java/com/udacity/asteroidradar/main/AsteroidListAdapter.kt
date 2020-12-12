package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.AsteroidItemBinding

class AsteroidListAdapter :
    ListAdapter<Asteroid, AsteroidListAdapter.AsteroidViewHolder>(DiffCallback) {
    class AsteroidViewHolder(private var binding: AsteroidItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroid: Asteroid) {
            binding.asteroidProperty = asteroid
            binding.executePendingBindings()
        }

    }

    companion object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AsteroidListAdapter.AsteroidViewHolder {
        var view=AsteroidItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AsteroidViewHolder(view)
    }

    override fun onBindViewHolder(holder: AsteroidListAdapter.AsteroidViewHolder, position: Int) {
        val asteroid=getItem(position)
        holder.bind(asteroid)
    }
}