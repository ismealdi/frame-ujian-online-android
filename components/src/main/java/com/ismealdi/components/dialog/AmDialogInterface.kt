package com.ismealdi.components.dialog

import android.app.Dialog
import android.content.Context

interface AmDialogInterface {
    fun actionDelete(action: String, contentId: Int): String
    fun actionReport(action: String, contentId: Int): String
    fun actionMenu(action: String, contentId: Int): String
    fun initMenu(
        ownerId: Int,
        contentId: Int,
        context: Context?,
        type: String,
        dialogImplementInterface: AmDialogInterface
    ): Dialog?
}