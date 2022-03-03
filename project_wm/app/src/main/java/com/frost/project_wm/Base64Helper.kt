package com.frost.project_wm

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import java.io.ByteArrayOutputStream
import android.os.Build

import android.content.ContentResolver
import android.graphics.ImageDecoder
import java.lang.Exception


class Base64Helper(val context: Context) {

    //encode image to base64 string
    fun enconde(image: Bitmap): String{
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imageBytes = baos.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

    //decode base64 string to image
    fun decode(image: String): Bitmap {
        val imageBytes = Base64.decode(image, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    //turn uri into bitmap
    fun uriToBitmap(uri: Uri): Bitmap? {
        var bitmap: Bitmap? = null
        val contentResolver: ContentResolver = context.contentResolver
        try {
            bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(contentResolver, uri)
            } else {
                val source: ImageDecoder.Source =
                    ImageDecoder.createSource(contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap
    }
}