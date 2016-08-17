package io.harry.zealot.component;

import javax.inject.Singleton;

import dagger.Component;
import io.harry.zealot.activity.SplashActivity;
import io.harry.zealot.activity.SplashActivityTest;
import io.harry.zealot.module.TestAnimationModule;

@Singleton
@Component(modules = {
        TestAnimationModule.class,
})
public interface TestZealotComponent extends ZealotComponent {
    void inject(SplashActivity splashActivity);
    void inject(SplashActivityTest splashActivityTest);
}
