package ua.palamarenko.cozyandroid2.tools.image_viewer

import android.os.Bundle
import com.google.gson.Gson
import ua.palamarenko.cozyandroid2.R
import ua.palamarenko.cozyandroid2.base_fragment.navigation.putObject
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.CozyActivity
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.EmptyViewModel


const val DATA = "IMAGE_DATA"
const val URL = "IMAGE_URL"
class ImageViewObj(val list: List<String>,val position : Int = 0)

fun imageViewBundle(imageList : List<String>?,url: String = "") : Bundle {
    if(imageList==null){
        return imageViewBundle(url)
    }


    return Bundle().apply {
        putString(DATA, Gson().toJson(ImageViewObj(imageList, imageList.indexOf(url))))
    }
}

fun imageViewBundle(url: String) : Bundle {
    return Bundle().apply {
        putString(DATA, Gson().toJson(ImageViewObj(arrayListOf(url))))
    }
}




open class ImageViewerActivity : CozyActivity<EmptyViewModel>(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.ImageView)
        val obj = Gson().fromJson<ImageViewObj>(intent.getStringExtra(DATA),ImageViewObj::class.java)
        simpleInit(ImageViewerFragment().putObject(DATA,obj))
    }


}