package com.example.hardware.ui.main.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.hardware.util.SecureItPreferences
import javax.inject.Inject

class MicrophoneViewmodel @Inject constructor( val sharedPrefs:SecureItPreferences) : ViewModel() {
    var NOISE_THRESHOLD = 60.0

    fun getAverage(signal: ShortArray?): Double{

        var total = 0
        var count = 0
        for (peak in signal!!) {
            if (peak.toInt() != 0) {
                total += Math.abs(peak.toInt())
                count++
            }
        }
        Log.i("MicrophoneFragment", "Total value: $total")
        var average = 0
        if (count > 0) average = total / count
        var averageDB = 0.0
        if (average != 0) {
            averageDB = 20 * Math.log10(Math.abs(average) / 1.toDouble())
        }
        return averageDB
    }

    fun noiseThreshold() {
        if (sharedPrefs?.microphoneSensitivity == "High") {
            NOISE_THRESHOLD = 30.0
        } else if (sharedPrefs!!.microphoneSensitivity == "Medium") {
            NOISE_THRESHOLD = 40.0
        }
    }


}