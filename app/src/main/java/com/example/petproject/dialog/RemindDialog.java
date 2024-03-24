package com.example.petproject.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.petproject.R;


public class RemindDialog extends DialogFragment {
    private View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_remind, container, false);
        initView();
        return view;
    }

    private void initView() {
        TextView left = view.findViewById(R.id.tv_sure);
        left.setOnClickListener(v -> {
            dismiss();
        });
    }
}
