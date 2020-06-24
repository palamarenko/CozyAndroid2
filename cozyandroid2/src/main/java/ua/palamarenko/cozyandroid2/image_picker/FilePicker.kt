package ua.palamarenko.cozyandroid2.image_picker

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import androidx.annotation.NonNull
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.popup_pick_image.*
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.MediaFile
import pl.aprilapps.easyphotopicker.MediaSource
import ua.palamarenko.cozyandroid2.CozyLibrarySettings
import ua.palamarenko.cozyandroid2.R
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.popups.CozyBasePopup
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.CozyFragment
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.EmptyViewModel
import ua.palamarenko.cozyandroid2.base_fragment.navigation.tasks.convertAnyToTitle
import ua.palamarenko.cozyandroid2.tools.click
import java.io.File
import java.io.FileOutputStream


enum class CROP_MODE { NONE, AVATAR, CUSTOM }


class PickSingleImagePopupRequest(
    val strings: ImagePickPopupStrings = ImagePickPopupStrings(),
    val callback: (File) -> Unit,
    val cropMode: CROP_MODE = CROP_MODE.AVATAR
) : ImageRequest()

class PickMultipleImageRequest(val callback: (List<File>) -> Unit) : ImageRequest()
class PickFileRequest(val callback: (File) -> Unit, val type: String = "*/*") : ImageRequest()
class PickPhotoRequest(val callback: (File) -> Unit, val cropMode: CROP_MODE = CROP_MODE.AVATAR) :
    ImageRequest()


sealed class ImageRequest


object FilePicker {

    private fun convertUriToFile(uri: Uri, name: String): File {
        val tmpFile = File(CozyLibrarySettings.appContext!!.cacheDir, name)
        val os = FileOutputStream(tmpFile)
        val iss = CozyLibrarySettings.appContext!!.contentResolver.openInputStream(uri)
        iss?.copyTo(os)
        os.flush()
        iss?.close()
        os.close()
        return tmpFile
    }


    fun pickFile(
        cozyFragment: CozyFragment<*>,
        pickFileRequest: PickFileRequest
    ): ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = pickFileRequest.type
        val i = Intent.createChooser(intent, "File")
        cozyFragment.startActivityForResult(i, 99)

        val activityResultCallBack: ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit) =
            { i: Int, i1: Int, intent: Intent? ->
                pickFileRequest.callback.invoke(
                    convertUriToFile(
                        intent!!.data!!,
                        dumpImageMetaData(intent.data!!)
                    )
                )
            }

        return activityResultCallBack
    }

    fun dumpImageMetaData(uri: Uri): String {
        val cursor: Cursor = CozyLibrarySettings.appContext!!.contentResolver
            .query(uri, null, null, null, null, null)!!
        cursor.use { cursor ->
            if (cursor.moveToFirst()) {
                return cursor.getString(
                    cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                )

            }
        }
        return ""
    }


    fun pickMultipleImage(
        cozyFragment: CozyFragment<*>,
        callback: (List<File>) -> Unit
    ): ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit) {

        val easyImage = EasyImage.Builder(cozyFragment.context!!)
            .setCopyImagesToPublicGalleryFolder(false)
            .setFolderName("Images")
            .allowMultiple(true)
            .build()


        val activityResultCallBack: ((requestCode: Int, resultCode: Int, data: Intent?) -> Unit) =
            { i: Int, i1: Int, intent: Intent? ->
                easyImage.handleActivityResult(
                    i,
                    i1,
                    intent,
                    cozyFragment.activity!!,
                    object : DefaultCallback() {
                        override fun onMediaFilesPicked(
                            imageFiles: Array<MediaFile>,
                            source: MediaSource
                        ) {
                            callback.invoke(imageFiles.map { it.file })
                        }

                        override fun onImagePickerError(
                            @NonNull error: Throwable,
                            @NonNull source: MediaSource
                        ) {
                            error.printStackTrace()
                        }

                        override fun onCanceled(@NonNull source: MediaSource) {}
                    })


            }
        easyImage.openGallery(cozyFragment)

        return activityResultCallBack
    }


    fun pickJustPhoto(
        cozyFragment: CozyFragment<*>,
        cropMode: CROP_MODE,
        callback: (File) -> Unit
    ) {
        val picker = ImagePicker.Companion.with(cozyFragment)
            .cameraOnly()
            .compress(1024)
            .maxResultSize(1080, 1080)

        when (cropMode) {
            CROP_MODE.AVATAR -> picker.crop(1f, 1f)
            CROP_MODE.CUSTOM -> picker.crop()
        }


        picker.start(cozyFragment.RESULT_ACTIVITY_CODE)


        cozyFragment.activityResultCallBack = { i, i1, data ->
            ImagePicker.getFile(data)?.apply {
                callback.invoke(this)
            }
        }
    }


    fun pickSingleImage(
        cozyFragment: CozyFragment<*>,
        strings: ImagePickPopupStrings = ImagePickPopupStrings(),
        callback: (File) -> Unit,
        cropMode: CROP_MODE
    ) {
        ImagePickPopup(
            cozyFragment = cozyFragment,
            strings = strings,
            callback = callback,
            cropMode = cropMode
        ).show(cozyFragment.fragmentManager)
    }


}


