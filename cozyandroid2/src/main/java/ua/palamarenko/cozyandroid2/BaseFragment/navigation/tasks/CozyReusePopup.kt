package ua.palamarenko.cozyandroid2.BaseFragment.navigation.tasks

abstract class CozyReusePopup : CozyBasePopup<EmptyViewModel>(), CozyPopupSetter



interface CozyPopupSetter{
    fun setPositiveCallBack(callback: (ResponseModel) -> Unit)
    fun setFinishCallBack(callback: (ResponseModel) -> Unit)
    fun setDissmisCallBack(callback: (ResponseModel) -> Unit)
    fun setNegativeCallBack(callback: (ResponseModel) -> Unit)
}