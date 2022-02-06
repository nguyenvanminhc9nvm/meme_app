package com.minhnv.meme_app.ui.main.create.meme_template.export

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.minhnv.meme_app.R
import com.minhnv.meme_app.data.networking.model.local.MemeTemplate
import com.minhnv.meme_app.databinding.ExportMemeFragmentBinding
import com.minhnv.meme_app.ui.base.BaseFragment
import com.minhnv.meme_app.ui.main.create.meme_template.ItemDecorationAlbumColumns
import com.minhnv.meme_app.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class ExportMemeFragment : BaseFragment<ExportMemeFragmentBinding>() {
    override val inflaterBinding: (LayoutInflater, ViewGroup?, Boolean) -> ExportMemeFragmentBinding
        get() = ExportMemeFragmentBinding::inflate

    private val viewModel by viewModels<ExportMemeViewModel>()
    private val memeIconAdapter = MemeIconAdapter()
    private lateinit var dialog: Dialog
    private val bundle = Bundle()
    override fun setup() {
        val memeTemplate =
            arguments?.getSerializable(Constants.ARGUMENT_SERIALIZABLE) as? MemeTemplate
        binding.memeEdt.loadImage(mActivity, memeTemplate?.memeUrl!!)
        binding.memeEdt.set(mActivity, memeTemplate.getListRect())
        binding.memeEdt.selectEditText = { selected ->
            binding.btnChange.isEnabled = selected
        }
        binding.btnChange.btnAlignLeft.setOnClickListener {
            binding.memeEdt.changeAlign(Gravity.START)
        }
        binding.btnChange.btnAlignCenter.setOnClickListener {
            binding.memeEdt.changeAlign(Gravity.CENTER)
        }
        binding.btnChange.btnAlignRight.setOnClickListener {
            binding.memeEdt.changeAlign(Gravity.END)
        }
        binding.btnChange.btnFontBold.setOnClickListener {
            binding.memeEdt.changeFont(R.font.poppins_bold)
        }
        binding.btnChange.btnFontGothic.setOnClickListener {
            binding.memeEdt.changeFont(R.font.poppins_gothic)
        }
        binding.btnChange.btnFontRegular.setOnClickListener {
            binding.memeEdt.changeFont(R.font.poppins_regular)
        }
        binding.btnChange.btnFontGreen.setOnClickListener {
            binding.memeEdt.changeColor(Color.GREEN)
        }
        binding.btnChange.btnFontRed.setOnClickListener {
            binding.memeEdt.changeColor(Color.RED)
        }
        binding.btnChange.btnFontYellow.setOnClickListener {
            binding.memeEdt.changeColor(Color.YELLOW)
        }
        binding.btnChange.btnFontWhite.setOnClickListener {
            binding.memeEdt.changeColor(Color.WHITE)
        }
        binding.btnChange.btnTextBoundFilled.setOnClickListener {
            binding.memeEdt.changeBound(R.drawable.ic_text_bound_background_filled)
        }
        binding.btnChange.btnTextBoundStroke.setOnClickListener {
            binding.memeEdt.changeBound(R.drawable.ic_text_bound_background_stroke)
        }
        binding.btnChange.btnTextBoundTextOnly.setOnClickListener {
            binding.memeEdt.changeBound(R.drawable.ic_text_bound_background_text_only)
        }
        binding.btnMemeIconSelection.setOnClickListener {
            val alertDialog = AlertDialog.Builder(mActivity)
            val view =
                LayoutInflater.from(mActivity).inflate(R.layout.dialog_select_meme_icon, null)
            alertDialog.setView(view)
            dialog = alertDialog.create()
            val rycMemeIcon = view.findViewById<RecyclerView>(R.id.rycMemeIcon)
            rycMemeIcon.apply {
                layoutManager = GridLayoutManager(mActivity, 4)
                setHasFixedSize(true)
                addItemDecoration(
                    ItemDecorationAlbumColumns(
                        10,
                        4
                    )
                )
                adapter = memeIconAdapter
            }
            dialog.show()
        }

        binding.memeEdt.actionUserMoveImage = { isMove ->
            binding.nsvExportMeme.requestDisallowInterceptTouchEvent(isMove)
        }
        binding.memeEdt.notifyUserSelectView = {
            Toast.makeText(mActivity, getString(R.string.select_image_to_move), Toast.LENGTH_SHORT)
                .show()
        }
        binding.memeEdt.notifyUserSelectEditView = {
            Toast.makeText(mActivity, getString(R.string.select_text_to_edit), Toast.LENGTH_SHORT)
                .show()
        }
        memeIconAdapter.onItemMemeSelected = { memeIcon ->
            binding.memeEdt.addIcon(mActivity, memeIcon.meme)
            dialog.dismiss()
        }

        viewModel.memeIcons.observe(viewLifecycleOwner) {
            memeIconAdapter.set(it)
        }

        binding.btnExportMeme.setOnClickListener {
            viewModel.getBitmapFromView(binding.memeEdt)
            switchFragment(R.id.publishMemeFragment, bundle)
        }

        viewModel.finalMeme.observe(viewLifecycleOwner) {
            if (it != null) {
                val stream = ByteArrayOutputStream()
                it.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                bundle.putByteArray(Constants.ARGUMENT_1, stream.toByteArray())
            }
        }

    }

    override val title: Int
        get() = R.string.export

}