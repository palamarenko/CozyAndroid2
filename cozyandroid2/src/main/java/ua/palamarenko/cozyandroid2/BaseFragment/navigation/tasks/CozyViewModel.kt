package ua.palamarenko.cozyandroid2.BaseFragment.navigation.tasks

import android.os.Bundle
import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.HttpException
import ua.palamarenko.cozyandroid2.BaseFragment.navigation.BaseViewModel
import java.lang.IllegalStateException
import java.net.UnknownHostException

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
        onNext: (T) -> Unit = {},
        onError: (Throwable) -> Unit = { errorHandler(it) },
        onComplete: () -> Unit = {}
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


}

class EmptyViewModel : CozyViewModel()