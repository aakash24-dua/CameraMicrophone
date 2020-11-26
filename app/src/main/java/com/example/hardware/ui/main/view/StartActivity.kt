package com.example.hardware.ui.main.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.example.hardware.App
import com.example.hardware.R
import com.example.hardware.util.SecureItPreferences
import com.example.hardware.viewmodel.StartActivityViewModel
import java.io.File


class StartActivity : AppCompatActivity() {
    private var preferences: SecureItPreferences? = null

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(StartActivityViewModel::class.java)
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        App(this).component.inject(this)
        preferences = SecureItPreferences(this.applicationContext)

        /*
         * We create an application directory to store images and audio
         */
        val directory = File(
            Environment.getExternalStorageDirectory()
                .toString() + SecureItPreferences.Companion.dirPath
        )
        directory.mkdirs()
        /**
         * Checkboxes for enabled app options
         */

        /*
         * Detecting if the device has a front camera
         * and configuring the spinner of camera selection
         * properly
         */
        val selectCameraSpinner =
            findViewById<View>(R.id.camera_spinner) as Spinner
        val pm = packageManager
        val frontCam: Boolean
        frontCam = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)
        if (!frontCam) {
            selectCameraSpinner.isEnabled = false
        }

        /*
         * Detecting if the device has the flash
         * and configuring properly the check box
         */
        val flashCheck = findViewById<View>(R.id.flash_check) as CheckBox
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            flashCheck.isEnabled = false
        }
        val cameraOptions =
            findViewById<View>(R.id.camera_options) as RelativeLayout


        val cameraSensitivity =
            findViewById<View>(R.id.camera_sensitivity_spinner) as Spinner


        val startButton =
            findViewById<View>(R.id.start_button) as Button
        cameraOptions.visibility = View.VISIBLE



        startButton.setOnClickListener(View.OnClickListener {

                preferences!!.activateCamera(true)
                preferences!!.activateFlash(
                    flashCheck.isChecked
                )
                preferences!!.camera = selectCameraSpinner.selectedItem as String
                preferences!!.cameraSensitivity = cameraSensitivity.selectedItem as String

            preferences!!.activateMicrophone(true)



            if (checkPermission()){
                val intent = Intent(
                    this@StartActivity,
                    MonitorActivity::class.java
                )
                this@StartActivity.startActivity(intent)
            }
            else{
                requestPermission(Manifest.permission.CAMERA,PERMISSION_REQUEST_CODE)
            }




        })
        /**
         * Loads preferences and sets view
         */
        if (preferences!!.cameraActivation) {
            val sensitivity = preferences!!.cameraSensitivity
            if (sensitivity == SecureItPreferences.LOW) cameraSensitivity.setSelection(0) else if (sensitivity == SecureItPreferences.MEDIUM) cameraSensitivity.setSelection(
                1
            ) else if (sensitivity == SecureItPreferences.HIGH) cameraSensitivity.setSelection(2)
            flashCheck.isChecked = preferences!!.flashActivation
            val camera = preferences!!.camera
            if (camera == SecureItPreferences.BACK) selectCameraSpinner.setSelection(0)
        }


    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    val PERMISSION_REQUEST_CODE = 9
    val PERMISSION_REQUEST_CODE_AUDIO = 90

    private fun requestPermission( ss:String,code:Int) {
        ActivityCompat.requestPermissions(
            this, arrayOf(ss,Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE),
            code
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.size > 3 && grantResults[0] === PackageManager.PERMISSION_GRANTED &&  grantResults[1] === android.content.pm.PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(
                    this@StartActivity,
                    MonitorActivity::class.java
                )
                this@StartActivity.startActivity(intent)
            } else {
                Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_SHORT)
                    .show()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        requestPermission(Manifest.permission.CAMERA,PERMISSION_REQUEST_CODE)

                    }
                }
            }

            PERMISSION_REQUEST_CODE_AUDIO -> if (grantResults.size > 0 && grantResults[0] === PackageManager.PERMISSION_GRANTED) {

                val intent = Intent(
                    this@StartActivity,
                    MonitorActivity::class.java
                )
                this@StartActivity.startActivity(intent)
                Toast.makeText(applicationContext, "Permission Granted", Toast.LENGTH_SHORT)
                    .show()

                // main logic
            } else {
                Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_SHORT)
                    .show()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        requestPermission(Manifest.permission.RECORD_AUDIO,PERMISSION_REQUEST_CODE_AUDIO)

                        /*showMessageOKCancel("You need to allow access permissions",
                            DialogInterface.OnClickListener { dialog, which ->
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermission()
                                }
                            })*/
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}