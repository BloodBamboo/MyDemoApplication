package com.example.admin.myapplication.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.admin.myapplication.fragment.LeadPagerFragment;

/**
 * Created by admin on 2017/2/13.
 */
public class LeadPagerAdapter extends FragmentPagerAdapter {
    public LeadPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return LeadPagerFragment.newInstant(position);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
