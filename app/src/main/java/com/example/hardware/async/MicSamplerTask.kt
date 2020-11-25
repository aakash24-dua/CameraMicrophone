
package com.example.hardware.async

import android.os.AsyncTask
import android.util.Log
import com.example.hardware.util.AudioCodec
import java.io.IOException

class MicSamplerTask : AsyncTask<Void?, Any?, Void?>() {
    private var listener: MicListener? = null
    private var volumeMeter = AudioCodec()
    var isSampling = true
        private set
    private var paused = false

    interface MicListener {
        fun onSignalReceived(signal: ShortArray?)
        fun onMicError()
    }

    fun setMicListener(listener: MicListener?) {
        this.listener = listener
    }



    fun restart() {
        paused = false
        isSampling = true
    }

    fun pause() {
        paused = true
    }

     override fun onProgressUpdate(vararg progress: Any?) {
        val data = progress[0] as ShortArray?
        listener!!.onSignalReceived(data)
    }



    override fun doInBackground(vararg params: Void?): Void? {
        try {
            volumeMeter.start()
        } catch (e: Exception) {
            Log.e("MicSamplerTask", "Failed to start VolumeMeter")
            e.printStackTrace()
            if (listener != null) {
                listener!!.onMicError()
            }
            return null
        }
        while (true) {
            if (listener != null) {
                Log.i("MicSamplerTask", "Requesting amplitude")
                publishProgress(volumeMeter.amplitude)
            }
            try {
                Thread.sleep(500)
            } catch (e: InterruptedException) {
                //Nothing to do we exit next line
            }
            var restartVolumeMeter = false
            if (paused) {
                restartVolumeMeter = true
                volumeMeter.stop()
                isSampling = false
            }
            while (paused) {
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }
            }
            if (restartVolumeMeter) {
                try {
                    Log.i("MicSamplerTask", "Task restarted")
                    volumeMeter = AudioCodec()
                    volumeMeter.start()
                    isSampling = true
                } catch (e: IllegalStateException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                } catch (e: IOException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }
            }
            if (isCancelled) {
                volumeMeter.stop()
                isSampling = false
                return null
            }
        }
    }
}