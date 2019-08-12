package ua.palamarenko.cozyandroid2.tools

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class CozyTimer {

    var disposible: Disposable? = null
    var globalCallBack = {}

    fun startTimer(count: Long, util: TimeUnit = TimeUnit.SECONDS, needCancel: Boolean = true, callBack : () -> Unit) {

        if (needCancel && disposible != null) {
            disposible!!.dispose()
        }

        disposible = Observable.timer(count, util, Schedulers.io())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                globalCallBack.invoke()
                callBack.invoke() }

    }


}