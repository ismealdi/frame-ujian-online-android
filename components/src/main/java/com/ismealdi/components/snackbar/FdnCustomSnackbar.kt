package com.ismealdi.components.snackbar

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.ismealdi.components.R
import com.ismealdi.components.ext.findSuitableParent
import com.google.android.material.snackbar.BaseTransientBottomBar

class FdnCustomSnackbar(
    parent: ViewGroup,
    content: FdnCustomSnackbarView
) : BaseTransientBottomBar<FdnCustomSnackbar>(parent, content, content) {


    init {
        getView().setBackgroundColor(
            ContextCompat.getColor(
                view.context,
                android.R.color.transparent
            )
        )
        getView().setPadding(0, 0, 0, 0)
    }

    companion object {

        fun make(
            view: View,
            message: String, duretion: Int,
            listener: View.OnClickListener?, icon: Int?, actionLable: String?, bgColor: Int = R.color.colorAmPrimary
        ): FdnCustomSnackbar? {

            // First we find a suitable parent for our custom view
            val parent = view.findSuitableParent() ?: throw IllegalArgumentException(
                "No suitable parent found from the given view. Please provide a valid view."
            )

            // We inflate our custom view
            try {
                val customView = LayoutInflater.from(view.context).inflate(
                    R.layout.component_snackbar_inflation,
                    parent,
                    false
                ) as FdnCustomSnackbarView
                // We create and return our Snackbar
                with(customView) {
                    tvMsg.text = message
                    actionLable?.let {
                        tvAction.text = actionLable
                        tvAction.setOnClickListener {
                            listener?.onClick(customView.tvAction)
                        }
                    }
                    icon?.let { icon ->
                        imLeft.setImageResource(icon)
                        imLeft.isVisible = true
                    }

                    layRoot.setBackgroundColor(ContextCompat.getColor(view.context, bgColor))

                }



                return FdnCustomSnackbar(
                    parent,
                    customView
                ).setDuration(duretion)
            } catch (e: Exception) {
                Log.v("exception ", e.message)
            }

            return null
        }

    }

}