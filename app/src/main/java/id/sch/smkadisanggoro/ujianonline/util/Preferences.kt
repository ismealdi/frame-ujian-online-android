package id.sch.smkadisanggoro.ujianonline.util

import android.content.Context
import androidx.preference.PreferenceManager

/**
 * Created by Al for Female Daily Network
 * on 22/11/18 | 15:03
 */
class Preferences(private val context: Context, private val name: String) {
    private var sharedPref = PreferenceManager.getDefaultSharedPreferences(context)

    fun setString(value: String) = with (sharedPref.edit()) { putString(name, value.encrypt()).apply() }
    fun setBoolean(value: Boolean) = with (sharedPref.edit()) { putString(name, value.toString().encrypt()).apply() }
    fun setInt(value: Int) = with (sharedPref.edit()) { putString(name, value.toString().encrypt()).apply() }
    fun setLong(value: Long) = with (sharedPref.edit()) { putString(name, value.toString().encrypt()).apply() }
    fun setFloat(value: Float) = with (sharedPref.edit()) { putString(name, value.toString().encrypt()).apply() }
    fun getString() : String = (sharedPref.getString(name, "") ?: "").decrypt()
    fun getBoolean() : Boolean  = (sharedPref.getString(name, true.toString()) ?: true.toString()).decrypt().toBoolean()
    fun getInt() : Int  = (sharedPref.getString(name, 0.toString()) ?: 0.toString()).decrypt().toInt()
    fun getLong() : Long  = (sharedPref.getString(name, 0.toString()) ?: 0.toString()).decrypt().toLong()
    fun getFloat() : Float  = (sharedPref.getString(name, 0.toString()) ?: 0.toString()).decrypt().toFloat()
    fun remove()  = sharedPref.edit().remove(name).commit()

    fun removeAll() {
        sharedPref.edit().clear().apply()
    }

    fun toggleBoolean() {
        this.apply {
            setBoolean(!getBoolean())
        }
    }

    private fun String.decrypt() = this
    private fun String.encrypt() = this
}
