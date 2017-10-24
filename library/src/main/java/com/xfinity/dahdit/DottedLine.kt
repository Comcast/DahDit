package com.xfinity.dahdit;

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

class DottedLine
@JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr) {

    var dotRadius: Float
    var minimumDotGap: Float
    var orientation = Orientation.HORIZONTAL

    val paint: Paint = Paint()

    init {
        val metrics = resources.displayMetrics
        val twoDpDefault = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, metrics)
        val defaultBlack = Color.argb(255, 0, 0, 0)

        if (attrs != null) {
            val typedArray = context?.theme?.obtainStyledAttributes(attrs, R.styleable.DottedLine, defStyleAttr, 0)
            dotRadius = typedArray?.getDimension(R.styleable.DottedLine_dotRadius, twoDpDefault) ?: twoDpDefault
            minimumDotGap = typedArray?.getDimension(R.styleable.DottedLine_minimumDotGap, twoDpDefault) ?: twoDpDefault
            paint.color = typedArray?.getColor(R.styleable.DottedLine_dotColor, defaultBlack) ?: defaultBlack
            val orientationOrdinal = typedArray?.getInt(R.styleable.DottedLine_orientation, Orientation.HORIZONTAL.ordinal)
                    ?: Orientation.HORIZONTAL.ordinal
            if (orientationOrdinal == Orientation.VERTICAL.ordinal) {
                orientation = Orientation.VERTICAL
            } else {
                orientation = Orientation.HORIZONTAL
            }
            typedArray?.recycle()
        } else {
            dotRadius = twoDpDefault
            minimumDotGap = twoDpDefault
            paint.color = defaultBlack
        }

        paint.style = Paint.Style.FILL
        paint.flags = Paint.ANTI_ALIAS_FLAG
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (orientation == Orientation.HORIZONTAL) {
            val widthNeeded = paddingLeft + paddingRight + suggestedMinimumWidth
            val width = resolveSize(widthNeeded, widthMeasureSpec)

            val heightNeeded = paddingTop + paddingBottom + 2 * dotRadius
            val height = resolveSize(heightNeeded.toInt(), heightMeasureSpec)

            setMeasuredDimension(width, height)
        } else {
            val widthNeeded = paddingLeft + paddingRight + 2 * dotRadius
            val width = resolveSize(widthNeeded.toInt(), widthMeasureSpec)

            val heightNeeded = paddingTop + paddingBottom + suggestedMinimumHeight
            val height = resolveSize(heightNeeded, heightMeasureSpec)

            setMeasuredDimension(width, height)
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (orientation == Orientation.HORIZONTAL) {
            val w = canvas.width - paddingLeft - paddingRight
            val d = 2 * dotRadius
            val m = minimumDotGap
            val c: Int = Math.floor(((w - d) / (d + m)).toDouble()).toInt()
            val g: Float = (w - (d * (c + 1))) / c
            for (i in 0..c) {
                canvas.drawCircle(paddingLeft + dotRadius + i * (d + g),
                        paddingTop + dotRadius,
                        dotRadius,
                        paint)
            }
        } else {
            val h = (canvas.height - paddingTop - paddingBottom).toFloat()
            val d = 2 * dotRadius
            val m = minimumDotGap
            val c: Int = Math.floor(((h - d) / (d + m)).toDouble()).toInt()
            val g: Float = (h - (d * (c + 1))) / c
            for (i in 0..c) {
                canvas.drawCircle(paddingLeft + dotRadius,
                        paddingTop + dotRadius + i * (d + g),
                        dotRadius,
                        paint)
            }
        }

    }

    enum class Orientation {
        HORIZONTAL, VERTICAL
    }
}