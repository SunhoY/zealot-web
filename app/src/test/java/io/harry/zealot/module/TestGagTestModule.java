package io.harry.zealot.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.harry.zealot.wrapper.GagPagerAdapterWrapper;

import static org.mockito.Mockito.mock;

@Module
public class TestGagTestModule {
    @Singleton @Provides
    public GagPagerAdapterWrapper provideGagPagerAdapterWrapper() {
        return mock(GagPagerAdapterWrapper.class);
    }
}
