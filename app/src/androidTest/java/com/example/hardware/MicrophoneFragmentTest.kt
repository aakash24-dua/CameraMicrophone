package com.example.hardware

import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.hardware.ui.main.view.MicrophoneFragment
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.equalToIgnoringCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MicrophoneFragmentTest {
    private val testNoteText = "test note"

    @Before
    fun setUp() {

    }


    @Test
    fun checkMicrophoneTextViewAssertions() {
                // The "fragmentArgs" argument is optional.
        val fragmentArgs = bundleOf()
        val scenario = launchFragmentInContainer<MicrophoneFragment>(fragmentArgs)
        scenario.onFragment {
            assertThat<View>(it.view?.findViewById(R.id.microphone), Matchers.notNullValue() )
            assertThat<String>((it.view?.findViewById(R.id.microphone) as TextView).text.toString(),
                equalToIgnoringCase(""))
        }
    }
}