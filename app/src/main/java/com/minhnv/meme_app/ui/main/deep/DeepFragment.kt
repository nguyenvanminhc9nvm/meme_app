package com.minhnv.meme_app.ui.main.deep

import android.view.LayoutInflater
import android.view.ViewGroup
import com.minhnv.meme_app.R
import com.minhnv.meme_app.databinding.DeepFragmentBinding
import com.minhnv.meme_app.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeepFragment : BaseFragment<DeepFragmentBinding>() {
    override val inflaterBinding: (LayoutInflater, ViewGroup?, Boolean) -> DeepFragmentBinding
        get() = DeepFragmentBinding::inflate

    override fun setup() {

    }

    override val title: Int
        get() = R.string.deep

}