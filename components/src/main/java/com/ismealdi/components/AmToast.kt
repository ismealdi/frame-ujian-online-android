package com.ismealdi.components

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import kotlinx.android.synthetic.main.component_toast.view.*

/**
 * Created by Al for Female Daily Network
 * on 12/12/18 | 09:05
 */
@SuppressLint("InflateParams")
class AmToast(context: Context, text: String, duration: Int = LENGTH_SHORT) : Toast(context) {

    init {
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.component_toast, null)

        layout.textToast.text = text

        this.duration = duration
        this.setGravity(Gravity.CENTER, 0, 0)
        this.view = layout
    }
}