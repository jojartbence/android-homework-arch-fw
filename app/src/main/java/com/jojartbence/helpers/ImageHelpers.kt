package com.jojartbence.helpers

import android.content.Intent
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
