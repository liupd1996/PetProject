package com.example.petproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.petproject.adapter.MyFragmentPagerAdapter2;
import com.example.petproject.base.BaseActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
        initView();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }

    private void initView() {
        ViewPager viewPager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tabs);

        List<Fragment> fragments = new ArrayList<>();
        TabFragment1 fragment1 = TabFragment1.newInstance(1);
        TabFragment2 fragment2 = TabFragment2.newInstance(2);
        TabFragment3 fragment3 = TabFragment3.newInstance(3);
        TabFragment4 fragment4 = TabFragment4.newInstance(4);
        TabFragment5 fragment5 = TabFragment5.newInstance(5);
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        fragments.add(fragment4);
        fragments.add(fragment5);
        List<String> titles = new ArrayList<>();
        titles.add("数据");
        titles.add("宠友圈");
        titles.add("地图");
        titles.add("消息");
        titles.add("我的");
        MyFragmentPagerAdapter2 myFragmentPagerAdapter = new MyFragmentPagerAdapter2(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(myFragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        TabLayout.Tab tab1 = tabLayout.getTabAt(0);
        tab1.setIcon(R.drawable.data_selector); // 设置图标
        tab1.setText("数据");
        TabLayout.Tab tab2 = tabLayout.getTabAt(1);
        tab2.setText("宠友圈");
        tab2.setIcon(R.drawable.friend_selector); // 设置图标
        TabLayout.Tab tab3 = tabLayout.getTabAt(2);
        tab3.setText("地图");
        tab3.setIcon(R.drawable.map_selector); // 设置图标
        TabLayout.Tab tab4 = tabLayout.getTabAt(3);
        tab4.setText("消息");
        tab4.setIcon(R.drawable.data_selector); // 设置图标
        TabLayout.Tab tab5 = tabLayout.getTabAt(4);
        tab5.setText("我的");
        tab5.setIcon(R.drawable.me_selector); // 设置图标
    }

    public String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.ACCESS_FINE_LOCATION
            ,Manifest.permission.ACCESS_COARSE_LOCATION
            ,Manifest.permission.ACCESS_NETWORK_STATE
            };

    private boolean checkPermission() {
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, 1);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermissionDismiss = false;
        if (1 == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    hasPermissionDismiss = true;
                }
            }

            if (hasPermissionDismiss) {
                //
            } else {
            }
        }
    }
}