package com.example.petproject.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.petproject.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class PetTypeBottomSheetFragment extends DialogFragment {

    private String[] petTypes = {"猫", "狗"};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_pet_type, container, false);

        final NumberPicker numberPicker = view.findViewById(R.id.numberPicker);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(petTypes.length - 1);
        numberPicker.setDisplayedValues(petTypes);
        numberPicker.setWrapSelectorWheel(false);

        Button btnConfirm = view.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedIndex = numberPicker.getValue();
                String selectedPetType = petTypes[selectedIndex];
                handlePetTypeSelection(selectedPetType,selectedIndex);
            }
        });

        return view;
    }

    public void setOptions(String[] petTypes) {
        this.petTypes = petTypes;
    }

    private void handlePetTypeSelection(String selectedPetType,int index) {
        // 处理选择逻辑
        // 在这里执行你的逻辑，比如显示 Toast
        if (listener != null) {
            listener.onItemSelected(selectedPetType,index);
        }
        dismiss(); // 关闭底部弹窗
    }

    private OnItemSelectedListener listener;

    public void setOnItemSelectedListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }

    public interface OnItemSelectedListener {
        void onItemSelected(String selectedItem,int index);
    }
}

