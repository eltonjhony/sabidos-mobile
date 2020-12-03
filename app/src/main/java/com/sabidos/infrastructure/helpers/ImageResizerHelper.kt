package com.sabidos.infrastructure.helpers

import android.content.Context
import android.graphics.*
import android.net.Uri
import com.sabidos.infrastructure.extensions.getBitmapFromUri
import com.sabidos.infrastructure.logging.Logger
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ImageResizerHelper {

    enum class ScalingLogic {
        CROP, FIT
    }

    companion object {

        fun getTimestampString(): String {
            val date = Calendar.getInstance()
            return SimpleDateFormat("yyyy MM dd hh mm ss", Locale.US).format(date.time)
                .replace(" ", "")
        }

        fun getImageQualityPercent(file: File): Int {
            val sizeInBytes = file.length()
            val sizeInKB = sizeInBytes / 1024
            val sizeInMB = sizeInKB / 1024

            return when {
                sizeInMB <= 1 -> 80
                sizeInMB <= 2 -> 60
                else -> 40
            }
        }

        fun decodeFile(
            context: Context,
            uri: Uri,
            dstWidth: Int,
            dstHeight: Int,
            scalingLogic: ScalingLogic
        ): Bitmap? {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            context.getBitmapFromUri(uri, options)
            options.inJustDecodeBounds = false

            options.inSampleSize = calculateSampleSize(
                options.outWidth,
                options.outHeight,
                dstWidth,
                dstHeight,
                scalingLogic
            )

            return context.getBitmapFromUri(uri, options)
        }

        fun createScaledBitmap(
            unscaledBitmap: Bitmap, dstWidth: Int, dstHeight: Int,
            scalingLogic: ScalingLogic
        ): Bitmap =
            runCatching {
                val srcRect = calculateSrcRect(
                    unscaledBitmap.width, unscaledBitmap.height,
                    dstWidth, dstHeight, scalingLogic
                )
                val dstRect = calculateDstRect(
                    unscaledBitmap.width,
                    unscaledBitmap.height,
                    dstWidth,
                    dstHeight,
                    scalingLogic
                )
                val scaledBitmap =
                    Bitmap.createBitmap(dstRect.width(), dstRect.height(), Bitmap.Config.ARGB_8888)
                val canvas = Canvas(scaledBitmap)
                canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, Paint(Paint.FILTER_BITMAP_FLAG))

                return@createScaledBitmap scaledBitmap
            }.getOrElse {
                Logger.withTag(ImageResizerHelper::class.java.simpleName).withCause(it)
                return@createScaledBitmap unscaledBitmap
            }


        private fun calculateSampleSize(
            srcWidth: Int, srcHeight: Int, dstWidth: Int, dstHeight: Int,
            scalingLogic: ScalingLogic
        ): Int {
            if (scalingLogic == ScalingLogic.FIT) {
                val srcAspect = srcWidth.toFloat() / srcHeight.toFloat()
                val dstAspect = dstWidth.toFloat() / dstHeight.toFloat()

                return if (srcAspect > dstAspect) {
                    srcWidth / dstWidth
                } else {
                    srcHeight / dstHeight
                }
            } else {
                val srcAspect = srcWidth.toFloat() / srcHeight.toFloat()
                val dstAspect = dstWidth.toFloat() / dstHeight.toFloat()

                return if (srcAspect > dstAspect) {
                    srcHeight / dstHeight
                } else {
                    srcWidth / dstWidth
                }
            }
        }

        private fun calculateSrcRect(
            srcWidth: Int, srcHeight: Int, dstWidth: Int, dstHeight: Int,
            scalingLogic: ScalingLogic
        ): Rect {
            if (scalingLogic == ScalingLogic.CROP) {
                val srcAspect = srcWidth.toFloat() / srcHeight.toFloat()
                val dstAspect = dstWidth.toFloat() / dstHeight.toFloat()

                return if (srcAspect > dstAspect) {
                    val srcRectWidth = (srcHeight * dstAspect).toInt()
                    val srcRectLeft = (srcWidth - srcRectWidth) / 2
                    Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth, srcHeight)
                } else {
                    val srcRectHeight = (srcWidth / dstAspect).toInt()
                    val scrRectTop = (srcHeight - srcRectHeight) / 2
                    Rect(0, scrRectTop, srcWidth, scrRectTop + srcRectHeight)
                }
            } else {
                return Rect(0, 0, srcWidth, srcHeight)
            }
        }

        private fun calculateDstRect(
            srcWidth: Int, srcHeight: Int, dstWidth: Int, dstHeight: Int,
            scalingLogic: ScalingLogic
        ): Rect {
            return if (scalingLogic == ScalingLogic.FIT) {
                val srcAspect = srcWidth.toFloat() / srcHeight.toFloat()
                val dstAspect = dstWidth.toFloat() / dstHeight.toFloat()

                if (srcAspect > dstAspect) {
                    Rect(0, 0, dstWidth, (dstWidth / srcAspect).toInt())
                } else {
                    Rect(0, 0, (dstHeight * srcAspect).toInt(), dstHeight)
                }
            } else {
                Rect(0, 0, dstWidth, dstHeight)
            }
        }

    }
}