package com.example.petproject;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.petproject.base.BaseActivity;
import com.example.petproject.bean.AvatarResponse;
import com.example.petproject.bean.Breed;
import com.example.petproject.bean.BreedRecord;
import com.example.petproject.bean.PetRequest;
import com.example.petproject.bean.PetResponse;
import com.example.petproject.bean.RemoteResult;
import com.example.petproject.customview.CircularImageView;
import com.example.petproject.dialog.AvatorFragment;
import com.example.petproject.dialog.PetTypeBottomSheetFragment;
import com.example.petproject.dialog.SureCancelDialog;
import com.example.petproject.retrofit.ResultFunction;
import com.example.petproject.retrofit.RetrofitUtils;
import com.example.petproject.utils.ConfigPreferences;
import com.example.petproject.utils.ExceptionHandle;
import com.example.petproject.utils.ToastUtils;
import com.example.petproject.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddPetActivity extends BaseActivity {
    private static final String TAG = "AddPetActivity";
    private PetTypeBottomSheetFragment fragmentType;
    private PetTypeBottomSheetFragment fragmentGender;
    private PetTypeBottomSheetFragment fragmentBreed;
    private PetTypeBottomSheetFragment fragmentCut;
    private PetTypeBottomSheetFragment fragmentVaccine;
    private CircularImageView imageView;
    String avatar = "";
    private String name = "";
    private String weight = "";
    private int indexType = 0;
    private int indexGender = 0;
    private int indexCut = 0;
    private int indexVaccine = 0;
    private int indexBreed = 0;
    private List<Breed> listBreed = new ArrayList<>();
    private String selectedDate = "";
    private String id;
    private String breed;
    private String breedId;
    private SureCancelDialog mDialog;
    private PetResponse petRequest;
    String[] breedArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialog = SureCancelDialog.newInstance("是否删除宠物", "取消", "确定");
        mDialog.setOnCancelListener(new SureCancelDialog.OnSureCancelListener() {
            @Override
            public void onCancel() {
                mDialog.dismiss();
            }

            @Override
            public void onSureListener(String text) {
                String token = "Bearer " + ConfigPreferences.login_token(AddPetActivity.this);
                petDelete(token);
            }
        });
        Intent intent = getIntent();
        if (intent != null) {
            petRequest = (PetResponse) intent.getParcelableExtra("bean");
            if (petRequest != null) {
                name = petRequest.name;
                weight = petRequest.weight;
                indexType = petRequest.type;
                indexGender = petRequest.gender;
                indexCut = petRequest.isSpayed;
                indexVaccine = petRequest.isVaccinated;
                selectedDate = petRequest.birth;
                avatar = petRequest.avatar;
                id = petRequest.id;
                breed = petRequest.breed;
                breedId = petRequest.breedId;
                // 在这里使用 petRequest 对象
                findViewById(R.id.iv_delete).setVisibility(View.VISIBLE);
                findViewById(R.id.iv_delete).setOnClickListener(view -> {
                    mDialog.show(getSupportFragmentManager(), "delete");
                });
            }
        }
        imageView = findViewById(R.id.pet_avator);
        Log.d(TAG, "pet_avator: " + avatar);
        String avatarUrl = "http://47.94.99.63:8088/pet/download/" + avatar;
        String token = "Bearer " + ConfigPreferences.login_token(AddPetActivity.this);

        GlideUrl glideUrl = new GlideUrl(avatarUrl, new LazyHeaders.Builder()
                .addHeader("Authorization", token)  // 如果你使用 Bearer Token
                .build());

        Glide.with(AddPetActivity.this).load(glideUrl).into(imageView);


        initDialogFragment();
//        findViewById(R.id.cl_bar_back).setBackgroundColor(Color.parseColor("#FFC0CB"));
        findViewById(R.id.cl_birth).setOnClickListener(view -> {
            showDatePickerDialog();
        });
        findViewById(R.id.cl_type).setOnClickListener(view -> {
            fragmentType.show(getSupportFragmentManager(), fragmentType.getTag());
            //String token = "Bearer " + ConfigPreferences.login_token(AddPetActivity.this);
            //breedSearch(token);
        });
        findViewById(R.id.cl_breed).setOnClickListener(view -> {
            fragmentBreed.setDefaultSelectedIndex(findBreedIndex(breed, breedArray));
            fragmentBreed.show(getSupportFragmentManager(), fragmentBreed.getTag());

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
        TextView tv_breed = findViewById(R.id.tv_breed);
        tv_breed.setText(breed);
        TextView tv_type = findViewById(R.id.tv_type);
        if (indexType == 0) {
            tv_type.setText("狗狗");
        } else {
            tv_type.setText("猫猫");
        }
        TextView tv_gender = findViewById(R.id.tv_gender);
        if (indexGender == 0) {
            tv_gender.setText("男生");
        } else {
            tv_gender.setText("女生");
        }
        TextView tv_cut = findViewById(R.id.tv_cut);
        if (indexType == 0) {
            tv_cut.setText("是");
        } else {
            tv_cut.setText("否");
        }
        TextView tv_vaccine = findViewById(R.id.tv_vaccine);
        if (indexType == 0) {
            tv_vaccine.setText("是");
        } else {
            tv_vaccine.setText("否");
        }
        TextView tv_birth = findViewById(R.id.tv_birth);
        tv_birth.setText(selectedDate);
        EditText et_name = findViewById(R.id.et_name);
        et_name.setText(name);
        EditText et_weight = findViewById(R.id.et_weight);
        et_weight.setText(weight);
        ImageButton button = findViewById(R.id.iv_back);
        button.setOnClickListener(v -> {
            onBackPressed();
        });
        TextView title = findViewById(R.id.tv_bar_title);
        title.setText("添加宠物");
        imageView = findViewById(R.id.pet_avator);
        imageView.setOnClickListener(view -> {
            if (checkPermission()) {
                showBottomSheet();
            }
        });
        findViewById(R.id.btn_save).setOnClickListener(view -> {
            String name = et_name.getText().toString();
            if (TextUtils.isEmpty(name)) {
                ToastUtils.customToast(AddPetActivity.this, "请输入宠物名字");
                return;
            }
            String weight = et_weight.getText().toString();
            if (TextUtils.isEmpty(weight)) {
                ToastUtils.customToast(AddPetActivity.this, "请输入宠物体重");
                return;
            }
            breed = listBreed.get(indexBreed).breed;
            breedId = listBreed.get(indexBreed).id;
            PetRequest request = new PetRequest(avatar, selectedDate
                    , breed, breedId, indexGender, id, indexCut, indexVaccine, name, indexType, indexType + "", weight);
            if (petRequest == null) {
                petInsert(token, request);
            } else {
                petUpdate(token, request);
            }
        });
    }

    public int findBreedIndex(String breed, String[] breedArray) {
        if (breed == null || breedArray == null) {
            return 0;
        }
        for (int i = 0; i < breedArray.length; i++) {
            if (breedArray[i].equals(breed)) {
                return i;  // 找到匹配项，返回索引
            }
        }
        return 0;  // 如果没有找到匹配项，返回 0
    }

    public String[] permissions = new String[]{Manifest.permission.CAMERA};

    private boolean checkPermission() {
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, 1);
                return false;
            }
        }
        return true;
    }

    private void petInsert(String token, PetRequest request) {//todo pet update
        Log.d(TAG, "petInsert:" + request.toString());
        RetrofitUtils.getRetrofitService().petInsert(token, request)
                .filter(new ResultFunction())
                .subscribeOn(Schedulers.io())//todo add edit
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RemoteResult<Object>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull RemoteResult<Object> result) {
                        ToastUtils.customToast(AddPetActivity.this, "添加成功");
                        Intent intentResult = getIntent();
                        int type = intentResult.getIntExtra("type", -1);
                        if (type == 1) {
                            Intent resultIntent = new Intent();
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        } else {
                            Intent intent = new Intent(AddPetActivity.this, PetActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        finish();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        ExceptionHandle.ResponeThrowable responeThrowable = ExceptionHandle.handleException(e);
                        if (responeThrowable.code.equals("020000")) {
                            ConfigPreferences.setLoginName(AddPetActivity.this, "");
                            ConfigPreferences.setLoginToken(AddPetActivity.this, "");
                            startActivity(new Intent(AddPetActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            ToastUtils.customToast(AddPetActivity.this, responeThrowable.message);
                        }

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void breedSearch(String token) {//todo pet update
        RetrofitUtils.getRetrofitService().breedSearch(token, indexType)
                .filter(new ResultFunction())
                .subscribeOn(Schedulers.io())//todo add edit
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RemoteResult<BreedRecord>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull RemoteResult<BreedRecord> result) {
                        if (result.data.records == null) {
                            return;
                        }
                        listBreed = result.data.records;
                        int size = result.data.records.size();
                        breedArray = new String[size];

                        for (int i = 0; i < size; i++) {
                            breedArray[i] = result.data.records.get(i).breed;  // 获取 breed 字段
                        }
                        fragmentBreed.setOptions(breedArray);
                        Log.d(TAG, "breedArray: " + Arrays.toString(breedArray));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        ExceptionHandle.ResponeThrowable responeThrowable = ExceptionHandle.handleException(e);
                        if (responeThrowable.code.equals("020000")) {
                            ConfigPreferences.setLoginName(AddPetActivity.this, "");
                            ConfigPreferences.setLoginToken(AddPetActivity.this, "");
                            startActivity(new Intent(AddPetActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            ToastUtils.customToast(AddPetActivity.this, responeThrowable.message);
                        }

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void petUpdate(String token, PetRequest request) {//todo pet update
        RetrofitUtils.getRetrofitService().petUpdate(token, request)
                .filter(new ResultFunction())
                .subscribeOn(Schedulers.io())//todo add edit
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RemoteResult<Object>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull RemoteResult<Object> result) {
                        ToastUtils.customToast(AddPetActivity.this, "更新成功");
                        Intent intentResult = getIntent();
                        int type = intentResult.getIntExtra("type", -1);
                        if (type == 1) {
                            Intent resultIntent = new Intent();
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        } else {
                            Intent intent = new Intent(AddPetActivity.this, PetActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        finish();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        ExceptionHandle.ResponeThrowable responeThrowable = ExceptionHandle.handleException(e);
                        if (responeThrowable.code.equals("020000")) {
                            ConfigPreferences.setLoginName(AddPetActivity.this, "");
                            ConfigPreferences.setLoginToken(AddPetActivity.this, "");
                            startActivity(new Intent(AddPetActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            ToastUtils.customToast(AddPetActivity.this, responeThrowable.message);
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void petDelete(String token) {
        RetrofitUtils.getRetrofitService().petDelete(token, id)
                .filter(new ResultFunction())
                .subscribeOn(Schedulers.io())//todo add edit
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RemoteResult<Object>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull RemoteResult<Object> result) {
                        ToastUtils.customToast(AddPetActivity.this, "删除成功");
                        Intent intent = new Intent(AddPetActivity.this, PetActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        ExceptionHandle.ResponeThrowable responeThrowable = ExceptionHandle.handleException(e);
                        if (responeThrowable.code.equals("020000")) {
                            ConfigPreferences.setLoginName(AddPetActivity.this, "");
                            ConfigPreferences.setLoginToken(AddPetActivity.this, "");
                            startActivity(new Intent(AddPetActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            ToastUtils.customToast(AddPetActivity.this, responeThrowable.message);
                        }
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
        String token = "Bearer " + ConfigPreferences.login_token(AddPetActivity.this);
        breedSearch(token);
        fragmentType = new PetTypeBottomSheetFragment();
        fragmentType.setDefaultSelectedIndex(indexType);
        String[] petTypes = {"狗狗", "猫猫"};
        fragmentType.setOptions(petTypes);
        fragmentType.setOnItemSelectedListener((selectedItem, index) -> {
            indexType = index;
            TextView tv_type = findViewById(R.id.tv_type);
            tv_type.setText(selectedItem);
            breedSearch(token);
        });

        fragmentBreed = new PetTypeBottomSheetFragment();
        fragmentBreed.setDefaultSelectedIndex(findBreedIndex(breed, breedArray));
        fragmentBreed.setOnItemSelectedListener((selectedItem, index) -> {
            indexBreed = index;
            TextView tv_breed = findViewById(R.id.tv_breed);
            tv_breed.setText(selectedItem);
        });


        fragmentGender = new PetTypeBottomSheetFragment();
        fragmentGender.setDefaultSelectedIndex(indexGender);
        String[] genders = {"男生", "女生"};
        fragmentGender.setOptions(genders);
        fragmentGender.setOnItemSelectedListener((selectedItem, index) -> {
            indexGender = index;
            TextView tv_gender = findViewById(R.id.tv_gender);
            tv_gender.setText(selectedItem);
        });

        fragmentCut = new PetTypeBottomSheetFragment();
        fragmentCut.setDefaultSelectedIndex(indexCut);
        String[] cuts = {"是", "否"};
        fragmentCut.setOptions(cuts);
        fragmentCut.setOnItemSelectedListener((selectedItem, index) -> {
            indexCut = index;
            TextView tv_cut = findViewById(R.id.tv_cut);
            tv_cut.setText(selectedItem);
        });

        fragmentVaccine = new PetTypeBottomSheetFragment();
        fragmentVaccine.setDefaultSelectedIndex(indexVaccine);
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
                    //selectedDate = selectedYear + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    selectedDate = Utils.formatDate(selectedYear, monthOfYear, dayOfMonth);
                    Log.d(TAG, "onDateSet: " + selectedDate);
                    TextView tv_birth = findViewById(R.id.tv_birth);
                    tv_birth.setText(selectedDate);
                },
                year,
                month,
                day
        );
        if (!TextUtils.isEmpty(selectedDate)) {
            String[] parts = selectedDate.split("-");

            int year1 = Integer.parseInt(parts[0]);  // 年：2024
            int month1 = Integer.parseInt(parts[1]) - 1;  // 月：09 （减1，因为月份从0开始）
            int day1 = Integer.parseInt(parts[2]);  // 日：14
            datePickerDialog.updateDate(year1, month1, day1);
        }
        // 显示日期选择对话框
        datePickerDialog.show();
    }


    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

    private void showBottomSheet() {
        AvatorFragment fragment = new AvatorFragment();
        fragment.setOnItemSelectedListener(index -> {
            Log.d(TAG, "showBottomSheet: " + index);
            if (index == 0) {
                dispatchTakePictureIntent();
            } else {
                dispatchPickPictureIntent();
            }
        });
        fragment.show(getSupportFragmentManager(), fragment.getTag());
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Log.d(TAG, "dispatchTakePictureIntent: " + takePictureIntent);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            ToastUtils.customToast(AddPetActivity.this, "未找到相机应用");
        }
    }

    private void dispatchPickPictureIntent() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        startActivityForResult(pickIntent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImage = data.getData();
            Bitmap selectedBitmap = getBitmapFromUri(selectedImage);
            if (selectedBitmap == null) {
                ToastUtils.customToast(AddPetActivity.this, "图片选择失败");
                return;
            }
            Bitmap compressBitmap = compressBitmap(selectedBitmap);
            imageView.setImageBitmap(compressBitmap);
            uploadPetImage(getImageUri(getApplicationContext(), compressBitmap));
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Bitmap compressBitmap = compressBitmap(imageBitmap);
            imageView.setImageBitmap(compressBitmap);
            uploadPetImage(getImageUri(getApplicationContext(), compressBitmap));
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            // Load bitmap from the URI
            return MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap compressBitmap(Bitmap bitmap) {
        // 设置压缩参数
        int maxWidth = 300;
        int maxHeight = 300;

        // 获取原始图像的宽高
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();

        // 计算缩放比例
        float scale = Math.min((float) maxWidth / originalWidth, (float) maxHeight / originalHeight);

        // 创建Matrix进行缩放
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        // 使用Matrix进行缩放
        return Bitmap.createBitmap(bitmap, 0, 0, originalWidth, originalHeight, matrix, true);
    }

    // 将Bitmap转换为Uri
    private Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    // 上传图片
    private void uploadPetImage(Uri imageUri) {
        File file = new File(getRealPathFromURI(imageUri));
        Log.d(TAG, "uploadImage: " + file.exists());
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        String token = "Bearer " + ConfigPreferences.login_token(AddPetActivity.this);
        RetrofitUtils.getRetrofitService().uploadPetImage(token, body)
                .filter(new ResultFunction())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RemoteResult<AvatarResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull RemoteResult<AvatarResponse> result) {
                        avatar = result.data.fileName;
                        //Glide.with(AddPetActivity.this).load(result.data.fileDownloadUri).into(imageView);

                        String avatarUrl = "http://47.94.99.63:8088/pet/download/" + avatar;
                        String token = "Bearer " + ConfigPreferences.login_token(AddPetActivity.this);

                        GlideUrl glideUrl = new GlideUrl(avatarUrl, new LazyHeaders.Builder()
                                .addHeader("Authorization", token)  // 如果你使用 Bearer Token
                                .build());

                        Glide.with(AddPetActivity.this).load(glideUrl).into(imageView);


                        ToastUtils.customToast(AddPetActivity.this, "上传成功");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        ExceptionHandle.ResponeThrowable responeThrowable = ExceptionHandle.handleException(e);
                        if (responeThrowable.code.equals("020000")) {
                            ConfigPreferences.setLoginName(AddPetActivity.this, "");
                            ConfigPreferences.setLoginToken(AddPetActivity.this, "");
                            startActivity(new Intent(AddPetActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            ToastUtils.customToast(AddPetActivity.this, responeThrowable.message);
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor == null) {
            return null;
        }
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(columnIndex);
        Log.d(TAG, "getRealPathFromURI: " + filePath);
        cursor.close();
        return filePath;
    }

}