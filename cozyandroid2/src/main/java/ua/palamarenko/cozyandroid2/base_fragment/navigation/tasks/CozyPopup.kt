package ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.popup_cozy.*
import ua.palamarenko.cozyandroid2.R
import ua.palamarenko.cozyandroid2.tools.makeCharSequence
import java.lang.Exception

open class CozyPopup : CozyReusePopup() {

    override val layout: Int get() = R.layout.popup_cozy

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        popupModel?.apply {
            if (title.isNotEmpty()) {
                tvTitle.text = title
                tvTitle.visibility = View.VISIBLE
                tvText.setTextColor(Color.GRAY)
            }

            tvText.text = body

            if (positiveButtonName != 0) {
                tvOk.setText(positiveButtonName)
            } else {
                tvOk.text = "Ok"
            }

            if (negativeButtonName != 0) {
                tvCancel.visibility = View.VISIBLE
                tvCancel.setText(negativeButtonName)
            }

            if (checkBoxTitle != 0) {
                cbCheckBox.visibility = View.VISIBLE
                cbCheckBox.setText(checkBoxTitle)
            }


            tvOk.setOnClickListener {
                positiveCallback?.invoke(ResponseModel(cbCheckBox.isChecked))
                finishAction?.invoke(ResponseModel(cbCheckBox.isChecked))
                finishAction = null
                dismissAllowingStateLoss()
            }

            tvCancel.setOnClickListener {
                negativeCallback?.invoke(ResponseModel(cbCheckBox.isChecked))
                finishAction?.invoke(ResponseModel(cbCheckBox.isChecked))
                finishAction = null
                dismissAllowingStateLoss()
            }

            isCancelable = isCancelable

        }

    }



}


class Popup(
    val title: Any = "",
    val body: Any,
    val positiveButtonName: Int = 0,
    val negativeButtonName: Int = 0,
    var isCancelable: Boolean = false,
    var checkBoxTitle: Int = 0,
    var positiveAction: ((ResponseModel) -> Unit)? = null,
    var negativeAction: ((ResponseModel) -> Unit)? = null,
    var dissmisAction: ((ResponseModel) -> Unit)? = null,
    var finishAction: ((ResponseModel) -> Unit)? = null

)

fun Popup.generationShortPopup(context: Context): ShortPopup {

    val newTitle = when (title) {
        is String -> title.makeCharSequence()
        is Int -> context.getString(title).makeCharSequence()
        is CharSequence -> title
        else -> "".makeCharSequence()
    }


    val newBody = when (body) {
        is String -> body.makeCharSequence()
        is Int -> context.getString(body).makeCharSequence()
        is CharSequence -> body
        else -> "".makeCharSequence()
    }

    return ShortPopup(
        title = newTitle,
        body = newBody,
        positiveButtonName = positiveButtonName,
        negativeButtonName = negativeButtonName,
        isCancelable = isCancelable,
        checkBoxTitle = checkBoxTitle
    )

}

class ShortPopup(
    val title: CharSequence,
    val body: CharSequence,
    val positiveButtonName: Int = 0,
    val negativeButtonName: Int = 0,
    var isCancelable: Boolean = false,
    var checkBoxTitle: Int = 0
)

class ResponseModel(
    val checkBox: Boolean = false
)