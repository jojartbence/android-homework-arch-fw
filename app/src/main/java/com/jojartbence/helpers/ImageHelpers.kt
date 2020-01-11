package com.jojartbence.helpers

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.jojartbence.archeologicalfieldwork.R
import com.jojartbence.model.SiteModel


fun showImagePicker(parent: Fragment, id: Int) {
    val intent = Intent()
    intent.type = "image/*"
    intent.action = Intent.ACTION_OPEN_DOCUMENT
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    val chooser = Intent.createChooser(intent, R.string.image_picker_select_image.toString())
    parent.startActivityForResult(chooser, id)
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


fun showImageUsingGlide(context: Context, image: SiteModel.ImageContainer?, into: ImageView, onError: Drawable? = null) {

    // Check if local image path is valid or not
    val path = when(readImageFromPath(context, image?.memoryPath) != null) {
        true -> image?.memoryPath
        false -> image?.url
    }

    Glide.with(context).load(path).error(onError).into(into)
}