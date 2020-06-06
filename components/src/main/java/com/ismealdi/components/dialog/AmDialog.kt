package com.ismealdi.components.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.ismealdi.components.R
import kotlinx.android.synthetic.main.component_list.view.*
import kotlinx.android.synthetic.main.component_list.view.textTitle
import kotlinx.android.synthetic.main.component_prompt.view.*

class AmDialog {

    @SuppressLint("InflateParams")
    fun loader(context: Context, cancelOnTouchOutside: Boolean = false): Dialog {
        val dialog = Dialog(context, R.style.AmAppTheme_Dialog)
        val layoutInflater = LayoutInflater.from(context)
        val dialogView = layoutInflater.inflate(R.layout.component_loader, null)

        dialog.setContentView(dialogView)

        dialog.window?.let {
            it.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        dialog.setCanceledOnTouchOutside(cancelOnTouchOutside)

        return dialog
    }

    fun list(
        context: Context,
        lists: List<String>,
        title: String = "",
        cancelOnTouchOutside: Boolean = true,
        actionClickCallBack: ((String) -> String?)? = null
    ): Dialog {
        val dialog = Dialog(context, R.style.AmAppTheme_Dialog)
        val layoutInflater = LayoutInflater.from(context)
        val dialogView = layoutInflater.inflate(R.layout.component_list, null)
        val dialogListAdapter = AmDialogAdapter(lists, context, dialog, actionClickCallBack)

        if (title.isNotEmpty()) {
            dialogView.textTitle.text = title
            dialogView.textTitle.visibility = View.VISIBLE
        } else {
            dialogView.textTitle.visibility = View.GONE
        }

        dialogView.listViewDialog.layoutManager = LinearLayoutManager(context)
        dialogView.listViewDialog.adapter = dialogListAdapter

        dialog.setContentView(dialogView)

        if (dialog.window != null) {
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        dialog.setCanceledOnTouchOutside(cancelOnTouchOutside)

        return dialog
    }

    fun prompt(
        context: Context,
        title: String,
        description: String,
        positive: String? = context.getString(R.string.am_label_yes),
        negative: String? = context.getString(R.string.am_label_no),
        positiveCallBack: ((String) -> Unit?)? = null,
        negativeCallback: (() -> Unit?)? = null
    ): Dialog {
        val dialog = Dialog(context, R.style.AmAppTheme_Dialog)
        val layoutInflater = LayoutInflater.from(context)
        val dialogView = layoutInflater.inflate(R.layout.component_prompt, null)

        with(dialogView) {
            buttonNegative.setOnClickListener {
                negativeCallback?.invoke()
                dialog.dismiss()
            }

            buttonPositive.setOnClickListener {
                positiveCallBack?.invoke(positive.orEmpty())
                dialog.dismiss()
            }
            textTitle.text = title
            textDescription.text = description
            buttonPositive.text = positive.orEmpty()
            buttonNegative.text = negative.orEmpty()
            if (positive.orEmpty().isEmpty()) {
                buttonPositive.visibility = View.GONE
            }

            if (negative.orEmpty().isEmpty()) {
                buttonNegative.visibility = View.GONE
            }
        }

        dialog.setContentView(dialogView)

        if (dialog.window != null) {
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        dialog.setCanceledOnTouchOutside(false)

        return dialog
    }

}