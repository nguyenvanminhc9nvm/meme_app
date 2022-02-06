package com.minhnv.meme_app.ui.main.create

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.minhnv.meme_app.R
import com.minhnv.meme_app.databinding.CreateFragmentBinding
import com.minhnv.meme_app.ui.base.BaseFragment
import com.minhnv.meme_app.ui.main.MainActivity
import com.minhnv.meme_app.utils.Constants
import com.minhnv.meme_app.utils.URIPathHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateFragment : BaseFragment<CreateFragmentBinding>() {
    override val inflaterBinding: (LayoutInflater, ViewGroup?, Boolean) -> CreateFragmentBinding
        get() = CreateFragmentBinding::inflate

    private val viewModel by viewModels<CreateViewModel>()

    override fun setup() {
        binding.llVideoToGif.setOnClickListener {
            switchFragment(R.id.videoToGifFragment)
        }
        binding.llMemeTemplate.setOnClickListener {
            switchFragment(R.id.memeTemplateListFragment)
        }
        binding.llGallery.setOnClickListener {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    mActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) -> {
                    val intent = Intent()
                    intent.type = "image/*"
                    intent.action = Intent.ACTION_GET_CONTENT
                    openImageSelected.launch(intent)
                }
                else -> {
                    requestPermissionStorage.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }
        }
    }

    private val requestPermissionStorage = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { result ->
        if (result) {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            openImageSelected.launch(intent)
        }
    }

    private val openImageSelected = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        CoroutineScope(Dispatchers.Main).launch {
            val uri = result.data?.data
            val maxSize = 20000 * 1024
            if (uri != null) {
                val sizeFile = viewModel.getFileSize(mActivity as MainActivity, uri)
                if (sizeFile > maxSize) {
                    MaterialAlertDialogBuilder(mActivity)
                        .setMessage(R.string.file_size_smaller_than_10mb)
                        .setNegativeButton(R.string.ok, null)
                        .setPositiveButton(R.string.cancel, null)
                        .show()
                    return@launch
                }
                val inputPath = URIPathHelper.getPath(mActivity, uri) ?: ""
                val bundle = Bundle()
                bundle.putString(Constants.ARGUMENT_3, inputPath)
                switchFragment(R.id.publishMemeFragment, bundle)
            }
        }
    }

    override val title: Int = com.minhnv.meme_app.R.string.create
}