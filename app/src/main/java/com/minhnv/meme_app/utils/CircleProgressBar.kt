package com.minhnv.meme_app.utils

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.IntDef
import com.minhnv.meme_app.R
import kotlin.math.cos
import kotlin.math.sin

class CircleProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        const val DEFAULT_MAX = 100
        const val MAX_DEGREE = 360f
        const val LINEAR_START_DEGREE = 90f

        // mode progress shape line
        const val LINE = 0

        // mode progress type circle
        const val SOLID = 1

        // mode progress type circle compile line
        const val SOLID_LINE = 2

        // Linear gradient color
        const val LINEAR = 0

        // Radial gradient color
        const val RADIAL = 1

        // Scan gradient color
        const val SWEEP = 2

        const val STOP_ANIM_SIMPLE = 0
        const val STOP_ANIM_REVERSE = 1

        const val DEFAULT_START_DEGREE = -90f
        const val DEFAULT_LINE_COUNT = 45
        const val DEFAULT_LINE_WIDTH = 4f
        const val DEFAULT_PROGRESS_TEXT_SIZE = 21f
        const val DEFAULT_PROGRESS_STROKE_WIDTH = 1f

        const val COLOR_FFF2A670 = "#fff2a670"
        const val COLOR_FFD3D3D5 = "#ffe3e3e5"

        /**
         * Get text boundary
         */
        fun getTextBounds(text: String, paint: Paint): Rect {
            val rect = Rect()
            paint.getTextBounds(text, 0, text.length, rect)
            return rect
        }
    }

    private val mProgressRectF = RectF()
    private var mProgressTextRect = Rect()

    private val mProgressPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mProgressBackgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mProgressTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mProgressCenterPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var mRadius = 0f
    private var mCenterX = 0f
    private var mCenterY = 0f

    // show progress the unit is percent
    private var mShowValue: Boolean = true
        set(value) {
            field = value
            invalidate()
        }

    // set/get progress
    var mProgress = 0
        set(value) {
            field = value
            invalidate()
        }

    // max progress
    var mMax = DEFAULT_MAX
        set(value) {
            field = value
            invalidate()
        }

    // Only the dial type progress bar is useful,
    // indicating the number of dial scales
    var mLineCount = 0
        set(value) {
            field = value
            invalidate()
        }

    // Only the dial type progress bar is useful,
    // indicating the width of the tick mark
    var mLineWidth = 0f
        set(value) {
            field = value
            invalidate()
        }

    // Progress bar width
    var mProgressStrokeWidth = 0f
        set(value) {
            field = value
            mProgressRectF.inset(value / 2, value / 2)
            mProgressPaint.strokeWidth = value
            mProgressBackgroundPaint.strokeWidth = value
            invalidate()
        }

    // Progress bar text size
    var mProgressTextSize = 0f
        set(value) {
            field = value
            invalidate()
        }

    // Progress bar start linear gradient color
    @ColorInt
    var mProgressStartColor = Color.BLACK
        set(value) {
            field = value
            updateProgressShader()
            invalidate()
        }

    // Progress bar end linear gradient color
    @ColorInt
    var mProgressEndColor = Color.BLACK
        set(value) {
            field = value
            updateProgressShader()
            invalidate()
        }

    // Progress bar text color
    @ColorInt
    var mProgressTextColor = Color.BLACK
        set(value) {
            field = value
            invalidate()
        }

    // 进度条背景色
    @ColorInt
    var mProgressBackgroundColor = Color.WHITE
        set(value) {
            field = value
            mProgressBackgroundPaint.color = value
            invalidate()
        }

    // The filling color in the middle of the control
    // (only the dial type and linear progress are valid,
    // the sector is not filled)
    @ColorInt
    var mCenterColor: Int = Color.TRANSPARENT
        set(value) {
            field = value
            mProgressCenterPaint.color = value
            invalidate()
        }

    // The starting angle of the progress bar rotation. Default -90
    var mStartDegree = -90f
        set(value) {
            field = value
            invalidate()
        }

    // Whether to draw the background color only outside the progress bar
    var mDrawBackgroundOutsideProgress = false
        set(value) {
            field = value
            invalidate()
        }

    // Centered picture (only drawn in dial and linear progress,
    // not drawn in fan-shaped progress)
    var mCenterDrawable: Drawable? = null
        set(value) {
            field = value
            invalidate()
        }

    // Format the progress value in a special format
    var mProgressFormatter: ProgressFormatter = DefaultProgressFormatter()
        set(value) {
            field = value
            invalidate()
        }

    /**Whether the animation has stopped, this judgment prevents repeated responses to stop the interface method */
    private var isStopedAnim = true

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(STOP_ANIM_SIMPLE, STOP_ANIM_REVERSE)
    annotation class StopAnimType

    // Stop animation type
    @StopAnimType
    var mStopAnimType = STOP_ANIM_SIMPLE

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(LINE, SOLID, SOLID_LINE)
    annotation class Style

    /**mode change type progress with type [LINEAR], [SOLID], [SOLID_LINE]*/
    @Style
    var mStyle = LINE
        set(value) {
            field = value
            mProgressPaint.style = if (value == SOLID) Paint.Style.FILL else Paint.Style.STROKE
            mProgressBackgroundPaint.style =
                if (value == SOLID) Paint.Style.FILL else Paint.Style.STROKE
            invalidate()
        }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(LINEAR, RADIAL, SWEEP)
    annotation class ShaderMode

    // Brush shader
    @ShaderMode
    var mShader = LINEAR
        set(value) {
            field = value
            updateProgressShader()
            invalidate()
        }

    // Progress and background brushes to draw the shape of both ends. Cap.ROUND (round line cap),
    // Cap.SQUARE (square line cap), Paint.Cap.BUTT (wireless cap)
    var mCap: Paint.Cap = Paint.Cap.BUTT
        set(value) {
            field = value
            mProgressPaint.strokeCap = value
            mProgressBackgroundPaint.strokeCap = value
            invalidate()
        }

    /**Press to listen*/
    var mOnPressedListener: OnPressedListener? = null

    // Canvas provider
    var mCanvasProvider: ICanvasProvider? = null

    // Progress bar animation
    private var mAnimator: ValueAnimator? = null

    // Whether to support continuous progress loading
    var isContinuable: Boolean = false
        set(value) {
            field = value
            if (value) {
                mAnimator?.repeatCount = 0
            }
        }

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar, defStyleAttr, 0)
            .apply {
                try {
                    mLineCount =
                        getInt(R.styleable.CircleProgressBar_cpb_line_count, DEFAULT_LINE_COUNT)
                    mStopAnimType =
                        getInt(R.styleable.CircleProgressBar_cpb_stop_anim_type, STOP_ANIM_SIMPLE)
                    mStyle = getInt(R.styleable.CircleProgressBar_cpb_style, LINE)
                    mShader = getInt(R.styleable.CircleProgressBar_cpb_shader, LINEAR)
                    mCap =
                        if (hasValue(R.styleable.CircleProgressBar_cpb_stroke_cap)) {
                            Paint.Cap.values()[
                                getInt(
                                    R.styleable.CircleProgressBar_cpb_stroke_cap,
                                    0
                                )
                            ]
                        } else {
                            Paint.Cap.BUTT
                        }
                    mLineWidth = getDimension(
                        R.styleable.CircleProgressBar_cpb_line_width,
                        dip2px(context, DEFAULT_LINE_WIDTH)
                    )
                    mProgressTextSize = getDimension(
                        R.styleable.CircleProgressBar_cpb_text_size,
                        DEFAULT_PROGRESS_TEXT_SIZE
                    )
                    mProgressStrokeWidth = getDimension(
                        R.styleable.CircleProgressBar_cpb_stroke_width,
                        DEFAULT_PROGRESS_STROKE_WIDTH
                    )
                    mProgressStartColor = getColor(
                        R.styleable.CircleProgressBar_cpb_start_color,
                        Color.parseColor(COLOR_FFF2A670)
                    )
                    mProgressEndColor = getColor(
                        R.styleable.CircleProgressBar_cpb_end_color,
                        Color.parseColor(COLOR_FFF2A670)
                    )
                    mProgressTextColor = getColor(
                        R.styleable.CircleProgressBar_cpb_text_color,
                        Color.parseColor(COLOR_FFF2A670)
                    )
                    mProgressBackgroundColor = getColor(
                        R.styleable.CircleProgressBar_cpb_background_color,
                        Color.parseColor(COLOR_FFD3D3D5)
                    )
                    mStartDegree = getFloat(
                        R.styleable.CircleProgressBar_cpb_start_degree,
                        DEFAULT_START_DEGREE
                    )
                    mDrawBackgroundOutsideProgress = getBoolean(
                        R.styleable.CircleProgressBar_cpb_drawBackgroundOutsideProgress,
                        false
                    )
                    mCenterColor =
                        getColor(R.styleable.CircleProgressBar_cpb_center_color, Color.TRANSPARENT)
                    mShowValue = getBoolean(R.styleable.CircleProgressBar_cpb_show_value, true)
                    mCenterDrawable = getDrawable(R.styleable.CircleProgressBar_cpb_center_src)
                    isContinuable = getBoolean(R.styleable.CircleProgressBar_cpb_continuable, false)
                } finally {
                    recycle()
                }
            }
        initPaint()
    }

    private fun initPaint() {
        mProgressTextPaint.textAlign = Paint.Align.CENTER
        mProgressTextPaint.textSize = mProgressTextSize

        mProgressPaint.style = if (mStyle == SOLID) Paint.Style.FILL else Paint.Style.STROKE
        mProgressPaint.strokeWidth = mProgressStrokeWidth
        mProgressPaint.color = mProgressStartColor
        mProgressPaint.strokeCap = mCap

        mProgressBackgroundPaint.style =
            if (mStyle == SOLID) Paint.Style.FILL else Paint.Style.STROKE
        mProgressBackgroundPaint.strokeWidth = mProgressStrokeWidth
        mProgressBackgroundPaint.color = mProgressBackgroundColor
        mProgressBackgroundPaint.strokeCap = mCap

        mProgressCenterPaint.style = Paint.Style.FILL
        mProgressCenterPaint.color = mCenterColor
    }

    /**
     * update shader
     * Need to be executed in onSizeChanged
     * @link [onSizeChanged](int, int, int, int)
     *
     */
    private fun updateProgressShader() {
        if (mProgressStartColor != mProgressEndColor) {
            var shader: Shader? = null
            when (mShader) {
                LINEAR -> {
                    shader = LinearGradient(
                        mProgressRectF.left,
                        mProgressRectF.top,
                        mProgressRectF.left,
                        mProgressRectF.bottom,
                        mProgressStartColor,
                        mProgressEndColor,
                        Shader.TileMode.CLAMP
                    )
                    val matrix = Matrix()
                    matrix.setRotate(LINEAR_START_DEGREE, mCenterX, mCenterY)
                    shader.getLocalMatrix(matrix)
                }
                RADIAL -> {
                    if (mRadius <= 0) return
                    shader = RadialGradient(
                        mCenterX,
                        mCenterY,
                        mRadius,
                        mProgressStartColor,
                        mProgressEndColor,
                        Shader.TileMode.CLAMP
                    )
                }
                SWEEP -> {
                    if (mRadius <= 0) return
                    val radian = mProgressStrokeWidth / Math.PI * 2f / mRadius
                    val rotateDegrees: Float =
                        -(if (mCap == Paint.Cap.BUTT && mStyle == SOLID_LINE) {
                            0f
                        } else {
                            Math.toDegrees(radian).toFloat()
                        })
                    shader = SweepGradient(
                        mCenterX,
                        mCenterY,
                        intArrayOf(mProgressStartColor, mProgressEndColor),
                        floatArrayOf(0f, 1f)
                    )
                    val matrix = Matrix()
                    matrix.setRotate(rotateDegrees, mCenterX, mCenterY)
                    shader.setLocalMatrix(matrix)
                }
            }
            mProgressPaint.shader = shader
        } else {
            mProgressPaint.shader = null
            mProgressPaint.color = mProgressStartColor
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.save()
        canvas?.rotate(mStartDegree, mCenterX, mCenterY)
        drawProgress(canvas)
        canvas?.restore()

        drawCenterColor(canvas)

        drawCenterDrawable(canvas)

        drawProgressText(canvas)

        canvas?.save()
        mCanvasProvider?.provideCanvas(mCenterX, mCenterY, mRadius, canvas)
        canvas?.restore()
    }

    /**
     * init text progress
     */
    private fun drawProgressText(canvas: Canvas?) {
        if (mProgressFormatter == null || !mShowValue) {
            return
        }

        val progressText = mProgressFormatter.format(mProgress, mMax)

        if (TextUtils.isEmpty(progressText)) return

        mProgressTextPaint.textSize = mProgressTextSize
        mProgressTextPaint.color = mProgressTextColor
        mProgressTextRect = getTextBounds(progressText.toString(), mProgressTextPaint)
        canvas?.drawText(
            progressText,
            0,
            progressText.length,
            mCenterX,
            mCenterY + mProgressTextRect.height() / 2,
            mProgressTextPaint
        )
    }

    /**
     *When the color is not filled in the middle, draw the center color
     */
    private fun drawCenterColor(canvas: Canvas?) {
        if (mStyle == LINE || mStyle == SOLID_LINE) {
            canvas?.drawCircle(
                mCenterX,
                mCenterY,
                mRadius - mProgressStrokeWidth,
                mProgressCenterPaint
            )
        }
    }

    /**
     * Draw a centered picture
     */
    private fun drawCenterDrawable(canvas: Canvas?) {
        try {
            if ((mStyle == LINE || mStyle == SOLID_LINE) && mCenterDrawable != null) {
                val bmp: Bitmap = (mCenterDrawable as BitmapDrawable).bitmap
                val bmpWidth = bmp.width
                val bmpHeight = bmp.height
                val bmpRect = Rect(0, 0, bmpWidth, bmpHeight)
                val desLeft: Int = (mCenterX - bmpWidth / 2 + mProgressStrokeWidth).toInt()
                val desTop: Int = (mCenterY - bmpHeight / 2 + mProgressStrokeWidth).toInt()
                val desRight: Int = (mCenterX + bmpWidth / 2 - mProgressStrokeWidth).toInt()
                val desBottom: Int = (mCenterX + bmpWidth / 2 - mProgressStrokeWidth).toInt()
                val desRect = Rect(desLeft, desTop, desRight, desBottom)
                canvas?.drawBitmap(bmp, bmpRect, desRect, mProgressCenterPaint)
            }
        } catch (e: Exception) {
            println("CircleProgressBar :Exception:${e.message}")
        }
    }

    /**
     *Drawing progress style
     */
    private fun drawProgress(canvas: Canvas?) {
        when (mStyle) {
            SOLID -> drawSolidProgress(canvas)
            SOLID_LINE -> drawSolidLineProgress(canvas)
            else -> drawLineProgress(canvas)
        }
    }

    /**
     *Draw dial-like linear ring in the center
     */
    private fun drawLineProgress(canvas: Canvas?) {
        val unitDegrees: Float = (2f * Math.PI / mLineCount).toFloat()
        val outerCircleRadius = mRadius
        val interCircleRadius = mRadius - mLineWidth

        val progressLineCount = mProgress.toFloat() / mMax * mLineCount

        for (i in 0 until mLineCount) {
            val rotateDegrees = i * -unitDegrees

            val startX: Float =
                (mCenterX + cos(rotateDegrees.toDouble()) * interCircleRadius).toFloat()
            val startY: Float =
                (mCenterY - sin(rotateDegrees.toDouble()) * interCircleRadius).toFloat()

            val stopX: Float =
                (mCenterX + cos(rotateDegrees.toDouble()) * outerCircleRadius).toFloat()
            val stopY: Float =
                (mCenterY - sin(rotateDegrees.toDouble()) * outerCircleRadius).toFloat()

            if (mDrawBackgroundOutsideProgress) {
                if (i >= progressLineCount) canvas?.drawLine(
                    startX,
                    startY,
                    stopX,
                    stopY,
                    mProgressBackgroundPaint
                )
            } else {
                canvas?.drawLine(startX, startY, stopX, stopY, mProgressBackgroundPaint)
            }

            if (i < progressLineCount) canvas?.drawLine(
                startX,
                startY,
                stopX,
                stopY,
                mProgressPaint
            )
        }
    }

    /**
     * Draw a solid sector arc
     */
    private fun drawSolidProgress(canvas: Canvas?) {
        if (mDrawBackgroundOutsideProgress) {
            val startAngle: Float = MAX_DEGREE * mProgress / mMax
            val sweepAngle: Float = MAX_DEGREE - startAngle
            canvas?.drawArc(mProgressRectF, startAngle, sweepAngle, true, mProgressBackgroundPaint)
        } else {
            canvas?.drawArc(mProgressRectF, 0f, MAX_DEGREE, true, mProgressBackgroundPaint)
        }
        canvas?.drawArc(mProgressRectF, 0f, MAX_DEGREE * mProgress / mMax, true, mProgressPaint)
    }

    /**
     * Draw a solid line sector arc
     */
    private fun drawSolidLineProgress(canvas: Canvas?) {
        if (mDrawBackgroundOutsideProgress) {
            val startAngle: Float = MAX_DEGREE * mProgress / mMax
            val sweepAngle: Float = MAX_DEGREE - startAngle
            canvas?.drawArc(mProgressRectF, startAngle, sweepAngle, false, mProgressBackgroundPaint)
        } else {
            canvas?.drawArc(mProgressRectF, 0f, MAX_DEGREE, false, mProgressBackgroundPaint)
        }
        canvas?.drawArc(mProgressRectF, 0f, MAX_DEGREE * mProgress / mMax, false, mProgressPaint)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCenterX = w / 2f
        mCenterY = h / 2f

        mRadius = Math.min(mCenterX, mCenterY)
        mProgressRectF.top = mCenterY - mRadius
        mProgressRectF.bottom = mCenterY + mRadius
        mProgressRectF.left = mCenterX - mRadius
        mProgressRectF.right = mCenterX + mRadius

        updateProgressShader()

        // 防止进度条被裁剪
        mProgressRectF.inset(mProgressStrokeWidth / 2, mProgressStrokeWidth / 2)
    }

    /**
     * Start animation
     * @param duration Animation execution time. Default 1s
     * @param start The progress when the animation starts. Default 0
     * @param end Progress when the animation ends. Default maximum
     * @param repeatCount The number of times the animation is repeated.
     * The default is 0, no repeat, ValueAnimator.INFINITE (infinite loop)
     */
    @JvmOverloads
    fun startAnimator(
        duration: Long = 1000,
        start: Int = 0,
        end: Int = mMax,
        repeatCount: Int = 0
    ) {
        if (mAnimator == null) {
            mAnimator = ValueAnimator()
            mAnimator?.addUpdateListener {
                mProgress = it.animatedValue as Int
                mOnPressedListener?.onPressProcess(mProgress)
                if (mProgress == end && !isStopedAnim) {
                    mOnPressedListener?.onPressEnd()
                    isStopedAnim = true
                    mAnimator?.cancel()
                }
            }
        }
        val s = if (isContinuable) {
            if (mProgress >= mMax) 0 else mProgress
        } else if (start < 0) 0 else if (start > mMax) mMax else start
        val e = if (end > mMax) mMax else if (end < 0) 0 else end
        mAnimator?.setIntValues(s, e)
        mAnimator?.duration = (1f * Math.abs(e - s) / mMax * duration).toLong()
        mAnimator?.repeatCount = if (isContinuable) 0 else repeatCount
        mAnimator?.start()

        mOnPressedListener?.onPressStart()
        isStopedAnim = false
    }

    /**
     * Stop animation
     */
    fun stopAnimator() {
        if (mAnimator != null && mAnimator!!.isRunning && !isStopedAnim) {
            if (!isStopedAnim) {
                mOnPressedListener?.onPressInterrupt(mAnimator!!.animatedValue as Int)
                isStopedAnim = true
            }
            if (isContinuable) {
                mAnimator?.cancel()
            } else {
                if (mStopAnimType == STOP_ANIM_SIMPLE) {
                    // 直接停止动画并恢复到进度0
                    mAnimator?.cancel()
                    mProgress = 0
                } else {
                    // 动画回退到进度0
                    mAnimator?.reverse()
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null || !isClickable) {
            return true
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startAnimator()
            }
            MotionEvent.ACTION_UP -> {
                stopAnimator()
            }
            MotionEvent.ACTION_MOVE -> {
                if (!isValid(event.x, event.y)) {
                    stopAnimator()
                }
            }
            MotionEvent.ACTION_CANCEL -> {
                stopAnimator()
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     *Whether the touch point is within the control range
     */
    fun isValid(touchX: Float, touchY: Float): Boolean {
        return touchX >= 0 && touchX <= width && touchY >= 0 && touchY <= height
    }

    interface OnPressedListener {
        fun onPressStart()

        fun onPressProcess(progress: Int)

        fun onPressInterrupt(progress: Int)

        fun onPressEnd()
    }

    interface ProgressFormatter {
        fun format(progress: Int, max: Int): CharSequence
    }

    class DefaultProgressFormatter : ProgressFormatter {
        private val defaultPattern = "%d%%"

        override fun format(progress: Int, max: Int): CharSequence {
            return String.format(
                defaultPattern,
                (progress.toFloat() / max.toFloat() * 100).toInt()
            )
        }
    }

    interface ICanvasProvider {
        fun provideCanvas(centerX: Float, centerY: Float, radius: Float, canvas: Canvas?)
    }

    class SavedState : BaseSavedState {

        var progress = 0

        constructor(source: Parcelable) : super(source)
        constructor(source: Parcel) : super(source) {
            progress = source.readInt()
        }

        override fun writeToParcel(out: Parcel?, flags: Int) {
            super.writeToParcel(out, flags)
            out?.writeInt(progress)
        }

        companion object CREATOR : Parcelable.Creator<SavedState> {
            override fun createFromParcel(parcel: Parcel): SavedState {
                return SavedState(parcel)
            }

            override fun newArray(size: Int): Array<SavedState?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        // Force preservation of ancestor class state
        val superState = super.onSaveInstanceState()
        val ss = superState?.let { SavedState(it) }

        ss?.progress = mProgress

        return ss
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val ss: SavedState = state as SavedState
        super.onRestoreInstanceState(ss.superState)

        mProgress = ss.progress
    }

    private fun dip2px(context: Context, dpValue: Float): Float {
        val scale = context.resources.displayMetrics.density
        return dpValue * scale + .5f
    }
}
