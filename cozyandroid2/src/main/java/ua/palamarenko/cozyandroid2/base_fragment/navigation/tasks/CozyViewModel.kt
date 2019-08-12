package ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import ua.palamarenko.cozyandroid2.base_fragment.navigation.BaseViewModel

open class CozyViewModel : BaseViewModel() {

    val tm: TaskManager = TaskManager()


    open fun errorHandler(throwable: Throwable) {}


    fun CozyViewModel.task(id: Int, data: Any = "", rule: Bundle = Bundle()) {
        tm.task(id, data, rule)
    }


    fun <T> Single<T>.showProgress(needPorgess: Boolean = true): Single<T> {
        return this.doOnSubscribe {
            if (needPorgess) {
                tm.task(SHOW_PROGRESS, true)
            }
        }
            .doFinally {
                if (needPorgess) {
                    tm.task(SHOW_PROGRESS, false)
                }
            }
    }

    fun Completable.showProgress(): Completable {
        return this.doOnSubscribe { tm.task(SHOW_PROGRESS, true) }
            .doFinally { tm.task(SHOW_PROGRESS, false) }
    }


    fun <T> Flowable<T>.showProgress(): Flowable<T> {
        var firstTime = false
        return this
            .doOnSubscribe {
                tm.task(SHOW_PROGRESS, true)
                firstTime = true
            }
            .doOnError {
                tm.task(SHOW_PROGRESS, false)
                firstTime = false
            }
            .doOnNext {
                if (firstTime) {
                    tm.task(SHOW_PROGRESS, false)
                    firstTime = false
                }
            }
    }


    fun <T : Any> Observable<T>.bindSubscribe(
        bindName: String? = null,
        onError: (Throwable) -> Unit = { errorHandler(it) },
        onComplete: () -> Unit = {},
        onNext: (T) -> Unit = {}
    ) = subscribe(onNext, onError, onComplete).apply {
        if (bindName == null) compositeDisposable.bind(this) else compositeDisposable.bind(bindName, this)
    }


    fun <T : Any> Flowable<T>.bindSubscribe(
        bindName: String? = null,
        onError: (Throwable) -> Unit = { errorHandler(it) },
        onComplete: () -> Unit = {},
        onNext: (T) -> Unit = {}
    ) = subscribe(onNext, onError, onComplete).apply {
        if (bindName == null) compositeDisposable.bind(this) else compositeDisposable.bind(bindName, this)
    }

    fun <T : Any> Single<T>.bindSubscribe(
        bindName: String? = null,
        onError: (Throwable) -> Unit = { errorHandler(it) },
        onNext: (T) -> Unit = {}
    ) = subscribe(onNext, onError).apply {
        if (bindName == null) compositeDisposable.bind(this) else compositeDisposable.bind(bindName, this)
    }


    fun <T : Any> Single<T>.toLiveData(
        bindName: String? = null,
        onError: (Throwable) -> Unit = { errorHandler(it) }
    ): MutableLiveData<T> {
        val liveData = MutableLiveData<T>()
        bindSubscribe(bindName = bindName, onNext = {
            liveData.postValue(it)
        }, onError = onError)

        return liveData
    }


    fun <T : Any> Single<T>.justSubscribe(onNext: (T) -> Unit = {}) = subscribe(onNext, {}).apply {
        compositeDisposable.bind(this)
    }


    fun <T : Any> Flowable<T>.justSubscribe(
        bindName: String? = null,
        onComplete: () -> Unit = {},
        onNext: (T) -> Unit = {}
    ) = subscribe(onNext, {}, onComplete).apply {
        if (bindName == null) compositeDisposable.bind(this) else compositeDisposable.bind(bindName, this)
    }

    fun Completable.bindSubscribe(
        bindName: String? = null,
        onError: (Throwable) -> Unit =
            { errorHandler(it) },
        onNext: () -> Unit = {}
    ) = subscribe(onNext, onError).apply {
        if (bindName == null) compositeDisposable.bind(this) else compositeDisposable.bind(bindName, this)
    }

    fun Completable.justSubscribe(
        onNext: () -> Unit = {}
    ) = subscribe(onNext, {})

}

class EmptyViewModel : CozyViewModel()