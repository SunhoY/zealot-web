package io.harry.zealot.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.harry.zealot.helper.BitmapHelper;

@Module
public class ContentModule {
    @Provides @Singleton
    BitmapHelper provideBitmapHelper() {
        return new BitmapHelper();
    }
}
