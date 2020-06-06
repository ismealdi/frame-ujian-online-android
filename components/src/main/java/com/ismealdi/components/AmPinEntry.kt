package com.ismealdi.components

import PinWatcher
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import kotlin.math.roundToInt


class AmPinEntry : LinearLayout {

    private var pinWidth = 0
    private var pinHeight = 0
    private var backgroundFilled =
        ContextCompat.getDrawable(context, R.drawable.ic_bg_otp_item_filled)
    private var backgroundInput = ContextCompat.getDrawable(context, R.drawable.ic_bg_otp_item)

    private val end = 6

    var listener: OnAmPinEntryListener? = null

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        setValues(attrs)
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setValues(attrs)
        init()
    }

    private fun init() {
        orientation = HORIZONTAL
        removeAllViews()
        for (i in 1..end) {
            val pinEntry = AmEditText(context)
            val params = LayoutParams(pinWidth.dpToPx(), pinHeight.dpToPx())

            pinEntry.id = i
            params.marginEnd = 4.dpToPx()
            params.marginStart = 4.dpToPx()
            pinEntry.layoutParams = params
            pinEntry.isSingleLine = true
            pinEntry.textAlignment = View.TEXT_ALIGNMENT_CENTER
            pinEntry.inputType = InputType.TYPE_CLASS_NUMBER
            pinEntry.filters += InputFilter.LengthFilter(2)
            pinEntry.background = backgroundFilled
            if (i == end) {
                pinEntry.imeOptions = EditorInfo.IME_ACTION_DONE
            } else {
                pinEntry.imeOptions = EditorInfo.IME_ACTION_NEXT
            }
            pinEntry.setTextColor(ContextCompat.getColor(context, R.color.colorAmPrimary))

            pinEntry.addTextChangedListener(
                PinWatcher(
                    pinEntry,
                    backgroundFilled,
                    backgroundInput
                ) {
                    pinEntry.clearFocus()
                    stateBackground(pinEntry)
                    focusNext(i, pinEntry.text.toString().trim())
                    listener?.onOtpIsCompleteed(
                        getValue()?.length == 6,
                        getValue().orEmpty()
                    )
                })

            pinEntry.onPaste = {
                if (it.length > 1 && it.isDigitsOnly()) {
                    /*
                    var positionPasted = 0
                    for (x in (i + 1)..end) {
                        findViewById<AmEditText>(x)?.apply {
                            positionPasted += 1
                            setText(it[positionPasted].toString())
                        }
                    }*/

                    for (x in 1..end) {
                        findViewById<AmEditText>(x)?.apply {
                            setText(it[x - 1].toString())
                        }
                    }
                }
            }

            pinEntry.setOnKeyListener { _, x, _ ->
                if (x == KeyEvent.KEYCODE_DEL) {
                    move(i - 1)
                }

                false
            }

            pinEntry.setOnFocusChangeListener { _, b ->
                if (b) {
                    pinEntry.background = backgroundInput
                    pinEntry.text?.length?.let { len -> pinEntry.setSelection(len) }
                } else {
                    stateBackground(pinEntry)
                }
            }

            addView(pinEntry)
        }
    }

    private fun stateBackground(pinEntry: EditText) {
        if (pinEntry.text.toString().trim().isNotEmpty()) {
            pinEntry.background = backgroundInput
        } else {
            pinEntry.background = backgroundFilled
        }
    }

    private fun focusNext(i: Int, change: String) {
        if (change.isNotEmpty() && change.length > 1) {
            findViewById<AmEditText>(i)?.setText(change.last().toString())
        }

        var next = if (change.isNotEmpty()) i + 1 else i - 1

        if (next > 6) next = 6
        if (next < 1) next = 1

        move(next)
    }

    private fun move(next: Int) {
        findViewById<AmEditText>(next)?.apply {
            requestFocus()
            text?.length?.let { len -> setSelection(len) }
        }
    }

    @SuppressLint("CustomViewStyleable")
    private fun setValues(attrs: AttributeSet?) {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.AmView)
        try {
            val n = attr.indexCount
            for (i in 0 until n) {
                when (val attribute = attr.getIndex(i)) {
                    R.styleable.AmView_am_pin_height -> pinHeight = attr.getInt(attribute, 0)
                    R.styleable.AmView_am_pin_width -> pinWidth = attr.getInt(attribute, 0)
                    else -> Logs.i("$javaClass: $attribute")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            attr.recycle()
        }
    }

    fun Int.dpToPx(): Int {
        val metrics = Resources.getSystem().displayMetrics
        val px = this * (metrics.densityDpi / 160f)
        return px.roundToInt()
    }

    fun getValue(): String? {
        var nilai: String? = ""
        for (i in 1..end) {
            this.rootView.findViewById<EditText>(i)?.let { inputText ->
                inputText.text?.let {
                    nilai += inputText.text
                }
            }
        }
        return nilai
    }

    fun setValue(code: String){
        code.forEachIndexed { index, char ->
            val id = index +1
            this.rootView.findViewById<EditText>(id)?.let { inputText ->
                inputText.setText(char.toString())
            }
        }
    }

    interface OnAmPinEntryListener {
        fun onOtpIsCompleteed(isComplete: Boolean, otpCode: String)
    }

}