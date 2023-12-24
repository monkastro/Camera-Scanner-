package com.scanner.camera.phototopdf.papercamerascanner.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.ArrayList;

public class MyTabPageAdapter extends FragmentPagerAdapter {
    ArrayList<String> fragmentTitleList = new ArrayList<>();
    ArrayList<Fragment> fragmentsList = new ArrayList<>();

    public int getItemPosition(Object obj) {
        return -2;
    }

    public MyTabPageAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public Fragment getItem(int i) {
        return this.fragmentsList.get(i);
    }

    public int getCount() {
        return this.fragmentTitleList.size();
    }

    public void addFragments(Fragment fragment, String str) {
        this.fragmentsList.add(fragment);
        this.fragmentTitleList.add(str);
    }

    public CharSequence getPageTitle(int i) {
        return this.fragmentTitleList.get(i);
    }
}
