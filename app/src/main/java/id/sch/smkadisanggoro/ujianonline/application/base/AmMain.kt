package id.sch.smkadisanggoro.ujianonline.application.base

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Rect
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import id.sch.smkadisanggoro.ujianonline.R
import id.sch.smkadisanggoro.ujianonline.application.service.AmPowerService


class AmMain : AmActivity(R.layout.view_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        window.setFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )

    }

    override fun initView(savedInstanceState: Bundle?) {
        pageController()
        pageTitle(getString(R.string.app_name))
    }

    private fun pageController() {
        pageAppBarConfiguration(R.id.navHostFragments)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (onSupportNavigateUp()) {
                    onBackPressed()
                } else {
                    back?.invoke()
                }
            }

            else -> return super.onOptionsItemSelected(item)
        }

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return back == null
    }


    override fun onPause() {
        super.onPause()
        val activityManager =
            applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.moveTaskToFront(taskId, 0)
    }

    override fun onStop() {
        super.onStop()

        finish()
    }

    // clear focus on touch outside
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }

        return super.dispatchTouchEvent(event)
    }
}