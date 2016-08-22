package io.harry.zealot.fragment;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import io.harry.zealot.BuildConfig;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class GagFragmentTest {
    private GagFragment subject;

    @Test
    @Ignore
    public void gagImage_showsReceivedGagImage() throws Exception {
        subject = new GagFragment();
        subject.setUrl("http://gag1.png");
    }
}