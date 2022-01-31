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
        viewModel.themeSystemIsDark.observe(viewLifecycleOwner) {
            if (!it) {
                binding.tvTitleTheme.text = getString(R.string.light_themes)
                binding.tvSubTitleTheme.text = getString(R.string.disable_light_enable_dark)
                binding.icSelectMaterial1.setOnClickListener {
                    reloadThemes(R.style.Theme_Android2021_Material1)
                }
                binding.icSelectMaterial2.setOnClickListener {
                    reloadThemes(R.style.Theme_Android2021_Material2)
                }
                binding.icSelectMaterial3.setOnClickListener {
                    reloadThemes(R.style.Theme_Android2021_Material3)
                }
                binding.icSelectMaterial4.setOnClickListener {
                    reloadThemes(R.style.Theme_Android2021_Material4)
                }
                binding.icSelectMaterial5.setOnClickListener {
                    reloadThemes(R.style.Theme_Android2021_Material5)
                }
                binding.icSelectMaterial6.setOnClickListener {
                    reloadThemes(R.style.Theme_Android2021_Material6)
                }
                binding.icSelectMaterial7.setOnClickListener {
                    reloadThemes(R.style.Theme_Android2021_Material7)
                }
                binding.icSelectMaterial8.setOnClickListener {
                    reloadThemes(R.style.Theme_Android2021_Material8)
                }
                binding.icSelectMaterial9.setOnClickListener {
                    reloadThemes(R.style.Theme_Android2021_Material9)
                }
                binding.icSelectMaterial10.setOnClickListener {
                    reloadThemes(R.style.Theme_Android2021_Material10)
                }
                binding.icSelectMaterial11.setOnClickListener {
                    reloadThemes(R.style.Theme_Android2021_Material11)
                }
                binding.icSelectMaterial12.setOnClickListener {
                    reloadThemes(R.style.Theme_Android2021_Material12)
                }
            } else {
                binding.tvTitleTheme.text = getString(R.string.dark_themes)
                binding.tvSubTitleTheme.text = getString(R.string.disable_dark_enable_light)
                binding.icSelectMaterial1.setOnClickListener {
                    reloadThemes(R.style.Theme_Android2021_Material1_Dark)
                }
                binding.icSelectMaterial2.setOnClickListener {
                    reloadThemes(R.style.Theme_Android2021_Material2_Dark)
                }
                binding.icSelectMaterial3.setOnClickListener {
                    reloadThemes(R.style.Theme_Android2021_Material3_Dark)
                }
                binding.icSelectMaterial4.setOnClickListener {
                    reloadThemes(R.style.Theme_Android2021_Material4_Dark)
                }
                binding.icSelectMaterial5.setOnClickListener {
                    reloadThemes(R.style.Theme_Android2021_Material5_Dark)
                }
                binding.icSelectMaterial6.setOnClickListener {
                    reloadThemes(R.style.Theme_Android2021_Material6_Dark)
                }
                binding.icSelectMaterial7.setOnClickListener {
                    reloadThemes(R.style.Theme_Android2021_Material7_Dark)
                }
                binding.icSelectMaterial8.setOnClickListener {
                    reloadThemes(R.style.Theme_Android2021_Material8_Dark)
                }
                binding.icSelectMaterial9.setOnClickListener {
                    reloadThemes(R.style.Theme_Android2021_Material9_Dark)
                }
                binding.icSelectMaterial10.setOnClickListener {
                    reloadThemes(R.style.Theme_Android2021_Material10_Dark)
                }
                binding.icSelectMaterial11.setOnClickListener {
                    reloadThemes(R.style.Theme_Android2021_Material11_Dark)
                }
                binding.icSelectMaterial12.setOnClickListener {
                    reloadThemes(R.style.Theme_Android2021_Material12_Dark)
                }
            }
        }
        binding.llChangeThemes.setOnClickListener {
            binding.switchThemes.isChecked = !binding.switchThemes.isChecked
            val themes = if (baseApplication.preferenceGetInteger(
                    Constants.KEY_THEMES,
                    R.style.Theme_Android2021
                ) == R.style.Theme_Android2021
            ) {
                viewModel.setThemeSystem(true)
                R.style.Theme_Android2021_Dark
            } else {
                viewModel.setThemeSystem(false)
                R.style.Theme_Android2021
            }
            reloadThemes(themes)
        }
        binding.switchThemes.setOnClickListener {
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
        binding.llSupport.setOnClickListener {
            switchFragment(R.id.donationFragment)
        }
    }



    override val title: Int = R.string.settings
}