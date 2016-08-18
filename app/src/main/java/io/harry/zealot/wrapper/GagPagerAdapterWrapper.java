package io.harry.zealot.wrapper;

import android.support.v4.app.FragmentManager;

import java.util.List;

import io.harry.zealot.adapter.GagPagerAdapter;

public class GagPagerAdapterWrapper {
    public GagPagerAdapter getGagPagerAdapter(FragmentManager fragmentManager, List<Integer> drawableResourceIds) {
        return new GagPagerAdapter(fragmentManager);
    }
}
