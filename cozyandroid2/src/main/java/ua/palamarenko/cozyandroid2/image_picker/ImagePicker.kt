package ua.palamarenko.cozyandroid2.image_picker

import android.os.Bundle
import android.view.View
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.popup_pick_image.*
import ua.palamarenko.cozyandroid2.R
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.CozyBasePopup
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.CozyFragment
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.EmptyViewModel
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.convertAnyToTitle
import ua.palamarenko.cozyandroid2.tools.click
import java.io.File

class ImagePickerRequest(val strings : ImagePickPopupStrings = ImagePickPopupStrings(),val callback: (File) -> Unit)


object ImagePicker {

    fun pickImage(cozyFragment: CozyFragment<*>, strings : ImagePickPopupStrings = ImagePickPopupStrings(),callback: (File) -> Unit) {
        ImagePickPopup(
            cozyFragment = cozyFragment,
            strings = strings,
            callback =  callback).show(cozyFragment.fragmentManager)
    }


}


class ImagePickPopup(val cozyFragment: CozyFragment<*>, val strings : ImagePickPopupStrings,
                     val callback: (File) -> Unit) :
    CozyBasePopup<EmptyViewModel>() {

    override val layout = R.layout.popup_pick_image


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(strings.title!=null){
            tvTitle.text = convertAnyToTitle(strings.title!!)
        }

        if(strings.cameraTitle!=null){
            tvCameraTitle.text = convertAnyToTitle(strings.cameraTitle!!)
        }

        if(strings.cameraSubTitle!=null){
            tvCameraSubTitle.text = convertAnyToTitle(strings.cameraSubTitle!!)
        }

        if(strings.galleryTitle != null){
            tvGalleryTitle.text = convertAnyToTitle(strings.galleryTitle!!)
        }

        if(strings.gallerySubTitle != null){
            tvGallerySubTitle.text = convertAnyToTitle(strings.gallerySubTitle!!)
        }

        if(strings.cancel != null){
            tvCancel.text = convertAnyToTitle(strings.cancel!!)
        }






        cozyFragment.activityResultCallBack = {
            ImagePicker.getFile(it)?.apply {
                callback.invoke(this)
            }
        }

        tvCancel.click(false) {
            dismissAllowingStateLoss()
        }

        llCamera.click(false) {
            ImagePicker.Companion.with(cozyFragment)
                .crop(1f, 1f)
                .cameraOnly()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(cozyFragment.RESULT_ACTIVITY_CODE)

            dismissAllowingStateLoss()
        }


        llGallery.click(false) {
            ImagePicker.Companion.with(cozyFragment)
                .galleryOnly()

                .crop(1f, 1f)
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(cozyFragment.RESULT_ACTIVITY_CODE)

            dismissAllowingStateLoss()
        }


    }
}


class ImagePickPopupStrings(
    var title: Any? = null,
    var cameraTitle: Any? = null,
    var cameraSubTitle: Any? = null,
    var galleryTitle: Any? = null,
    var gallerySubTitle: Any? = null,
    var cancel: Any? = null
)

