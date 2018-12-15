package com.gg.shapeview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

/**
 * Creator : GG
 * Time    : 2017/12/11
 * Mail    : gg.jin.yu@gmail.com
 * Explain :
 */
class ShapeView : View {


    private var mCurrentShape: Shape = Shape.Circle

    private val mPaint: Paint by lazy {
        Paint().apply {
            isAntiAlias = true
        }
    }

    private val mPath: Path by lazy {
        Path().apply {
            moveTo(width / 2f, 0f)
            lineTo(0f, width / 2 * Math.sqrt(3.toDouble()).toFloat())
            lineTo(width.toFloat(), width / 2 * Math.sqrt(3.toDouble()).toFloat())
            close()
        }
    }

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(Math.min(width, height), Math.min(width, height))
    }


    override fun onDraw(canvas: Canvas?) {

        when (mCurrentShape) {
            Shape.Circle -> {
                mPaint.color = Color.RED
                val center = width / 2.toFloat()
                canvas?.drawCircle(center, center, center, mPaint)
            }
            Shape.Square -> {
                mPaint.color = Color.BLUE
                canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), mPaint)
            }
            Shape.Triangle -> {
                canvas?.drawPath(mPath, mPaint)
            }

        }
    }


    fun exchange() {
        mCurrentShape = when (mCurrentShape) {
            Shape.Circle -> Shape.Square
            Shape.Square -> Shape.Triangle
            Shape.Triangle -> Shape.Circle
        }
        invalidate()
    }

    fun getShape(): Shape {
        return mCurrentShape
    }

    enum class Shape {
        Circle, Square, Triangle
    }
}