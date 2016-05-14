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
 * This fragment displays other fragments containing information about specific aspects of the active character.
 *
 */
public class CharacterFragment extends Fragment implements ViewPager.OnPageChangeListener {
    private int mCurrentPosition;
    private Adapter mAdapter;

    public static CharacterFragment newInstance() {
        return new CharacterFragment();
    }

    public CharacterFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_char, container, false);

        ViewPager viewPager = (ViewPager)view.findViewById(R.id.viewpager);
        mAdapter = new Adapter(getChildFragmentManager());
        mAdapter.addFragment(new AttributesFragment(), getString(R.string.attributes));
        mAdapter.addFragment(new SkillsFragment(), getString(R.string.skills));
        mAdapter.addFragment(new PerkFragment(), getString(R.string.perks));
        mAdapter.addFragment(new EffectFragment(), getString(R.string.effects));
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
