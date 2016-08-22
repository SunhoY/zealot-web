package io.harry.zealot.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import io.harry.zealot.fragment.GagFragment;

public class GagPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<GagFragment> gagFragments;

    public GagPagerAdapter(FragmentManager fragmentManager, List<String> gagURLs) {
        super(fragmentManager);

        gagFragments = new ArrayList<>();

        for(String gagURL : gagURLs) {
            GagFragment gagFragment = new GagFragment();
            gagFragment.setUrl(gagURL);
            gagFragments.add(gagFragment);
        }
    }

    @Override
    public Fragment getItem(int position) {
        return gagFragments.get(position);
    }

    @Override
    public int getCount() {
        return gagFragments.size();
    }
}
