package ua.palamarenko.cozyandroid2.tools.image_viewer

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import com.google.gson.Gson
import ua.palamarenko.cozyandroid2.CozyLibrarySettings
import ua.palamarenko.cozyandroid2.R
import ua.palamarenko.cozyandroid2.base_fragment.navigation.putObject
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.CozyActivity
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.EmptyViewModel


const val DATA = "IMAGE_DATA"
const val URL = "IMAGE_URL"
class ImageViewObj(val list: List<String>,val position : Int = 0, val title : String)


fun getImageViewBundle(imageList : List<String>?,url: String, title : String) : Intent{
    val intent = Intent(Intent.ACTION_VIEW)
    intent.component = ComponentName(CozyLibrarySettings.appContext!!,
        ImageViewerActivity::class.java
    )
    intent.putExtra(DATA,Gson().toJson(ImageViewObj(imageList?: arrayListOf(url), (imageList?:ArrayList()).indexOf(url),title)))
    return intent
}




open class ImageViewerActivity : CozyActivity<EmptyViewModel>(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.ImageView)
        val obj = Gson().fromJson<ImageViewObj>(intent.getStringExtra(DATA),ImageViewObj::class.java)
        simpleInit(ImageViewerFragment().putObject(DATA,obj))
    }


}