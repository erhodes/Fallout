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
 * Created by Eric on 14/03/2016.
 */
public class CharacterFragment extends Fragment {

    public static CharacterFragment newInstance() {
        return new CharacterFragment();
    }

    public CharacterFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_char, container, false);

        ViewPager viewPager = (ViewPager)view.findViewById(R.id.viewpager);
        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new AttributesFragment(), "Attributes");
        adapter.addFragment(new SkillsFragment(), "Skills");
        adapter.addFragment(new PerkFragment(), "Perks");
        viewPager.setAdapter(adapter);

        TabLayout tabs = (TabLayout)view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        return view;
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
