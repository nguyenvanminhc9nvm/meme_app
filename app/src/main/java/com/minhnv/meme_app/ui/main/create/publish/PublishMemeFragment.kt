package com.minhnv.meme_app.ui.main.create.publish

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.minhnv.meme_app.R
import com.minhnv.meme_app.databinding.PublishMemeFragmentBinding
import com.minhnv.meme_app.ui.base.BaseFragment
import com.minhnv.meme_app.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PublishMemeFragment : BaseFragment<PublishMemeFragmentBinding>() {
    override val inflaterBinding: (LayoutInflater, ViewGroup?, Boolean) -> PublishMemeFragmentBinding
        get() = PublishMemeFragmentBinding::inflate

    override fun setup() {
        arguments?.run {
            getByteArray(Constants.ARGUMENT_1)?.let {
                val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                binding.imageView5.setImageBitmap(bitmap)
            }
            getString(Constants.ARGUMENT_2)?.let {
                Glide.with(mActivity)
                    .asGif()
                    .load(it)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(binding.imageView5)
            }
            getString(Constants.ARGUMENT_3)?.let {
                Glide.with(mActivity).load(it).into(binding.imageView5)
            }
        }

    }

    override val title: Int
        get() = R.string.post_to_community


}