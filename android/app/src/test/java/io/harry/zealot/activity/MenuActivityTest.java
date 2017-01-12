package io.harry.zealot.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

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
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowToast;

import javax.inject.Inject;

import io.harry.zealot.BuildConfig;
import io.harry.zealot.TestZealotApplication;
import io.harry.zealot.helper.BitmapHelper;
import io.harry.zealot.service.GagService;
import io.harry.zealot.service.ServiceCallback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.RuntimeEnvironment.application;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MenuActivityTest {
    private static final int PICK_PHOTO = 9;
    private static final int INT_DOESNT_MATTER = 0;
    private MenuActivity subject;

    @Mock
    Uri mockUri;

    @Inject
    BitmapHelper bitmapHelper;
    @Inject
    GagService mockGagService;

    @Captor
    ArgumentCaptor<ServiceCallback> serviceCallbackCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ((TestZealotApplication)application).getZealotComponent().inject(this);

        subject = Robolectric.setupActivity(MenuActivity.class);
    }

    @Test
    public void onStartClick_startsTestAjaeActivity() throws Exception {
        subject.onStartClick();

        IntentAssert intentAssert = new IntentAssert(shadowOf(subject).getNextStartedActivity());
        intentAssert.hasComponent(application, TestAjaeActivity.class);
    }

    @Test
    public void onUploadAjae_launchesGalleryAppToPickPhoto() throws Exception {
        subject.onUploadClick();

        ShadowActivity.IntentForResult actual = shadowOf(subject).getNextStartedActivityForResult();

        assertThat(actual.requestCode).isEqualTo(PICK_PHOTO);

        IntentAssert intentAssert = new IntentAssert(actual.intent);

        intentAssert.hasAction(Intent.ACTION_PICK);
        intentAssert.hasData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    }

    @Test
    public void onCancelPickPhoto_doesNothing_makeSureAppDoesNotCrash() throws Exception {
        subject.onActivityResult(PICK_PHOTO, Activity.RESULT_CANCELED, null);

        verify(bitmapHelper, never()).getBitmapByURI(any(Uri.class));
    }

    private void assumePhotoHasBeenPicked(Uri uri, Bitmap bitmapToBeReturned, int bitmapWidth, int bitmapHeight) {
        Intent data = new Intent();
        data.setData(uri);

        when(bitmapHelper.getBitmapByURI(uri)).thenReturn(bitmapToBeReturned);
        when(bitmapToBeReturned.getWidth()).thenReturn(bitmapWidth);
        when(bitmapToBeReturned.getHeight()).thenReturn(bitmapHeight);

        subject.onActivityResult(PICK_PHOTO, Activity.RESULT_OK, data);
    }

    @Test
    public void onActivityResult_createsBitmapImageFromURI() throws Exception {
        assumePhotoHasBeenPicked(mockUri, mock(Bitmap.class), 900, 1200);
        
        verify(bitmapHelper).getBitmapByURI(mockUri);
    }

    @Test
    public void onActivityResult_scalesBitmap() throws Exception {
        Bitmap mockBitmap = mock(Bitmap.class);
        assumePhotoHasBeenPicked(mockUri, mockBitmap, 900, 1200);

        verify(bitmapHelper).scaleBitmap(mockBitmap, 720, 960);
    }

    @Test
    public void onActivityResult_callsGagServiceToUploadImage() throws Exception {
        Bitmap mockBitmap = mock(Bitmap.class);
        when(bitmapHelper.scaleBitmap(eq(mockBitmap), anyInt(), anyInt())).thenReturn(mockBitmap);

        assumePhotoHasBeenPicked(mockUri, mockBitmap, 900, 1200);

        verify(mockGagService).uploadGag(eq(mockBitmap), Matchers.<ServiceCallback>any());
    }

    @Test
    public void onSuccessUpload_toastsSuccessMessage() throws Exception {
        Bitmap mockBitmap = mock(Bitmap.class);
        when(bitmapHelper.scaleBitmap(eq(mockBitmap), anyInt(), anyInt())).thenReturn(mockBitmap);

        assumePhotoHasBeenPicked(mockUri, mockBitmap, INT_DOESNT_MATTER, INT_DOESNT_MATTER);

        verify(mockGagService).uploadGag(eq(mockBitmap), serviceCallbackCaptor.capture());

        serviceCallbackCaptor.getValue().onSuccess(null);

        assertThat(ShadowToast.getTextOfLatestToast()).isEqualTo("업로드 완료");
    }
}