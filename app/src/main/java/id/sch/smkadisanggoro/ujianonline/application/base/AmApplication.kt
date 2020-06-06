package id.sch.smkadisanggoro.ujianonline.application.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.play.core.splitcompat.SplitCompat
import com.ismealdi.components.Logs

@SuppressLint("Registered")
class AmApplication : Application(), LifecycleObserver {

    companion object {
        var mContext: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this.applicationContext
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun connectListener() {
        Logs.e("App in background")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun disconnectListener() {
        Logs.e("App in foreground")
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        SplitCompat.install(this)
    }
}