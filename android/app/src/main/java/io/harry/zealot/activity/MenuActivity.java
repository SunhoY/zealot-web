package io.harry.zealot.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.harry.zealot.R;
import io.harry.zealot.helper.BitmapHelper;
import io.harry.zealot.service.GagService;
import io.harry.zealot.service.ServiceCallback;

public class MenuActivity extends ZealotBaseActivity {
    public static final int PICK_PHOTO = 9;
    public static final int MAX_WIDTH = 720;

    @Inject
    BitmapHelper bitmapHelper;
    @Inject
    GagService gagService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        zealotComponent.inject(this);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.start_button)
    public void onStartClick() {
        startActivity(new Intent(this, TestAjaeActivity.class));
    }

    @OnClick(R.id.upload_button)
    public void onUploadClick() {
        startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), PICK_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data == null) {
            return;
        }

        Uri selectedImage = data.getData();

        Bitmap bitmap = bitmapHelper.getBitmapByURI(selectedImage);

        BitmapSize size = calculateSize(bitmap.getWidth(), bitmap.getHeight(), MAX_WIDTH);

        Bitmap scaledBitmap = bitmapHelper.scaleBitmap(bitmap, size.width, size.height);

        gagService.uploadGag(scaledBitmap, new ServiceCallback() {
            @Override
            public void onSuccess(Object result) {
                Toast.makeText(MenuActivity.this, R.string.upload_complete, Toast.LENGTH_LONG).show();
            }
        });
    }

    class BitmapSize {
        int width;
        int height;

        BitmapSize(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }

    private BitmapSize calculateSize(int width, int height, int maxWidth) {
        if(width < maxWidth) {
            return new BitmapSize(width, height);
        }

        double scale = (double) maxWidth / (double) width;

        int newWidth = (int) (width * scale);
        int newHeight = (int) (height * scale);

        return new BitmapSize(newWidth, newHeight);
    }
}
