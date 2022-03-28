package com.arctextview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.alteratom.arctextview.lib.R

open class ArcTextView(
    context: Context,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
) : View(context, attrs, defStyleAttr, defStyleRes) {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(
        context,
        attrs,
        defStyleAttr,
        0
    )

    private lateinit var paint: Paint

    var text: String
    val textSize: Float
    var textColor: Int
    val textStyle: Int
    val textPlacement: TextPlacement
    val textOrientation: TextOrientation
    val textDirection: TextDirection

    val fontFamily: String?

    var angle: Float
    var anchorType: AnchorType
    var drawDebugCircle: Boolean

    override fun onDraw(canvas: Canvas) {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.textSize = textSize
        paint.color = textColor
        paint.typeface = Typeface.create(fontFamily, textStyle)

        //Get text height
        val textBounds = Rect()
        paint.getTextBounds(text, 0, text.length, textBounds)
        val textHeight = textBounds.height().toFloat()


        var pad = intArrayOf(paddingLeft, paddingRight, paddingTop, paddingBottom)

        //Create rect
        val rectF = RectF(
            pad[0].toFloat(),
            pad[2].toFloat(),
            canvas.width.toFloat() - pad[1],
            canvas.height.toFloat() - pad[3]
        )

        //Draw path
        val circle = Path()
        circle.addArc(
            rectF,
            if (anchorType == AnchorType.ANCHOR_CENTER) 180f else 0f,
            when (textOrientation) {
                TextOrientation.OUTWARD -> 360f
                TextOrientation.INWARD -> -360f
            }
        )

        //Set angle
        canvas.rotate(angle, rectF.centerX(), rectF.centerY())

        (textDirection.type != textOrientation.type).let {

            //Set anchor type
            paint.textAlign = when (anchorType) {
                AnchorType.ANCHOR_START -> if (it) Paint.Align.RIGHT else Paint.Align.LEFT
                AnchorType.ANCHOR_CENTER -> Paint.Align.CENTER
                AnchorType.ANCHOR_END -> if (it) Paint.Align.LEFT else Paint.Align.RIGHT
            }

            //Set text placement
            var vOffset =
                if (textPlacement.type != textOrientation.type) textHeight else 0f

            //Draw text
            canvas.drawTextOnPath(
                if (it) text.reversed() else text,
                circle,
                0f,
                vOffset,
                paint
            )
        }

        if (drawDebugCircle) {
            paint.style = Paint.Style.STROKE
            canvas.drawArc(rectF, 0f, 360f, true, paint)
        }

        //invalidate()
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ArcTextView, 0, 0
        ).apply {
            try {
                text = getString(R.styleable.ArcTextView_text) ?: "placeholder"
                textSize = getDimension(R.styleable.ArcTextView_textSize, 30f)
                textColor = getColor(R.styleable.ArcTextView_textColor, Color.parseColor("#999999"))
                textStyle = getInt(R.styleable.ArcTextView_textStyle, Typeface.NORMAL)

                textPlacement =
                    TextPlacement.values()[getInt(R.styleable.ArcTextView_textPlacement, 0)]
                textOrientation =
                    TextOrientation.values()[getInt(R.styleable.ArcTextView_textOrientation, 0)]
                textDirection =
                    TextDirection.values()[getInt(R.styleable.ArcTextView_textDirection, 0)]

                fontFamily = getString(R.styleable.ArcTextView_fontFamily)

                angle = getFloat(R.styleable.ArcTextView_anchorAngle, -90f)
                anchorType = AnchorType.values()[getInt(R.styleable.ArcTextView_anchorType, 1)]

                drawDebugCircle = getBoolean(R.styleable.ArcTextView_drawDebugCircle, false)
            } finally {
                recycle()
            }
        }
    }
}

enum class AnchorType(val type: Int) {
    ANCHOR_START(0),
    ANCHOR_CENTER(1),
    ANCHOR_END(2)
}

enum class TextPlacement(val type: Int) {
    OUTER(0),
    INNER(1)
}

enum class TextOrientation(val type: Int) {
    OUTWARD(0),
    INWARD(1)
}

enum class TextDirection(val type: Int) {
    CLOCKWISE(0),
    ANTICLOCKWISE(1)
}



