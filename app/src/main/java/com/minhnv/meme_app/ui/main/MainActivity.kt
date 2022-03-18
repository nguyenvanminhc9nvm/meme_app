package com.minhnv.meme_app.ui.main

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.ads.MobileAds
import com.minhnv.meme_app.BaseApplication
import com.minhnv.meme_app.R
import com.minhnv.meme_app.databinding.ActivityMainBinding
import com.minhnv.meme_app.ui.base.BaseActivity
import com.minhnv.meme_app.ui.base.INavigatorActivity
import com.minhnv.meme_app.utils.Constants
import com.minhnv.meme_app.utils.custom_view.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(), INavigatorActivity {
    private var currentNavController: LiveData<NavController>? = null

    override fun setup() {
        MobileAds.initialize(
            this
        ) { }
    }

    @RequiresApi(Build.VERSION_CODES.S)
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
        navBuilder.setEnterAnim(R.anim.slide_in)
            .setExitAnim(R.anim.fade_out)
            .setPopEnterAnim(R.anim.fade_in)
            .setPopExitAnim(R.anim.slide_out)

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

    override fun setTitleToolbar(title: String) {
        val typeface = ResourcesCompat.getFont(this, R.font.poppins_medium)
        binding.collapsingToolbar.setCollapsedTitleTypeface(typeface)
        binding.collapsingToolbar.title = title
    }

    override fun popBackStackFragment(fragmentId: Int) {
        currentNavController?.value?.popBackStack(fragmentId, false)
    }

    override val inflater: (LayoutInflater) -> ActivityMainBinding = ActivityMainBinding::inflate

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun setupBottomNavigationBar() {
        val navGraphIds = listOf(
            R.navigation.homes,
            R.navigation.deep,
            R.navigation.create,
            R.navigation.profiles
        )
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

    override fun visibleToolbar(visible: Boolean) {
        val visibility = if (visible) {
            View.VISIBLE
        } else {
            View.GONE
        }
        binding.collapsingToolbar.visibility = visibility
    }
}