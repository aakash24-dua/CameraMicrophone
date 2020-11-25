
package com.example.hardware.util

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import java.io.IOException
import java.util.*

class AudioCodec {
    private var recorder: AudioRecord? = null
    private var minSize = 0

    /**
     * Configures the recorder and starts it
     * @throws IOException
     * @throws IllegalStateException
     */
    @Throws(IllegalStateException::class, IOException::class)
    fun start() {
        if (recorder == null) {
            minSize = AudioRecord.getMinBufferSize(
                8000,
                AudioFormat.CHANNEL_IN_DEFAULT,
                AudioFormat.ENCODING_PCM_16BIT
            )
            Log.e("AudioCodec", "Minimum size is $minSize")
            recorder = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                8000,
                AudioFormat.CHANNEL_IN_DEFAULT,
                AudioFormat.ENCODING_PCM_16BIT,
                minSize
            )
            recorder!!.startRecording()
        }
    }

    /**
     * Returns current sound level
     * @return sound level
     */
    val amplitude: ShortArray?
        get() {
            if (recorder != null) {
                val buffer = ShortArray(8192)
                var readBytes = 0
                while (readBytes < 8192) {
                    readBytes += recorder!!.read(buffer, readBytes, 8192 - readBytes)
                }
                val copyToReturn = Arrays.copyOf(buffer, 512)
                Arrays.sort(buffer)
                Log.e(
                    "AudioCodec", "Recorder has read: " + readBytes + " the maximum is: " +
                            buffer[minSize - 1]
                )
                return copyToReturn
            }
            return null
        }

    fun stop() {
        if (recorder != null
            && recorder!!.state != AudioRecord.STATE_UNINITIALIZED
        ) {
            recorder!!.stop()
            recorder!!.release()
            Log.i("AudioCodec", "Sampling stopped")
        }
        Log.i("AudioCodec", "Recorder set to null")
        recorder = null
    }
}