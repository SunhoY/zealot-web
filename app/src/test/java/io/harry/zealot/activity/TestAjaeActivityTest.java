package io.harry.zealot.activity;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.harry.zealot.BuildConfig;
import io.harry.zealot.R;
import io.harry.zealot.TestZealotApplication;
import io.harry.zealot.adapter.GagPagerAdapter;
import io.harry.zealot.service.GagService;
import io.harry.zealot.service.ServiceCallback;
import io.harry.zealot.wrapper.GagPagerAdapterWrapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class TestAjaeActivityTest {
    private TestAjaeActivity subject;

    @BindView(R.id.gag_pager)
    ViewPager gagPager;

    @Inject
    GagPagerAdapterWrapper mockGagPagerAdapterWrapper;
    @Inject
    GagService mockGagService;

    @Mock
    private GagPagerAdapter mockGagPagerAdapter;

    @Captor
    ArgumentCaptor<ServiceCallback<List<String>>> stringListServiceCallbackCaptor;
    @Captor
    ArgumentCaptor<ServiceCallback<List<Uri>>> uriListServiceCallbackCaptor;

    @Before
    public void setUp() throws Exception {
        ((TestZealotApplication) RuntimeEnvironment.application).getZealotComponent().inject(this);
        MockitoAnnotations.initMocks(this);

        when(mockGagPagerAdapterWrapper.getGagPagerAdapter(any(FragmentManager.class), anyListOf(Uri.class)))
            .thenReturn(mockGagPagerAdapter);

        subject = Robolectric.buildActivity(TestAjaeActivity.class).create().get();

        ButterKnife.bind(this, subject);
    }

    @Test
    public void onCreate_callGagServiceToFetchGagImageFileNames() throws Exception {
        verify(mockGagService).getGagImageFileNames(Matchers.<ServiceCallback<List<String>>>any());
    }

    @Test
    public void afterFetchingGagImageFileNames_callsGagServiceToGetImageURLs() throws Exception {
        verify(mockGagService).getGagImageFileNames(stringListServiceCallbackCaptor.capture());

        stringListServiceCallbackCaptor.getValue().onSuccess(Arrays.asList("gag1.png", "gag2.png"));

        verify(mockGagService).getGagImageUris(eq(Arrays.asList("gag1.png", "gag2.png")),
                Matchers.<ServiceCallback<List<Uri>>> any());
    }

    @Test
    public void onCreate_getPagerAdapterWithRequestSizeOfFragments() throws Exception {
        verify(mockGagService).getGagImageFileNames(stringListServiceCallbackCaptor.capture());

        stringListServiceCallbackCaptor.getValue().onSuccess(Arrays.asList("gag1.png", "gag2.png"));

        verify(mockGagService).getGagImageUris(eq(Arrays.asList("gag1.png", "gag2.png")),
                uriListServiceCallbackCaptor.capture());

        List<Uri> actualUris = Arrays.asList(Uri.parse("http://gag1.png"),
                Uri.parse("http://gag2.png"), Uri.parse("http://gag3.png"));

        uriListServiceCallbackCaptor.getValue().onSuccess(actualUris);

        List<Uri> expectedUris = Arrays.asList(Uri.parse("http://gag1.png"),
                Uri.parse("http://gag2.png"), Uri.parse("http://gag3.png"));

        verify(mockGagPagerAdapterWrapper).getGagPagerAdapter(subject.getSupportFragmentManager(),
                expectedUris);
    }

    @Test
    public void onCreate_setAdapterOnViewPager() throws Exception {
        verify(mockGagService).getGagImageFileNames(stringListServiceCallbackCaptor.capture());

        stringListServiceCallbackCaptor.getValue().onSuccess(Arrays.asList("gag1.png", "gag2.png"));

        verify(mockGagService).getGagImageUris(eq(Arrays.asList("gag1.png", "gag2.png")),
                uriListServiceCallbackCaptor.capture());

        List<Uri> actualUris = Arrays.asList(Uri.parse("http://gag1.png"),
                Uri.parse("http://gag2.png"), Uri.parse("http://gag3.png"));

        when(mockGagPagerAdapterWrapper.getGagPagerAdapter(any(FragmentManager.class), anyListOf(Uri.class)))
                .thenReturn(mockGagPagerAdapter);

        uriListServiceCallbackCaptor.getValue().onSuccess(actualUris);

        assertThat(gagPager.getAdapter()).isEqualTo(mockGagPagerAdapter);
    }
}