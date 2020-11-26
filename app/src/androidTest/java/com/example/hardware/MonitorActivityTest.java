package com.example.hardware;

import android.content.Intent;
import android.view.View;

import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.hardware.ui.main.view.MonitorActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(AndroidJUnit4.class)
public class MonitorActivityTest {

@Rule
public ActivityTestRule<MonitorActivity> rule = new ActivityTestRule<MonitorActivity>(MonitorActivity.class) {
@Override
protected Intent getActivityIntent() {
InstrumentationRegistry.getTargetContext();
Intent intent = new Intent(Intent.ACTION_MAIN);
return intent;
}
};


@Test
public void ensureViewsAreGettingInitialized() throws Exception {
MonitorActivity activity = rule.getActivity();
View viewById = activity.findViewById(R.id.pager);
assertThat(viewById, notNullValue());
assertThat(viewById, instanceOf(ViewPager.class));
ViewPager pager = (ViewPager) viewById;
assertThat(pager.getAdapter(), notNullValue());
}

@Test
public void checkCurrentItemPagerAdapter() throws Exception {
MonitorActivity activity = rule.getActivity();
View viewById = activity.findViewById(R.id.pager);
ViewPager pager = (ViewPager) viewById;
PagerAdapter adapter = pager.getAdapter();
assertThat(adapter.getPageTitle(1).toString(), equalToIgnoringCase("Mic."));
}
}