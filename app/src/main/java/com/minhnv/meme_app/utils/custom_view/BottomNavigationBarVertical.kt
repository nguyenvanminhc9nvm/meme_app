package com.minhnv.meme_app.utils.custom_view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.SparseArray
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.core.util.forEach
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.minhnv.meme_app.R

class BottomNavigationBarVertical @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    inner class IconItem constructor(
        context: Context,
        icon: Drawable,
        title: String
    ) : LinearLayout(context) {
        private val imgIcon = ImageView(context).apply {
            setImageDrawable(icon)
        }
        private val tvName = TextView(context).apply {
            text = title
            setPadding(0, 10, 0, 0)
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        }

        init {
            val params1 = LayoutParams(LayoutParams.MATCH_PARENT, 0, 3f)
            val params2 = LayoutParams(LayoutParams.MATCH_PARENT, 0, 2f)
            orientation = VERTICAL
            addView(View(context), params1)
            addView(imgIcon, params2)
            addView(tvName, params1)
        }
    }

    private var idSelected = 0
    private var onItemSelectedListener: OnItemSelectedListener? = null
    private var onItemReselectedListener: OnItemReselectedListener? = null

    @SuppressLint("ResourceType")
    private val itemHomes = IconItem(
        context,
        ResourcesCompat.getDrawable(resources, R.drawable.ic_home, null)!!,
        context.getString(R.string.home)
    ).apply {
        id = resources.getIdentifier("ic_home", "drawable", context.packageName)
        setOnClickListener {
            if (idSelected == id) {
                onItemReselectedListener?.onNavigationItemReselectedListener(id)
            } else {
                onItemSelectedListener?.onNavigationItemSelected(id)
            }
            idSelected = id
        }
    }

    @SuppressLint("ResourceType")
    private val itemDeeps = IconItem(
        context,
        ResourcesCompat.getDrawable(resources, R.drawable.ic_meme_icon_10, null)!!,
        context.getString(R.string.deep)
    ).apply {
        id = resources.getIdentifier("ic_meme_icon_10", "drawable", context.packageName)
        setOnClickListener {
            if (idSelected == id) {
                onItemReselectedListener?.onNavigationItemReselectedListener(id)
            } else {
                onItemSelectedListener?.onNavigationItemSelected(id)
            }
            idSelected = id
        }
    }

    @SuppressLint("ResourceType")
    private val itemCreate = IconItem(
        context,
        ResourcesCompat.getDrawable(resources, R.drawable.ic_create, null)!!,
        context.getString(R.string.create)
    ).apply {
        id = resources.getIdentifier("ic_create", "drawable", context.packageName)
        setOnClickListener {
            if (idSelected == id) {
                onItemReselectedListener?.onNavigationItemReselectedListener(id)
            } else {
                onItemSelectedListener?.onNavigationItemSelected(id)
            }
            idSelected = id
        }
    }

    @SuppressLint("ResourceType")
    private val itemSettings = IconItem(
        context,
        ResourcesCompat.getDrawable(resources, R.drawable.ic_settings, null)!!,
        context.getString(R.string.settings)
    ).apply {
        id = resources.getIdentifier("ic_settings", "drawable", context.packageName)
        setOnClickListener {
            if (idSelected == id) {
                onItemReselectedListener?.onNavigationItemReselectedListener(id)
            } else {
                onItemSelectedListener?.onNavigationItemSelected(id)
            }
            idSelected = id
        }
    }

    init {
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f)
        orientation = VERTICAL
        addView(
            itemHomes,
            layoutParams
        )
        addView(
            itemDeeps,
            layoutParams
        )
        addView(
            itemCreate,
            layoutParams
        )
        addView(
            itemSettings,
            layoutParams
        )
    }

    private interface OnItemSelectedListener {
        fun onNavigationItemSelected(id: Int): Boolean
    }

    private interface OnItemReselectedListener {
        fun onNavigationItemReselectedListener(id: Int)
    }

    private fun setOnNavigationItemSelectedListener(listener: OnItemSelectedListener) {
        onItemSelectedListener = listener
    }

    private fun setOnNavigationItemReSelectedListener(listener: OnItemReselectedListener) {
        onItemReselectedListener = listener
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun setUpWithNavController(
        navGraphIds: List<Int>,
        fragmentManager: FragmentManager,
        containerId: Int
    ): LiveData<NavController> {

        val graphIdToTagMap = SparseArray<String>()
        val selectedNavController = MutableLiveData<NavController>()
        var firstFragmentGraphId = 0

        navGraphIds.forEachIndexed { index, navGraphId ->
            val fragmentTag = getFragmentTag(index)

            val navHostFragment = obtainNavHostFragment(
                fragmentManager, fragmentTag, navGraphId, containerId
            )

            val graphId = navHostFragment.navController.graph.id

            if (index == 0) {
                firstFragmentGraphId = graphId
            }

            graphIdToTagMap[graphId] = fragmentTag

            if (idSelected == graphId) {
                selectedNavController.value = navHostFragment.navController
                attachNavHostFragment(fragmentManager, navHostFragment, index == 0)
            } else {
                detachNavHostFragment(fragmentManager, navHostFragment)
            }
        }

        var selectedItemTag = graphIdToTagMap[idSelected]
        val firstFragmentTag = graphIdToTagMap[firstFragmentGraphId]
        var isOnFirstFragment = selectedItemTag == firstFragmentTag

        setOnNavigationItemSelectedListener(object : OnItemSelectedListener {
            override fun onNavigationItemSelected(id: Int): Boolean {
                return if (fragmentManager.isStateSaved) {
                    false
                } else {
                    val newlySelectedItemTag = graphIdToTagMap[id]
                    if (selectedItemTag != newlySelectedItemTag) {
                        fragmentManager.popBackStack(firstFragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                        val selectedFragment = fragmentManager.findFragmentByTag(newlySelectedItemTag) as NavHostFragment
                        if (firstFragmentTag != newlySelectedItemTag) {
                            fragmentManager.beginTransaction()
                                .attach(selectedFragment)
                                .setPrimaryNavigationFragment(selectedFragment)
                                .apply {
                                    graphIdToTagMap.forEach { _, fragmentTagItem ->
                                        if (fragmentTagItem != newlySelectedItemTag) {
                                            detach(fragmentManager.findFragmentByTag(firstFragmentTag)!!)
                                        }
                                    }
                                }.addToBackStack(firstFragmentTag)
                                .setCustomAnimations(
                                    R.anim.nav_default_enter_anim,
                                    R.anim.nav_default_exit_anim,
                                    R.anim.nav_default_pop_enter_anim,
                                    R.anim.nav_default_pop_exit_anim)
                                .setReorderingAllowed(true)
                                .commit()
                        }
                        selectedItemTag = newlySelectedItemTag
                        isOnFirstFragment = selectedItemTag == firstFragmentTag
                        selectedNavController.value = selectedFragment.navController
                        true
                    } else {
                        false
                    }
                }
            }
        })

        setUpItemReselected(graphIdToTagMap, fragmentManager)
        fragmentManager.addOnBackStackChangedListener {
            if (!isOnFirstFragment && !fragmentManager.isOnBackStack(firstFragmentTag)) {
                idSelected = firstFragmentGraphId
            }

            // Reset the graph if the currentDestination is not valid (happens when the back
            // stack is popped after using the back button).
            selectedNavController.value?.let { controller ->
                if (controller.currentDestination == null) {
                    controller.navigate(controller.graph.id)
                }
            }
        }

        return selectedNavController
    }

    private fun setUpItemReselected(
        graphIdToTagMap: SparseArray<String>,
        fragmentManager: FragmentManager
    ) {
        setOnNavigationItemReSelectedListener(object : OnItemReselectedListener {
            override fun onNavigationItemReselectedListener(id: Int) {
                val newlySelectedItemTag = graphIdToTagMap[id]
                val selectedFragment = fragmentManager.findFragmentByTag(newlySelectedItemTag) as NavHostFragment
                val navController = selectedFragment.navController
                navController.popBackStack(
                    navController.graph.startDestinationId, false
                )
            }
        })
    }

    private fun detachNavHostFragment(
        fragmentManager: FragmentManager,
        navHostFragment: NavHostFragment
    ) {
        fragmentManager.beginTransaction().detach(navHostFragment).commitNow()
    }

    private fun attachNavHostFragment(
        fragmentManager: FragmentManager,
        navHostFragment: NavHostFragment,
        isPrimaryNavFragment: Boolean
    ) {
        fragmentManager.beginTransaction()
            .attach(navHostFragment)
            .apply {
                if (isPrimaryNavFragment) {
                    setPrimaryNavigationFragment(navHostFragment)
                }
            }.commitNow()
    }

    private fun obtainNavHostFragment(
        fragmentManager: FragmentManager,
        fragmentTag: String,
        navGraphId: Int,
        containerId: Int
    ): NavHostFragment {
        val existingFragment = fragmentManager.findFragmentByTag(fragmentTag) as NavHostFragment?
        existingFragment?.let { return it }

        val navHostFragment = NavHostFragment.create(navGraphId)
        fragmentManager.beginTransaction().add(containerId, navHostFragment, fragmentTag)
            .commitNow()
        return navHostFragment
    }

    private fun getFragmentTag(index: Int) = "bottomNavigation#$index"
}