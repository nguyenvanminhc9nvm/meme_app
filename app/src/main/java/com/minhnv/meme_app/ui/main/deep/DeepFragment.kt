package com.minhnv.meme_app.ui.main.deep

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.minhnv.meme_app.R
import com.minhnv.meme_app.databinding.DeepFragmentBinding
import com.minhnv.meme_app.ui.base.BaseFragment
import com.minhnv.meme_app.ui.main.home.NetworkDisconnectAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class DeepFragment : BaseFragment<DeepFragmentBinding>() {
    override val inflaterBinding: (LayoutInflater, ViewGroup?, Boolean) -> DeepFragmentBinding
        get() = DeepFragmentBinding::inflate

    private val viewModels by viewModels<DeepViewModel>()
    private lateinit var deepAdapter: DeepAdapter
    private val networkDisconnectAdapter = NetworkDisconnectAdapter()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private val job = Job()
    private var duration = 1000L
    override fun setup() {
        linearLayoutManager = LinearLayoutManager(mActivity)
        deepAdapter = DeepAdapter(mActivity)
        binding.rycDeep.apply {
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            adapter = deepAdapter.withLoadStateFooter(networkDisconnectAdapter)
        }
        networkDisconnectAdapter.didRetry = {
            deepAdapter.retry()
        }
        lifecycleScope.launchWhenStarted {
            deepAdapter.loadStateFlow.collect {
                binding.swDeep.isRefreshing = it.refresh is LoadState.Loading
            }
        }
        binding.swDeep.setOnRefreshListener {
            deepAdapter.refresh()
        }
        lifecycleScope.launchWhenStarted {
            viewModels.deeps.collect {
                deepAdapter.submitData(it)
            }
        }
        binding.fabMoveToTop.setOnClickListener {
            linearLayoutManager.smoothScrollToPosition(
                binding.rycDeep,
                RecyclerView.State(),
                0
            )
        }
        binding.progressAutoScroll.mMax = 4

        binding.progressAutoScroll.setOnClickListener { view ->
            duration += 1000L
            job.cancelChildren()
            Toast.makeText(
                mActivity,
                "${getString(R.string.auto_scroll_mes)} ${TimeUnit.MILLISECONDS.toSeconds(duration)} ${
                    getString(
                        R.string.auto_scroll_mes_second
                    )
                }",
                Toast.LENGTH_SHORT
            ).show()
            CoroutineScope(Dispatchers.IO + job).launch {
                flow {
                    var counter: Long = 0
                    while (true) {
                        binding.progressAutoScroll.startAnimator(
                            duration = duration,
                            start = 0,
                            end = 4,
                            repeatCount = counter.toInt()
                        )
                        delay(duration)
                        emit(counter++)
                    }
                }.flowOn(Dispatchers.Main).collect {
                    binding.rycDeep.smoothScrollBy(0, 1000)
                    binding.progressAutoScroll.mProgress = it.toInt()
                }
            }
        }

        binding.rycDeep.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    job.cancelChildren()
                    binding.progressAutoScroll.mProgress = 0
                }
            }
        })
    }

    override val title: Int
        get() = R.string.deep

}