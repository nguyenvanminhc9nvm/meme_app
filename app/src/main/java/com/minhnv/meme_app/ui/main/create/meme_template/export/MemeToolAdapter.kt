package com.minhnv.meme_app.ui.main.create.meme_template.export

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.minhnv.meme_app.R
import com.minhnv.meme_app.databinding.ItemMemeDataBinding
import com.minhnv.meme_app.ui.main.create.meme_template.export.TypedValueTool.*

typealias DidSelectedToolIcon = (MemeToolIcon) -> Unit

class MemeToolAdapter(
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val dataSource: MutableList<MemeToolIcon> = mutableListOf()
    var didSelectedToolIcon: DidSelectedToolIcon? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemMemeDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MemeToolViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MemeToolViewHolder) {
            holder.bind(dataSource[position])
        }
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    fun set(list: MutableList<MemeToolIcon>) {
        dataSource.clear()
        dataSource.addAll(list)
        notifyDataSetChanged()
    }

    private inner class MemeToolViewHolder(
        private val binding: ItemMemeDataBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(memeToolIcon: MemeToolIcon) {
            binding.root.setOnClickListener {
                didSelectedToolIcon?.invoke(memeToolIcon)
            }
            when (memeToolIcon.type) {
                ALIGN, SPACING -> {
                    binding.imgMemeData.visibility = View.VISIBLE
                    binding.tvMemeData.visibility = View.GONE
                    binding.imgMemeData.setImageResource(
                        memeToolIcon.icon ?: R.drawable.ic_place_holder
                    )
                    binding.imgMemeData.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            R.color.black
                        )
                    )
                }
                COLOR -> {
                    binding.imgMemeData.visibility = View.GONE
                    binding.tvMemeData.visibility = View.VISIBLE
                    binding.tvMemeData.apply {
                        text = memeToolIcon.text
                        val typeFace = ResourcesCompat.getFont(context, R.font.poppins_bold)
                        typeface = typeFace
                        setTextColor(
                            ContextCompat.getColor(
                                context,
                                memeToolIcon.colorPrimary ?: Color.BLACK
                            )
                        )
                        setShadowLayer(
                            5F,
                            0F,
                            10F,
                            ContextCompat.getColor(
                                context,
                                memeToolIcon.colorSecondary ?: Color.BLACK
                            )
                        )
                    }
                }
                SIZE -> {
                    binding.imgMemeData.visibility = View.GONE
                    binding.tvMemeData.visibility = View.VISIBLE
                    binding.tvMemeData.text = memeToolIcon.text
                    val typeFace = ResourcesCompat.getFont(context, R.font.poppins_regular)
                    binding.tvMemeData.typeface = typeFace
                    binding.tvMemeData.setTextColor(Color.BLACK)
                    binding.tvMemeData.setShadowLayer(5F, 0F, 5F, Color.TRANSPARENT)
                }
                FONT -> {
                    binding.imgMemeData.visibility = View.GONE
                    binding.tvMemeData.visibility = View.VISIBLE
                    val typeFace = ResourcesCompat.getFont(
                        context,
                        memeToolIcon.attributes ?: R.font.poppins_medium
                    )
                    binding.tvMemeData.typeface = typeFace
                    binding.tvMemeData.text = memeToolIcon.text
                }
                BOUND -> {
                    binding.imgMemeData.visibility = View.VISIBLE
                    binding.tvMemeData.visibility = View.GONE
                    binding.imgMemeData.setImageResource(
                        memeToolIcon.icon ?: R.drawable.ic_place_holder
                    )
                    binding.imgMemeData.setColorFilter(
                        ContextCompat.getColor(
                            context,
                            memeToolIcon.colorPrimary ?: R.color.black
                        )
                    )
                }
            }
        }
    }
}