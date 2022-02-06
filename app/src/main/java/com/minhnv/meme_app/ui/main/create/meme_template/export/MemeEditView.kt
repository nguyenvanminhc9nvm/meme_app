package com.minhnv.meme_app.ui.main.create.meme_template.export

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.widget.EditText
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.setPadding
import com.bumptech.glide.Glide
import com.minhnv.meme_app.R

typealias SelectEditText = (Boolean) -> Unit
typealias ActionUserMoveImage = (Boolean) -> Unit
typealias NotifyUserSelectView = (Unit) -> Unit
typealias NotifyUserSelectEditView = (Unit) -> Unit

class MemeEditView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val listRect = mutableListOf<Rect?>()
    private lateinit var edittextAttention: EditText
    private var imageViewAttention: ImageView? = null
    var selectEditText: SelectEditText? = null
    var actionUserMoveImage: ActionUserMoveImage? = null
    var notifyUserSelectView: NotifyUserSelectView? = null
    var notifyUserSelectEditView: NotifyUserSelectEditView? = null

    fun set(context: Context, rect: MutableList<Rect?>) {
        listRect.clear()
        listRect.addAll(rect)
        listRect.forEach {
            addViewWithRect(context, it)
        }
        invalidate()
    }

    fun loadImage(context: Context, url: String) {
        val imageView = ImageView(context)
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        imageView.layoutParams = params
        Glide.with(context).load(url).into(imageView)
        addView(imageView)
    }

    private fun addViewWithRect(context: Context, rect: Rect?) {
        if (rect == null) {
            return
        }
        val editText = EditText(context)
        editText.setBackgroundResource(R.drawable.ic_edittext)
        editText.setOnFocusChangeListener { view, b ->
            selectEditText?.invoke(b)
            if (b) {
                edittextAttention = view as EditText
            }
        }
        // Measure the view at the exact dimensions (otherwise the text won't center correctly)
        val widthSpec = MeasureSpec.makeMeasureSpec(rect.width(), MeasureSpec.EXACTLY)
        val heightSpec = MeasureSpec.makeMeasureSpec(rect.height(), MeasureSpec.EXACTLY)
        val params = LayoutParams(widthSpec, heightSpec)
        editText.layoutParams = params
        editText.setPadding(5)
        editText.translationX = rect.left.toFloat()
        editText.translationY = rect.top.toFloat()
        this.addView(editText)
    }

    fun changeColor(color: Int) {
        if (this::edittextAttention.isInitialized) {
            edittextAttention.setTextColor(color)
        } else {
            notifyUserSelectEditView?.invoke(Unit)
        }
    }

    fun changeAlign(alignment: Int) {
        if (this::edittextAttention.isInitialized) {
            edittextAttention.gravity = alignment or Gravity.CENTER_VERTICAL
        } else {
            notifyUserSelectEditView?.invoke(Unit)
        }
    }

    fun changeFont(font: Int) {
        if (this::edittextAttention.isInitialized) {
            val typefaceBold = ResourcesCompat.getFont(context, font)
            edittextAttention.typeface = typefaceBold
        } else {
            notifyUserSelectEditView?.invoke(Unit)
        }
    }

    fun changeBound(drawable: Int) {
        if (this::edittextAttention.isInitialized) {
            edittextAttention.setBackgroundResource(drawable)
        } else {
            notifyUserSelectEditView?.invoke(Unit)
        }
    }

    fun addIcon(context: Context, icon: Int) {
        val imgView = ImageView(context)
        imgView.setImageResource(icon)
        val rect = Rect(
            100,
            100,
            250,
            250
        )
        imgView.setOnClickListener {
            imageViewAttention = it as ImageView
        }
        // Measure the view at the exact dimensions (otherwise the text won't center correctly)
        val widthSpec = MeasureSpec.makeMeasureSpec(rect.width(), MeasureSpec.EXACTLY)
        val heightSpec = MeasureSpec.makeMeasureSpec(rect.height(), MeasureSpec.EXACTLY)
        val params = LayoutParams(widthSpec, heightSpec)
        params.startToStart = 0
        params.endToEnd = 0
        params.topToTop = 0
        params.bottomToBottom = 0
        imgView.layoutParams = params
        imgView.setPadding(5)
        imgView.translationX = rect.left.toFloat()
        imgView.translationY = rect.top.toFloat()
        this.addView(imgView)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        // Let the ScaleGestureDetector inspect all events.
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                actionUserMoveImage?.invoke(true)
            }
            MotionEvent.ACTION_MOVE -> {
                actionUserMoveImage?.invoke(true)
                if (imageViewAttention != null) {
                    imageViewAttention!!.x = ev.x
                    imageViewAttention!!.y = ev.y
                } else {
                    notifyUserSelectEditView?.invoke(Unit)
                }
            }
            MotionEvent.ACTION_UP -> {
                actionUserMoveImage?.invoke(false)
                performClick()
            }
            MotionEvent.ACTION_POINTER_UP -> {
                actionUserMoveImage?.invoke(false)
            }
        }
        return true
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }
}