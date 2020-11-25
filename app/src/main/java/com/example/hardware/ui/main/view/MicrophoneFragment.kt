
package com.example.hardware.ui.main.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.hardware.App
import com.example.hardware.util.MicrophoneVolumePicker
import com.example.hardware.R
import com.example.hardware.async.MicSamplerTask
import com.example.hardware.async.MicSamplerTask.MicListener
import com.example.hardware.async.MicrophoneTaskFactory
import com.example.hardware.async.MicrophoneTaskFactory.RecordLimitExceeded
import com.example.hardware.ui.main.viewmodel.MicrophoneViewmodel
import javax.inject.Inject

class MicrophoneFragment : Fragment(), MicListener {
    private var microphone: MicSamplerTask? = null
    private var microphoneText: TextView? = null

    /**
     * View for microphone data
     */
    private var picker: MicrophoneVolumePicker? = null

    /**
     * Object used to fetch application dependencies
     */

    /**
     * Threshold for the decibels sampled
     */


    @Inject
    lateinit var viewModel: MicrophoneViewmodel
    /**
     * Messenger used to communicate with alert service
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.microphone_fragment, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App(context!!).component.inject(this)
        viewModel.noiseThreshold()
        try {
            microphone = MicrophoneTaskFactory.makeSampler(activity)
            microphone?.setMicListener(this)
            microphone?.execute()
        } catch (e: RecordLimitExceeded) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }


    override fun onResume() {
        super.onResume()
        microphoneText = activity?.findViewById<View>(R.id.microphone) as TextView
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val layout =
            activity!!.findViewById<View>(R.id.linear_layout) as LinearLayout
        if (layout.childCount == 1) {
            picker =
                MicrophoneVolumePicker(this.activity)
            picker?.setNoiseThreshold(viewModel.NOISE_THRESHOLD)
            layout.addView(picker, params)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("MicrophoneFramgnet", "Fragment destroyed")
        microphone!!.cancel(true)
    }

    override fun onSignalReceived(signal: ShortArray?) {
        val averageDB = viewModel.getAverage(signal)
        microphoneText!!.text = "Sampled DBs: $averageDB"
        picker!!.setValues(averageDB, averageDB)
        picker!!.invalidate()
    }



    override fun onMicError() {
        Log.e("MicrophoneActivity", "Microphone is not ready")
    }
}