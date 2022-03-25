package com.minhnv.meme_app.ui.main.create.meme_template.export

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.minhnv.meme_app.R
import com.minhnv.meme_app.data.networking.model.local.MemeTemplate
import com.minhnv.meme_app.databinding.ExportMemeFragmentBinding
import com.minhnv.meme_app.ui.base.BaseFragment
import com.minhnv.meme_app.ui.main.create.meme_template.ItemDecorationAlbumColumns
import com.minhnv.meme_app.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExportMemeFragment : BaseFragment<ExportMemeFragmentBinding>() {
    override val inflaterBinding: (LayoutInflater, ViewGroup?, Boolean) -> ExportMemeFragmentBinding
        get() = ExportMemeFragmentBinding::inflate

    private val viewModel by viewModels<ExportMemeViewModel>()
    private val memeIconAdapter = MemeIconAdapter()
    private lateinit var dialog: Dialog
    private val bundle = Bundle()
    private lateinit var memeToolAdapter: MemeToolAdapter
    private val rectDefault = Rect(
        100,
        100,
        300,
        300
    )

    override fun setup() {
        memeToolAdapter = MemeToolAdapter(mActivity).apply {
            set(memeToolsAlign)
        }
        val memeTemplate =
            arguments?.getSerializable(Constants.ARGUMENT_SERIALIZABLE) as? MemeTemplate
        binding.memeEdt.loadImage(mActivity, memeTemplate?.memeUrl!!)
        binding.memeEdt.set(mActivity, memeTemplate.getListRect())
        binding.memeEdt.selectEditText = { selected ->
            binding.btnChange.isEnabled = selected
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

        }
        binding.memeEdt.notifyUserSelectEditView = {

        }
        binding.memeEdt.didIconAddIntoView = { memeIcon, rect ->
            viewModel.setMemeEdtValue(memeIcon, rect)
        }
        memeIconAdapter.onItemMemeSelected = { memeIcon ->
            binding.memeEdt.didIconAddIntoView?.invoke(memeIcon, rectDefault)
            dialog.dismiss()
            binding.nsvExportMeme.smoothScrollTo(0, 0)
        }

        viewModel.memeIcons.observe(viewLifecycleOwner) {
            memeIconAdapter.set(it)
        }

        binding.btnExportMeme.setOnClickListener {
            val path = viewModel.storeAndGetPathImage(binding.memeEdt, mActivity)
            bundle.putString(Constants.ARGUMENT_1, path)
            switchFragment(R.id.publishMemeFragment, bundle)
        }

        binding.vpToolIcon.apply {
            layoutManager = GridLayoutManager(mActivity, 5)
            setHasFixedSize(true)
            adapter = memeToolAdapter
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> memeToolAdapter.set(memeToolsAlign)
                    1 -> memeToolAdapter.set(memeIconsFont)
                    2 -> memeToolAdapter.set(memeIconsSpacing)
                    3 -> memeToolAdapter.set(memeIconsBound)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
        memeToolAdapter.didSelectedToolIcon = { memeToolIcon ->
            when (memeToolIcon.type) {
                TypedValueTool.FONT -> {
                    binding.memeEdt.changeFont(
                        memeToolIcon.attributes ?: R.font.poppins_medium
                    )
                }
                TypedValueTool.COLOR -> {
                    binding.memeEdt.changeColor(
                        memeToolIcon.colorPrimary ?: Color.BLACK,
                        memeToolIcon.colorSecondary ?: Color.BLACK
                    )
                }
                TypedValueTool.SIZE -> {
                    binding.memeEdt.changeSize(
                        memeToolIcon.attributes ?: 100
                    )
                }
                TypedValueTool.SPACING -> {
                    binding.memeEdt.changeLineSpacingExtra(
                        memeToolIcon.attributes ?: 6
                    )
                }
                TypedValueTool.BOUND -> {
                    binding.memeEdt.changeBound(
                        memeToolIcon.attributes ?: R.drawable.ic_place_holder,
                        memeToolIcon.colorPrimary ?: R.color.black
                    )
                }
                TypedValueTool.ALIGN -> {
                    binding.memeEdt.changeAlign(
                        memeToolIcon.attributes ?: Gravity.CENTER
                    )
                }
            }
        }

        viewModel.memeEdtValue.observe(viewLifecycleOwner) { list ->
            list.forEach {
                binding.memeEdt.addIcon(mActivity, it.value.first, it.value.second)
            }
        }
    }

    override val title: Int
        get() = R.string.export

}