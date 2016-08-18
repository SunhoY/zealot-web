package io.harry.zealot.component;

import javax.inject.Singleton;

import dagger.Component;
import io.harry.zealot.activity.SplashActivity;
import io.harry.zealot.activity.TestAjaeActivity;
import io.harry.zealot.module.AnimationModule;
import io.harry.zealot.module.ContextModule;
import io.harry.zealot.module.GagTestModule;

@Singleton
@Component(modules = {
        AnimationModule.class,
        ContextModule.class,
        GagTestModule.class,
})
public interface ZealotComponent {
    void inject(SplashActivity splashActivity);
    void inject(TestAjaeActivity testAjaeActivity);
}
