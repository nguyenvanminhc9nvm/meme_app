package com.minhnv.meme_app.ui.main.create.meme_template.export

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.setPadding
import com.bumptech.glide.Glide
import com.minhnv.meme_app.R
import com.minhnv.meme_app.ui.main.MainActivity

typealias SelectEditText = (Boolean) -> Unit
typealias ActionUserMoveImage = (Boolean) -> Unit
typealias NotifyUserSelectView = (Unit) -> Unit
typealias NotifyUserSelectEditView = (Unit) -> Unit
typealias DidIconAddIntoView = (MemeIcon, Rect) -> Unit

class MemeEditView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val listRect = mutableListOf<Rect?>()
    private lateinit var edittextAttention: EdittextResizeAuto
    private var imageViewAttention: ImageView? = null
    var selectEditText: SelectEditText? = null
    var actionUserMoveImage: ActionUserMoveImage? = null
    var notifyUserSelectView: NotifyUserSelectView? = null
    var notifyUserSelectEditView: NotifyUserSelectEditView? = null
    var didIconAddIntoView: DidIconAddIntoView? = null
    var collapsingToolbarLayoutHeight = 0


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
        val displayMetrics = DisplayMetrics()
        (context as MainActivity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        val widthScreen = displayMetrics.widthPixels
        val baseWidth = 1070 * 1.0
        val baseHeight = 1070 * 1.0
        val oldLeft = rect?.left ?: 0
        val oldTop = rect?.top ?: 0
        val oldRight = rect?.right ?: 0
        val oldBottom = rect?.bottom ?: 0
        val ratioLeft = if (oldLeft == 0) 0.0 else (baseWidth / oldLeft)
        val ratioRight = if (oldRight == 0) 0.0 else (baseWidth / oldRight)
        val ratioTop = if (oldTop == 0) 0.0 else (baseHeight / oldTop)
        val ratioBottom = if (oldBottom == 0) 0.0 else (baseHeight / oldBottom)
        val left = if (ratioLeft == 0.0) 0 else widthScreen / ratioLeft
        val top = if (ratioTop == 0.0) 0 else widthScreen / ratioTop
        val right = if (ratioRight == 0.0) 0 else widthScreen / ratioRight
        val bottom = if (ratioBottom == 0.0) 0 else widthScreen / ratioBottom
        val newRect = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
        if (rect == null) {
            return
        }
        val editText = EdittextResizeAuto(context)
        editText.setBackgroundResource(R.drawable.ic_edittext)
        editText.setOnFocusChangeListener { view, b ->
            selectEditText?.invoke(b)
            if (b) {
                edittextAttention = view as EdittextResizeAuto
            }
        }
        val idView = (rect.top + rect.bottom + rect.left + rect.right) / 4
        editText.id = idView
        // Measure the view at the exact dimensions (otherwise the text won't center correctly)
        val widthSpec = View.MeasureSpec.makeMeasureSpec(newRect.width(), View.MeasureSpec.EXACTLY)
        val heightSpec = MeasureSpec.makeMeasureSpec(newRect.height(), MeasureSpec.EXACTLY)
        val params = LayoutParams(widthSpec, heightSpec)
        editText.layoutParams = params
        editText.setPadding(5)
        editText.translationX = newRect.left.toFloat()
        editText.translationY = newRect.top.toFloat()
        this.addView(editText)
    }

    fun changeColor(color: Int, colorShadow: Int) {
        if (this::edittextAttention.isInitialized) {
            edittextAttention.setTextColor(ContextCompat.getColor(context, color))
            edittextAttention.setShadowLayer(
                5F,
                0F,
                5F,
                ContextCompat.getColor(context, colorShadow)
            )
        } else {
            notifyUserSelectEditView?.invoke(Unit)
        }
    }

    fun changeAlign(alignment: Int) {
        if (this::edittextAttention.isInitialized) {
            edittextAttention.gravity = alignment
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

    fun changeBound(drawable: Int, colorTint: Int) {
        if (this::edittextAttention.isInitialized) {
            edittextAttention.background = ResourcesCompat.getDrawable(
                resources,
                drawable,
                null
            )
            DrawableCompat.setTint(
                edittextAttention.background,
                ContextCompat.getColor(context, colorTint)
            )
        } else {
            notifyUserSelectEditView?.invoke(Unit)
        }
    }

    fun changeSize(size: Int) {
        if (this::edittextAttention.isInitialized) {
            edittextAttention.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(size))
        } else {
            notifyUserSelectEditView?.invoke(Unit)
        }
    }

    fun changeLineSpacingExtra(line: Int) {
        if (this::edittextAttention.isInitialized) {
            edittextAttention.setLineSpacing(resources.getDimension(line), 1.0F)
        } else {
            notifyUserSelectEditView?.invoke(Unit)
        }
    }

    fun addIcon(context: Context, icon: Int, rect: Rect) {
        val imageExists = findViewWithTag<ImageView>(icon)
        if (imageExists != null) {
            removeView(imageExists)
        }
        val imgView = ImageView(context)
        imgView.setImageResource(icon)
        imgView.setOnClickListener {
            imageViewAttention = it as ImageView
            imageViewAttention!!.background =
                ResourcesCompat.getDrawable(resources, R.drawable.ic_pin_image, null)
            imageViewAttention!!.setPadding(20)
        }
        imgView.tag = icon
        // Measure the view at the exact dimensions (otherwise the text won't center correctly)
        val widthSpec = View.MeasureSpec.makeMeasureSpec(rect.width(), MeasureSpec.EXACTLY)
        val heightSpec = MeasureSpec.makeMeasureSpec(rect.height(), MeasureSpec.EXACTLY)
        val params = LayoutParams(widthSpec, heightSpec)
        imgView.layoutParams = params
        imgView.translationX = rect.left.toFloat()
        imgView.translationY = rect.top.toFloat()
        this.addView(imgView)
        invalidate()
    }

    private var isMoveIcon = false

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_MOVE -> {
                if (imageViewAttention != null) {
                    val locationArray = IntArray(2)
                    imageViewAttention!!.getLocationOnScreen(locationArray)
                    val left = locationArray[0]
                    val top = locationArray[1] - collapsingToolbarLayoutHeight
                    val right = locationArray[0] + imageViewAttention!!.width
                    val bottom =
                        (locationArray[1] + imageViewAttention!!.height) - collapsingToolbarLayoutHeight

                    Log.d(
                        "#Location", "onTouchEvent: " +
                                " x: ${ev.x}, y: ${ev.y}" +
                                "old: left: $left, top: $top right: $right, bottom: $bottom " +
                                "new: left: "
                    )
                }
            }
        }
        return true
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        // Let the ScaleGestureDetector inspect all events.
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                isMoveIcon = false
                actionUserMoveImage?.invoke(true)
            }
            MotionEvent.ACTION_MOVE -> {
                actionUserMoveImage?.invoke(true)
                if (imageViewAttention != null) {
                    imageViewAttention!!.x = ev.x
                    imageViewAttention!!.y = ev.y
                    isMoveIcon = true
                } else {
                    notifyUserSelectEditView?.invoke(Unit)
                }
            }
            MotionEvent.ACTION_UP -> {
                if (imageViewAttention != null && isMoveIcon) {
                    val icon = imageViewAttention!!.tag.toString().toInt()
                    val locationArray = IntArray(2)
                    imageViewAttention!!.getLocationOnScreen(locationArray)
                    val rectOutLocation = Rect(
                        locationArray[0],
                        locationArray[1] - collapsingToolbarLayoutHeight,
                        locationArray[0] + imageViewAttention!!.width,
                        (locationArray[1] + imageViewAttention!!.height) - collapsingToolbarLayoutHeight
                    )
                    memeIcons.firstOrNull { it.meme == icon }?.let {
                        didIconAddIntoView?.invoke(it, rectOutLocation)
                    }
                }
                imageViewAttention = null
                actionUserMoveImage?.invoke(false)
                performClick()
                isMoveIcon = false
            }
            MotionEvent.ACTION_POINTER_UP -> {
                actionUserMoveImage?.invoke(false)
            }
        }
        return false
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }
}
