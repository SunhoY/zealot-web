package io.harry.zealot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import io.harry.zealot.component.TestZealotComponent;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ZealotBaseTest {
    protected TestZealotComponent zealotComponent;

    @Before
    public void setUp() throws Exception {
        zealotComponent = ((TestZealotApplication) RuntimeEnvironment.application).getZealotComponent();
    }

    @Test
    public void testNothing() throws Exception {

    }
}
