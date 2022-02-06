package com.minhnv.meme_app.ui.main.create.meme_template

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.minhnv.meme_app.R
import com.minhnv.meme_app.databinding.MemeTemplateListFragmentBinding
import com.minhnv.meme_app.ui.base.BaseFragment
import com.minhnv.meme_app.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MemeTemplateListFragment : BaseFragment<MemeTemplateListFragmentBinding>() {
    private val viewModel by viewModels<MemeTemplateListViewModel>()
    override val inflaterBinding: (LayoutInflater, ViewGroup?, Boolean) -> MemeTemplateListFragmentBinding
        get() = MemeTemplateListFragmentBinding::inflate

    override fun setup() {
        val memeTemplateAdapter = MemeTemplateAdapter(mActivity)
        memeTemplateAdapter.onItemSelected = { memeTemplate ->
            val bundle = Bundle()
            bundle.putSerializable(Constants.ARGUMENT_SERIALIZABLE, memeTemplate)
            switchFragment(R.id.exportMemeFragment, bundle)
        }
        viewModel.memeTemplates.observe(viewLifecycleOwner) {
            memeTemplateAdapter.set(it)
        }
        binding.rycMemeTemplate.apply {
            layoutManager = GridLayoutManager(mActivity, 2)
            setHasFixedSize(true)
            addItemDecoration(
                ItemDecorationAlbumColumns(
                    10,
                    2
                )
            )
            adapter = memeTemplateAdapter
        }

    }

    override val title: Int
        get() = R.string.meme_template


}