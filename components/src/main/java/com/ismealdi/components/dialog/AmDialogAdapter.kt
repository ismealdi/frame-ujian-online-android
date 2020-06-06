package com.ismealdi.components.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.ismealdi.components.AmTextView
import com.ismealdi.components.R
import kotlinx.android.synthetic.main.item_dialog.view.*

class AmDialogAdapter(
    private val listData: List<String>, private var context: Context, private var dialog: Dialog,
    private val actionClickCallBack: ((String) -> String?)? = null
) : RecyclerView.Adapter<AmDialogAdapter.ViewHolder>() {

    private var selected: String = ""

    override fun getItemCount(): Int {
        return listData.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: AmTextView = itemView.textValue
        val layoutParent: LinearLayout = itemView.parentLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_dialog, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = listData[position]
        holder.layoutParent.setOnClickListener {
            actionClickCallBack?.invoke(listData[position])
            actionClick()
        }
    }

    private fun actionClick() {
        dialog.dismiss()
    }

    fun selected(selected: String) {
        this.selected = selected
    }
}