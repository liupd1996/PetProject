package com.example.petproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.petproject.base.BaseActivity;
import com.example.petproject.bean.LoginResponse;
import com.example.petproject.bean.PetRequest;
import com.example.petproject.bean.RemoteResult;
import com.example.petproject.dialog.PetTypeBottomSheetFragment;
import com.example.petproject.retrofit.ResultFunction;
import com.example.petproject.retrofit.RetrofitUtils;
import com.example.petproject.utils.ConfigPreferences;
import com.example.petproject.utils.ExceptionHandle;
import com.example.petproject.utils.ToastUtils;

import java.util.Calendar;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddPetActivity extends BaseActivity {
    private static final String TAG = "AddPetActivity";
    private PetTypeBottomSheetFragment fragmentType;
    private PetTypeBottomSheetFragment fragmentGender;
    private PetTypeBottomSheetFragment fragmentCut;
    private PetTypeBottomSheetFragment fragmentVaccine;
    private int indexType = 0;
    private int indexGender = 0;
    private int indexCut = 0;
    private int indexVaccine = 0;
    private String selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDialogFragment();
//        findViewById(R.id.cl_bar_back).setBackgroundColor(Color.parseColor("#FFC0CB"));
        findViewById(R.id.cl_birth).setOnClickListener(view -> {
            showDatePickerDialog();
        });
        findViewById(R.id.cl_type).setOnClickListener(view -> {
            fragmentType.show(getSupportFragmentManager(), fragmentType.getTag());
        });
        findViewById(R.id.cl_gender).setOnClickListener(view -> {
            fragmentGender.show(getSupportFragmentManager(), fragmentGender.getTag());
        });
        findViewById(R.id.cl_sex).setOnClickListener(view -> {
            fragmentCut.show(getSupportFragmentManager(), fragmentCut.getTag());
        });
        findViewById(R.id.cl_vaccine).setOnClickListener(view -> {
            fragmentVaccine.show(getSupportFragmentManager(), fragmentVaccine.getTag());
        });
        TextView tv_type = findViewById(R.id.tv_type);
        tv_type.setText("猫猫");
        TextView tv_gender = findViewById(R.id.tv_gender);
        tv_gender.setText("男生");
        TextView tv_cut = findViewById(R.id.tv_cut);
        tv_cut.setText("是");
        TextView tv_vaccine = findViewById(R.id.tv_vaccine);
        tv_vaccine.setText("是");

        ImageButton button = findViewById(R.id.iv_back);
        button.setOnClickListener(v -> {
            onBackPressed();
        });
        TextView title = findViewById(R.id.tv_bar_title);
        title.setText("添加宠物");

        findViewById(R.id.btn_save).setOnClickListener(view -> {
            EditText et_name = findViewById(R.id.et_name);
            String name = et_name.getText().toString();
            if (TextUtils.isEmpty(name)) {
                ToastUtils.customToast(AddPetActivity.this,"请输入宠物名字");
                return;
            }
            EditText et_weight = findViewById(R.id.et_weight);
            String weight = et_weight.getText().toString();
            if (TextUtils.isEmpty(weight)) {
                ToastUtils.customToast(AddPetActivity.this,"请输入宠物体重");
                return;
            }
            String token = "Bearer " + ConfigPreferences.login_token(AddPetActivity.this);
            Log.d(TAG, "token1111: " + token);
            PetRequest request = new PetRequest("",selectedDate
                    ,indexGender,indexCut,indexVaccine,name,indexType,weight);
            petInsert(token, request);
        });
    }

    private void petInsert(String token, PetRequest request) {
        RetrofitUtils.getRetrofitService().petInsert(token,request)
                .filter(new ResultFunction())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RemoteResult<Object>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull RemoteResult<Object> result) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        ToastUtils.customToast(AddPetActivity.this, ExceptionHandle.handleException(e).message);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public int getContentView() {
        return R.layout.activity_add_pet;
    }

    private void initDialogFragment() {
        fragmentType = new PetTypeBottomSheetFragment();
        String[] petTypes = {"猫猫", "狗狗"};
        fragmentType.setOptions(petTypes);
        fragmentType.setOnItemSelectedListener((selectedItem, index) -> {
            indexType = index;
            TextView tv_type = findViewById(R.id.tv_type);
            tv_type.setText(selectedItem);
        });

        fragmentGender = new PetTypeBottomSheetFragment();
        String[] genders = {"男生", "女生"};
        fragmentGender.setOptions(genders);
        fragmentGender.setOnItemSelectedListener((selectedItem, index) -> {
            indexGender = index;
            TextView tv_gender = findViewById(R.id.tv_gender);
            tv_gender.setText(selectedItem);
        });

        fragmentCut = new PetTypeBottomSheetFragment();
        String[] cuts = {"是", "否"};
        fragmentCut.setOptions(cuts);
        fragmentCut.setOnItemSelectedListener((selectedItem, index) -> {
            indexCut = index;
            TextView tv_cut = findViewById(R.id.tv_cut);
            tv_cut.setText(selectedItem);
        });

        fragmentVaccine = new PetTypeBottomSheetFragment();
        String[] vaccine = {"是", "否"};
        fragmentVaccine.setOptions(vaccine);
        fragmentVaccine.setOnItemSelectedListener((selectedItem, index) -> {
            indexVaccine = index;
            TextView tv_vaccine = findViewById(R.id.tv_vaccine);
            tv_vaccine.setText(selectedItem);
        });
    }

    public void showDatePickerDialog() {
        // 获取当前日期
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // 创建日期选择对话框
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, monthOfYear, dayOfMonth) -> {
                    // 处理选择的日期
                    selectedDate = selectedYear + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    Log.d(TAG, "onDateSet: " + selectedDate);
                    TextView tv_birth = findViewById(R.id.tv_birth);
                    tv_birth.setText(selectedDate);
                },
                year,
                month,
                day
        );

        // 显示日期选择对话框
        datePickerDialog.show();
    }
}