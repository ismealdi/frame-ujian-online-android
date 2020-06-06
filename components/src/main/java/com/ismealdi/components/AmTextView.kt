package com.ismealdi.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat.getColor
import com.ismealdi.components.ext.CustomTypefaceSpan

/**
 * Created by Al
 * on 06/04/19 | 01:44
 */
class AmTextView : AppCompatTextView {

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

        //setAutoSizeTextTypeUniformWithPresetSizes(intArrayOf(8,10,12,14,16,18,20,22), TypedValue.COMPLEX_UNIT_SP)
        //setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM)
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

    fun bold() {
        setFont(3, 0)
        init()
    }
    fun normal() {
        setFont(0, 0)
        init()
    }

    fun talkCaption(context: Context, topic: String, talk: String) {
        val spannable = SpannableStringBuilder.valueOf(topic)

        val inText = SpannableString(" in ")
        val font = Typeface.createFromAsset(context.assets, Configure.View.Name[0] + Configure.View.Style[0] + Configure.View.Type)
        inText.setSpan(CustomTypefaceSpan(newType = font), 0, inText.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)

        spannable.append(inText)

        val talkName = SpannableString(talk)

        talkName.setSpan(ForegroundColorSpan(getColor(context, R.color.colorAmTextAccent)), 0, talkName.length, 0)
        spannable.append(talkName)

        text = spannable
    }

}