package io.harry.zealot.activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.akexorcist.roundcornerprogressbar.common.BaseRoundCornerProgressBar;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.harry.zealot.R;
import io.harry.zealot.adapter.GagPagerAdapter;
import io.harry.zealot.helper.AnimationHelper;
import io.harry.zealot.listener.FaceListener;
import io.harry.zealot.service.GagService;
import io.harry.zealot.service.ServiceCallback;
import io.harry.zealot.view.TestAjaePreview;
import io.harry.zealot.vision.ZealotFaceFactory;
import io.harry.zealot.vision.wrapper.ZealotCameraSourceWrapper;
import io.harry.zealot.vision.wrapper.ZealotFaceDetectorWrapper;
import io.harry.zealot.vision.wrapper.ZealotFaceFactoryWrapper;
import io.harry.zealot.vision.wrapper.ZealotMultiProcessorWrapper;
import io.harry.zealot.wrapper.GagPagerAdapterWrapper;

public class TestAjaeActivity extends ZealotBaseActivity implements FaceListener {

    private final float AJAE_POWER_UNIT = 10.0f;

    @BindView(R.id.gag_pager)
    ViewPager gagPager;
    @BindView(R.id.test_ajae_preview)
    TestAjaePreview testAjaePreview;
    @BindView(R.id.progress)
    RoundCornerProgressBar progress;
    @BindView(R.id.ajae_icon)
    ImageView ajaeIcon;

    @Inject
    GagPagerAdapterWrapper gagPagerAdapterWrapper;
    @Inject
    GagService gagService;

    @Inject
    ZealotFaceDetectorWrapper faceDetectorWrapper;
    @Inject
    ZealotMultiProcessorWrapper multiProcessorWrapper;
    @Inject
    ZealotFaceFactoryWrapper faceFactoryWrapper;
    @Inject
    ZealotCameraSourceWrapper cameraSourceWrapper;
    @Inject
    AnimationHelper animationHelper;

    private GagPagerAdapter gagPagerAdapter;
    private FaceDetector faceDetector;
    private MultiProcessor<Face> faceProcessor;
    private ZealotFaceFactory faceFactory;
    private CameraSource cameraSource;

    private float ajaePower = .0f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_ajae);

        ButterKnife.bind(this);
        zealotComponent.inject(this);

        faceFactory = faceFactoryWrapper.getZealotFaceFactory(this);
        faceDetector = faceDetectorWrapper.getFaceDetector(this);
        cameraSource = cameraSourceWrapper.getCameraSource(this, faceDetector);
        faceProcessor = multiProcessorWrapper.getMultiProcessor(faceFactory);
        faceDetector.setProcessor(faceProcessor);

        testAjaePreview.setCameraSource(cameraSource);

        gagService.getGagImageFileNames(new ServiceCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> result) {
                getGagImageURLsWithImageNames(result);
            }
        });

        final int ajaeFullPower = getResources().getInteger(R.integer.ajae_full_power);
        progress.setOnProgressChangedListener(new BaseRoundCornerProgressBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int viewId, float progress, boolean isPrimaryProgress, boolean isSecondaryProgress) {
                if(progress == ajaeFullPower) {
                    Animation scaleXYAnimation = animationHelper.loadAnimation(R.animator.scale_xy);
                    animationHelper.startAnimation(ajaeIcon, scaleXYAnimation);
                }
            }
        });
    }

    @Override
    public void onFaceDetect(Face face) {
        final float smile = face.getIsSmilingProbability();

        Handler handler = new Handler(getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(smile > .30f) {
                    ajaePower += AJAE_POWER_UNIT;
                    progress.setProgress(ajaePower);
                }
            }
        });
    }

    private void getGagImageURLsWithImageNames(List<String> result) {
        gagService.getGagImageUris(result, new ServiceCallback<List<Uri>>() {
            @Override
            public void onSuccess(List<Uri> result) {
                gagPagerAdapter = gagPagerAdapterWrapper.getGagPagerAdapter(
                        getSupportFragmentManager(), result);
                gagPager.setAdapter(gagPagerAdapter);
            }
        });
    }
}
