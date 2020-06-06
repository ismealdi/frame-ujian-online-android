import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import com.ismealdi.components.AmEditText

class PinWatcher(
    val pinText: AmEditText? = null,
    val backgroundFilled: Drawable? = null,
    val backgroundInput: Drawable? = null,
    val afterTextChanged: (() -> Unit)? = null
) : TextWatcher {

    override fun afterTextChanged(edit: Editable?) {
        afterTextChanged?.invoke()
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        pinText?.background = backgroundInput
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

}