package ua.palamarenko.cozyandroid2.tools

import android.graphics.Point
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.SystemClock
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
import io.reactivex.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ua.palamarenko.cozyandroid2.CozyLibrarySettings
import ua.palamarenko.cozyandroid2.R
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.CozyFragment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


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

fun EditText.toText(): String {
    return text.toString()
}

fun CheckBox.listen(listener: (Boolean) -> Unit) {
    setOnCheckedChangeListener { _, isChecked ->
        listener.invoke(isChecked)
    }
}


fun SearchView.listen(listener: (String) -> Unit, submitListener: ((String) -> Unit)? = null) {

    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            submitListener?.invoke(query ?: "")
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            listener.invoke(newText ?: "")
            return true
        }
    })

}


fun ViewPager.listen(clear: Boolean = true, listener: (Int) -> Unit) {
    if (clear)
        clearOnPageChangeListeners()
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {}
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {

        }

        override fun onPageSelected(position: Int) {
            listener.invoke(position)
        }
    })
}

fun View.click(clickBack: Boolean = true, click: () -> Unit) {
    if (clickBack) {
        try {
            post {
                try {
                    val outValue = TypedValue()

                    if (width / 2 > height) {
                        context.theme.resolveAttribute(
                            android.R.attr.selectableItemBackground,
                            outValue,
                            true
                        )
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            foreground = getDrawable(outValue.resourceId)
                        }
                    } else {
                        context.theme.resolveAttribute(
                            android.R.attr.selectableItemBackgroundBorderless,
                            outValue,
                            true
                        )
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            foreground = getDrawable(outValue.resourceId)
                        }
                    }
                } catch (e: Exception) {
                }
            }


        } catch (e: Exception) {
        }

    }

    setOnClickListener { click.invoke() }
}

fun EditText.openKeyboard() {
    post {
        dispatchTouchEvent(
            MotionEvent.obtain(
                SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(),
                MotionEvent.ACTION_DOWN,
                10f,
                10f,
                0
            )
        )
        dispatchTouchEvent(
            MotionEvent.obtain(
                SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(),
                MotionEvent.ACTION_UP,
                10f,
                10f,
                0
            )
        )
    }
}

fun <T> ViewPager.initSimple(fm: FragmentManager?, list: List<T>, bind: (T) -> Fragment) {
    if (fm != null)
        adapter = CozyPagerAdapter(fm, list, bind, { "" })
}

fun <T> ViewPager.initWithTitles(
    fm: FragmentManager?,
    list: List<T>,
    bind: (T) -> Fragment,
    bindTitle: (Int) -> CharSequence = { "" }
) {
    if (fm != null)
        adapter = CozyPagerAdapter(fm, list, bind, bindTitle)
}

fun ViewPager.initWithTitles(
    fm: FragmentManager?,
    count: Int,
    bind: (Int) -> Fragment,
    bindTitle: (Int) -> CharSequence = { "" }
) {

    val list = ArrayList<Int>()

    for (i in 0 until count) {
        list.add(i)
    }

    if (fm != null)
        adapter = CozyPagerAdapter(fm, list, bind, bindTitle)
}


fun ViewPager.getCurrentFragment(): Fragment? {

    if (adapter != null && adapter is CozyPagerAdapter<*>) {
        return (adapter as CozyPagerAdapter<*>).instantiateItem(this, currentItem) as? Fragment
    }
    return null
}

fun Long.formatDate(format: String = "dd-M-yyyy hh:mm:ss"): String {
    val formatter = SimpleDateFormat(format, Locale.US)
    return formatter.format(Date(this))
}

fun Date.formatDate(format: String = "dd-M-yyyy hh:mm:ss"): String {
    val formatter = SimpleDateFormat(format, Locale.US)
    return formatter.format(this)
}


class CozyPagerAdapter<T>(
    fm: FragmentManager,
    val adapterList: List<T>,
    val bindAdapter: (T) -> Fragment,
    val bindTitle: (Int) -> CharSequence
) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return bindAdapter.invoke(adapterList[position])
    }

    override fun getCount(): Int {
        return adapterList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return bindTitle.invoke(position)
    }


}


fun ViewPager.initSimple(fm: FragmentManager?, count: Int, bind: (Int) -> Fragment) {

    val list = ArrayList<Int>()

    for (i in 0 until count) {
        list.add(i)
    }

    if (fm != null)
        adapter = CozyPagerAdapter(fm, list, bind, { "" })
}


fun ViewPager.listenScroll(listener: (position: Int, positionOffset: Float, positionOffsetPixels: Int) -> Unit) {
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {}
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            listener.invoke(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {}
    })
}


fun Switch.listen(listener: (Boolean) -> Unit) {

    setOnClickListener {
        listener.invoke(isChecked)
    }
}

fun EditText.listen(listener: (String) -> Unit) {

    var lastData: String = ""


    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable) {
            if (lastData == s.toString()) {
            } else {
                lastData = s.toString()
                listener.invoke(s.toString())
            }

        }
    })
}

fun dpToPx(dp: Float): Int {
    val resources = CozyLibrarySettings.appContext!!.resources
    val metrics = resources.displayMetrics
    val px = dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    return px.toInt()
}

fun pxToDp(px: Float): Int {
    val resources = CozyLibrarySettings.appContext!!.resources
    val metrics = resources.displayMetrics
    val dp = px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    return dp.toInt()
}


fun View.visibleState(visible: Boolean, hideState: Int = View.GONE) {
    visibility = if (visible) {
        View.VISIBLE
    } else {
        hideState
    }
}

fun getString(id: Int): String {
    return CozyLibrarySettings.appContext!!.getString(id)
}

fun getColor(id: Int): Int {
    return ContextCompat.getColor(CozyLibrarySettings.appContext!!, id)
}

fun getDrawable(id: Int): Drawable {
    return ContextCompat.getDrawable(CozyLibrarySettings.appContext!!, id)!!
}


fun <T> List<T>.trySubList(from: Int? = null, to: Int? = null): List<T> {
    return if (size > from ?: 0 && size > to ?: 0) {
        subList(from ?: 0, to ?: size)
    } else {
        return if (from == size) {
            ArrayList()
        } else {
            this
        }
    }
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


fun EditText.string(): String {
    return text.toString()
}


private fun LOG_THIS(key: String?, mes: Any?) {
    val useKey = key ?: "logos"
    val useMes = mes ?: "message == null"

    Log.d("MY_LOG_$useKey", useMes.toString())
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