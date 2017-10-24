package com.xfinity.dahdit

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

class DashedLine
@JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr) {

    var dashHeight: Float
    var dashLength: Float
    var minimumDashGap: Float
    var orientation = Orientation.HORIZONTAL

    val path: Path = Path()
    val paint: Paint = Paint()

    init {
        val metrics = resources.displayMetrics
        val twoDpDefault = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, metrics)
        val defaultBlack = Color.argb(255, 0, 0, 0)

        if (attrs != null) {
            val typedArray = context?.theme?.obtainStyledAttributes(attrs, R.styleable.DashedLine, defStyleAttr, 0)
            dashHeight = typedArray?.getDimension(R.styleable.DashedLine_dashHeight, twoDpDefault) ?: twoDpDefault
            dashLength = typedArray?.getDimension(R.styleable.DashedLine_dashLength, twoDpDefault) ?: twoDpDefault
            minimumDashGap = typedArray?.getDimension(R.styleable.DashedLine_minimumDashGap, twoDpDefault) ?: twoDpDefault
            paint.color = typedArray?.getColor(R.styleable.DashedLine_dashColor, defaultBlack) ?: defaultBlack
            val orientationOrdinal = typedArray?.getInt(R.styleable.DashedLine_orientation, DottedLine.Orientation.HORIZONTAL.ordinal)
                    ?: DottedLine.Orientation.HORIZONTAL.ordinal
            if (orientationOrdinal == DottedLine.Orientation.VERTICAL.ordinal) {
                orientation = Orientation.VERTICAL
            } else {
                orientation = Orientation.HORIZONTAL
            }
            typedArray?.recycle()
        } else {
            dashHeight = twoDpDefault;
            dashLength = twoDpDefault;
            minimumDashGap = twoDpDefault;
            paint.color = defaultBlack
        }

        paint.strokeWidth = dashHeight
        paint.style = Paint.Style.STROKE


    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (orientation == Orientation.HORIZONTAL) {
            val widthNeeded = paddingLeft + paddingRight + suggestedMinimumWidth
            val width = resolveSize(widthNeeded, widthMeasureSpec)

            val heightNeeded = paddingTop + paddingBottom + dashHeight
            val height = resolveSize(heightNeeded.toInt(), heightMeasureSpec)

            setMeasuredDimension(width, height)
        } else {
            val widthNeeded = paddingLeft + paddingRight + dashHeight
            val width = resolveSize(widthNeeded.toInt(), widthMeasureSpec)

            val heightNeeded = paddingTop + paddingBottom + suggestedMinimumHeight
            val height = resolveSize(heightNeeded, heightMeasureSpec)

            setMeasuredDimension(width, height)
        }
    }

    override fun onDraw(canvas: Canvas) {
        path.reset()
        path.moveTo(paddingLeft.toFloat(), paddingTop.toFloat())
        if (orientation == Orientation.HORIZONTAL) {
            val w = canvas.width - paddingLeft - paddingRight
            val d = dashLength
            val m = minimumDashGap
            val c: Int = Math.floor(((w - d) / (d + m)).toDouble()).toInt()
            val g: Float = (w - (d * (c + 1))) / c
            path.lineTo((canvas.width - paddingLeft - paddingRight).toFloat(),
                    paddingTop.toFloat())
            paint.pathEffect = DashPathEffect(floatArrayOf(d, g), 0f)
        } else {
            val h = (canvas.height - paddingTop - paddingBottom).toFloat()
            val d = dashLength
            val m = minimumDashGap
            val c: Int = Math.floor(((h - d) / (d + m)).toDouble()).toInt()
            val g: Float = (h - (d * (c + 1))) / c
            paint.pathEffect = DashPathEffect(floatArrayOf(d, g), 0f)
            path.lineTo(paddingLeft.toFloat(), h)
        }
        canvas.drawPath(path, paint)
    }

    enum class Orientation {
        HORIZONTAL, VERTICAL
    }

}