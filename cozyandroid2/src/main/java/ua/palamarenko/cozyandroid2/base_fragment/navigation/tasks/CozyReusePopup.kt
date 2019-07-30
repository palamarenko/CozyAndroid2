package ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks

import android.content.DialogInterface
import kotlinx.android.synthetic.main.popup_cozy.*
import java.lang.Exception

abstract class CozyReusePopup : CozyBasePopup<EmptyViewModel>(), CozyPopupSetter {

    var positiveCallback: ((ResponseModel) -> Unit)? = null
    var negativeCallback: ((ResponseModel) -> Unit)? = null
    var dissmisCallback: ((ResponseModel) -> Unit)? = null
    var finishAction: ((ResponseModel) -> Unit)? = null
    var popupModel: ShortPopup? = null


    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        try {
            dissmisCallback?.invoke(ResponseModel(cbCheckBox.isChecked))
            finishAction?.invoke(ResponseModel(cbCheckBox.isChecked))
            finishAction = null
        } catch (e: Exception) {
        }
    }


    override fun setShortPopup(popup: ShortPopup) {
        this.popupModel = popup
    }

    override fun setPositiveCallBack(callback: (ResponseModel) -> Unit) {
        positiveCallback = callback
    }

    override fun setFinishCallBack(callback: (ResponseModel) -> Unit) {
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


interface CozyPopupSetter {
    fun setPositiveCallBack(callback: (ResponseModel) -> Unit)
    fun setFinishCallBack(callback: (ResponseModel) -> Unit)
    fun setDissmisCallBack(callback: (ResponseModel) -> Unit)
    fun setShortPopup(popup: ShortPopup)
    fun setNegativeCallBack(callback: (ResponseModel) -> Unit)
}