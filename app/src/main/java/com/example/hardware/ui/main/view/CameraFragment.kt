
package com.example.hardware.ui.main.view

import android.graphics.Bitmap
import android.hardware.Camera
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.hardware.async.MotionAsyncTask
import com.example.hardware.R
import com.example.hardware.util.ImageCodec
import com.example.hardware.util.Preview

class CameraFragment : Fragment() {
    private var preview: Preview? = null
    private var oldImage: ImageView? = null
    private var newImage: ImageView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.camera_fragment, container, false)
    }

    override fun onResume() {
        super.onResume()
        if (preview == null) {
            //Uncomment to see the camera
            preview = Preview(activity!!)
            (activity!!.findViewById<View>(R.id.preview) as FrameLayout).addView(
                preview
            )
            oldImage = activity?.findViewById(R.id.old_image)
            newImage = activity?.findViewById(R.id.new_image)




            preview?.addListener (object:
                MotionAsyncTask.MotionListener{
                override fun onProcess(
                    oldBitmap: Bitmap?,
                    newBitmap: Bitmap?,
                    motionDetected: Boolean
                ) {
                    var rotation = 0
                    var reflex = false
                    if (preview!!.cameraFacing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                        rotation = 90
                    } else {
                        rotation = 270
                        reflex = true
                    }
                    oldImage?.setImageBitmap(ImageCodec.rotate(oldBitmap, rotation, reflex))
                    newImage?.setImageBitmap(ImageCodec.rotate(newBitmap, rotation, reflex))
                }
            })


        }
    }
}