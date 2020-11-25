
package com.example.hardware.util

import android.graphics.Bitmap
import android.graphics.Matrix
import java.io.ByteArrayOutputStream

object ImageCodec {
    /**
     * Extracts the luminance component from the
     * given YCbCr 420 image
     */
	@JvmStatic
	fun N21toLuma(YUVimage: ByteArray?, width: Int, height: Int): IntArray {
        if (YUVimage == null) throw NullPointerException()
        val frameSize = width * height
        val lumaImage = IntArray(frameSize)
        for (ij in 0 until height * width) {
            var luminance = (0xff and YUVimage[ij].toInt()) - 16
            if (luminance < 0) luminance = 0
            lumaImage[ij] = luminance
        }
        return lumaImage
    }

    /**
     * Converts a luminance matrix to a RGB grayscale bitmap
     * @param lum
     * @param width
     * @param height
     * @return
     */
	@JvmStatic
	fun lumaToGreyscale(lum: IntArray?, width: Int, height: Int): IntArray {
        if (lum == null) throw NullPointerException()
        val greyscale = IntArray(height * width)
        for (ij in greyscale.indices) {
            // create the RGB-grey color corresponding to the specified luma component
            greyscale[ij] =
                lum[ij] shl 8 or lum[ij] shl 8 or lum[ij] and 0x00FFFFFF
        }
        return greyscale
    }

    @JvmStatic
	fun lumaToBitmapGreyscale(lum: IntArray?, width: Int, height: Int): Bitmap {
        if (lum == null) throw NullPointerException()
        return Bitmap.createBitmap(
            lumaToGreyscale(lum, width, height),
            width,
            height,
            Bitmap.Config.RGB_565
        )
    }

    /**
     * Rotates a bitmat of the given degrees
     * @param bmp
     * @param degrees
     * @return
     */
    fun rotate(bmp: Bitmap?, degrees: Int, reflex: Boolean): Bitmap {
        if (bmp == null) throw NullPointerException()

        //getting scales of the image  
        val width = bmp.width
        val height = bmp.height

        //Creating a Matrix and rotating it to specified degrees   
        val matrix = Matrix()
        matrix.postRotate(degrees.toFloat())
        if (reflex) matrix.postScale(-1f, 1f)

        //Getting the rotated Bitmap  
        val rotatedBmp = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true)
        val stream = ByteArrayOutputStream()
        rotatedBmp.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return rotatedBmp
    }
}