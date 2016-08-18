package io.harry.zealot.wrapper;

import android.support.v4.app.FragmentManager;

import io.harry.zealot.adapter.GagPagerAdapter;

public class GagPagerAdapterWrapper {
    public GagPagerAdapter getGagPagerAdapter(FragmentManager fragmentManager) {
        return new GagPagerAdapter(fragmentManager);
    }
}
