package io.harry.zealot.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.harry.zealot.BuildConfig;
import io.harry.zealot.R;
import io.harry.zealot.TestZealotApplication;
import io.harry.zealot.adapter.GagPagerAdapter;
import io.harry.zealot.wrapper.GagPagerAdapterWrapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class TestAjaeActivityTest {

    private TestAjaeActivity subject;

    @BindView(R.id.gag_pager)
    ViewPager gagPager;

    @Inject
    GagPagerAdapterWrapper gagPagerAdapterWrapper;

    @Mock
    private GagPagerAdapter mockGagPagerAdapter;

    @Before
    public void setUp() throws Exception {
        ((TestZealotApplication) RuntimeEnvironment.application).getZealotComponent().inject(this);
        MockitoAnnotations.initMocks(this);

        when(gagPagerAdapterWrapper.getGagPagerAdapter(any(FragmentManager.class)))
            .thenReturn(mockGagPagerAdapter);

        subject = Robolectric.buildActivity(TestAjaeActivity.class).create().get();

        ButterKnife.bind(this, subject);
    }

    @Test
    public void onCreate_getPagerAdapterFromGagPagerAdapterWrapper_withFragmentManager() throws Exception {
        verify(gagPagerAdapterWrapper).getGagPagerAdapter(subject.getSupportFragmentManager());
    }

    @Test
    public void onCreate_setAdapterOnViewPager() throws Exception {
        assertThat(gagPager.getAdapter()).isEqualTo(mockGagPagerAdapter);
    }
}