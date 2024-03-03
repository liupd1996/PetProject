package com.example.petproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.petproject.base.BaseActivity;
import com.example.petproject.bean.AvatarResponse;
import com.example.petproject.bean.LoginResponse;
import com.example.petproject.bean.RegisterRequest;
import com.example.petproject.bean.RemoteResult;
import com.example.petproject.bean.UserEditRequest;
import com.example.petproject.customview.CircularImageView;
import com.example.petproject.dialog.AvatorFragment;
import com.example.petproject.dialog.PetTypeBottomSheetFragment;
import com.example.petproject.retrofit.ResultFunction;
import com.example.petproject.retrofit.RetrofitUtils;
import com.example.petproject.utils.ConfigPreferences;
import com.example.petproject.utils.ExceptionHandle;
import com.example.petproject.utils.ToastUtils;
import com.example.petproject.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UserCenterActivity extends BaseActivity {
    private static final String TAG = "UserCenterActivity";
    private TextView mTvGender;
    private EditText mTvName;
    private PetTypeBottomSheetFragment fragmentGender;
    private int indexGender = -1;
    private String avatar = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    public int getContentView() {
        return R.layout.activity_user_center;
    }

    private void initView() {
        mTvName = findViewById(R.id.tv_name);
        mTvGender = findViewById(R.id.tv_gender);
        imageView = findViewById(R.id.iv_head);
        imageView.setOnClickListener(view -> {
            if (checkPermission()) {
                showBottomSheet();
            }
        });
        TextView title = findViewById(R.id.tv_bar_title);
        title.setText("个人中心");

        fragmentGender = new PetTypeBottomSheetFragment();
        String[] genders = {"男生", "女生"};
        fragmentGender.setOptions(genders);
        fragmentGender.setOnItemSelectedListener((selectedItem, index) -> {
            indexGender = index;
            mTvGender.setText(selectedItem);
        });

        findViewById(R.id.cl_title_bar).setBackgroundColor(Color.WHITE);
        findViewById(R.id.iv_back).setOnClickListener(v -> {
            onBackPressed();
        });
        findViewById(R.id.cl_gender).setOnClickListener(view -> {
            fragmentGender.show(getSupportFragmentManager(), fragmentGender.getTag());
        });

        Intent intentResult = getIntent();
        int type = intentResult.getIntExtra("type", -1);//type == 1 ,编辑个人信息
        Button btn_register = findViewById(R.id.btn_register);
        if (type == 1) {
            btn_register.setText("完成");
            mTvName.setText(ConfigPreferences.name(UserCenterActivity.this));
            String gender = ConfigPreferences.gender(UserCenterActivity.this);
            mTvGender.setText(gender);
            if (gender.equals("男")) {
                indexGender = 0;
            } else if (gender.equals("女")) {
                indexGender = 1;
            } else {
                indexGender = -1;
            }
            avatar = ConfigPreferences.avatar(UserCenterActivity.this);
            Glide.with(UserCenterActivity.this).load("http://47.94.99.63:8087/user/download/" + avatar).into(imageView);
        }
        btn_register.setOnClickListener(v -> {
//            if (type == 1) {
//                ToastUtils.customToast(UserCenterActivity.this,"编辑接口升级中");
//                return;
//            }
            Intent intent = getIntent();
            String phone = intent.getStringExtra("phone");
            String smsCode = intent.getStringExtra("smsCode");
            String name = mTvName.getText().toString();
            String id = ConfigPreferences._id(UserCenterActivity.this);
            if (TextUtils.isEmpty(name)) {
                ToastUtils.customToast(UserCenterActivity.this, "昵称不能为空");
                return;
            }
            if (indexGender == -1) {
                ToastUtils.customToast(UserCenterActivity.this, "请选择性别");
                return;
            }
            if (type == 1) {
                edit(indexGender, id, avatar, name);
            } else {
                register(indexGender, phone, smsCode, name);
            }
        });
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

    private void register(int gender, String phone, String smsCode, String name) {
        RetrofitUtils.getRetrofitService().register(new RegisterRequest("", gender, phone, smsCode, name))
                .filter(new ResultFunction())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RemoteResult<Object>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull RemoteResult<Object> result) {
                        login(phone, smsCode);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        ToastUtils.customToast(UserCenterActivity.this, ExceptionHandle.handleException(e).message);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void edit(int gender, String id, String avatar, String name) {
        Log.d(TAG, "edit gender: " + gender + "--id:" + id + "--avatar:" + avatar + "--name:" + name);
        String token = "Bearer " + ConfigPreferences.login_token(UserCenterActivity.this);
        RetrofitUtils.getRetrofitService().userEdit(token, new UserEditRequest(avatar, gender, id, name))
                .filter(new ResultFunction())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RemoteResult<Object>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull RemoteResult<Object> result) {
                        ToastUtils.customToast(UserCenterActivity.this, "修改成功");
                        finish();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        ToastUtils.customToast(UserCenterActivity.this, ExceptionHandle.handleException(e).message);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void login(String userName, String verify) {
        RetrofitUtils.getRetrofitService().login(userName, verify, "password", "mydog", "all", "myDog")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull LoginResponse result) {
                        ConfigPreferences.setLoginName(UserCenterActivity.this, userName);
                        ConfigPreferences.setLoginToken(UserCenterActivity.this, result.access_token);
                        ConfigPreferences.setRefreshToken(UserCenterActivity.this, result.refresh_token);
                        startActivity(new Intent(UserCenterActivity.this, MainActivity.class));
                        ToastUtils.customToast(UserCenterActivity.this, "登录成功");
                        finish();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        ToastUtils.customToast(UserCenterActivity.this, ExceptionHandle.handleException(e).message);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private CircularImageView imageView;

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
            ToastUtils.customToast(UserCenterActivity.this, "未找到相机应用");
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
                ToastUtils.customToast(UserCenterActivity.this,"图片选择失败");
                return;
            }
            Bitmap compressBitmap = compressBitmap(selectedBitmap);
            imageView.setImageBitmap(compressBitmap);
            uploadImage(getImageUri(getApplicationContext(), compressBitmap));
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Bitmap compressBitmap = compressBitmap(imageBitmap);
            imageView.setImageBitmap(compressBitmap);
            uploadImage(getImageUri(getApplicationContext(), compressBitmap));
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
    private void uploadImage(Uri imageUri) {
        File file = new File(getRealPathFromURI(imageUri));
        Log.d(TAG, "uploadImage: " + file.exists());
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        RetrofitUtils.getRetrofitService().uploadImage(body)
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
                        Glide.with(UserCenterActivity.this).load(result.data.fileDownloadUri).into(imageView);
                        ToastUtils.customToast(UserCenterActivity.this, "上传成功");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        String message = ExceptionHandle.handleException(e).message;
                        if (message.equals("invalid_token")) {
                            ConfigPreferences.setLoginName(UserCenterActivity.this, "");
                            ConfigPreferences.setLoginToken(UserCenterActivity.this, "");
                            startActivity(new Intent(UserCenterActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            ToastUtils.customToast(UserCenterActivity.this, message);
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