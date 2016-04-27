package com.appdev.postify.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import com.appdev.postify.BaseApplication;
import com.appdev.postify.Controller.DBController;
import com.appdev.postify.R;
import com.appdev.postify.fragments.CompleteEntriesFragment;
import com.appdev.postify.fragments.TodayEntriesFragment;
import com.appdev.postify.fragments.WeekEntriesFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ViewPager viewPager;
    private static ViewPagerAdapter adapter;
    DBController dbController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);                   // Setting toolbar as the ActionBar with setSupportActionBar() call

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager();

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        dbController = DBController.getInstance();
        dbController.readExternalEntries(this);

        BaseApplication.removeAllBadges();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dbController.readExternalEntries(this);
    }

    public void setupViewPager() {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        String tabOneTitle = this.getResources().getString(R.string.day_txt);
        String tabTwoTitle = this.getResources().getString(R.string.week_txt);
        String tabThreeTitle = this.getResources().getString(R.string.complete_txt);

        adapter.addFragment(new TodayEntriesFragment(), tabOneTitle);
        adapter.addFragment(new WeekEntriesFragment(), tabTwoTitle);
        adapter.addFragment(new CompleteEntriesFragment(), tabThreeTitle);
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            // Wird ausgeführt beim notify
            return POSITION_NONE;
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public static void updateAdapter(){
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.men_settings:
                startActivity(new Intent(this, ConfigActivity.class));
                return true;
            //// TODO: 25.04.2016 statistic menu
            case R.id.men_stats:
                startActivity(new Intent(this, StatisticsActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}