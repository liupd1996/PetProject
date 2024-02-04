package com.example.petproject.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

public class MyFragmentPagerAdapter2 extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    private List<String> title;
    private FragmentManager fragmentManager;

    public MyFragmentPagerAdapter2(FragmentManager fragmentManager, List<Fragment> fragments, List<String> titles) {
        super(fragmentManager);
        fragmentList = fragments;
        title = titles;
        this.fragmentManager = fragmentManager;

    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        // 在切换到新的 Fragment 时执行操作
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment != object) {
                fragmentTransaction.hide(fragment);
            } else {
                fragmentTransaction.show(fragment);
            }
        }

        fragmentTransaction.commitNowAllowingStateLoss();

        super.setPrimaryItem(container, position, object);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }

}
