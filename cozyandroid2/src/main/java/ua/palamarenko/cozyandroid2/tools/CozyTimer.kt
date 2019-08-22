package ua.palamarenko.cozyandroid2.tools

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class CozyTimer {

    var disposible: Disposable? = null
    var globalCallBack = {}

    fun startTimer(
        count: Long,
        unit: TimeUnit = TimeUnit.SECONDS,
        needCancel: Boolean = true,
        finishCallBack: () -> Unit = {}
    ) {

        if (needCancel && disposible != null) {
            disposible!!.dispose()
        }

        disposible = Observable.timer(count, unit, Schedulers.io())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                globalCallBack.invoke()
                finishCallBack.invoke()
            }

    }

    fun startTimer(
        count: Long,
        unit: TimeUnit = TimeUnit.SECONDS,
        needCancel: Boolean = true,
        unitCallBack: (Long) -> Unit,
        finishCallBack: () -> Unit = {}
    ) {

        if (needCancel && disposible != null) {
            disposible!!.dispose()
        }

        disposible = Observable.interval(1, unit, Schedulers.io())
            .take(count)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnDispose {
                finishCallBack.invoke()
            }
            .subscribe({
                unitCallBack.invoke(it + 1)
            }, {

            }, {
                finishCallBack.invoke()
            })

    }


}