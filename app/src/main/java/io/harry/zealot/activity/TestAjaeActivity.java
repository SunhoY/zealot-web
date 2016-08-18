package io.harry.zealot.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.harry.zealot.R;
import io.harry.zealot.adapter.GagPagerAdapter;
import io.harry.zealot.wrapper.GagPagerAdapterWrapper;

public class TestAjaeActivity extends ZealotBaseActivity {

    @BindView(R.id.gag_pager)
    ViewPager gagPager;

    @Inject
    GagPagerAdapterWrapper gagPagerAdapterWrapper;

    private GagPagerAdapter gagPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_ajae);

        ButterKnife.bind(this);
        zealotComponent.inject(this);

        gagPagerAdapter = gagPagerAdapterWrapper.getGagPagerAdapter(getSupportFragmentManager());
        gagPager.setAdapter(gagPagerAdapter);
    }
}
