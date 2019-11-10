package ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import ua.palamarenko.cozyandroid2.CozyLibrarySettings
import ua.palamarenko.cozyandroid2.base_fragment.navigation.CozyBaseViewModel
import java.io.File

open class CozyViewModel : CozyBaseViewModel() {

    val tm: TaskManager = TaskManager()
    val liveDataMap = HashMap<String, MutableLiveData<*>>()


    open fun errorHandler(throwable: Throwable) {}


    private fun validateError(throwable: Throwable) {
            errorHandler(throwable)
    }


    fun checkMigrationError(throwable: Throwable, dbName: String): Boolean {
        if (
            throwable is IllegalStateException &&
            throwable.message?.startsWith("Room cannot verify the data integrity.") == true
        ) {
            deleteDatabaseFile(dbName)
            return true
        }
        return false
    }


    private fun deleteDatabaseFile(dbName: String) {
        try {
            val databases =
                File(CozyLibrarySettings.appContext!!.applicationInfo.dataDir + "/databases")
            val db = File(databases, dbName)
            db.delete()
            val journal = File(databases, "Db-journal")
            if (journal.exists()) {
                journal.delete()
            }
        } catch (e: Exception) {
        }
    }


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
        onError: (Throwable) -> Unit = { validateError(it) },
        onComplete: () -> Unit = {},
        onNext: (T) -> Unit = {}
    ) = subscribe(onNext, onError, onComplete).apply {
        if (bindName == null) compositeDisposable.bind(this) else compositeDisposable.bind(
            bindName,
            this
        )
    }


    fun <T : Any> Flowable<T>.bindSubscribe(
        bindName: String? = null,
        onError: (Throwable) -> Unit = { validateError(it) },
        onComplete: () -> Unit = {},
        onNext: (T) -> Unit = {}
    ) = subscribe(onNext, onError, onComplete).apply {
        if (bindName == null) compositeDisposable.bind(this) else compositeDisposable.bind(
            bindName,
            this
        )
    }

    fun <T : Any> Single<T>.bindSubscribe(
        bindName: String? = null,
        onError: (Throwable) -> Unit = { validateError(it) },
        onNext: (T) -> Unit = {}
    ) = subscribe(onNext, onError).apply {
        if (bindName == null) compositeDisposable.bind(this) else compositeDisposable.bind(
            bindName,
            this
        )
    }

    fun <T : Any> Flowable<T>.toLiveData(
        bindName: String? = null,
        onError: (Throwable) -> Unit = { validateError(it) }
    ): MutableLiveData<T> {

        val liveData = if (bindName != null && liveDataMap.get(bindName) != null) {
            liveDataMap.get(bindName) as MutableLiveData<T>
        } else {
            MutableLiveData()
        }

        if (bindName != null && liveDataMap.get(bindName) == null) {
            liveDataMap.put(bindName, liveData)
        }

        bindSubscribe(bindName, onError, {}, {
            liveData.postValue(it)
        })

        return liveData

    }


    fun <T : Any> Single<T>.toLiveData(
        bindName: String? = null,
        onError: (Throwable) -> Unit = { validateError(it) }
    ): MutableLiveData<T> {

        val liveData = if (bindName != null && liveDataMap[bindName] != null) {
            liveDataMap.get(bindName) as MutableLiveData<T>
        } else {
            MutableLiveData()
        }

        if (bindName != null && liveDataMap.get(bindName) == null) {
            liveDataMap[bindName] = liveData
        }


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
        if (bindName == null) compositeDisposable.bind(this) else compositeDisposable.bind(
            bindName,
            this
        )
    }

    fun Completable.bindSubscribe(
        bindName: String? = null,
        onError: (Throwable) -> Unit =
            { validateError(it) },
        onNext: () -> Unit = {}
    ) = subscribe(onNext, onError).apply {
        if (bindName == null) compositeDisposable.bind(this) else compositeDisposable.bind(
            bindName,
            this
        )
    }

    fun Completable.justSubscribe(
        onNext: () -> Unit = {}
    ) = subscribe(onNext, {})

    override fun onCleared() {
        super.onCleared()
        liveDataMap.clear()
    }
}

class EmptyViewModel : CozyViewModel()