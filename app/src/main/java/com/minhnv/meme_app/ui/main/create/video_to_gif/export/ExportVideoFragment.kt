package com.minhnv.meme_app.ui.main.create.video_to_gif.export

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.minhnv.meme_app.R
import com.minhnv.meme_app.databinding.ExportVideoFragmentBinding
import com.minhnv.meme_app.ui.base.BaseFragment
import com.minhnv.meme_app.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExportVideoFragment : BaseFragment<ExportVideoFragmentBinding>() {
    override val inflaterBinding: (LayoutInflater, ViewGroup?, Boolean) -> ExportVideoFragmentBinding
        get() = ExportVideoFragmentBinding::inflate

    override fun setup() {
        arguments?.run {
            getString(Constants.ARGUMENT_1)?.let {
                Glide.with(mActivity).asGif().load(it).into(binding.imgPreview)
            }
        }
    }

    override val title: Int
        get() = R.string.export


}