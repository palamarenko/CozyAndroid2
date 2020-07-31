package ua.palamarenko.cozyandroid2.tools.image_viewer

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.viewpager.widget.ViewPager
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_image_viewer_single.*
import kotlinx.android.synthetic.main.fragment_imageviewer.*
import ua.palamarenko.cozyandroid2.R
import ua.palamarenko.cozyandroid2.base_fragment.navigation.putString
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.BACK_PRESS
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.CozyFragment
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.EmptyViewModel
import ua.palamarenko.cozyandroid2.tools.*

class ImageViewerFragment : CozyFragment<EmptyViewModel>() {

    override val layout = R.layout.fragment_imageviewer

    override fun onStartScreen() {
        super.onStartScreen()
        ivBack.click { task(BACK_PRESS) }

        val obj = getArgumentObject<ImageViewObj>(DATA)!!


        if(obj?.list != null && obj.list.size > 1){
            pwPlate.setMaxCount(obj.list.size)
            pwPlate.currentItem(obj.position)
        }else{
            pwPlate.visibility = View.GONE
        }

        if(obj==null){
            task(BACK_PRESS)
        }else{
            tvToolbar.text = obj.title
            viewPager.initSimple(fragmentManager, obj.list) {
                ImageFragment().putString(URL,it)
            }
            viewPager.setCurrentItem(obj.position,false)

            viewPager.listen {
                pwPlate.currentItem(it)
            }
            pwPlate.listen {
                viewPager.setCurrentItem(viewPager.currentItem + it,true)
            }



            ivClick.setOnTouchListener { v, event ->
                pwPlate.showPlate()
                return@setOnTouchListener false
            }

        }

    }

    class ImageFragment : CozyFragment<EmptyViewModel>() {
        override val layout = R.layout.fragment_image_viewer_single

        override fun onStartScreen() {
            super.onStartScreen()
            imageLoad(photoView,getArgumentString(URL))
        }

        fun imageLoad(
            imageView : ImageView,
            url: String) {
            if (url.isNullOrEmpty()) {
                return
            }

            imageView.load(url,needCenterCrop = false)
        }
    }





}

class ViewPagerForImage : ViewPager {
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        try {
            return super.onTouchEvent(ev)
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
        }
        return false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        try {
            return super.onInterceptTouchEvent(ev)
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
        }
        return false
    }
}

