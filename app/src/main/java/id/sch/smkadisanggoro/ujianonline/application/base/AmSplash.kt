package id.sch.smkadisanggoro.ujianonline.application.base

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import id.sch.smkadisanggoro.ujianonline.R

class AmSplash : AmActivity(R.layout.view_splash) {

    override fun initView(savedInstanceState: Bundle?) {
        Handler().postDelayed({
            startActivity(Intent(this, AmMain::class.java))
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)

            finish()
        }, 300)

    }
}