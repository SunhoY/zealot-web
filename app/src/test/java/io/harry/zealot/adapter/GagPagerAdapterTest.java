package io.harry.zealot.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import io.harry.zealot.BuildConfig;
import io.harry.zealot.activity.SplashActivity;
import io.harry.zealot.fragment.GagFragment;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class GagPagerAdapterTest {

    private GagPagerAdapter subject;

    @Before
    public void setUp() throws Exception {
        FragmentManager supportFragmentManager = Robolectric.buildActivity(SplashActivity.class)
                .create().get().getSupportFragmentManager();
        List<String> URLs = Arrays.asList("http://gag1.png", "http://gag2.png", "http://gag3.png");
        subject = new GagPagerAdapter(supportFragmentManager, URLs);
    }

    @Test
    public void getItem_returnsFragmentAtRequestedPosition() throws Exception {
        Fragment firstFragment = subject.getItem(0);

        String gagURL1 = ((GagFragment) firstFragment).getURL();
        assertThat(gagURL1).isEqualTo("http://gag1.png");

        Fragment secondFragment = subject.getItem(1);

        String gagURL2 = ((GagFragment) secondFragment).getURL();
        assertThat(gagURL2).isEqualTo("http://gag2.png");
    }

    @Test
    public void getCount_returnsNumberOfURLs() throws Exception {
        assertThat(subject.getCount()).isEqualTo(3);
    }
}