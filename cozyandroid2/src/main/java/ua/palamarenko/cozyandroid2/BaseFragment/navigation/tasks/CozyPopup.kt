package ua.palamarenko.cozyandroid2.BaseFragment.navigation.tasks

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.popup_cozy.*
import ua.palamarenko.cozyandroid2.R
import java.lang.Exception

class CozyPopup : CozyReusePopup() {

    override val layout: Int get() = R.layout.popup_cozy


    private var positiveCallback: ((ResponseModel) -> Unit)? = null
    private var negativeCallback: ((ResponseModel) -> Unit)? = null
    private var dissmisCallback: ((ResponseModel) -> Unit)? = null
    private var finishAction: ((ResponseModel) -> Unit)? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val popupModel = getArgumentObject<ShortPopup>(POPUP_MODEL)!!

        if (popupModel.title != 0) {
            tvTitle.setText(popupModel.title)
            tvTitle.visibility = View.VISIBLE
            tvText.setTextColor(Color.GRAY)
        }

        tvText.text = popupModel.body



        if (popupModel.positiveButtonName != 0) {
            tvOk.setText(popupModel.positiveButtonName)
        } else {
            tvOk.text = "Ok"
        }

        if (popupModel.negativeButtonName != 0) {
            tvCancel.visibility = View.VISIBLE
            tvCancel.setText(popupModel.negativeButtonName)
        }

        if (popupModel.checkBoxTitle != 0) {
            cbCheckBox.visibility = View.VISIBLE
            cbCheckBox.setText(popupModel.checkBoxTitle)
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

        isCancelable = popupModel.isCancelable
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        try {
            dissmisCallback?.invoke(ResponseModel(cbCheckBox.isChecked))
            finishAction?.invoke(ResponseModel(cbCheckBox.isChecked))
            finishAction = null
        } catch (e: Exception) {
        }
    }

    override fun setPositiveCallBack(callback: (ResponseModel) -> Unit) {
        positiveCallback = callback
    }

    override fun setFinishCallBack(callback: (ResponseModel) -> Unit){
        finishAction = callback
    }

    override fun setDissmisCallBack(callback: (ResponseModel) -> Unit) {
        dissmisCallback = callback
    }

    override fun setNegativeCallBack(callback: (ResponseModel) -> Unit) {
        negativeCallback = callback
    }


    companion object {
        const val POPUP_MODEL = "POPUP_MODEL"
    }

}


class Popup(
    val title: Int = 0,
    val body: String,
    val positiveButtonName: Int = 0,
    val negativeButtonName: Int = 0,
    var isCancelable: Boolean = false,
    var checkBoxTitle: Int = 0,
    var positiveAction: ((ResponseModel) -> Unit)? = null,
    var negativeAction: ((ResponseModel) -> Unit)? = null,
    var dissmisAction: ((ResponseModel) -> Unit)? = null,
    var finishAction: ((ResponseModel) -> Unit)? = null

)

fun Popup.generationShortPopup(): ShortPopup {


    return ShortPopup(title=title,body = body,positiveButtonName = positiveButtonName,negativeButtonName = negativeButtonName,isCancelable = isCancelable,checkBoxTitle = checkBoxTitle)

}

class ShortPopup(
    val title: Int = 0,
    val body: String,
    val positiveButtonName: Int = 0,
    val negativeButtonName: Int = 0,
    var isCancelable: Boolean = false,
    var checkBoxTitle: Int = 0
)

class ResponseModel(
    val checkBox: Boolean = false
)