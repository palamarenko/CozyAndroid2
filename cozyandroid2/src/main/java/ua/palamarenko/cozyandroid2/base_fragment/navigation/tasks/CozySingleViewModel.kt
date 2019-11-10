package ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import ua.palamarenko.cozyandroid2.tools.listen


open class CozySingleViewModel<T> : CozyViewModel() {

    val data: MutableLiveData<T> = MutableLiveData()


    fun listen(owner: LifecycleOwner, observe: (T) -> Unit) {
        data.listen(owner,observe)
    }


    fun post(o : T){
        this.data.postValue(o)
    }

}
