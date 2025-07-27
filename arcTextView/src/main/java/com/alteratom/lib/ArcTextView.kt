package com.alteratom.lib

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.use
import androidx.core.graphics.toColorInt
import androidx.core.graphics.withRotation

open class ArcTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    // Drawing objects
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val circlePath = Path()
    private val textBounds = Rect()
    private val arcBounds = RectF()

    // Text properties
    var text: String = ""
        set(value) {
            field = value
            invalidate()
        }

    var textSize: Float = 30f
        set(value) {
            field = value
            paint.textSize = value
            invalidate()
        }

    var textColor: Int = "#999999".toColorInt()
        set(value) {
            field = value
            paint.color = value
            invalidate()
        }

    private var textStyle: Int = Typeface.NORMAL
    private var fontFamily: String? = null

    // Layout properties
    var textPlacement: TextPlacement = TextPlacement.OUTER
        set(value) {
            field = value
            invalidate()
        }

    var textOrientation: TextOrientation = TextOrientation.OUTWARD
        set(value) {
            field = value
            invalidate()
        }

    var textDirection: TextDirection = TextDirection.CLOCKWISE
        set(value) {
            field = value
            invalidate()
        }

    var angle: Float = -90f
        set(value) {
            field = value
            invalidate()
        }

    var anchorType: AnchorType = AnchorType.ANCHOR_CENTER
        set(value) {
            field = value
            invalidate()
        }

    var drawDebugCircle: Boolean = false
        set(value) {
            field = value
            invalidate()
        }

    init {
        if (attrs != null) {
            initAttributes(attrs)
        }
        setupPaint()
    }

    private fun initAttributes(attrs: AttributeSet) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ArcTextView,
            0,
            0
        ).use { typedArray ->
            text = typedArray.getString(R.styleable.ArcTextView_text) ?: "placeholder"
            textSize = typedArray.getDimension(R.styleable.ArcTextView_textSize, 30f)
            textColor = typedArray.getColor(
                R.styleable.ArcTextView_textColor,
                "#999999".toColorInt()
            )
            textStyle = typedArray.getInt(R.styleable.ArcTextView_textStyle, Typeface.NORMAL)

            textPlacement = TextPlacement.fromInt(
                typedArray.getInt(R.styleable.ArcTextView_textPlacement, 0)
            )
            textOrientation = TextOrientation.fromInt(
                typedArray.getInt(R.styleable.ArcTextView_textOrientation, 0)
            )
            textDirection = TextDirection.fromInt(
                typedArray.getInt(R.styleable.ArcTextView_textDirection, 0)
            )

            fontFamily = typedArray.getString(R.styleable.ArcTextView_fontFamily)
            angle = typedArray.getFloat(R.styleable.ArcTextView_anchorAngle, -90f)
            anchorType = AnchorType.fromInt(
                typedArray.getInt(R.styleable.ArcTextView_anchorType, 1)
            )
            drawDebugCircle = typedArray.getBoolean(R.styleable.ArcTextView_drawDebugCircle, false)
        }
    }

    private fun setupPaint() {
        paint.apply {
            style = Paint.Style.FILL_AND_STROKE
            textSize = this@ArcTextView.textSize
            color = textColor
            typeface = Typeface.create(fontFamily, textStyle)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateArcBounds()
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        updateArcBounds()
    }

    private fun updateArcBounds() {
        arcBounds.set(
            paddingLeft.toFloat(),
            paddingTop.toFloat(),
            width.toFloat() - paddingRight,
            height.toFloat() - paddingBottom
        )
    }

    override fun onDraw(canvas: Canvas) {
        if (text.isEmpty()) return

        // Update paint properties
        setupPaint()

        // Calculate text metrics
        paint.getTextBounds(text, 0, text.length, textBounds)
        val textHeight = textBounds.height().toFloat()

        // Prepare path
        circlePath.reset()
        val startAngle = if (anchorType == AnchorType.ANCHOR_CENTER) 180f else 0f
        val sweepAngle = when (textOrientation) {
            TextOrientation.OUTWARD -> 360f
            TextOrientation.INWARD -> -360f
        }
        circlePath.addArc(arcBounds, startAngle, sweepAngle)

        // Apply rotation
        canvas.withRotation(angle, arcBounds.centerX(), arcBounds.centerY()) {
            // Configure text alignment and direction
            val isReversed = textDirection.type != textOrientation.type
            paint.textAlign = getTextAlignment(anchorType, isReversed)

            // Calculate vertical offset
            val vOffset = if (textPlacement.type != textOrientation.type) textHeight else 0f

            // Draw text
            val textToDraw = if (isReversed) text.reversed() else text
            drawTextOnPath(textToDraw, circlePath, 0f, vOffset, paint)

            // Draw debug circle if enabled
            if (drawDebugCircle) {
                paint.style = Paint.Style.STROKE
                drawArc(arcBounds, 0f, 360f, true, paint)
            }
        }
    }

    private fun getTextAlignment(anchorType: AnchorType, isReversed: Boolean): Paint.Align {
        return when (anchorType) {
            AnchorType.ANCHOR_START -> if (isReversed) Paint.Align.RIGHT else Paint.Align.LEFT
            AnchorType.ANCHOR_CENTER -> Paint.Align.CENTER
            AnchorType.ANCHOR_END -> if (isReversed) Paint.Align.LEFT else Paint.Align.RIGHT
        }
    }
}

enum class AnchorType(val type: Int) {
    ANCHOR_START(0),
    ANCHOR_CENTER(1),
    ANCHOR_END(2);

    companion object {
        fun fromInt(value: Int): AnchorType = entries.getOrNull(value) ?: ANCHOR_CENTER
    }
}

enum class TextPlacement(val type: Int) {
    OUTER(0),
    INNER(1);

    companion object {
        fun fromInt(value: Int): TextPlacement = entries.getOrNull(value) ?: OUTER
    }
}

enum class TextOrientation(val type: Int) {
    OUTWARD(0),
    INWARD(1);

    companion object {
        fun fromInt(value: Int): TextOrientation = entries.getOrNull(value) ?: OUTWARD
    }
}

enum class TextDirection(val type: Int) {
    CLOCKWISE(0),
    ANTICLOCKWISE(1);

    companion object {
        fun fromInt(value: Int): TextDirection = entries.getOrNull(value) ?: CLOCKWISE
    }
}