
package com.example.hardware.async

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Handler
import android.util.Log
import com.example.hardware.util.IMotionDetector
import com.example.hardware.util.ImageCodec.N21toLuma
import com.example.hardware.util.ImageCodec.lumaToBitmapGreyscale
import com.example.hardware.util.ImageCodec.lumaToGreyscale
import com.example.hardware.util.LuminanceMotionDetector
import java.util.*

/**
 * Task doing all image processing in backgrounds,
 * has a collection of listeners to notify in after having processed
 * the image
 * @author marco
 */
class MotionAsyncTask(
    private val rawOldPic: ByteArray?,
    private val rawNewPic: ByteArray,
    private val width: Int,
    private val height: Int,
    private val handler: Handler,
    private val motionSensitivity: Int
) : Thread() {
    // Input data
    private val listeners: MutableList<MotionListener> =
        ArrayList()

    // Output data
    private var lastBitmap: Bitmap? = null
    private var newBitmap: Bitmap? = null
    private var hasChanged = false

    interface MotionListener {
        fun onProcess(
            oldBitmap: Bitmap?,
            newBitmap: Bitmap?,
            motionDetected: Boolean
        )
    }

    fun addListener(listener: MotionListener) {
        listeners.add(listener)
    }

    override fun run() {
        val newPicLuma = N21toLuma(rawNewPic, width, height)
        if (rawOldPic == null) {
            newBitmap = lumaToBitmapGreyscale(newPicLuma, width, height)
            lastBitmap = newBitmap
        } else {
            val oldPicLuma = N21toLuma(rawOldPic, width, height)
            val detector: IMotionDetector = LuminanceMotionDetector()
            detector.setThreshold(motionSensitivity)
            val changedPixels =
                detector.detectMotion(oldPicLuma, newPicLuma, width, height)
            hasChanged = false
            val newPic = lumaToGreyscale(newPicLuma, width, height)
            if (changedPixels != null) {
                hasChanged = true
                for (changedPixel in changedPixels) {
                    newPic[changedPixel!!] = Color.RED
                }
            }
            lastBitmap = lumaToBitmapGreyscale(oldPicLuma, width, height)
            newBitmap = Bitmap.createBitmap(newPic, width, height, Bitmap.Config.RGB_565)
        }
        Log.i("MotionAsyncTask", "Finished processing, sending results")
        handler.post {
            for (listener in listeners) {
                Log.i("MotionAsyncTask", "Updating back view")
                listener.onProcess(
                    lastBitmap,
                    newBitmap,
                    hasChanged
                )
            }
        }
    }

}