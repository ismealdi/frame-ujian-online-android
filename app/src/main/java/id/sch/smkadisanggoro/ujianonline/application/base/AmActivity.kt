package id.sch.smkadisanggoro.ujianonline.application.base

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.IntentFilter
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.ismealdi.components.AmToast
import com.ismealdi.components.dialog.AmDialog
import com.ismealdi.components.network.AmConnectionInterface
import com.ismealdi.components.network.ConnectionReceiver
import id.sch.smkadisanggoro.ujianonline.R
import kotlinx.android.synthetic.main.component_toolbar.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.view_error.view.*
import kotlinx.android.synthetic.main.view_splash.*

@SuppressLint("Registered", "SwitchIntDef")
abstract class AmActivity(@LayoutRes var layout: Int) : AppCompatActivity(),
    AmConnectionInterface, LifecycleOwner {

    lateinit var manager: SplitInstallManager

    private var connectionReceiver: ConnectionReceiver? = null
    private var isRegisteredReceiver: Boolean = false
    private var dialogLoader: Dialog? = null

    public var errorRetry: (() -> Unit)? = null

    var isBack = true
    var back: (() -> Any?)? = null

    /**
     * Default constructor for AmActivity. to define onCreate
     */
    protected abstract fun initView(savedInstanceState: Bundle?)

    /**
     * Observe View Model
     */
    protected open fun observer() {}

    /**
     * Handling intent data
     */
    protected open fun handleIntent() {}

    /**
     * Adding action or listener to any object
     */
    protected open fun listener() {

    }

    override fun onConnectionChange(message: String) {
        if (message == "No Internet Connection") message(message)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initConnection(this)
        super.onCreate(savedInstanceState)

        dialogLoader = AmDialog().loader(this)
        manager = SplitInstallManagerFactory.create(this)

        setContentView(layout)

        layoutError?.buttonRetryNoNetwork?.setOnClickListener {
            errorRetry?.invoke()
        }

        toolbar?.let {
            setSupportActionBar(it)
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }


        initView(savedInstanceState)
        listener()
        handleIntent()
        observer()
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyConnection()

    }

    override fun onPause() {
        super.onPause()
        destroyConnection()
    }

    override fun onResume() {
        super.onResume()
        initConnection(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()

            else -> return super.onOptionsItemSelected(item)
        }

        return true
    }

    private fun initConnection(receiver: AmConnectionInterface) {
        if (connectionReceiver == null) {
            connectionReceiver = ConnectionReceiver()
            connectionReceiver?.let {
                val mIntentFilter = IntentFilter()

                it.registerReceiver(receiver)
                mIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")

                registerReceiver(connectionReceiver, mIntentFilter)
                isRegisteredReceiver = true
            }

        }
    }

    private fun destroyConnection() {
        if (isRegisteredReceiver) {
            unregisterReceiver(connectionReceiver)
            isRegisteredReceiver = false
        }
    }

    /**
     * Showing toast message, toast will showing if showErrorToast from Preference is false
     */
    fun message(message: String) {
        // Give an user access to disabling the annoying toast message
        if (!message.isBlank()) {
            AmToast(this, message).show()
        }
        // if (!Preferences(this, Constants.Preference.Setting.showErrorToast).getBoolean()) { }
    }

    /**
     * Toggle bottom line on toolbar
     */
    fun pageBottomLine(state: Boolean) {
        toolbarLineDivider?.let {
            it.visibility = if (state) View.VISIBLE else View.GONE
        }
    }

    /**
     * Set title on toolbar
     */
    fun pageTitle(
        title: String? = null,
        state: Boolean = true,
        left: Boolean = false,
        bold: Boolean = false,
        subTitle: String? = null
    ) {
        toolbarSubTitle?.let {
            it.isVisible = state && !subTitle.isNullOrEmpty()
            subTitle?.let { text ->
                it.text = text
            }
        }

        toolbarTitle?.let {
            title?.let { title ->
                it.text = title
            }

            it.visibility = if (state) View.VISIBLE else View.GONE

            if (bold) {
                it.bold()
            } else {
                it.normal()
            }

            /*val params = ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            if (left) {
                params.gravity = Gravity.START
            } else {
                params.gravity = Gravity.CENTER
            }

            layoutTitle?.layoutParams = params*/
        }
    }

    fun pageToolbarBackground(@ColorRes color: Int) {
        toolbar?.setBackgroundColor(ContextCompat.getColor(this, color))
        parentToolbar?.setBackgroundColor(ContextCompat.getColor(this, color))
    }

    /**
     * Toggle back button on toolbar
     */
    fun pageBack(state: Boolean, isMenu: Boolean = false) {
        isBack = state
        supportActionBar?.setDisplayHomeAsUpEnabled(state)

        if (state) {
            supportActionBar?.setHomeAsUpIndicator(
                ContextCompat.getDrawable(
                    this,
                    if(isMenu) R.drawable.ic_menu else R.drawable.ic_back
                )
            )
        }
    }

    fun pageMain() {
        imageProfile?.isVisible = true
        toolbarSearch?.isVisible = true
    }

    /**
     * Loader as dialog
     */
    open fun dialogLoader(state: Boolean) {
        if (state) {
            dialogLoader?.show()
        } else {
            dialogLoader?.dismiss()
        }
    }

    /**
     * Loader as in frame layout
     */
    fun loader(state: Boolean) {
        layoutLoader?.isVisible = state
    }


    /**
     * Error state in frame layout
     */
    fun error(state: Boolean) {
        layoutError?.isVisible = state
    }

    fun pageAppBarConfiguration(
        navHost: Int? = null,
        noHomeIconFragment: Set<Int>? = null,
        navController: NavController? = null,
        bottomLine: Boolean = false
    ) {
        var nc = navController

        if (navController == null) {
            nc = navHost?.let { findNavController(it) }
        }

        supportActionBar?.setHomeAsUpIndicator(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_back
            )
        )

        pageBottomLine(bottomLine)

        nc?.addOnDestinationChangedListener { _, destination: NavDestination, _ ->
            supportActionBar?.apply {
                noHomeIconFragment?.let {
                    toolbar?.isVisible = (destination.id in noHomeIconFragment).not()
                    pageBack((destination.id in noHomeIconFragment).not())
                }
            }
        }

    }

}