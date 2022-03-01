package com.minhnv.meme_app.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.minhnv.meme_app.utils.custom_view.ProgressDialog
import com.minhnv.meme_app.utils.helper.TrackingError
import javax.inject.Inject

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    @Inject
    lateinit var trackingErrorHelper: TrackingError

    private var _binding: ViewBinding? = null
    abstract val inflater: (LayoutInflater) -> VB
    private lateinit var progressDialog: ProgressDialog

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = inflater.invoke(layoutInflater)
        setContentView(requireNotNull(_binding).root)
        progressDialog = ProgressDialog(this)
        trackingErrorHelper.coroutineExceptionHandler()
        setup()
    }

    abstract fun setup()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showLoading() {
        if (!progressDialog.isShowing) {
            progressDialog.show()
        }
    }

    private fun hideLoading() {
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }
}