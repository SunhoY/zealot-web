package io.harry.zealot.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import io.harry.zealot.R;

public class MenuActivity extends ZealotBaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);
    }
}
