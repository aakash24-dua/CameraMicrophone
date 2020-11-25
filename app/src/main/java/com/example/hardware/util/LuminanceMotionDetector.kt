
package com.example.hardware.util

import java.util.*

class LuminanceMotionDetector : IMotionDetector {
    /**
     * Difference in luma for each pixel
     */
    private var VALUE_THRESHOLD = 50

    /**
     * Difference in number of pixel for each image
     */
    private var NUMBER_THRESHOLD = 10000

    /**
     * Sets different sensitivity for the algorithm
     * @param thresh sensitivity identifier
     */
    override fun setThreshold(thresh: Int) {
        when (thresh) {
            MOTION_LOW -> {
                VALUE_THRESHOLD = 60
                NUMBER_THRESHOLD = 20000
            }
            MOTION_MEDIUM -> {
                VALUE_THRESHOLD = 50
                NUMBER_THRESHOLD = 10000
            }
            MOTION_HIGH -> {
                VALUE_THRESHOLD = 40
                NUMBER_THRESHOLD = 9000
            }
        }
    }

    /*
	 * (non-Javadoc)
	 * @see me.ziccard.secureit.motiondetection.IMotionDetector#detectMotion(int[], int[], int, int)
	 */
    override fun detectMotion(
        oldImage: IntArray?, newImage: IntArray?, width: Int,
        height: Int
    ): List<Int?>? {
        if (oldImage == null || newImage == null) throw NullPointerException()
        require(oldImage.size == newImage.size)
        val differentPixels = ArrayList<Int?>()
        var differentPixelNumber = 0
        for (ij in 0 until height * width) {
            val newPixelValue = newImage[ij]
            val oldPixelValue = oldImage[ij]
            if (Math.abs(newPixelValue - oldPixelValue) >= VALUE_THRESHOLD) {
                differentPixelNumber++
                differentPixels.add(ij)
            }
        }
        return if (differentPixelNumber > NUMBER_THRESHOLD) {
            differentPixels
        } else null
    }

    companion object {
        /**
         * Levels of motion detection
         */
        const val MOTION_LOW = 0
        const val MOTION_MEDIUM = 1
        const val MOTION_HIGH = 2
    }
}