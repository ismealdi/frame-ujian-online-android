package id.sch.smkadisanggoro.ujianonline.application.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ismealdi.components.AmToast

class AmPowerService  : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        context?.let {
            if (intent.action == Intent.ACTION_SCREEN_OFF) {
                AmToast(context,"Screen is off").show()
            } else if (intent.action == Intent.ACTION_SCREEN_ON) {
                AmToast(context,"Screen is on").show()
            }
        }
    }
}