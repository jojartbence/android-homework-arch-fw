package com.jojartbence.helpers

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import com.jojartbence.archeologicalfieldwork.R


fun showImagePicker(parent: Fragment, id: Int) {
    val intent = Intent()
    intent.type = "image/*"
    intent.action = Intent.ACTION_OPEN_DOCUMENT
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    val chooser = Intent.createChooser(intent, R.string.image_picker_select_image.toString())
    parent.startActivityForResult(chooser, id)
}


fun showCameraIntent(parent: Fragment, id: Int) {
    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    parent.startActivityForResult(cameraIntent, id)
}


fun readImageFromPath(context: Context, path : String?) : Bitmap? {
    if (path == null) {
        return null
    }

    var bitmap : Bitmap? = null
    val uri = Uri.parse(path)
    if (uri != null) {
        try {
            val parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r")
            val fileDescriptor = parcelFileDescriptor?.getFileDescriptor()
            bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor?.close()
        } catch (e: Exception) {
        }
    }
    return bitmap
}