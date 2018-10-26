package com.pasyappagent.pasy.modul.premium.agent;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.pasyappagent.pasy.BaseActivity;
import com.pasyappagent.pasy.R;
import com.pasyappagent.pasy.modul.home.AgentFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dhimas on 2/4/18.
 */

public class PremiumActivity extends BaseActivity {
    private SectionsPagerAdapter pagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.premium_activity;
    }

    @Override
    protected void setContentViewOnChild() {
        setSlidingMenu(this);
        pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.all_trans_viewPager);
        TabLayout mTabLayout = (TabLayout) findViewById(R.id.all_trans_tab);
        mViewPager.setAdapter(pagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        pagerAdapter.addTabFragment(new AgentFragment().newInstance(false));
    }

    @Override
    protected void onCreateAtChild() {

    }

    @Override
    protected void onBackBtnPressed() {
        onBackPressed();
    }

    @Override
    protected void onSubmitBtnPressed() {

    }

    private class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> mFragment;

        private SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            this.mFragment = new ArrayList<>();
        }

        private void addTabFragment(Fragment fragment) {
            mFragment.add(fragment);
            notifyDataSetChanged();
        }

        private void removeTabFragment() {
            mFragment.clear();
            notifyDataSetChanged();
        }

        @Override
        public Fragment getItem(int position) {
            return this.mFragment.get(position);
        }

        @Override
        public int getCount() {
            return mFragment != null ? mFragment.size() : 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "PREMIUM MEMBER";
                case 1:
                    return "REWARD";
                default:
                    return "";
            }
        }
    }
}
