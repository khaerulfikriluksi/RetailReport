package com.urban.urbanreport.CustomClass;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPager_Adapter extends FragmentPagerAdapter {

    ArrayList<String> page = new ArrayList<>();
    List<Fragment> panel = new ArrayList<>();

    public void addFragment(Fragment fragment, String title){
        page.add(title);
        panel.add(fragment);
    }

    public ViewPager_Adapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return panel.get(position);
    }

    @Override
    public int getCount() {
        return panel.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return page.get(position);
    }
}