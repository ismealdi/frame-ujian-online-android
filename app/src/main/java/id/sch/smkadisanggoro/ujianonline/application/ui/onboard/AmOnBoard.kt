package id.sch.smkadisanggoro.ujianonline.application.ui.onboard

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.*
import androidx.activity.addCallback
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import id.sch.smkadisanggoro.ujianonline.R
import id.sch.smkadisanggoro.ujianonline.application.base.AmFragment
import id.sch.smkadisanggoro.ujianonline.util.Constants.Preference.BaseUri
import id.sch.smkadisanggoro.ujianonline.util.Preferences
import kotlinx.android.synthetic.main.view_on_board.*
import kotlinx.android.synthetic.main.view_web.*
import kotlinx.android.synthetic.main.view_web.layoutError
import kotlinx.android.synthetic.main.view_web.layoutLoader
import kotlinx.coroutines.*

class AmOnBoard : AmFragment(R.layout.view_on_board) {

    override fun onStart() {
        super.onStart()
        amActivity?.pageBack(false)
        amActivity?.pageTitle(getString(R.string.app_name))
    }

    override fun initView(context: Context, savedInstanceState: Bundle?) {
        layoutPageLoader = layoutLoader
        layoutPageError = layoutError
        setBaseUri()
    }

    private fun setBaseUri() {
        Preferences(requireContext(), BaseUri).getString().let { uri ->
            if (uri.isNotEmpty() && (uri.contains("http://") || uri.contains("https://"))) {
                inputUri.setText(uri)
            }
        }
    }

    override fun listener() {
        super.listener()

        buttonProcess.setOnClickListener {
            val uri = inputUri.text.toString()

            if (uri.isNotEmpty() && (uri.contains("http://") || uri.contains("https://"))) {
                Preferences(requireContext(), BaseUri).setString(uri)

                val actionDetail = AmOnBoardDirections.actionWeb(
                    urlTarget = uri,
                    title = getString(R.string.text_ujian)
                )

                navigate(directions = actionDetail)
            } else {
                if (uri.isNotEmpty()) {
                    message("Tolong sertakan protokol http atau https pada alamat.")
                } else {
                    message("Alamat ujian tidak boleh kosong.")
                }
            }
        }

        errorRetry = {

        }
    }

}
