package com.ismealdi.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.Dimension
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout

/**
 * Created by Al
 * on 06/04/19 | 19:43
 */
class AmTextInputLayout : TextInputLayout {

    private var editTextType: Int = 0
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
        typeface = font
    }

    @SuppressLint("CustomViewStyleable")
    private fun setValues(attrs: AttributeSet?) {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.AmView)
        try {
            val n = attr.indexCount
            for (i in 0 until n) {
                when (val attribute = attr.getIndex(i)) {
                    R.styleable.AmView_am_edit_text_style -> editTextType =
                        attr.getInt(attribute, 0)
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
        setErrorTextColor(ContextCompat.getColorStateList(context, R.color.colorAmError))
    }

    override fun setErrorEnabled(enabled: Boolean) {
        super.setErrorEnabled(enabled)

        if (!enabled) {
            return
        }

        try {
            val font = Typeface.createFromAsset(context.assets, Configure.View.Name[fontName] + Configure.View.Style[fontStyle] + Configure.View.Type)

            this.findViewById<AppCompatTextView?>(com.google.android.material.R.id.textinput_error)?.let {
                val errorViewParent = it.parent as FrameLayout?

                it.typeface = font
                it.setTextSize(Dimension.SP, 10f)

                errorViewParent?.layoutParams = LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT
                )

                it.gravity = Gravity.END
                it.textAlignment = View.TEXT_ALIGNMENT_TEXT_END

            }
        } catch (e: Exception) { // At least log what went wrong
            e.printStackTrace()
        }
    }


}