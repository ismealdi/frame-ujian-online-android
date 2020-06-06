package com.ismealdi.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import kotlin.math.roundToInt
import kotlin.random.Random

/**
 * Created by Al
 * on 06/04/19 | 01:44
 */
class AmRating : LinearLayout {

    private var ratingSize = 0
    private var ratingHeight = 0
    private var ratingEnable = true
    private var ratingDark = false

    var listener: OnAmRatingChangeListener? = null

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
        orientation = HORIZONTAL

        removeAllViews()

        for (i in 1..5) {
            val star = AppCompatImageView(context)
            val params = LayoutParams(ratingHeight.dpToPx(), ratingHeight.dpToPx())

            star.id = Random.nextInt()

            params.marginEnd = 2.dpToPx()

            if (i <= ratingSize) {
                star.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_stars_on))
            } else {
                star.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_stars_off))
            }

            star.layoutParams = params

            if (ratingEnable) {
                star.setOnClickListener {
                    ratingSize = i
                    init()

                    listener?.onChange(ratingSize)
                }
            }

            addView(star)
        }
    }

    fun getRating() = ratingSize

    fun setRating(rate: Int) {
        ratingSize = rate
        init()
    }

    @SuppressLint("CustomViewStyleable")
    private fun setValues(attrs: AttributeSet?) {
        val attr = context.obtainStyledAttributes(attrs, R.styleable.AmView)
        try {
            val n = attr.indexCount
            for (i in 0 until n) {
                when (val attribute = attr.getIndex(i)) {
                    R.styleable.AmView_am_rating_size -> ratingSize = attr.getInt(attribute, 0)
                    R.styleable.AmView_am_rating_height -> ratingHeight = attr.getInt(attribute, 0)
                    R.styleable.AmView_am_rating_enable -> ratingEnable =
                        attr.getBoolean(attribute, true)
                    R.styleable.AmView_am_rating_dark -> ratingDark =
                        attr.getBoolean(attribute, false)
                    else -> Logs.i("$javaClass: $attribute")
                }
            }
        } finally {
            attr.recycle()
        }
    }

    fun Int.dpToPx(): Int {
        val metrics = Resources.getSystem().displayMetrics
        val px = this * (metrics.densityDpi / 160f)
        return px.roundToInt()
    }

    interface OnAmRatingChangeListener {
        fun onChange(rating: Int)
    }

}

