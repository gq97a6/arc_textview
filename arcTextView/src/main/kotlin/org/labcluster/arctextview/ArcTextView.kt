package org.labcluster.arctextview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.withRotation

class ArcTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL_AND_STROKE
    }

    private val debugPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
    }

    private val viewRect = RectF()
    private val textPath = Path()
    private var textHeight = 0f

    var text: String = "placeholder"
        set(value) {
            if (field != value) {
                field = value
                updateTextMeasurements()
                invalidate()
            }
        }

    var textSize: Float = 30f
        set(value) {
            if (field != value) {
                field = value
                updatePaintProperties()
                updateTextMeasurements()
                invalidate()
            }
        }

    var textColor: Int = Color.GRAY
        set(value) {
            if (field != value) {
                field = value
                updatePaintProperties()
                invalidate()
            }
        }

    var textStyle: Int = Typeface.NORMAL
        set(value) {
            if (field != value) {
                field = value
                updatePaintProperties()
                updateTextMeasurements()
                invalidate()
            }
        }

    var fontFamily: String? = null
        set(value) {
            if (field != value) {
                field = value
                updatePaintProperties()
                updateTextMeasurements()
                invalidate()
            }
        }

    var angle: Float = -90f
        set(value) {
            if (field != value) {
                field = value
                invalidate()
            }
        }

    var textPlacement: TextPlacement = TextPlacement.OUTER
        set(value) {
            if (field != value) {
                field = value
                invalidate()
            }
        }

    var textOrientation: TextOrientation = TextOrientation.OUTWARD
        set(value) {
            if (field != value) {
                field = value
                updateTextPath()
                invalidate()
            }
        }

    var textDirection: TextDirection = TextDirection.CLOCKWISE
        set(value) {
            if (field != value) {
                field = value
                invalidate()
            }
        }

    var anchorType: AnchorType = AnchorType.CENTER
        set(value) {
            if (field != value) {
                field = value
                updateTextPath()
                invalidate()
            }
        }

    var drawDebugCircle: Boolean = false
        set(value) {
            if (field != value) {
                field = value
                invalidate()
            }
        }

    init {
        attrs?.let { parseAttributes(it) }
        updatePaintProperties()
        updateTextMeasurements()
    }

    private fun parseAttributes(attrs: AttributeSet) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ArcTextView, 0, 0
        ).apply {
            try {
                text = getString(R.styleable.ArcTextView_text) ?: text
                textSize = getDimension(R.styleable.ArcTextView_textSize, textSize)
                textColor = getColor(R.styleable.ArcTextView_textColor, textColor)
                textStyle = getInt(R.styleable.ArcTextView_textStyle, textStyle)
                fontFamily = getString(R.styleable.ArcTextView_fontFamily)
                angle = getFloat(R.styleable.ArcTextView_anchorAngle, angle)
                drawDebugCircle = getBoolean(R.styleable.ArcTextView_drawDebugCircle, drawDebugCircle)
                textPlacement = TextPlacement.fromInt(getInt(R.styleable.ArcTextView_textPlacement, textPlacement.ordinal))
                textOrientation = TextOrientation.fromInt(getInt(R.styleable.ArcTextView_textOrientation, textOrientation.ordinal))
                textDirection = TextDirection.fromInt(getInt(R.styleable.ArcTextView_textDirection, textDirection.ordinal))
                anchorType = AnchorType.fromInt(getInt(R.styleable.ArcTextView_anchorType, anchorType.ordinal))
            } finally {
                recycle()
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewRect.set(
            paddingLeft.toFloat(),
            paddingTop.toFloat(),
            (w - paddingRight).toFloat(),
            (h - paddingBottom).toFloat()
        )
        updateTextPath()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val isRenderReversed = textDirection.ordinal != textOrientation.ordinal

        textPaint.textAlign = when (anchorType) {
            AnchorType.START -> if (isRenderReversed) Paint.Align.RIGHT else Paint.Align.LEFT
            AnchorType.CENTER -> Paint.Align.CENTER
            AnchorType.END -> if (isRenderReversed) Paint.Align.LEFT else Paint.Align.RIGHT
        }

        val vOffset = if (textPlacement.ordinal != textOrientation.ordinal) textHeight else 0f
        val textToDraw = if (isRenderReversed) text.reversed() else text

        canvas.withRotation(angle, viewRect.centerX(), viewRect.centerY()) {
            drawTextOnPath(textToDraw, textPath, 0f, vOffset, textPaint)
        }

        if (drawDebugCircle) {
            debugPaint.color = textColor
            canvas.drawArc(viewRect, 0f, 360f, false, debugPaint)
        }
    }

    private fun updatePaintProperties() {
        textPaint.color = textColor
        textPaint.textSize = textSize
        textPaint.typeface = Typeface.create(fontFamily, textStyle)
    }

    private fun updateTextMeasurements() {
        val textBounds = Rect()
        textPaint.getTextBounds(text, 0, text.length, textBounds)
        textHeight = textBounds.height().toFloat()
    }

    private fun updateTextPath() {
        textPath.reset()
        val startAngle = if (anchorType == AnchorType.CENTER) 180f else 0f
        val sweepAngle = when (textOrientation) {
            TextOrientation.OUTWARD -> 360f
            TextOrientation.INWARD -> -360f
        }
        textPath.addArc(viewRect, startAngle, sweepAngle)
    }
}

enum class AnchorType {
    START, CENTER, END;
    companion object {
        fun fromInt(value: Int) = entries.getOrElse(value) { CENTER }
    }
}

enum class TextPlacement {
    OUTER, INNER;
    companion object {
        fun fromInt(value: Int) = entries.getOrElse(value) { OUTER }
    }
}

enum class TextOrientation {
    OUTWARD, INWARD;
    companion object {
        fun fromInt(value: Int) = entries.getOrElse(value) { OUTWARD }
    }
}

enum class TextDirection {
    CLOCKWISE, ANTICLOCKWISE;
    companion object {
        fun fromInt(value: Int) = entries.getOrElse(value) { CLOCKWISE }
    }
}