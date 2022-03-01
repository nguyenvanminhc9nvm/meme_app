package com.minhnv.meme_app.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.minhnv.meme_app.R
import com.minhnv.meme_app.utils.custom_view.ProgressDialog
import com.minhnv.meme_app.utils.helper.TrackingError
import javax.inject.Inject

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    @Inject
    lateinit var trackingErrorHelper: TrackingError

    lateinit var mINavigatorActivity: INavigatorActivity
    private var _binding: ViewBinding? = null
    abstract val inflaterBinding: (LayoutInflater, ViewGroup?, Boolean) -> VB
    private lateinit var progressDialog: ProgressDialog

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    lateinit var mActivity: BaseActivity<*>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is INavigatorActivity) {
            mINavigatorActivity = context
            progressDialog = ProgressDialog(context)
            mActivity = context as BaseActivity<*>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflaterBinding.invoke(inflater, container, false)
        return requireNotNull(_binding).root
    }

    open var showToolbar: Boolean = true

    override fun onResume() {
        super.onResume()
        mINavigatorActivity.setTitleToolbar(title)
        mINavigatorActivity.visibleToolbar(showToolbar)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trackingErrorHelper.coroutineExceptionHandler()
        setup()
    }

    abstract fun setup()

    abstract val title: Int

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun showLoading() {
        if (!progressDialog.isShowing) {
            progressDialog.show()
        }
    }

    fun hideLoading() {
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

    fun switchFragment(fragmentId: Int, bundle: Bundle? = null) {
        if (bundle != null) {
            mINavigatorActivity.navigateFragment(fragmentId, bundle)
        } else {
            mINavigatorActivity.navigateFragment(fragmentId, null)
        }
    }

    fun reloadThemes(themes: Int) {
        mINavigatorActivity.reloadThemes(themes)
    }

    fun showDialog(message: String) {
        MaterialAlertDialogBuilder(mActivity)
            .setMessage(message)
            .setNegativeButton(R.string.ok, null)
            .setPositiveButton(R.string.cancel, null)
            .show()
    }
}