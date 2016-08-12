package io.harry.zealot.module;

import android.content.Context;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.face.FaceDetector;

import dagger.Module;
import dagger.Provides;

@Module(
        includes = {
                ContextModule.class,
        }
)
public class GoogleFaceApiModule {
    private int classificationType;

    @Provides
    public FaceDetector provideFaceDetector(Context context) {
        classificationType = FaceDetector.ALL_CLASSIFICATIONS;
        return new FaceDetector.Builder(context)
                .setClassificationType(classificationType).build();
    }

    @Provides
    public CameraSource provideCameraSource(Context context, FaceDetector faceDetector) {
        return new CameraSource.Builder(context, faceDetector).build();
    }
}
