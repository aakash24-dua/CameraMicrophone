
package com.example.hardware.util

import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.YuvImage
import android.hardware.Camera
import android.hardware.Camera.CameraInfo
import android.hardware.Camera.PreviewCallback
import android.os.*
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.WindowManager
import android.widget.Toast
import com.example.hardware.async.MotionAsyncTask
import com.example.hardware.async.MotionAsyncTask.MotionListener
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class Preview(context: Context) : SurfaceView(context),
    SurfaceHolder.Callback {
    /**
     * Object to retrieve and set shared preferences
     */
    private val prefs: SecureItPreferences
    var cameraFacing = 0
        private set
    private val listeners: MutableList<MotionListener> =
        ArrayList()

    /**
     * Timestamp of the last picture processed
     */
    private var lastTimestamp: Long = 0

    /**
     * Last picture processed
     */
    private  var lastPic: ByteArray? = null

    /**
     * True IFF there's an async task processing images
     */
    private var doingProcessing = false

    /**
     * Handler used to update back the UI after motion detection
     */
    private val updateHandler = Handler()

    /**
     * Last frame captured
     */
    private var imageCount = 0

    /**
     * Sensitivity of motion detection
     */
    private var motionSensitivity = LuminanceMotionDetector.MOTION_MEDIUM

    /**
     * Messenger used to signal motion to the alert service
     */
    private var serviceMessenger: Messenger? = null
    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            className: ComponentName,
            service: IBinder
        ) {
            Log.i("CameraFragment", "SERVICE CONNECTED")
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            serviceMessenger = Messenger(service)
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            Log.i("CameraFragment", "SERVICE DISCONNECTED")
            serviceMessenger = null
        }
    }
    var mHolder: SurfaceHolder
    var camera: Camera? = null
    fun addListener(listener: MotionAsyncTask.MotionListener ) {
        listeners.add(listener)
    }

    /**
     * Called on the creation of the surface:
     * setting camera parameters to lower possible resolution
     * (preferred is 640x480)
     * in order to minimize CPU usage
     */
    override fun surfaceCreated(holder: SurfaceHolder) {

        /*
		 * We bind to the alert service
		 */
        //context.bindService(new Intent(context, UploadService.class), mConnection, Context.BIND_ABOVE_CLIENT);

        /*
		 *  The Surface has been created, acquire the camera and tell it where
		 *  to draw.
		 *  If the selected camera is the front one we open it
		 */
        if (prefs.camera == "Front") {
            val cameraInfo = CameraInfo()
            val cameraCount = Camera.getNumberOfCameras()
            for (camIdx in 0 until cameraCount) {
                Camera.getCameraInfo(camIdx, cameraInfo)
                if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
                    try {
                        camera = Camera.open(camIdx)
                        cameraFacing = CameraInfo.CAMERA_FACING_FRONT
                    } catch (e: RuntimeException) {
                        Log.e(
                            "Preview",
                            "Camera failed to open: " + e.localizedMessage
                        )
                    }
                }
            }
        } else {
            camera = Camera.open()
            cameraFacing = CameraInfo.CAMERA_FACING_BACK
        }
        val parameters = camera!!.parameters
        val sizes =
            parameters.supportedPictureSizes
        var w = 640
        var h = 480
        for (s in sizes) {
            Log.i("SurfaceView", "width: " + s.width + " height: " + s.height)
            if (s.width <= 640) {
                w = s.width
                h = s.height
                Log.i("SurfaceView", "selected width: $w selected height: $h")
                break
            }
        }
        parameters.setPictureSize(w, h)

        /*
		 * If the flash is needed
		 */if (prefs.flashActivation) {
            Log.i("Preview", "Flash activated")
            parameters.flashMode = Camera.Parameters.FLASH_MODE_TORCH
        }
        camera!!.parameters = parameters
        try {
            camera!!.setPreviewDisplay(mHolder)
            camera!!.setPreviewCallback(PreviewCallback { data, cam ->
                val size = cam.parameters.previewSize ?: return@PreviewCallback
                val now = System.currentTimeMillis()
                if (now < lastTimestamp + 1000) return@PreviewCallback
                if (!doingProcessing) {
                    /**
                     * Before processing the frame we save it
                     * to the SDCARD
                     */
                    /**
                     * Before processing the frame we save it
                     * to the SDCARD
                     */
                    /**
                     * Before processing the frame we save it
                     * to the SDCARD
                     */
                    /**
                     * Before processing the frame we save it
                     * to the SDCARD
                     */
                    try {
                        val image = YuvImage(
                            data, parameters.previewFormat,
                            size.width, size.height, null
                        )
                        imageCount = (imageCount + 1) % SecureItPreferences.maxImages
                        val file = File(
                            Environment.getExternalStorageDirectory().path +
                                    SecureItPreferences.imagePath +
                                    imageCount +
                                    ".jpg"
                        )
                        val filecon = FileOutputStream(file)
                        image.compressToJpeg(
                            Rect(0, 0, image.width, image.height),
                            90,
                            filecon
                        )
                    } catch (e: FileNotFoundException) {
                        val toast =
                            Toast.makeText(getContext(), e.message, Toast.LENGTH_LONG)
                        toast.show()
                    }
                    Log.i("Preview", "Processing new image")
                    lastTimestamp = now
                    val task = MotionAsyncTask(
                        lastPic,
                        data,
                        size.width,
                        size.height,
                        updateHandler,
                        motionSensitivity
                    )
                    for (listener in listeners) {
                        Log.i("Preview", "Added listener")
                        task.addListener(listener)
                    }
                    doingProcessing = true
                    task.addListener (object : MotionListener{
                        override fun onProcess(
                            oldBitmap: Bitmap?,
                            newBitmap: Bitmap?,
                            motionDetected: Boolean
                        ) {
                            if (motionDetected) {
                                Log.i("MotionListener", "Motion detected")
                                if (serviceMessenger != null) {
                                    val message = Message()
                                    try {
                                        serviceMessenger!!.send(message)
                                    } catch (e: RemoteException) {
                                        // Cannot happen
                                    }
                                }
                            }
                            Log.i("MotionListener", "Allowing further processing")
                            doingProcessing = false
                        }

                    })
                    task.start()
                    lastPic = data
                }
            })
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        // Surface will be destroyed when we return, so stop the preview.
        // Because the CameraDevice object is not a shared resource, it's very
        // important to release it when the activity is paused.
        context.unbindService(mConnection)
        camera!!.setPreviewCallback(null)
        camera!!.stopPreview()
        camera!!.release()
    }

    override fun surfaceChanged(
        holder: SurfaceHolder,
        format: Int,
        w: Int,
        h: Int
    ) {
        // Now that the size is known, set up the camera parameters and begin
        // the preview.
        val parameters = camera!!.parameters
        parameters.setPreviewSize(w, h)
        val degree =
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
                .rotation
        var displayOrientation = 0
        when (degree) {
            Surface.ROTATION_0 -> displayOrientation = 90
            Surface.ROTATION_90 -> displayOrientation = 0
            Surface.ROTATION_180 -> displayOrientation = 0
            Surface.ROTATION_270 -> displayOrientation = 180
        }
        camera!!.setDisplayOrientation(displayOrientation)

        //camera.setParameters(parameters);
        camera!!.startPreview()
    }

    init {
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = holder
        mHolder.addCallback(this)
        prefs = SecureItPreferences(context)

        /*
		 * Set sensitivity value
		 */if (prefs.cameraSensitivity == "Medium") {
            motionSensitivity = LuminanceMotionDetector.MOTION_MEDIUM
            Log.i("CameraFragment", "Sensitivity set to Medium")
        } else if (prefs.cameraSensitivity == "Low") {
            motionSensitivity = LuminanceMotionDetector.MOTION_LOW
            Log.i("CameraFragment", "Sensitivity set to Low")
        } else {
            motionSensitivity = LuminanceMotionDetector.MOTION_HIGH
            Log.i("CameraFragment", "Sensitivity set to High")
        }
    }
}