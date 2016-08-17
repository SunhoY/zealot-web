package io.harry.zealot.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.harry.zealot.wrapper.AnimationHelper;

@Module(includes = ContextModule.class)
public class AnimationModule {

    @Singleton @Provides
    public AnimationHelper provideAnimationWrapper(Context context) {
        return new AnimationHelper(context);
    }
}
