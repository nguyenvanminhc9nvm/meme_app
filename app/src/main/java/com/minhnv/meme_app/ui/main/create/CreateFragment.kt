package com.minhnv.meme_app.ui.main.create

import android.view.LayoutInflater
import android.view.ViewGroup
import com.minhnv.meme_app.R
import com.minhnv.meme_app.databinding.CreateFragmentBinding
import com.minhnv.meme_app.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateFragment : BaseFragment<CreateFragmentBinding>() {
    override val inflaterBinding: (LayoutInflater, ViewGroup?, Boolean) -> CreateFragmentBinding
        get() = CreateFragmentBinding::inflate

    override fun setup() {

    }

    override val title: Int = R.string.create
}