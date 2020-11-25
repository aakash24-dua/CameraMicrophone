package com.example.hardware.ui.main

import android.view.View
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.hardware.R
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.Matchers.greaterThan
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    var rule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    @Test
    @Throws(Exception::class)
    fun testRecyclerViewNotNull() {
        val activity: MainActivity = rule.activity
        val viewById: View = activity.findViewById(R.id.recycler_view)
        assertThat(viewById, notNullValue())
        assertThat(viewById, instanceOf(RecyclerView::class.java))
    }

    @Test
    @Throws(Exception::class)
    fun testRecyclerViewAdapterNotNull() {
        val activity: MainActivity = rule.activity
        val viewById: View = activity.findViewById(R.id.recycler_view)
        val listView: RecyclerView = viewById as RecyclerView
        val adapter = listView.adapter as PopularListAdapter
        assertThat(adapter, instanceOf(ArrayAdapter::class.java))
        assertThat(adapter.itemCount, greaterThan(5))
    }
}