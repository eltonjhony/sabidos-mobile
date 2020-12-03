package com.sabidos.infrastructure.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import com.sabidos.infrastructure.helpers.ImageResizerHelper
import com.sabidos.infrastructure.logging.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.InputStream

fun Context.showKeyboard() {
    val imm: InputMethodManager? =
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun Context.drawable(@DrawableRes id: Int): Drawable? = runCatching {
    ContextCompat.getDrawable(this, id)
}.getOrNull()

fun Context.color(@ColorRes id: Int): Int =
    ContextCompat.getColor(this, id)

fun Context.getBitmapFromUri(uri: Uri, options: BitmapFactory.Options? = null): Bitmap? =
    runCatching {
        val parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "r")
        val fileDescriptor = parcelFileDescriptor?.fileDescriptor
        val image: Bitmap? = if (options != null)
            BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options)
        else
            BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor?.close()
        return image
    }.getOrNull()

fun Context.getImageHgtWdt(uri: Uri): Pair<Int, Int> {
    val opt = BitmapFactory.Options()
    opt.inJustDecodeBounds = true
    getBitmapFromUri(uri, opt)

    var actualHgt = (opt.outHeight).toFloat()
    var actualWdt = (opt.outWidth).toFloat()

    val maxHeight = 720f
    val maxWidth = 1280f
    var imgRatio = actualWdt / actualHgt
    val maxRatio = maxWidth / maxHeight

    // width and height values are set maintaining the aspect ratio of the image
    if (actualHgt > maxHeight || actualWdt > maxWidth) {
        when {
            imgRatio < maxRatio -> {
                imgRatio = maxHeight / actualHgt
                actualWdt = (imgRatio * actualWdt)
                actualHgt = maxHeight
            }
            imgRatio > maxRatio -> {
                imgRatio = maxWidth / actualWdt
                actualHgt = (imgRatio * actualHgt)
                actualWdt = maxWidth
            }
            else -> {
                actualHgt = maxHeight
                actualWdt = maxWidth
            }
        }
    }

    return Pair(actualHgt.toInt(), actualWdt.toInt())
}

suspend fun Context.compressImageFile(
    path: String,
    shouldOverride: Boolean = true,
    uri: Uri
): String? {
    return withContext(Dispatchers.IO) {
        var scaledBitmap: Bitmap? = null

        try {

            val (hgt, wdt) = getImageHgtWdt(uri)

            // Part 1: Decode image
            val unscaledBitmap = ImageResizerHelper.decodeFile(
                this@compressImageFile,
                uri,
                wdt,
                hgt,
                ImageResizerHelper.ScalingLogic.FIT
            )

            if (unscaledBitmap != null) {
                scaledBitmap = if (!(unscaledBitmap.width <= 800 && unscaledBitmap.height <= 800)) {
                    // Part 2: Scale image
                    ImageResizerHelper.createScaledBitmap(
                        unscaledBitmap,
                        wdt,
                        hgt,
                        ImageResizerHelper.ScalingLogic.FIT
                    )
                } else {
                    unscaledBitmap
                }
            }

            // Store to tmp file
            val mFolder = File("$filesDir/Images")
            if (!mFolder.exists()) {
                mFolder.mkdir()
            }

            val tmpFile =
                File(mFolder.absolutePath, "IMG_${ImageResizerHelper.getTimestampString()}.jpg")

            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(tmpFile)
                scaledBitmap?.compress(
                    Bitmap.CompressFormat.JPEG,
                    ImageResizerHelper.getImageQualityPercent(tmpFile),
                    fos
                )
                fos.flush()
                fos.close()
            } catch (e: FileNotFoundException) {
                fos?.close()
                Logger.withTag(Activity::class.java.simpleName).withCause(e)

            } catch (e: Exception) {
                fos?.close()
                Logger.withTag(Activity::class.java.simpleName).withCause(e)
            }

            var compressedPath = ""
            if (tmpFile.exists() && tmpFile.length() > 0) {
                compressedPath = tmpFile.absolutePath
                if (shouldOverride) {
                    val srcFile = File(path)
                    val result = tmpFile.copyTo(srcFile, true)
                    Logger.withTag(Activity::class.java.simpleName)
                        .d("copied file ${result.absolutePath}")
                    Logger.withTag(Activity::class.java.simpleName)
                        .d("Delete temp file ${tmpFile.delete()}")
                }
            }

            scaledBitmap?.recycle()

            return@withContext if (shouldOverride) path else compressedPath
        } catch (e: Throwable) {
            Logger.withTag(Activity::class.java.simpleName).withCause(e)
        }

        return@withContext null
    }

}

fun Context.getOrientationFrom(uri: Uri): Int {
    var orientation = 0
    var inputStream: InputStream? = null
    try {
        inputStream = contentResolver.openInputStream(uri)
        inputStream?.let { stream ->
            val ei = ExifInterface(stream)
            orientation = ei.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )
        }
    } catch (e: Exception) {
        Logger.withTag(Context::class.java.simpleName).withCause(e)
    } finally {
        inputStream?.close()
    }
    return orientation
}