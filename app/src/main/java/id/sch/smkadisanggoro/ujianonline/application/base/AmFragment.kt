package id.sch.smkadisanggoro.ujianonline.application.base

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.ismealdi.components.AmToast
import com.ismealdi.components.dialog.AmDialogInterface
import kotlinx.android.synthetic.main.view_error.view.*

abstract class AmFragment(@LayoutRes var layout: Int) : Fragment() {

    var pageNotOnLoad: Boolean = true

    var errorRetry: (() -> Unit)? = null
    var errorLoadMore: (() -> Unit)? = null

    private var viewRoot: View? = null
    private var dialogImplement: AmDialogInterface? = null

    protected var amActivity: AmActivity? = null
    protected var layoutPageError: View? = null
    protected var layoutPageLoader: View? = null

    protected abstract fun initView(context: Context, savedInstanceState: Bundle?)

    protected open fun stateInstance(savedInstanceState: Bundle) {}

    protected open fun observer(activity: AmActivity) {}

    protected open fun listener() {
        amActivity?.back = null

        layoutPageError?.buttonRetryNoNetwork?.setOnClickListener {
            errorRetry?.invoke()
        }

        amActivity?.errorRetry = {
            errorRetry?.invoke()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (viewRoot == null) {
            viewRoot = inflater.inflate(layout, container, false)
        } else {
            val parent = viewRoot?.parent as ViewGroup?
            parent?.removeView(viewRoot)
        }

        /*val parent = viewRoot?.parent as ViewGroup?
        parent?.removeView(viewRoot)
        viewRoot = inflater.inflate(layout, container, false)*/


        return viewRoot
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.let { context ->
            if (activity is AmActivity) {
                amActivity = activity as AmActivity
            }

            initView(context, savedInstanceState)

            if (savedInstanceState == null) {
                listener()

                amActivity?.let {
                    observer(it)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        loader(false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        savedInstanceState?.let { stateInstance(it) }
    }

    protected fun message(message: String) {
        activity?.applicationContext?.let {
            // Give an user access to disabling the annoying toast message
            if (!message.isBlank()) {
                AmToast(it, message).show()
            }
            //if (!Preferences(it, Constants.Preference.Setting.showErrorToast).getBoolean()) { }
        }
    }

    internal fun pageTitle(title: String) {
        activity?.let { act ->
            (act as AmActivity).pageTitle(title)
        }
    }

    fun loader(state: Boolean, layoutCustom: View? = null, timer: Boolean? = null) {
        layoutCustom?.let {
            if (!state && layoutPageLoader?.isVisible == true) layoutPageLoader?.isVisible = false

            if (timer == true) {
                if (state) {
                    it.visibility = View.VISIBLE
                } else {
                    Handler().postDelayed({
                        layoutCustom.visibility = View.GONE
                    }, 500)
                }
            } else {
                it.visibility = if (state) View.VISIBLE else View.GONE
            }
        } ?: run {
            layoutPageLoader?.visibility = if (state) View.VISIBLE else View.GONE
        }

        Handler().postDelayed({
            layoutCustom?.visibility = View.GONE
            layoutPageLoader?.visibility = View.GONE
        }, 1500)
    }

    fun error(state: Boolean, layoutCustom: View? = null, retry: Boolean = true) {
        if (state) loader(false)
        val layout = layoutCustom ?: layoutPageError

        layout?.buttonRetryNoNetwork?.isVisible = retry
        layout?.visibility = if (state) View.VISIBLE else View.GONE

        if(!retry) {
            Handler().postDelayed({
                layout?.visibility = View.GONE
            }, 500)
        }
    }

    fun navigate(target: Int? = null, directions: NavDirections? = null) {
        try {
            target?.let { findNavController().navigate(it) } ?: run {
                directions?.let { findNavController().navigate(it) }
            }
        } catch (e: InstantiationException) {
            message("Module not installed.")
        }
    }

    fun navigateController(target: Int, bundle: Bundle? = null) {
        findNavController().navigate(target, bundle)
    }

    fun <T : AmActivity> amActivityCastTo(): T? {
        amActivity?.let {
            try {
                return it as T
            } catch (e: TypeCastException) {
                message("Type Cast Error")
            }
        }

        return null
    }
}