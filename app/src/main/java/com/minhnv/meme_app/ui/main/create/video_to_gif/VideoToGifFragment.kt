package com.minhnv.meme_app.ui.main.create.video_to_gif

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.minhnv.meme_app.R
import com.minhnv.meme_app.databinding.VideoToGifFragmentBinding
import com.minhnv.meme_app.ui.base.BaseFragment
import com.minhnv.meme_app.ui.main.MainActivity
import com.minhnv.meme_app.utils.Constants
import com.minhnv.meme_app.utils.URIPathHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class VideoToGifFragment : BaseFragment<VideoToGifFragmentBinding>() {

    private var isStart = false
    private val viewModel by viewModels<VideoToGifViewModel>()
    override val inflaterBinding: (LayoutInflater, ViewGroup?, Boolean) -> VideoToGifFragmentBinding
        get() = VideoToGifFragmentBinding::inflate

    override fun setup() {
        val alertDialog = AlertDialog.Builder(mActivity)
        val view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_progress_bar, null)
        alertDialog.setView(view)
        val dialog = alertDialog.create()
        dialog.setCancelable(false)
        binding.btnGetVideo.setOnClickListener {
            when (PackageManager.PERMISSION_GRANTED) {
                ContextCompat.checkSelfPermission(
                    mActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) -> {
                    val intent = Intent()
                    intent.type = "video/*"
                    intent.action = Intent.ACTION_GET_CONTENT
                    openImageSelected.launch(intent)
                }
                else -> {
                    requestPermissionStorage.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }
        }
        viewModel.startConvert.observe(viewLifecycleOwner) {
            if (it) {
                dialog.show()
            } else {
                if (dialog.isShowing) {
                    dialog.dismiss()
                }
            }
        }
        viewModel.outputVideo.observe(viewLifecycleOwner) { path ->
            if (path.isEmpty()) {
                binding.btnExport.visibility = View.GONE
                binding.btnGetVideo.visibility = View.VISIBLE
            } else {
                binding.btnExport.visibility = View.VISIBLE
                binding.btnGetVideo.visibility = View.GONE
            }
            binding.btnExport.setOnClickListener {
                val bundle = Bundle()
                bundle.putString(Constants.ARGUMENT_2, path)
                switchFragment(R.id.publishMemeFragment, bundle)
            }
        }
        viewModel.videoSelected.observe(viewLifecycleOwner) { uri ->
            val mediaController = MediaController(mActivity)
            binding.vvPreview.setMediaController(mediaController)
            binding.vvPreview.setVideoPath(uri)
            binding.vvPreview.requestFocus()
            binding.vvPreview.start()
        }
    }

    override val title: Int
        get() = R.string.video_to_gif

    private val requestPermissionStorage = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { result ->
        if (result) {
            val intent = Intent()
            intent.type = "video/*"
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
                val inputVideo = URIPathHelper.getPath(mActivity, uri) ?: ""
                val outputVideo = mActivity.filesDir.path + "/videoConverting.gif"
                viewModel.execute(inputVideo, outputVideo)
            }
        }
    }
}