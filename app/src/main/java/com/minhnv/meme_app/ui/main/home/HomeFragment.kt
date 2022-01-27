package com.minhnv.meme_app.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.minhnv.meme_app.R
import com.minhnv.meme_app.databinding.HomeFragmentBinding
import com.minhnv.meme_app.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeFragmentBinding>() {
    override val inflaterBinding: (LayoutInflater, ViewGroup?, Boolean) -> HomeFragmentBinding
        get() = HomeFragmentBinding::inflate

    override fun setup() {
    }

    override val title: Int = R.string.home
}