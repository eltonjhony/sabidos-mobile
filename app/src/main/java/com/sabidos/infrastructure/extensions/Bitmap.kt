package com.sabidos.infrastructure.extensions

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import com.sabidos.infrastructure.logging.Logger

fun Bitmap.modifyOrientation(orientation: Int): Bitmap =
    runCatching {
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotate(90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotate(180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotate(270f)
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> flip(
                horizontal = true,
                vertical = false
            )
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> flip(horizontal = false, vertical = true)
            else -> this
        }
    }.getOrElse {
        Logger.withTag(Bitmap::class.java.simpleName).withCause(it)
        this
    }

fun Bitmap.rotate(degrees: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degrees)
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

fun Bitmap.flip(horizontal: Boolean, vertical: Boolean): Bitmap {
    val matrix = Matrix()
    matrix.preScale(if (horizontal) -1f else 1f, if (vertical) -1f else 1f)
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}