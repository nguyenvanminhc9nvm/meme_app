package com.minhnv.meme_app.ui.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.minhnv.meme_app.databinding.ItemNetworkStateBinding

typealias DidRetry = () -> Unit

class NetworkDisconnectAdapter: LoadStateAdapter<NetworkDisconnectAdapter.NetworkDisconnectViewHolder>() {
    var didRetry: DidRetry? = null
    inner class NetworkDisconnectViewHolder(
        private val binding: ItemNetworkStateBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            binding.progressBar.visibility = if (loadState is LoadState.Loading) {
                View.VISIBLE
            } else {
                View.GONE
            }

            val visibleError = if (loadState is LoadState.Error) {
                View.VISIBLE
            } else {
                View.GONE
            }

            binding.errorMsg.visibility = visibleError
            binding.retryButton.visibility = visibleError

            binding.retryButton.setOnClickListener {
                didRetry?.invoke()
            }
        }
    }

    override fun onBindViewHolder(holder: NetworkDisconnectViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): NetworkDisconnectViewHolder {
        val view = ItemNetworkStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NetworkDisconnectViewHolder(view)
    }
}