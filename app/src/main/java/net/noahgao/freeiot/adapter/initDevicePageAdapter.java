package net.noahgao.freeiot.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import net.noahgao.freeiot.ui.InitDeviceActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Noah Gao on 17-2-15.
 * By Android Studio
 */

public class initDevicePageAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentContainter = new ArrayList<>();
    private InitDeviceActivity mContext;

    public initDevicePageAdapter(FragmentManager fm, List<Fragment> fc,InitDeviceActivity context) {
        super(fm);
        fragmentContainter.clear();
        fragmentContainter.addAll(fc);
        mContext = context;
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
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        mContext.handleStep(position);
    }
}
