package com.example.petproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

public class TabFragment5 extends Fragment {
    private static final String TAG = "TabFragment1";
    private View view;

    public TabFragment5() {
        // Required empty public constructor
    }

    public static TabFragment5 newInstance(int id) {
        TabFragment5 fragment = new TabFragment5();
        Bundle args = new Bundle();
        args.putInt("ID", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_tab5, container, false);
        LinearLayout ll_device = view.findViewById(R.id.ll_device);
        ll_device.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), DeviceActivity.class);
            startActivity(intent);
        });
        LinearLayout ll_pet = view.findViewById(R.id.ll_pet);
        ll_pet.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), PetActivity.class);
            startActivity(intent);
        });

        LinearLayout ll_setting = view.findViewById(R.id.ll_setting);
        ll_setting.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            startActivity(intent);
        });
        return view;
    }

}