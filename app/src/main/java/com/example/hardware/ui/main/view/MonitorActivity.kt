
package com.example.hardware.ui.main.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.hardware.R
import com.example.hardware.util.SecureItPreferences
import com.viewpagerindicator.TitlePageIndicator
import kotlinx.android.synthetic.main.activity_monitor.*

class MonitorActivity : FragmentActivity() {
    private var preferences: SecureItPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = SecureItPreferences(applicationContext)
        setContentView(R.layout.activity_monitor)
        val adapter: FragmentPagerAdapter = MonitorAdapter(
            supportFragmentManager
        )
        val pager = findViewById<View>(R.id.pager) as ViewPager
        pager.adapter = adapter
        val indicator =
            findViewById<View>(R.id.indicator) as TitlePageIndicator
        indicator.setViewPager(pager)
        val density = resources.displayMetrics.density
        indicator.setBackgroundColor(0x18FF0000)
        indicator.footerColor = -0x55ddde
        indicator.footerLineHeight = 1 * density //1dp
        indicator.footerIndicatorHeight = 3 * density //3dp
        indicator.footerIndicatorStyle = TitlePageIndicator.IndicatorStyle.Underline
        indicator.textColor = -0x56000000
        indicator.selectedColor = -0x1000000
        indicator.isSelectedBold = true
        pager.currentItem = 1
        /**
         * Binding to the bluetooth service
         */
        // startService(new Intent(this, UploadService.class));
    }

    internal inner class MonitorAdapter(fm: FragmentManager?) :
        FragmentPagerAdapter(fm) {
        private var cameraFragment: Fragment? = null
        private var microphoneFragment: Fragment? = null
        override fun getItem(position: Int): Fragment? {
            when (position) {
                0 -> {
                    if (preferences!!.cameraActivation) {
                        if (cameraFragment == null) cameraFragment = CameraFragment()
                        return cameraFragment!!
                    }
                    if (preferences!!.microphoneActivation) {
                        if (microphoneFragment == null) microphoneFragment = MicrophoneFragment()
                        return microphoneFragment!!
                    }
                }
                1 -> if (preferences!!.microphoneActivation) {
                    if (microphoneFragment == null) microphoneFragment = MicrophoneFragment()
                    return microphoneFragment!!
                }
            }
            return null
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return CONTENT[position % CONTENT.size]
                .toUpperCase()
        }

        override fun getCount(): Int {
            return CONTENT.size
        }
    }

    /**
     * Closes the monitor activity and unset session properties
     */
    private fun close() {
        val intent = Intent(
            applicationContext, StartActivity::class.java
        )
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        preferences!!.unsetAccessToken()
        preferences!!.unsetDelegatedAccessToken()
        preferences!!.unsetPhoneId()
    }

    /**
     * When user closes the activity
     */
    override fun onBackPressed() {
        close()
    }

    fun setFragment(microphoneFragment: MicrophoneFragment) {
        pager.currentItem = 1
    }

    companion object {
        private val CONTENT = arrayOf(
            "Camera",
            "Mic."
        )
    }
}