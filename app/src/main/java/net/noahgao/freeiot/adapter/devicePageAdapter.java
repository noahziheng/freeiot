package net.noahgao.freeiot.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Noah Gao on 17-2-15.
 * By Android Studio
 */

public class devicePageAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentContainter = new ArrayList<>();
    private String tabTitles[];

    public devicePageAdapter(FragmentManager fm, List<Fragment> fc, String[] tts) {
        super(fm);
        fragmentContainter.clear();
        fragmentContainter.addAll(fc);
        tabTitles = tts;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentContainter.get(position);
    }

    @Override
    public int getCount() {
        return fragmentContainter.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
