
package com.example.hardware.util

interface IMotionDetector {
    /**
     * Detects differences between old and new image
     * and return pixel indexes that differ more than
     * a specified threshold
     * @param oldImage
     * @param newImage
     * @param width
     * @param height
     * @return
     */
    fun detectMotion(
        oldImage: IntArray?,
        newImage: IntArray?,
        width: Int,
        height: Int
    ): List<Int?>?

    /**
     * Sets the sensitivity
     * @param thresh
     */
    fun setThreshold(thresh: Int)
}