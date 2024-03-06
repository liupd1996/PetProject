package com.example.petproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petproject.adapter.HistoryAdapter;
import com.example.petproject.adapter.HistoryItem;
import com.example.petproject.base.BaseActivity;
import com.example.petproject.bean.CountResponse;
import com.example.petproject.bean.PetWeightRequest;
import com.example.petproject.bean.RemoteResult;
import com.example.petproject.bean.WeightResponse;
import com.example.petproject.bean.WeightSearchResponse;
import com.example.petproject.retrofit.ResultFunction;
import com.example.petproject.retrofit.RetrofitUtils;
import com.example.petproject.utils.ConfigPreferences;
import com.example.petproject.utils.DataUtils;
import com.example.petproject.utils.ExceptionHandle;
import com.example.petproject.utils.ToastUtils;
import com.itheima.wheelpicker.WheelPicker;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DetailActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private HistoryAdapter mAdapter;
    private int type;
    private String mExtendParamDesc = "";
    private String weightNum = "";
    private String pet_Id = "";
    private String weight_edit = "";
    private List<HistoryItem> mListHistory1;
    private List<HistoryItem> mListHistory2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageButton button = findViewById(R.id.iv_back);
        button.setOnClickListener(v -> {
            onBackPressed();
        });
        //button.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        TextView title = findViewById(R.id.tv_bar_title);
        title.setTextColor(Color.BLACK);

        TextView title1 = findViewById(R.id.tv_title_1);
        TextView tv_seven_history = findViewById(R.id.tv_seven_history);
        TextView tv_weight = findViewById(R.id.tv_weight);

        mRecyclerView = findViewById(R.id.rv_history);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new HistoryAdapter(this);
        mAdapter.setOnItemClickListener((bean, position) -> {

        });
        mRecyclerView.setAdapter(mAdapter);
        WheelPicker wheelPicker = findViewById(R.id.wheelPicker);

        Intent intent = getIntent();
        type = intent.getIntExtra("type", -1);
        pet_Id = intent.getStringExtra("pet_Id");
        Log.d("1111", "onCreate----pet_Id" + pet_Id);
        mExtendParamDesc = intent.getStringExtra("message");
        weightNum = intent.getStringExtra("weight");
        weight_edit = weightNum;
        String weight = weightNum + " kg";

        // 设置WheelPicker的数据
        List<String> data = generateDecimalData(0.1, 100.0); // 生成0.1增加的数据，例如0.1, 0.2, ..., 10.0
        wheelPicker.setData(data);
        int initialIndex = data.indexOf(weightNum);
        if (initialIndex == -1) {
            wheelPicker.setSelectedItemPosition(data.indexOf("100.0"));
        } else {
            wheelPicker.setSelectedItemPosition(initialIndex);
        }
        // 设置滚轮选择监听器
        wheelPicker.setOnItemSelectedListener((picker, data1, position) -> {
            // 处理选中项的逻辑
            weight_edit = (String) data1;
            String selectedValue = (String) data1 + " kg";
            tv_weight.setText(getTextSizeSpan(selectedValue, 0, selectedValue.length() - 3));
        });

        TextView btn_update = findViewById(R.id.btn_update);
        btn_update.setOnClickListener(view -> {
            if (findViewById(R.id.cl_title).getVisibility() == View.VISIBLE) {
                findViewById(R.id.cl_title).setVisibility(View.GONE);
                findViewById(R.id.tv_weight).setVisibility(View.GONE);
                findViewById(R.id.wheelPicker).setVisibility(View.VISIBLE);
                findViewById(R.id.tv_wheel_kg).setVisibility(View.VISIBLE);
                btn_update.setText("完成");
            } else {
                findViewById(R.id.cl_title).setVisibility(View.VISIBLE);
                findViewById(R.id.tv_weight).setVisibility(View.VISIBLE);
                findViewById(R.id.wheelPicker).setVisibility(View.GONE);
                findViewById(R.id.tv_wheel_kg).setVisibility(View.GONE);
                String token = "Bearer " + ConfigPreferences.login_token(DetailActivity.this);
                Log.d("000000", "onCreate: " + weight_edit + "--" + pet_Id);
                if (!weightNum.equals(weight_edit) && !TextUtils.isEmpty(pet_Id)) {
                    addWeight(token, new PetWeightRequest(pet_Id, weight_edit));
                }
                btn_update.setText("更新");
            }
        });

        String deviceId = intent.getStringExtra("deviceId");
        switch (type) {
            case 1:
                title.setText("呼吸数据");
                title1.setText("昨日平均呼吸频率");
                tv_seven_history.setText("近七日平均呼吸频率");
                break;
            case 2:
                title.setText("睡眠数据");
                title1.setText("昨日睡眠时间");
                tv_seven_history.setText("近七日平均睡眠时间");
                break;
            case 3:
                title.setText("心率数据");
                title1.setText("昨日平均心率区间");
                tv_seven_history.setText("近七日平均心率区间");
                break;
            case 4:
                title.setText("体温数据");
                title1.setText("昨日平均体温");
                tv_seven_history.setText("近七日平均体温");
                break;
            case 5:
                title.setText("运动数据");
                title1.setText("昨日运动量");
                tv_seven_history.setText("近七日运动量");
                findViewById(R.id.cl_bar).setVisibility(View.VISIBLE);
                break;
            case 6:
                title.setText("消耗数据");
                title1.setText("昨日消耗量");
                tv_seven_history.setText("近七日消耗量");
                findViewById(R.id.cl_bar).setVisibility(View.VISIBLE);
                break;
            case 7:
                title.setText("体重数据");
                tv_seven_history.setText("近七日体重");
                tv_weight.setText(getTextSizeSpan(weight, 0, weight.length() - 3));
                findViewById(R.id.cl_title).setVisibility(View.VISIBLE);
                findViewById(R.id.tv_weight).setVisibility(View.VISIBLE);
                findViewById(R.id.btn_update).setVisibility(View.VISIBLE);
                findViewById(R.id.wheelPicker).setVisibility(View.GONE);
                findViewById(R.id.tv_wheel_kg).setVisibility(View.GONE);
                break;
            default:
                break;
        }

        String token = "Bearer " + ConfigPreferences.login_token(DetailActivity.this);
        Log.d("1111", "onCreate: " + deviceId);
        //breatheCount(token, deviceId);
        if (type == 7) {
            if (TextUtils.isEmpty(pet_Id)) {
                ToastUtils.customToast(DetailActivity.this, "宠物ID为空");
            } else {
                searchWeight(token, pet_Id.trim());
            }
        } else {
            countAll(token, deviceId);
        }

        TextView tv_title_exercise = findViewById(R.id.tv_title_exercise);
        TextView tv_title_cal = findViewById(R.id.tv_title_cal);
        tv_title_exercise.setSelected(true);
        tv_title_cal.setSelected(false);
        tv_title_exercise.setOnClickListener(view -> {
            tv_title_exercise.setSelected(true);
            tv_title_cal.setSelected(false);
            title.setText("运动数据");
            title1.setText("昨日运动量");
            tv_seven_history.setText("近七日运动量");
            updateView(mExtendParamDesc, 5);
            type = 5;
            if (mListHistory1 != null) {
                mAdapter.setList(mListHistory1);
            }
        });
        tv_title_cal.setOnClickListener(view -> {
            tv_title_exercise.setSelected(false);
            tv_title_cal.setSelected(true);
            title.setText("消耗数据");
            title1.setText("昨日消耗量");
            tv_seven_history.setText("近七日消耗量");
            updateView(mExtendParamDesc, 6);
            type = 6;
            if (mListHistory2 != null) {
                mAdapter.setList(mListHistory2);
            }
        });

    }

    // 生成0.1增加的数据
    private List<String> generateDecimalData(double minValue, double maxValue) {
        List<String> data = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("0.0");

        for (double i = minValue; i <= maxValue; i += 0.1) {
            data.add(decimalFormat.format(i));
        }

        return data;
    }


    @Override
    public int getContentView() {
        return R.layout.activity_detail;
    }


    private void countAll(String token, String deviceId) {//todo pet update
        RetrofitUtils.getRetrofitService().countAll(token, 0, deviceId)
                .filter(new ResultFunction())
                .subscribeOn(Schedulers.io())//todo add edit
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RemoteResult<List<CountResponse>>>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull RemoteResult<List<CountResponse>> result) {
                        Log.d("7777", "onNext: " + result.data.toString());
                        Log.d("7777", "onNext: " + result.data.get(0).toString());
                        mListHistory1 = getHistoryList(result.data, type);
                        mAdapter.setList(mListHistory1);
                        updateView(mExtendParamDesc, type);
                        if (type == 5 || type == 6) {
                            mListHistory2 = getHistoryList(result.data, 6);
                        }
                        //Log.d("7777", "onNext: " + result.data.get(0).get("2024-02-24").get("extendParamDesc"));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("7777", "onError: ");
                        String message = ExceptionHandle.handleException(e).message;
                        if (message.equals("invalid_token")) {
                            ConfigPreferences.setLoginName(DetailActivity.this, "");
                            ConfigPreferences.setLoginToken(DetailActivity.this, "");
                            startActivity(new Intent(DetailActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            ToastUtils.customToast(DetailActivity.this, message);
                        }

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void searchWeight(String token, String pet_Id) {//todo pet update
        Log.d("1111", "searchWeight pet_Id----" + pet_Id);
        RetrofitUtils.getRetrofitService().searchWeight(token, 7, pet_Id)
                .filter(new ResultFunction())
                .subscribeOn(Schedulers.io())//todo add edit
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RemoteResult<WeightSearchResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull RemoteResult<WeightSearchResponse> result) {
                        Log.d("7777", "onNext WeightResponse: " + result.data.records.size());
                        List<HistoryItem> list = getWeightHistoryList(result.data.records);
                        mAdapter.setList(list);
                        updateView(mExtendParamDesc, type);
                        //Log.d("7777", "onNext: " + result.data.get(0).get("2024-02-24").get("extendParamDesc"));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("7777", "onError: ");
                        String message = ExceptionHandle.handleException(e).message;
                        if (message.equals("invalid_token")) {
                            ConfigPreferences.setLoginName(DetailActivity.this, "");
                            ConfigPreferences.setLoginToken(DetailActivity.this, "");
                            startActivity(new Intent(DetailActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            ToastUtils.customToast(DetailActivity.this, message);
                        }

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void addWeight(String token, PetWeightRequest request) {//todo pet update
        Log.d("99999", "addWeight: ");
        RetrofitUtils.getRetrofitService().addWeight(token, request)
                .filter(new ResultFunction())
                .subscribeOn(Schedulers.io())//todo add edit
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
                        String message = ExceptionHandle.handleException(e).message;
                        if (message.equals("invalid_token")) {
                            ConfigPreferences.setLoginName(DetailActivity.this, "");
                            ConfigPreferences.setLoginToken(DetailActivity.this, "");
                            startActivity(new Intent(DetailActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            ToastUtils.customToast(DetailActivity.this, message);
                        }

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    private List<HistoryItem> getHistoryList(List<CountResponse> result, int type) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");

        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<HistoryItem> list = new ArrayList<>();
        for (CountResponse item : result) {
            String date = "";
            try {
                Date inputDate = inputFormat.parse(item.createdTime);
                date = outputFormat.format(inputDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String data = extractData(item.extendParamDesc, type);
            HistoryItem historyItem = new HistoryItem(date, data);
            list.add(historyItem);
        }
        return list;
    }


    private List<HistoryItem> getWeightHistoryList(List<WeightResponse> result) {
        List<HistoryItem> list = new ArrayList<>();
        for (WeightResponse item : result) {
            HistoryItem historyItem = new HistoryItem(item.recordDate, item.weight + " kg");
            list.add(historyItem);
        }
        return list;
    }

    private String extractData(String extendParamDesc, int type) {
        double maxXValue = DataUtils.extractXYZ(extendParamDesc);
        String petPosture = "未知";
        String respiratorRate = "未知";
        String heartRate = "未知";
        if (maxXValue < 1000) {
            petPosture = "休息中";
            respiratorRate = "10-20 次/分";
            heartRate = "60-100 次/秒";
        } else if (maxXValue < 2000 || maxXValue >= 1000) {
            petPosture = "行走中";
            String respiratorRate1 = "20-30 次/分";
            String respiratorRate2 = "30-40 次/分";
            String heartRate1 = "100-140 次/秒";
            String heartRate2 = "140-160 次/秒";
            Random random = new Random();
            int index = random.nextInt(2);
            if (index == 0) {
                respiratorRate = respiratorRate1;
                heartRate = heartRate1;
            } else {
                respiratorRate = respiratorRate2;
                heartRate = heartRate2;
            }
        } else {
            petPosture = "运动中";
            respiratorRate = ">40 次/分";
            heartRate = ">160 次/秒";
        }
        switch (type) {
            case 1:
                return "平均心率" + respiratorRate;
            case 2:
                return "未知";
            case 3:
                return heartRate;
            case 4:
                double tempature = DataUtils.extractTemperatures(extendParamDesc);
                DecimalFormat decimalFormat = new DecimalFormat("#.#");
                return "平均体温" + decimalFormat.format(tempature) + "°C";
            case 5:
                String todayKm = DataUtils.extractVTodayKm(extendParamDesc);
                return "运动量" + todayKm + " 千米";
            case 6:
                String todayKm1 = DataUtils.extractVTodayKm(extendParamDesc);
                double todayKm2 = Double.parseDouble(todayKm1);
                int todayCalorieConsumption = (int) (2.5 * todayKm2 * 1.063);
                return "消耗" + todayCalorieConsumption + " 卡路里";
            case 7:
                return "未知";
            default:
                return "";
        }
    }

    private void updateView(String extendParamDesc, int type) {
        TextView tv_data = findViewById(R.id.tv_data);
        //心率，呼吸
        double maxXValue = DataUtils.extractXYZ(extendParamDesc);
        String petPosture = "未知";
        String respiratorRate = "未知";
        String heartRate = "未知";
        if (maxXValue < 1000) {
            petPosture = "休息中";
            respiratorRate = "10-20 次/分";
            heartRate = "60-100 次/秒";
        } else if (maxXValue < 2000 || maxXValue >= 1000) {
            petPosture = "行走中";
            String respiratorRate1 = "20-30 次/分";
            String respiratorRate2 = "30-40 次/分";
            String heartRate1 = "100-140 次/秒";
            String heartRate2 = "140-160 次/秒";
            Random random = new Random();
            int index = random.nextInt(2);
            if (index == 0) {
                respiratorRate = respiratorRate1;
                heartRate = heartRate1;
            } else {
                respiratorRate = respiratorRate2;
                heartRate = heartRate2;
            }
        } else {
            petPosture = "运动中";
            respiratorRate = ">40 次/分";
            heartRate = ">160 次/秒";
        }
        switch (type) {
            case 1:
                tv_data.setText(getTextSizeSpan(respiratorRate, 0, respiratorRate.length() - 3));
                break;
            case 2:
                tv_data.setText("未知");
                break;
            case 3:
                tv_data.setText(getTextSizeSpan(heartRate, 0, heartRate.length() - 3));
                break;
            case 4:
                double tempature = DataUtils.extractTemperatures(extendParamDesc);
                DecimalFormat decimalFormat = new DecimalFormat("#.#");
                String formattedTemperature = decimalFormat.format(tempature);
                String temperature = formattedTemperature + " 摄氏度";
                tv_data.setText(getTextSizeSpan(temperature, 0, temperature.length() - 3));
                break;
            case 5:
                //当日里程
                String todayKm = DataUtils.extractVTodayKm(extendParamDesc);
                String fullTodayKm = todayKm + " 千米";
                tv_data.setText(getTextSizeSpan(fullTodayKm, 0, fullTodayKm.length() - 2));
                break;
            case 6:
                //卡路里
                String todayKm1 = DataUtils.extractVTodayKm(extendParamDesc);
                double todayKm2 = Double.parseDouble(todayKm1);
                int todayCalorieConsumption = (int) (2.5 * todayKm2 * 1.063);
                String fullTodayCalorieConsumption = todayCalorieConsumption + " 卡路里";
                tv_data.setText(getTextSizeSpan(fullTodayCalorieConsumption, 0, fullTodayCalorieConsumption.length() - 3));
                break;
            default:
                break;
        }

    }

    private SpannableStringBuilder getTextSizeSpan(String fullText, int start, int end) {

        // Create a SpannableString
        SpannableStringBuilder builder = new SpannableStringBuilder(fullText);

        builder.setSpan(new AbsoluteSizeSpan(36, true), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Set the size of the "km" part
        builder.setSpan(new AbsoluteSizeSpan(20, true), end, fullText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }


}