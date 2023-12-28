package com.example.petproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class TabFragment1 extends Fragment {
    private static final String TAG = "TabFragment1";
    private View view;

    public TabFragment1() {
        // Required empty public constructor
    }

    public static TabFragment1 newInstance(int id) {
        TabFragment1 fragment = new TabFragment1();
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
        view = inflater.inflate(R.layout.fragment_tab1, container, false);
        return view;
    }

}