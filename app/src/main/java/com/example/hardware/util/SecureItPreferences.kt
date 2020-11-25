
package com.example.hardware.util

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class SecureItPreferences(context: Context) {
    private val appSharedPrefs: SharedPreferences
    private val prefsEditor: SharedPreferences.Editor
    fun activateAccelerometer(active: Boolean) {
        prefsEditor.putBoolean(ACCELEROMETER_ACTIVE, active)
        prefsEditor.commit()
    }

    val accelerometerActivation: Boolean
        get() = appSharedPrefs.getBoolean(ACCELEROMETER_ACTIVE, true)

    var accelerometerSensitivity: String?
        get() = appSharedPrefs.getString(ACCELEROMETER_SENSITIVITY, "")
        set(sensitivity) {
            prefsEditor.putString(ACCELEROMETER_SENSITIVITY, sensitivity)
            prefsEditor.commit()
        }

    fun activateCamera(active: Boolean) {
        prefsEditor.putBoolean(CAMERA_ACTIVE, active)
        prefsEditor.commit()
    }

    val cameraActivation: Boolean
        get() = appSharedPrefs.getBoolean(CAMERA_ACTIVE, true)

    var camera: String?
        get() = appSharedPrefs.getString(
            CAMERA,
            BACK
        )
        set(camera) {
            prefsEditor.putString(CAMERA, camera)
            prefsEditor.commit()
        }

    var cameraSensitivity: String?
        get() = appSharedPrefs.getString(CAMERA_SENSITIVITY, "")
        set(sensitivity) {
            prefsEditor.putString(CAMERA_SENSITIVITY, sensitivity)
            prefsEditor.commit()
        }

    fun activateFlash(active: Boolean) {
        prefsEditor.putBoolean(FLASH_ACTIVE, active)
        prefsEditor.commit()
    }

    val flashActivation: Boolean
        get() = appSharedPrefs.getBoolean(FLASH_ACTIVE, true)

    fun activateMicrophone(active: Boolean) {
        prefsEditor.putBoolean(MICROPHONE_ACTIVE, active)
        prefsEditor.commit()
    }

    val microphoneActivation: Boolean
        get() = appSharedPrefs.getBoolean(MICROPHONE_ACTIVE, true)

    var microphoneSensitivity: String?
        get() = appSharedPrefs.getString(MICROPHONE_SENSITIVITY, "")
        set(sensitivity) {
            prefsEditor.putString(MICROPHONE_SENSITIVITY, sensitivity)
            prefsEditor.commit()
        }

    fun activateSms(active: Boolean) {
        prefsEditor.putBoolean(SMS_ACTIVE, active)
        prefsEditor.commit()
    }

    val smsActivation: Boolean
        get() = appSharedPrefs.getBoolean(SMS_ACTIVE, true)

    var smsNumber: String?
        get() = appSharedPrefs.getString(SMS_NUMBER, "")
        set(number) {
            prefsEditor.putString(SMS_NUMBER, number)
            prefsEditor.commit()
        }

    fun activateRemote(active: Boolean) {
        prefsEditor.putBoolean(REMOTE_ACTIVE, active)
        prefsEditor.commit()
    }

    val remoteActivation: Boolean
        get() = appSharedPrefs.getBoolean(REMOTE_ACTIVE, true)

    var unlockCode: String?
        get() = appSharedPrefs.getString(UNLOCK_CODE, "")
        set(unlockCode) {
            prefsEditor.putString(UNLOCK_CODE, unlockCode)
            prefsEditor.commit()
        }

    var remoteEmail: String?
        get() = appSharedPrefs.getString(REMOTE_EMAIL, "")
        set(email) {
            prefsEditor.putString(REMOTE_EMAIL, email)
            prefsEditor.commit()
        }

    var accessToken: String?
        get() = appSharedPrefs.getString(ACCESS_TOKEN, "")
        set(accessToken) {
            prefsEditor.putString(ACCESS_TOKEN, accessToken)
            prefsEditor.commit()
        }

    fun unsetAccessToken() {
        prefsEditor.remove(ACCESS_TOKEN)
    }

    var delegatedAccessToken: String?
        get() = appSharedPrefs.getString(DELEGATED_ACCESS_TOKEN, "")
        set(deferredAccessToken) {
            prefsEditor.putString(
                DELEGATED_ACCESS_TOKEN,
                deferredAccessToken
            )
            prefsEditor.commit()
        }

    fun unsetDelegatedAccessToken() {
        prefsEditor.remove(DELEGATED_ACCESS_TOKEN)
    }

    fun unsetPhoneId() {
        prefsEditor.remove(PHONE_ID)
    }

    var phoneId: String?
        get() = appSharedPrefs.getString(PHONE_ID, "")
        set(phoneId) {
            prefsEditor.putString(PHONE_ID, phoneId)
            prefsEditor.commit()
        }

    val sMSText: String
        get() = """
            WARNING: SecureIt has detected an intrusion.
            PhoneId: $phoneId
            """.trimIndent()

    companion object {
        const val LOW = "Low"
        const val MEDIUM = "Medium"
        const val HIGH = "High"
        const val FRONT = "Front"
        const val BACK = "Back"
        private const val APP_SHARED_PREFS = "com.example.hardware"
        private const val ACCELEROMETER_ACTIVE = "accelerometer_active"
        private const val ACCELEROMETER_SENSITIVITY = "accelerometer_sensibility"
        private const val CAMERA_ACTIVE = "camera_active"
        private const val CAMERA = "camera"
        private const val CAMERA_SENSITIVITY = "camera_sensitivity"
        private const val FLASH_ACTIVE = "flash_active"
        private const val MICROPHONE_ACTIVE = "microphone_active"
        private const val MICROPHONE_SENSITIVITY = "microphone_sensitivity"
        private const val SMS_ACTIVE = "sms_active"
        private const val SMS_NUMBER = "sms_number"
        private const val REMOTE_ACTIVE = "remote_active"
        private const val REMOTE_EMAIL = "remote_email"
        private const val UNLOCK_CODE = "unlock_code"
        private const val ACCESS_TOKEN = "access_token"
        private const val DELEGATED_ACCESS_TOKEN = "deferred_access_token"
        private const val PHONE_ID = "phone_id"
        const val dirPath = "/secureit"
        const val imagePath = dirPath + "/secureit"
        const val maxImages = 10
        const val audioPath =
            dirPath + "/SecureIt_Audio"
        const val audioLenght: Long = 10000
    }

    init {
        appSharedPrefs = context.getSharedPreferences(
            APP_SHARED_PREFS,
            Activity.MODE_PRIVATE
        )
        prefsEditor = appSharedPrefs.edit()
    }
}