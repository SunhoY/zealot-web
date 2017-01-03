package io.harry.zealot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.harry.zealot.R;

public class MenuActivity extends ZealotBaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.start_button)
    public void onStartClick() {
        startActivity(new Intent(this, TestAjaeActivity.class));
    }
}
