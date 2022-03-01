package com.minhnv.meme_app.ui.main.create.publish

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.minhnv.meme_app.R
import com.minhnv.meme_app.databinding.PublishMemeFragmentBinding
import com.minhnv.meme_app.ui.base.BaseFragment
import com.minhnv.meme_app.utils.Constants
import com.minhnv.meme_app.utils.helper.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class PublishMemeFragment : BaseFragment<PublishMemeFragmentBinding>() {
    private var path = ""
    override val inflaterBinding: (LayoutInflater, ViewGroup?, Boolean) -> PublishMemeFragmentBinding
        get() = PublishMemeFragmentBinding::inflate
    private val viewModel by viewModels<PublishMemeViewModel>()

    override fun setup() {
        arguments?.run {
            getString(Constants.ARGUMENT_1)?.let {
                path = it
                Glide.with(mActivity)
                    .load(Uri.fromFile(File(it)))
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(binding.imageView5)
            }
            getString(Constants.ARGUMENT_2)?.let {
                path = it
                Glide.with(mActivity)
                    .asGif()
                    .load(it)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(binding.imageView5)
            }
            getString(Constants.ARGUMENT_3)?.let {
                path = it
                Glide.with(mActivity).load(it).into(binding.imageView5)
            }
        }
        binding.btnPublish.setOnClickListener {
            val title = binding.edTitle.text.toString()
            val desc = binding.edDesc.text.toString()
            viewModel.doUploadFile(title, desc, path)
        }

        lifecycleScope.launch {
            viewModel.uploadSuccess.collect {
                when (it.status) {
                    Status.SUCCESS -> {
                        hideLoading()
                        findNavController().popBackStack()
                    }
                    Status.ERROR -> {
                        showDialog(it.message ?: "")
                        hideLoading()
                    }
                    Status.LOADING -> {
                        showLoading()
                    }
                    Status.EMPTY -> {
                        hideLoading()
                    }
                }
            }
        }
    }

    override val title: Int
        get() = R.string.post_to_community


}