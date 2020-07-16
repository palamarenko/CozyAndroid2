package ua.palamarenko.cozyandroid2.tools

import android.content.Context
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
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import io.reactivex.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import ua.palamarenko.cozyandroid2.CozyLibrarySettings
import java.io.File
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


fun inflateView(id: Int): View {
    return View.inflate(CozyLibrarySettings.appContext, id, null)
}


fun getLocale(): Locale {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        CozyLibrarySettings.appContext!!.resources.configuration.locales.get(0);
    } else {
        CozyLibrarySettings.appContext!!.resources.configuration.locale;
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

fun <T> T.toSingle(): Single<T> {
    return Single.just(this)
}

fun <T> T.toFlowable(): Flowable<T> {
    return Flowable.just(this)
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
                            if (foreground == null) {
                                foreground = getDrawable(outValue.resourceId)
                            }
                        }
                    } else {
                        context.theme.resolveAttribute(
                            android.R.attr.selectableItemBackgroundBorderless,
                            outValue,
                            true
                        )
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (background == null) {
                                val drawable = getDrawable(outValue.resourceId)
                                drawable.level = 882
                                background = drawable
                            } else if (foreground == null && background.level != 882) {
                                foreground = getDrawable(outValue.resourceId)
                            }

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

fun Long.isToday(): Boolean {
    val time = Calendar.getInstance()
    time.timeInMillis = this

    val now = Calendar.getInstance()

    return now.get(Calendar.DAY_OF_YEAR) == time.get(Calendar.DAY_OF_YEAR) && now.get(Calendar.YEAR) == time.get(
        Calendar.YEAR
    )

}


fun EditText.requestFocusAndShowKeyboard() {
    requestFocus()
    postDelayed({
        val keyboard = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        keyboard!!.showSoftInput(this, 0)
    }, 200)
}

fun Date.isToday(): Boolean {
    val time = Calendar.getInstance()
    time.timeInMillis = this.time

    val now = Calendar.getInstance()

    return now.get(Calendar.DAY_OF_YEAR) == time.get(Calendar.DAY_OF_YEAR) && now.get(Calendar.YEAR) == time.get(
        Calendar.YEAR
    )

}


fun ViewPager.initSimple(fm: FragmentManager?, count: Int, bind: (Int) -> Fragment) {

    val list = ArrayList<Int>()

    for (i in 0 until count) {
        list.add(i)
    }

    if (fm != null)
        adapter = CozyPagerAdapter(fm, list, bind, { "" })
}


fun TabLayout.listen(listener: (tab: TabLayout.Tab) -> Unit) {
    this.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            listener.invoke(tab)
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {

        }

        override fun onTabReselected(tab: TabLayout.Tab) {

        }
    })
}


fun <T> List<T>.add(item: T): List<T> {
    if (this is ArrayList<T>) {
        add(item)
        return this
    }
    if (this is LinkedList<T>) {
        add(item)
        return this
    }

    val list = ArrayList<T>()
    list.addAll(this)
    list.add(item)
    return list

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

fun File.toMultipartBody(name: String): MultipartBody.Part {
    val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), this)
    return MultipartBody.Part.createFormData(name, this.name, requestFile)
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

fun getAppString(id: Int): String {
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

fun <T> LiveData<T>.listen(owner: LifecycleOwner, observe: (T) -> Unit) {
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

private fun getDislay(any: Any?): String {
    try {
        if (any is String) {
            return any
        }
        if (any is Number) {
            return any.toString()
        }
        if (any == null) {
            return "NULL"
        }

        return Gson().toJson(any)
    } catch (e: java.lang.Exception) {
        return e.message ?: ""
    }


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
        message += " (" + i + " - " + getDislay(mes[i])
        message += ") "
    }



    LOG_THIS(key, message)
}