class ImagePickPopup(
    val cozyFragment: CozyFragment<*>, val strings: ImagePickPopupStrings,
    val callback: (File) -> Unit,
    val cropMode: CROP_MODE
) :
    CozyBasePopup<EmptyViewModel>() {

    override val layout = R.layout.popup_pick_image


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (strings.title != null) {
            tvTitle.text =
                convertAnyToTitle(
                    strings.title!!
                )
        }

        if (strings.cameraTitle != null) {
            tvCameraTitle.text =
                convertAnyToTitle(
                    strings.cameraTitle!!
                )
        }

        if (strings.cameraSubTitle != null) {
            tvCameraSubTitle.text =
                convertAnyToTitle(
                    strings.cameraSubTitle!!
                )
        }

        if (strings.galleryTitle != null) {
            tvGalleryTitle.text =
                convertAnyToTitle(
                    strings.galleryTitle!!
                )
        }

        if (strings.gallerySubTitle != null) {
            tvGallerySubTitle.text =
                convertAnyToTitle(
                    strings.gallerySubTitle!!
                )
        }

        if (strings.cancel != null) {
            tvCancel.text =
                convertAnyToTitle(
                    strings.cancel!!
                )
        }

        cozyFragment.activityResultCallBack = { i, i1, data ->
            ImagePicker.getFile(data)?.apply {
                callback.invoke(this)
            }
        }

        tvCancel.click(false) {
            dismissAllowingStateLoss()
        }

        llCamera.click(false) {
            val picker = ImagePicker.Companion.with(cozyFragment)
                .cameraOnly()
                .compress(1024)
                .maxResultSize(1080, 1080)

            when (cropMode) {
                CROP_MODE.AVATAR -> picker.crop(1f, 1f)
                CROP_MODE.CUSTOM -> picker.crop()
            }


            picker.start(cozyFragment.RESULT_ACTIVITY_CODE)

            dismissAllowingStateLoss()
        }


        llGallery.click(false) {
            val picker = ImagePicker.Companion.with(cozyFragment)
                .galleryOnly()
                .compress(1024)
                .maxResultSize(1080, 1080)

            when (cropMode) {
                CROP_MODE.AVATAR -> picker.crop(1f, 1f)
                CROP_MODE.CUSTOM -> picker.crop()
            }

            picker.start(cozyFragment.RESULT_ACTIVITY_CODE)

            dismissAllowingStateLoss()
        }


    }
}


open class ImagePickPopupStrings(
    var title: Any? = null,
    var cameraTitle: Any? = null,
    var cameraSubTitle: Any? = null,
    var galleryTitle: Any? = null,
    var gallerySubTitle: Any? = null,
    var cancel: Any? = null
)

