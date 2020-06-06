package com.ismealdi.components.dialog

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.LinearLayout
import android.widget.NumberPicker
import androidx.appcompat.content.res.AppCompatResources
import com.ismealdi.components.Logs
import com.ismealdi.components.R
import kotlinx.android.synthetic.main.component_date_picker_dialog.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DatePickerDialog {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun showDatePicker(
        context: Context,
        dobs: String,
        listener: DatePickerListener?
    ): Dialog {

        val dialog = Dialog(context, R.style.AmAppTheme_Dialog)
        val layoutInflater = LayoutInflater.from(context)
        val dialogView = layoutInflater.inflate(R.layout.component_date_picker_dialog, null)

        with(dialogView) {
            val calendar = Calendar.getInstance()
            val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val minimumYear = getMinimumYear(calendar.get(Calendar.YEAR))

            datepicker.descendantFocusability = DatePicker.FOCUS_BLOCK_DESCENDANTS

            val llFirst = datepicker.getChildAt(0) as LinearLayout
            val llSecond = llFirst.getChildAt(0) as LinearLayout

            try {
                for (i in 0 until llSecond.childCount) {
                    val picker = llSecond.getChildAt(i) as NumberPicker // Numberpickers in llSecond
                    val pickerFields = NumberPicker::class.java.declaredFields
                    for (pf in pickerFields) {
                        if (pf.name == "mSelectionDivider") {
                            pf.isAccessible = true
                            try {
                                pf.set(
                                    picker,
                                    AppCompatResources.getDrawable(
                                        context,
                                        R.drawable.shape_datepicker_div
                                    )
                                )
                            } catch (e: IllegalArgumentException) {
                                Logs.e("Error : $e")
                            } catch (e: Resources.NotFoundException) {
                                Logs.e("Error : $e")
                            } catch (e: IllegalAccessException) {
                                Logs.e("Error : $e")
                            }

                            break
                        }
                    }
                }
            } catch (ignored: Exception) {
                ignored.printStackTrace()
            }

            btn_ok.setOnClickListener {
                dialog?.dismiss()

                val newDate = Calendar.getInstance()
                newDate.set(datepicker.year, datepicker.month, datepicker.dayOfMonth)
                val displayDate = dateFormatter.format(newDate.time).toString()
                val dob = dateFormat.format(newDate.time).toString()
                listener?.onPositiveListener(dob, displayDate)
            }

            btn_cancel.setOnClickListener {
                dialog?.dismiss()
                listener?.onNegativeListener()
            }

            val calendarMin = Calendar.getInstance()
            calendarMin.set(Calendar.DAY_OF_MONTH, 31)
            calendarMin.set(Calendar.MONTH, 11)
            calendarMin.set(Calendar.YEAR, minimumYear)
            datepicker.maxDate = calendarMin.timeInMillis

            val calendarMax = Calendar.getInstance()
            calendarMax.set(Calendar.DAY_OF_MONTH, 31)
            calendarMax.set(Calendar.MONTH, 11)
            calendarMax.set(Calendar.YEAR, minimumYear - 150)
            datepicker.minDate = calendarMax.timeInMillis

            val calSet = Calendar.getInstance()
            if (TextUtils.isEmpty(dobs)) {
                datepicker.init(minimumYear, 0, 1) { _, _, _, _ -> }
            } else {
                try {
                    val date = dateFormat.parse(dobs)
                    calSet.timeInMillis = date.time
                } catch (e: ParseException) {
                    Logs.e("Error : $e")
                }

                datepicker.init(
                    calSet.get(Calendar.YEAR),
                    calSet.get(Calendar.MONTH),
                    calSet.get(Calendar.DAY_OF_MONTH)
                ) { _, _, _, _ -> }
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

        dialog.show()

        return dialog
    }

    private fun getMinimumYear(year: Int): Int {
        return year - 12
    }

    interface DatePickerListener {

        fun onPositiveListener(date: String, displayDate: String)

        fun onNegativeListener()
    }
}