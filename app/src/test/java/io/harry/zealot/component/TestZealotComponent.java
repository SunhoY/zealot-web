package io.harry.zealot.component;

import javax.inject.Singleton;

import dagger.Component;
import io.harry.zealot.activity.SplashActivity;
import io.harry.zealot.activity.SplashActivityTest;
import io.harry.zealot.activity.TestAjaeActivity;
import io.harry.zealot.activity.TestAjaeActivityTest;
import io.harry.zealot.module.TestAnimationModule;
import io.harry.zealot.module.TestGagTestModule;

@Singleton
@Component(modules = {
        TestAnimationModule.class,
        TestGagTestModule.class,
})
public interface TestZealotComponent extends ZealotComponent {
    void inject(SplashActivity splashActivity);
    void inject(SplashActivityTest splashActivityTest);
    void inject(TestAjaeActivity testAjaeActivity);
    void inject(TestAjaeActivityTest testAjaeActivityTest);
}
