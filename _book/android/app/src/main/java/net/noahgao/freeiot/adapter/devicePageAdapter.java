/*
 * Copyright (c) 2017. Noah Gao <noahgaocn@gmail.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package net.noahgao.freeiot.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

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
