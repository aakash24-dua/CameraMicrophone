package com.example.hardware

import android.view.View
import android.widget.TextView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.hardware.ui.main.view.MicrophoneFragment
import com.example.hardware.ui.main.view.MonitorActivity
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MicrophoneFragmentTest {
    private val testNoteText = "test note"

    @get:Rule
    val activityTestRule = ActivityTestRule(MonitorActivity::class.java)

    var fragment:MicrophoneFragment?=null
    @Before
    fun setUp() {
         fragment = MicrophoneFragment()
        activityTestRule.activity.getFragmentManager().beginTransaction().add(1, fragment as android.app.Fragment, null).commit();
       // activityTestRule.activity.setFragment(fragment!!)
    }

    @Test
    fun shouldDisplayNoteHintForANewNote() {

        val viewById: View? =
            fragment?.view?.findViewById(R.id.microphone)
        assertThat<View>(viewById, Matchers.notNullValue())
        assertThat<View>(
            viewById, Matchers.instanceOf(
                View::class.java
            )
        )
        val textView = viewById as TextView
        assertThat(
            textView.text.toString(),
            Matchers.equalToIgnoringCase("sss")
        )


    }

    @Test
    fun shouldChangeAddButtonEnableAfterChangingNoteText() {
/*onView(withId(R.id.add))
.check(matches(not(isEnabled())))

onView(withId(R.id.note))
.perform(replaceText(testNoteText))

onView(withId(R.id.add))
.check(matches(isEnabled()))*/
    }
}