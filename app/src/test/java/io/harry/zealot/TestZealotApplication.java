package io.harry.zealot;

import org.robolectric.TestLifecycleApplication;

import java.lang.reflect.Method;

public class TestZealotApplication extends ZealotApplication implements TestLifecycleApplication {
    @Override
    public void beforeTest(Method method) {

    }

    @Override
    public void prepareTest(Object test) {

    }

    @Override
    public void afterTest(Method method) {

    }
}
