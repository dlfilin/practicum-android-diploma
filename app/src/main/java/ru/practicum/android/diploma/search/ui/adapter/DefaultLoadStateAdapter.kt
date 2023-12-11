package ru.practicum.android.diploma.search.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.PagingProgressItemBinding

class DefaultLoadStateAdapter : LoadStateAdapter<DefaultLoadStateAdapter.Holder>() {

    override fun onBindViewHolder(holder: Holder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PagingProgressItemBinding.inflate(inflater, parent, false)
        return Holder(binding)
    }

    class Holder(
        private val binding: PagingProgressItemBinding,
//        private val swipeRefreshLayout: SwipeRefreshLayout?,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState) = with(binding) {
//            if (swipeRefreshLayout != null) {
//                swipeRefreshLayout.isRefreshing = loadState is LoadState.Loading
//                progressBar.isVisible = false
//            } else {
//                progressBar.isVisible = loadState is LoadState.Loading
//            }
                pagingProgressBar.isVisible = loadState is LoadState.Loading
        }
    }

}
