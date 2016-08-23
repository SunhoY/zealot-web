package io.harry.zealot.component;

import javax.inject.Singleton;

import dagger.Component;
import io.harry.zealot.activity.SplashActivity;
import io.harry.zealot.activity.TestAjaeActivity;
import io.harry.zealot.module.AnimationModule;
import io.harry.zealot.module.ContextModule;
import io.harry.zealot.module.FirebaseModule;
import io.harry.zealot.module.GagTestModule;
import io.harry.zealot.service.GagService;

@Singleton
@Component(modules = {
        AnimationModule.class,
        ContextModule.class,
        GagTestModule.class,
        FirebaseModule.class,
})
public interface ZealotComponent {
    void inject(SplashActivity splashActivity);
    void inject(TestAjaeActivity testAjaeActivity);
    void inject(GagService gagService);
}
