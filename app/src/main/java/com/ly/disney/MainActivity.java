package com.ly.disney;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuyan on 2016/8/16.
 */
public class MainActivity extends FragmentActivity{
    private ViewPager mViewPager;
    private IconTabPageIndicator mIndicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mIndicator = (IconTabPageIndicator) findViewById(R.id.indicator);
        List<BaseFragment> fragments = initFragments();
        FragmentAdapter adapter = new FragmentAdapter(fragments, getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mIndicator.setViewPager(mViewPager);
    }

    private List<BaseFragment> initFragments() {
        List<BaseFragment> fragments = new ArrayList<BaseFragment>();

        BaseFragment userFragment = new BaseFragment();
        userFragment.setTitle("用户");
        userFragment.setIconId(R.drawable.tab_user_selector);
        fragments.add(userFragment);

        BaseFragment noteFragment = new BaseFragment();
        noteFragment.setTitle("记事本");
        noteFragment.setIconId(R.drawable.tab_record_selector);
        fragments.add(noteFragment);

        BaseFragment contactFragment = new BaseFragment();
        contactFragment.setTitle("联系人");
        contactFragment.setIconId(R.drawable.tab_user_selector);
        fragments.add(contactFragment);

        BaseFragment recordFragment = new BaseFragment();
        recordFragment.setTitle("记录");
        recordFragment.setIconId(R.drawable.tab_record_selector);
        fragments.add(recordFragment);

        return fragments;
    }

    class FragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
        private List<BaseFragment> mFragments;

        public FragmentAdapter(List<BaseFragment> fragments, FragmentManager fm) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int i) {
            return mFragments.get(i);
        }

        @Override
        public int getIconResId(int index) {
            return mFragments.get(index).getIconId();
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragments.get(position).getTitle();
        }
    }
}