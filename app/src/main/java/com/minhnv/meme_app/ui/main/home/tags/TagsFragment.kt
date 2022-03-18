package com.minhnv.meme_app.ui.main.home.tags

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.minhnv.meme_app.R
import com.minhnv.meme_app.databinding.TagsFragmentBinding
import com.minhnv.meme_app.ui.base.BaseFragment
import com.minhnv.meme_app.ui.main.home.CommunityAdapter
import com.minhnv.meme_app.ui.main.home.NetworkDisconnectAdapter
import com.minhnv.meme_app.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TagsFragment : BaseFragment<TagsFragmentBinding>() {
    override val inflaterBinding: (LayoutInflater, ViewGroup?, Boolean) -> TagsFragmentBinding
        get() = TagsFragmentBinding::inflate

    private val viewModel by viewModels<TagsViewModel>({ requireActivity() })
    private lateinit var tagAdapter : CommunityAdapter
    private val networkDisconnectAdapter = NetworkDisconnectAdapter()
    private lateinit var linearLayoutManager : LinearLayoutManager
    private var tagNameTitle = ""

    override fun setup() {
        linearLayoutManager = LinearLayoutManager(mActivity)
        tagAdapter = CommunityAdapter(mActivity)
        lifecycleScope.launchWhenStarted {
            tagAdapter.loadStateFlow.collect {
                binding.swTags.isRefreshing = it.refresh is LoadState.Loading
            }
        }
        arguments?.getString(Constants.ARGUMENT_1)?.let {
            tagNameTitle = it
            lifecycleScope.launch(trackingErrorHelper.coroutineExceptionHandler()) {
                viewModel.tags(it).collect { data ->
                    tagAdapter.submitData(data)
                }
            }
        }
        binding.rycTags.apply {
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
            adapter = tagAdapter.withLoadStateFooter(networkDisconnectAdapter)
        }
        tagAdapter.didSearchTagName = {
            linearLayoutManager.scrollToPosition(0)
            tagNameTitle = it
            mINavigatorActivity.setTitleToolbar("#$tagNameTitle")
            lifecycleScope.launch(trackingErrorHelper.coroutineExceptionHandler()) {
                viewModel.tags(it).collect { data ->
                    tagAdapter.submitData(data)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mINavigatorActivity.setTitleToolbar("#$tagNameTitle")
    }

    override val title: Int
        get() = R.string.tags


}