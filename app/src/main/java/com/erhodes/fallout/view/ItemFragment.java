package com.erhodes.fallout.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.erhodes.fallout.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment with a view pager to display other fragments!
 */
public class ItemFragment extends Fragment implements ViewPager.OnPageChangeListener{
    private int mCurrentPosition;
    private Adapter mAdapter;

    public static ItemFragment newInstance() {
        return new ItemFragment();
    }

    public ItemFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_holder, container, false);

        ViewPager viewPager = (ViewPager)view.findViewById(R.id.viewpager);
        mAdapter = new Adapter(getChildFragmentManager());
        mAdapter.addFragment(EquipmentFragment.newInstance(), getString(R.string.equipped_items));
        mAdapter.addFragment(InventoryFragment.newInstance(), getString(R.string.inventory));
        viewPager.setAdapter(mAdapter);
        viewPager.addOnPageChangeListener(this);

        TabLayout tabs = (TabLayout)view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        FragmentLifecycle fragmentToShow = (FragmentLifecycle)mAdapter.getItem(position);
        fragmentToShow.onResumeFragment();

        FragmentLifecycle fragmentToHide = (FragmentLifecycle)mAdapter.getItem(mCurrentPosition);
        fragmentToHide.onPauseFragment();

        mCurrentPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
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
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
