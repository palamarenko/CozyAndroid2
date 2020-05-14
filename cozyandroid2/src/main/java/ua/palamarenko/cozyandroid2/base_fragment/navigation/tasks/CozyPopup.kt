package ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.popup_cozy.*
import ua.palamarenko.cozyandroid2.CozyLibrarySettings
import ua.palamarenko.cozyandroid2.R
import ua.palamarenko.cozyandroid2.tools.click
import ua.palamarenko.cozyandroid2.tools.makeCharSequence

open class CozyPopup : CozyReusePopup() {

    override val layout: Int get() = R.layout.popup_cozy

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        popupModel?.apply {
            if (title.isNotEmpty()) {
                tvTitle.text = title
                tvTitle.visibility = View.VISIBLE
            }

            tvText.text = body

            if (positiveButtonName.isNotEmpty()) {
                tvOk.text = positiveButtonName
            } else {
                tvOk.text = "Ok"
            }

            if (negativeButtonName.isNotEmpty()) {
                tvCancel.visibility = View.VISIBLE
                tvCancel.text = negativeButtonName
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

            if(isCancelable){
                flClick.setOnClickListener { dismissAllowingStateLoss() }
            }

        }
    }


}


class Popup(
    val title: Any = "",
    val body: Any,
    val positiveButtonName: Any = "",
    val negativeButtonName: Any = "",
    var isCancelable: Boolean = false,
    var checkBoxTitle: Int = 0,
    var positiveAction: ((ResponseModel) -> Unit)? = null,
    var negativeAction: ((ResponseModel) -> Unit)? = null,
    var dissmisAction: ((ResponseModel) -> Unit)? = null,
    var finishAction: ((ResponseModel) -> Unit)? = null

)


fun convertAnyToTitle(title: Any): CharSequence {
    return when (title) {
        is String -> title.makeCharSequence()
        is Int -> CozyLibrarySettings.appContext!!.getString(title).makeCharSequence()
        is CharSequence -> title
        else -> "".makeCharSequence()
    }
}

fun Popup.generationShortPopup(): ShortPopup {

    return ShortPopup(
        title = convertAnyToTitle(title),
        body = convertAnyToTitle(body),
        positiveButtonName =convertAnyToTitle(positiveButtonName),
        negativeButtonName = convertAnyToTitle(negativeButtonName),
        isCancelable = isCancelable,
        checkBoxTitle = checkBoxTitle
    )

}

class ShortPopup(
    val title: CharSequence,
    val body: CharSequence,
    val positiveButtonName: CharSequence,
    val negativeButtonName: CharSequence,
    var isCancelable: Boolean = false,
    var checkBoxTitle: Int = 0
)

class ResponseModel(
    val checkBox: Boolean = false
)