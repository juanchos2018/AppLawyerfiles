package com.document.lawyerfiles.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyViewPageAdapter  extends FragmentPagerAdapter {

    List<Fragment> list = new ArrayList<>();

    public MyViewPageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {
        return list.get(i);
    }

    @Override
    public int getCount() {
        return list.size();
    }
    public void addFragment(Fragment fragment){
        list.add(fragment);
    }
}
