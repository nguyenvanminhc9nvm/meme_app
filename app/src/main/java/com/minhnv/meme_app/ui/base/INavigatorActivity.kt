package com.minhnv.meme_app.ui.base

import android.os.Bundle

interface INavigatorActivity {
    fun navigateFragment(fragmentId: Int, bundle: Bundle?)

    fun popBackStackFragment(fragmentId: Int)

    fun setTitleToolbar(title: Int)

    fun setTitleToolbar(title: String)

    fun reloadThemes(themes: Int)

    fun visibleToolbar(visible: Boolean)
}