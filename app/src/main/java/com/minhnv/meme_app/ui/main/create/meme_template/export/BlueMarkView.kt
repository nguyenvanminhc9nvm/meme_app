package com.minhnv.meme_app.ui.main.create.meme_template.export

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class BlueMarkView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val p = Paint()

    var cx = 0

    var cy = 0

    private var radius = 20

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        p.color = Color.WHITE
        p.strokeWidth = 5f
        p.style = Paint.Style.FILL_AND_STROKE
        canvas.drawCircle(cx.toFloat(), cy.toFloat(), radius.toFloat(), p)
        p.color = Color.rgb(0, 80, 255)
        canvas.drawCircle(cx.toFloat(), cy.toFloat(), (radius * 3 / 4).toFloat(), p)
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }
}
