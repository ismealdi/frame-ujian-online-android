package id.sch.smkadisanggoro.ujianonline.application.ui.web

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
import kotlinx.android.synthetic.main.view_web.*
import kotlinx.coroutines.*

class AmWeb : AmFragment(R.layout.view_web) {

    //TODO: check leaking on first load, Coroutine explore

    private var job: Job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    private var topHeight = "10px"

    override fun initView(context: Context, savedInstanceState: Bundle?) {
        amActivity?.pageBack(true)

        layoutPageLoader = layoutLoader
        layoutPageError = layoutError

        errorRetry = {
            arguments?.getString("urlTarget")?.let {
                loadWeb("$it")
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (webView?.canGoBack() == true) {
                webView?.goBack()
            } else {
                findNavController().navigateUp()
            }
        }

        handleBack()

        pageTitle(arguments?.getString("title") ?: "")

        arguments?.getString("title")?.let {
            topHeight = "10px"
        } ?: run {
            topHeight = "25px"
        }

        val async = coroutineScope.async {
            withContext(Dispatchers.Main) {
                arguments?.getString("urlTarget")?.let {
                    loadWeb("$it")
                }
            }
        }
    }

    private fun handleBack() {
        amActivity?.back = {
            findNavController().navigateUp()
        }
    }

    override fun onResume() {
        super.onResume()

        handleBack()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadWeb(url: String) {
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {}

            override fun onPageFinished(view: WebView, url: String) {
                webLoaded()
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)

                error(true)
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                if (newProgress > 20) {
                    webLoaded()
                }

                if(newProgress > 90) {
                    loader(false)
                }
            }
        }

        webView?.isVisible = false
        webView?.settings?.javaScriptEnabled = true
        webView?.loadUrl("$url?browser=asexam")
        error(false)
        loader(true)
    }

    private fun webLoaded() {
        val hideComponent =
            "javascript:var x = document.getElementById('id-header').style.display='none';" +
                    "document.getElementById('id-footer').style.display='none';" +
                    "document.getElementById('floating-tokped').style.display='none';" +
                    "document.getElementsByClassName('breadcrumb')[0].style.opacity=0;document.getElementsByClassName('breadcrumb')[0].style.height='$topHeight';document.getElementsByClassName('breadcrumb')[0].style.padding=0;"

        webView?.loadUrl(hideComponent)
        webView?.isVisible = true
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

}
