package ua.palamarenko.cozyandroid2.BaseFragment.navigation.tasks

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer


open class CozySingleViewModel<T> : CozyViewModel() {

    val data: MutableLiveData<T> = MutableLiveData()


    fun listen(owner: LifecycleOwner, observe: (T) -> Unit) {
        data.listen(owner,observe)
    }


    fun post(o : T){
        this.data.postValue(o)
    }

}

fun <T> MutableLiveData<T>.listen(owner: LifecycleOwner, observe: (T) -> Unit) {
    observe(owner, Observer {
        if (it != null) {
            observe.invoke(it)
        }
    })
}