
package com.example.hardware.async

import android.content.Context

object MicrophoneTaskFactory {
    private var samplerTask: MicSamplerTask? = null

    @Throws(RecordLimitExceeded::class)
    fun makeSampler(context: Context?): MicSamplerTask? {
        if (samplerTask != null && !samplerTask!!.isCancelled) throw RecordLimitExceeded
        samplerTask = MicSamplerTask()
        return samplerTask
    }

    fun pauseSampling() {
        if (samplerTask != null) {
            samplerTask!!.pause()
        }
    }

    fun restartSampling() {
        if (samplerTask != null) {
            samplerTask!!.restart()
        }
    }

    val isSampling: Boolean
        get() = samplerTask != null && samplerTask!!.isSampling

    object RecordLimitExceeded : Exception() {
        private const val serialVersionUID = 7030672869928993643L
    }
}