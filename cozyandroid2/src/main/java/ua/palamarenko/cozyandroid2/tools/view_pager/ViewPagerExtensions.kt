package ua.palamarenko.cozyandroid2.tools.view_pager

import android.content.Context
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.view_image_adapter.view.*
import ua.palamarenko.cozyandroid2.R
import ua.palamarenko.cozyandroid2.tools.load


class ImagePageAdapter(private val mContext: Context, private val list : List<String>) : PagerAdapter() {

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(mContext)
        val view = inflater.inflate(R.layout.view_image_adapter, collection, false) as ViewGroup
        view.ivImage.load(list[position])
        collection.addView(view)
        return view
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}


fun ViewPager.imageList(list : List<String>){
    adapter = ImagePageAdapter(context,list)
}