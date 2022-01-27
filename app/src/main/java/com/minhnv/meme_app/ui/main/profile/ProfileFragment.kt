package com.minhnv.meme_app.ui.main.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.minhnv.meme_app.BaseApplication
import com.minhnv.meme_app.R
import com.minhnv.meme_app.databinding.ProfileFragmentBinding
import com.minhnv.meme_app.ui.base.BaseFragment
import com.minhnv.meme_app.ui.main.MainActivity
import com.minhnv.meme_app.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<ProfileFragmentBinding>() {
    private val viewModel by viewModels<ProfileViewModel>()
    override val inflaterBinding: (LayoutInflater, ViewGroup?, Boolean) -> ProfileFragmentBinding
        get() = ProfileFragmentBinding::inflate

    override fun setup() {
        val baseApplication = (mActivity as MainActivity).application as BaseApplication
        if (baseApplication.preferenceGetInteger(Constants.KEY_THEMES, R.style.Theme_Android2021) == R.style.Theme_Android2021) {
            binding.tvTitleTheme.text = getString(R.string.light_themes)
            binding.tvSubTitleTheme.text = getString(R.string.disable_light_enable_dark)
        } else {
            binding.tvTitleTheme.text = getString(R.string.dark_themes)
            binding.tvSubTitleTheme.text = getString(R.string.disable_dark_enable_light)
        }
        binding.llChangeThemes.setOnClickListener {
            binding.switchThemes.isChecked = !binding.switchThemes.isChecked
            val themes = if (baseApplication.preferenceGetInteger(
                    Constants.KEY_THEMES,
                    R.style.Theme_Android2021
                ) == R.style.Theme_Android2021
            ) {
                R.style.Theme_Android2021_Dark
            } else {
                R.style.Theme_Android2021
            }
            reloadThemes(themes)
        }

    }

    override val title: Int = R.string.settings
}