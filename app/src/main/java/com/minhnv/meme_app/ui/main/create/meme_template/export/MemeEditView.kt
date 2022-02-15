package com.minhnv.meme_app.ui.main.create.meme_template.export

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.MotionEvent
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
        println("#positionInfo rect: left: $left, top: $top, right: $right, bottom: $bottom")
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
        val widthSpec = MeasureSpec.makeMeasureSpec(newRect.width(), MeasureSpec.EXACTLY)
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
        val x = rect.left.toFloat()
        val y = rect.top.toFloat()
        imgView.setOnClickListener {
            imageViewAttention = it as ImageView
            imageViewAttention!!.setPadding(20)
            addIconIntoImg(imageViewAttention!!)
        }
        imgView.tag = icon
        // Measure the view at the exact dimensions (otherwise the text won't center correctly)
        val widthSpec = MeasureSpec.makeMeasureSpec(rect.width(), MeasureSpec.EXACTLY)
        val heightSpec = MeasureSpec.makeMeasureSpec(rect.height(), MeasureSpec.EXACTLY)
        val params = LayoutParams(widthSpec, heightSpec)
        imgView.layoutParams = params
        imgView.x = x
        imgView.y = y
        this.addView(imgView)
        invalidate()
    }

    private var isMoveIcon = false

    var xFirst = 0F
    var yFirst = 0F

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        // Let the ScaleGestureDetector inspect all events.
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                if (imageViewAttention != null) {
                    xFirst = ev.x
                    yFirst = ev.y
                }
                isMoveIcon = false
                actionUserMoveImage?.invoke(true)
            }
            MotionEvent.ACTION_MOVE -> {
                actionUserMoveImage?.invoke(true)
                when {
                    imageViewAttention != null -> {
                        moveImgView(imageViewAttention!!, ev.x, ev.y)
                        isMoveIcon = true
                    }
                    else -> {
                        notifyUserSelectEditView?.invoke(Unit)
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                if (imageViewAttention != null && isMoveIcon) {
                    val icon = imageViewAttention!!.tag.toString().toInt()
                    val left = imageViewAttention!!.x
                    val top = imageViewAttention!!.y
                    val right = left + imageViewAttention!!.width
                    val bottom = top + imageViewAttention!!.height
                    val rect = Rect(left.toInt(), top.toInt(), right.toInt(), bottom.toInt())
                    memeIcons.firstOrNull { it.meme == icon }?.let {
                        removeMarkView()
                        didIconAddIntoView?.invoke(it, rect)
                    }
                }
                imageViewAttention = null
                isMoveIcon = false
                xFirst = 0F
                yFirst = 0F
                actionUserMoveImage?.invoke(false)
                performClick()
            }
        }
        return false
    }

    private fun moveImgView(imageViewAttention: ImageView, newX: Float, newY: Float) {
        if (xFirst == 0F || yFirst == 0F) {
            return
        }
        val xImg = imageViewAttention.x
        val yImg = imageViewAttention.y
        imageViewAttention.x = if (newX > xFirst) {
            xImg + (newX - xFirst)
        } else {
            xImg - (xFirst - newX)
        }

        imageViewAttention.y = if (newY > yFirst) {
            yImg + (newY - yFirst)
        } else {
            yImg - (yFirst - newY)
        }
        addIconIntoImg(imageViewAttention)
        xFirst = newX
        yFirst = newY
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    private val blueMarkView = BlueMarkView(context)
    private val blueMarkView1 = BlueMarkView(context)
    private val blueMarkView2 = BlueMarkView(context)
    private val blueMarkView3 = BlueMarkView(context)

    private fun addIconIntoImg(imageView: ImageView) {
        val firstLeft = imageView.x
        val firstTop = imageView.y
        val firstRight = imageView.x + imageView.width
        val firstBottom = imageView.y + imageView.bottom
        blueMarkView.cx = firstLeft.toInt()
        blueMarkView.cy = firstTop.toInt()

        blueMarkView1.cx = firstRight.toInt()
        blueMarkView1.cy = firstTop.toInt()

        blueMarkView2.cx = firstLeft.toInt()
        blueMarkView2.cy = firstBottom.toInt()

        blueMarkView3.cx = firstRight.toInt()
        blueMarkView3.cy = firstBottom.toInt()

        removeMarkView()

        this.addView(blueMarkView)
        this.addView(blueMarkView1)
        this.addView(blueMarkView2)
        this.addView(blueMarkView3)
    }

    private fun removeMarkView() {
        removeView(blueMarkView)
        removeView(blueMarkView1)
        removeView(blueMarkView2)
        removeView(blueMarkView3)
    }
}
