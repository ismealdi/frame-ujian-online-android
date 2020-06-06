package com.ismealdi.components.network

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.ismealdi.components.Logs

class ConnectionReceiver : BroadcastReceiver() {

    private var callback: AmConnectionInterface? = null

    private val networkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .addTransportType(NetworkCapabilities.TRANSPORT_BLUETOOTH)
        .build()

    override fun onReceive(context: Context, arg1: Intent) {
        val connectivityManager = context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        Logs.e("Internet Check")
    }

    private fun showMessage(message: String) {
        this.callback?.let {
            it.onConnectionChange(message)
            Logs.e(message)
        } ?: run {
            Logs.e("No Callback for Internet State")
        }
    }

    fun registerReceiver(receiver: AmConnectionInterface) {
        this.callback = receiver
    }

    private var networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network?) {
            Logs.d("networkcallback called from onLost")
        }
        override fun onUnavailable() {
            Logs.d("networkcallback called from onUnvailable")
        }
        override fun onLosing(network: Network?, maxMsToLive: Int) {
            Logs.d("networkcallback called from onLosing")
        }
        override fun onAvailable(network: Network?) {
            Logs.d("NetworkCallback network called from onAvailable ")
        }
    }

}