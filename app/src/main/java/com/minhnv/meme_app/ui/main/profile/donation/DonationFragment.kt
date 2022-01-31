package com.minhnv.meme_app.ui.main.profile.donation

import android.view.LayoutInflater
import android.view.ViewGroup
import com.minhnv.meme_app.R
import com.minhnv.meme_app.databinding.DonationFragmentBinding
import com.minhnv.meme_app.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DonationFragment : BaseFragment<DonationFragmentBinding>() {
    override val inflaterBinding: (LayoutInflater, ViewGroup?, Boolean) -> DonationFragmentBinding
        get() = DonationFragmentBinding::inflate

    override fun setup() {

    }

    override val title: Int
        get() = R.string.donation


}