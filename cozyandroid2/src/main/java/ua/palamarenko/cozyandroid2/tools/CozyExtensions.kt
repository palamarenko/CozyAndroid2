package ua.palamarenko.cozyandroid2.tools

import android.graphics.Point
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.google.gson.Gson
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ua.palamarenko.cozyandroid2.BuildConfig
import ua.palamarenko.cozyandroid2.CozyLibrary
import ua.palamarenko.cozyandroid2.appContext
import java.lang.Exception


var SCREEN_WIDTH = 0
var SCREEN_HEIGHT = 0

fun AppCompatActivity.saveScreenDimensions() {
    windowManager.defaultDisplay.apply {
        val size = Point()
        this.getSize(size)
        SCREEN_WIDTH = size.x
        SCREEN_HEIGHT = size.y
    }
}

fun EditText.listen(listener: (String) -> Unit) {

    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable) {
            listener.invoke(s.toString())
        }
    })
}



fun dpToPx(dp: Float): Int {
    val resources = appContext!!.resources
    val metrics = resources.displayMetrics
    val px = dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    return px.toInt()
}


fun pxToDp(px: Float): Int {
    val resources = appContext!!.resources
    val metrics = resources.displayMetrics
    val dp = px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    return dp.toInt()
}




fun <T> Single<T>.setSchedulers(
    subscribeOn: Scheduler = Schedulers.io(),
    observeOn: Scheduler = AndroidSchedulers.mainThread()
): Single<T> {
    return this.subscribeOn(subscribeOn)
        .observeOn(observeOn)
}


fun <T> Flowable<T>.setSchedulers(
    subscribeOn: Scheduler = Schedulers.io(),
    observeOn: Scheduler = AndroidSchedulers.mainThread()
): Flowable<T> {
    return this.subscribeOn(subscribeOn)
        .observeOn(observeOn)
}

fun <T> Observable<T>.setSchedulers(
    subscribeOn: Scheduler = Schedulers.io(),
    observeOn: Scheduler = AndroidSchedulers.mainThread()
): Observable<T> {
    return this.subscribeOn(subscribeOn)
        .observeOn(observeOn)
}

fun Completable.setSchedulers(
    subscribeOn: Scheduler = Schedulers.io(),
    observeOn: Scheduler = AndroidSchedulers.mainThread()
): Completable {
    return this.subscribeOn(subscribeOn)
        .observeOn(observeOn)
}


fun <T> MutableLiveData<T>.listen(owner: LifecycleOwner, observe: (T) -> Unit) {
    observe(owner, Observer {
        if (it != null) {
            observe.invoke(it)
        }
    })
}

fun String.isValidEmail(): Boolean {
    return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun Any.string(id: Int): String {
    return appContext!!.string(id)
}



fun EditText.string(): String {
    return text.toString()
}


private fun LOG_THIS(key: String?, mes: Any?) {
    val useKey = key ?: "logos"
    val useMes = mes ?: "message == null"

    if (BuildConfig.DEBUG) Log.d("MY_LOG_$useKey", useMes.toString())
}

fun Any?.LOG(key: String = "TEST") {
    if (this == null) {
        LOG_EVENT(key, "OBJECT_NULL")
        return
    }
    try {
        LOG_EVENT(key, Gson().toJson(this))
    } catch (e: Exception) {

    }

}


fun LOG_EVENT(key: String, vararg mes: Any?) {
    var message = ""
    for (i in mes.indices) {
        message += " (" + i + " - " + if (mes[i] == null) "null" else mes[i].toString()
        message += ") "
    }
    LOG_THIS(key, message)
}