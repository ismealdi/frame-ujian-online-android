package com.ismealdi.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatCheckedTextView

/**
 * Created by Al
 * on 06/04/19 | 01:44
 */
class AmCheckedTextView : AppCompatCheckedTextView {

    private var fontStyle: Int = 0
    private var fontName: Int = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setValues(attrs)
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        setFont(fontStyle, fontName)
        setNewTypeFace()
    }

    private fun setNewTypeFace() {
        val font = Typeface.createFromAsset(
            context.assets,
            Configure.View.Name[fontName] + Configure.View.Style[fontStyle] + Configure.View.Type
        )
        setTypeface(font, Typeface.NORMAL)
    }

    @SuppressLint("CustomViewStyleable")
    private fun setValues(attrs: AttributeSet?) {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.AmView)
        try {
            val n = attr.indexCount
            for (i in 0 until n) {
                when (val attribute = attr.getIndex(i)) {
                    R.styleable.AmView_am_font_style -> fontStyle = attr.getInt(attribute, 0)
                    R.styleable.AmView_am_font_family -> fontName = attr.getInt(attribute, 0)
                    else -> Logs.i("$javaClass: $attribute")
                }
            }
        } finally {
            attr.recycle()
        }
    }

    private fun setFont(font: Int, name: Int) {
        fontStyle = font
        fontName = name
    }

}