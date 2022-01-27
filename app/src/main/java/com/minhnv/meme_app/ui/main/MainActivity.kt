package com.minhnv.meme_app.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.ui.setupWithNavController
import com.minhnv.meme_app.BaseApplication
import com.minhnv.meme_app.R
import com.minhnv.meme_app.databinding.ActivityMainBinding
import com.minhnv.meme_app.ui.base.BaseActivity
import com.minhnv.meme_app.ui.base.INavigatorActivity
import com.minhnv.meme_app.utils.Constants
import com.minhnv.meme_app.utils.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(), INavigatorActivity {
    private var currentNavController: LiveData<NavController>? = null

    override fun setup() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(
            (application as BaseApplication).preferenceGetInteger(
                Constants.KEY_THEMES,
                R.style.Theme_Android2021
            )
        )
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
    }

    override fun navigateFragment(fragmentId: Int, bundle: Bundle?) {
        val navBuilder = NavOptions.Builder()
        navBuilder.setEnterAnim(android.R.anim.slide_in_left)
            .setExitAnim(android.R.anim.slide_out_right)
            .setPopEnterAnim(android.R.anim.slide_out_right)
            .setPopExitAnim(android.R.anim.slide_in_left)

        if (bundle != null) {
            currentNavController?.value?.navigate(fragmentId, bundle, navBuilder.build())
        } else {
            currentNavController?.value?.navigate(fragmentId, null, navBuilder.build())
        }
    }

    override fun setTitleToolbar(title: Int) {
        val typeface = ResourcesCompat.getFont(this, R.font.poppins_medium)
        binding.collapsingToolbar.setCollapsedTitleTypeface(typeface)
        binding.collapsingToolbar.title = getString(title)
    }

    override fun popBackStackFragment(fragmentId: Int) {
        currentNavController?.value?.popBackStack(fragmentId, false)
    }

    override val inflater: (LayoutInflater) -> ActivityMainBinding = ActivityMainBinding::inflate

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val navGraphIds = listOf(R.navigation.homes, R.navigation.create, R.navigation.profiles)

        val controller = binding.bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_fragment,
            intent = intent
        )
        controller.observe(this) { navController ->
            binding.toolbar.setupWithNavController(navController)
        }
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    override fun onBackPressed() {
        if (currentNavController?.value?.popBackStack() != true) {
            super.onBackPressed()
        }
    }

    override fun reloadThemes(themes: Int) {
        val application = application as BaseApplication
        application.preferencePutInteger(Constants.KEY_THEMES, themes)
        recreate()
    }
}