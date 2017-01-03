package io.harry.zealot.activity;

import org.assertj.android.api.content.IntentAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import io.harry.zealot.BuildConfig;

import static org.robolectric.RuntimeEnvironment.application;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MenuActivityTest {
    private MenuActivity subject;

    @Before
    public void setUp() throws Exception {
        subject = Robolectric.setupActivity(MenuActivity.class);
    }

    @Test
    public void onStartClick_startsTestAjaeActivity() throws Exception {
        subject.onStartClick();

        IntentAssert intentAssert = new IntentAssert(shadowOf(subject).getNextStartedActivity());
        intentAssert.hasComponent(application, TestAjaeActivity.class);
    }
}