package io.harry.zealot.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import org.assertj.android.api.content.IntentAssert;
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
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.harry.zealot.BuildConfig;
import io.harry.zealot.R;
import io.harry.zealot.TestZealotApplication;
import io.harry.zealot.adapter.GagPagerAdapter;
import io.harry.zealot.helper.AnimationHelper;
import io.harry.zealot.service.GagService;
import io.harry.zealot.service.ServiceCallback;
import io.harry.zealot.view.TestAjaePreview;
import io.harry.zealot.viewpager.ZealotViewPager;
import io.harry.zealot.vision.wrapper.ZealotCameraSourceWrapper;
import io.harry.zealot.vision.wrapper.ZealotFaceDetectorWrapper;
import io.harry.zealot.wrapper.GagPagerAdapterWrapper;

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.RuntimeEnvironment.application;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class TestAjaeActivityTest {
    private TestAjaeActivity subject;

    @BindView(R.id.gag_pager)
    ZealotViewPager gagPager;
    @BindView(R.id.test_ajae_preview)
    TestAjaePreview testAjaePreview;
    @BindView(R.id.progress)
    RoundCornerProgressBar progress;
    @BindView(R.id.ajae_icon)
    ImageView ajaeIcon;

    @Inject
    GagPagerAdapterWrapper mockGagPagerAdapterWrapper;
    @Inject
    GagService mockGagService;
    @Inject
    ZealotFaceDetectorWrapper mockFaceDetectorWrapper;
    @Inject
    ZealotCameraSourceWrapper mockCameraSourceWrapper;
    @Inject
    AnimationHelper mockAnimationHelper;

    @Mock
    private GagPagerAdapter mockGagPagerAdapter;
    @Mock
    private com.google.android.gms.vision.CameraSource mockCameraSource;
    private FaceDetector testFaceDetector;

    @Captor
    ArgumentCaptor<ServiceCallback<List<String>>> stringListServiceCallbackCaptor;
    @Captor
    ArgumentCaptor<ServiceCallback<List<Uri>>> uriListServiceCallbackCaptor;
    private Animation mockScaleXYAnimation;


    @Before
    public void setUp() throws Exception {
        ((TestZealotApplication) application).getZealotComponent().inject(this);
        MockitoAnnotations.initMocks(this);

        testFaceDetector = new FaceDetector.Builder(application).build();
        mockScaleXYAnimation = mock(Animation.class);

        when(mockGagPagerAdapterWrapper.getGagPagerAdapter(any(FragmentManager.class), anyListOf(Uri.class)))
            .thenReturn(mockGagPagerAdapter);
        when(mockFaceDetectorWrapper.getFaceDetector(any(Context.class))).thenReturn(testFaceDetector);
        when(mockCameraSourceWrapper.getCameraSource(any(Context.class), eq(testFaceDetector))).thenReturn(mockCameraSource);
        when(mockAnimationHelper.loadAnimation(R.animator.scale_xy)).thenReturn(mockScaleXYAnimation);

        subject = Robolectric.buildActivity(TestAjaeActivity.class).create().get();

        ButterKnife.bind(this, subject);
    }

    @Test
    public void onCreate_setsCameraSourceOnAjaePreview() throws Exception {
        Field cameraSourceField = TestAjaePreview.class.getDeclaredField("cameraSource");
        cameraSourceField.setAccessible(true);
        CameraSource cameraSource = (CameraSource) cameraSourceField.get(testAjaePreview);
        cameraSourceField.setAccessible(false);

        assertThat(cameraSource).isEqualTo(mockCameraSource);
    }

    @Test
    public void onCreate_setsOnSwipeAttemptedOnLastPageListenerOnViewPager() throws Exception {
        assertThat(gagPager.getOnSwipeListener()).isEqualTo(subject);
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

    @Test
    public void onFaceDetect_fillsProgressBar_whenFaceIsSmiling() throws Exception {
        faceDetectsWithSmileyProbability(.40f);

        Robolectric.getForegroundThreadScheduler().advanceToLastPostedRunnable();

        assertThat(progress.getProgress()).isEqualTo(10);
    }

    @Test
    public void onFaceDetect_doesNotFillProgressBar_whenFaceIsNotSmiling() throws Exception {
        faceDetectsWithSmileyProbability(.20f);

        Robolectric.getForegroundThreadScheduler().advanceToLastPostedRunnable();

        assertThat(progress.getProgress()).isEqualTo(.0f);
    }

    @Test
    public void onAjaePowerChanges_animatesAjaeIcon_whenAjaePowerIsGreaterAndEqualTo700() throws Exception {
        progress.setProgress(700.f);

        verify(mockAnimationHelper).startAnimation(ajaeIcon, mockScaleXYAnimation);
    }

    @Test
    public void onAjaePowerChanges_doesNotAnimateAjaeIcon_whenAjaePowerIsLessThan700() throws Exception {
        progress.setProgress(699.f);

        verify(mockAnimationHelper, never()).startAnimation(any(ImageView.class), any(Animation.class));
    }

    @Test
    public void onAjaePowerChanges_setProgressColorAsOrange_whenAjaeLevelExceedsCaution() throws Exception {
        progress.setProgress(500.f);

        assertThat(progress.getProgressColor()).isEqualTo(ContextCompat.getColor(application, R.color.orange));
    }

    @Test
    public void onAjaePowerChanges_setProgressColorAsHotPink_whenAjaeLevelExceedsDanger() throws Exception {
        progress.setProgress(700.f);

        assertThat(progress.getProgressColor()).isEqualTo(ContextCompat.getColor(application, R.color.hot_pink));
    }

    @Test
    public void onAjaePowerChanges_setProgressColorAsRed_whenAjaeLevelReachesRealAjae() throws Exception {
        progress.setProgress(900.f);

        assertThat(progress.getProgressColor()).isEqualTo(ContextCompat.getColor(application, R.color.red));
    }

    @Test
    public void onAjaePowerChanges_launchesResultActivity_whenAjaePowerFullyCharged() throws Exception {
        progress.setProgress(1000.f);

        Intent actual = shadowOf(subject).getNextStartedActivity();

        IntentAssert intentAssert = new IntentAssert(actual);
        intentAssert.hasComponent(application, ResultActivity.class);
        intentAssert.hasExtra("ajaeScore", 100);
    }

    @Test
    public void onSwipeAttemptedOnLastPage_launchesResultActivity() throws Exception {
        progress.setProgress(699.f);

        subject.onAttemptedOnLastPage();

        Intent actual = shadowOf(subject).getNextStartedActivity();

        IntentAssert intentAssert = new IntentAssert(actual);
        intentAssert.hasComponent(application, ResultActivity.class);
        intentAssert.hasExtra("ajaeScore", 69);
        intentAssert.hasFlags(FLAG_ACTIVITY_SINGLE_TOP);
    }

    @Test
    public void launchingResultActivity_finishesActivity() throws Exception {
        subject.onAttemptedOnLastPage();

        assertThat(subject.isFinishing()).isTrue();
    }

    private void faceDetectsWithSmileyProbability(float smileyProbability) {
        Face mockFace = mock(Face.class);
        when(mockFace.getIsSmilingProbability()).thenReturn(smileyProbability);

        subject.onFaceDetect(mockFace);
    }
}