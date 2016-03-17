package com.appdev.postify.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.appdev.postify.R;
import com.appdev.postify.fragments.EntriesFragment;

/**
 * Created by Soere on 17.03.2016.
 */
public class EntriesPageAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;

    private String[] tabTitles = new String[3];
    private Context context;

    public EntriesPageAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;

        String tab1Title = context.getResources().getString(R.string.day_txt);
        String tab2Title = context.getResources().getString(R.string.week_txt);
        String tab3Title = context.getResources().getString(R.string.complete_txt);
        tabTitles = new String[] {tab1Title, tab2Title, tab3Title};
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return EntriesFragment.newInstance(position + 1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

}
