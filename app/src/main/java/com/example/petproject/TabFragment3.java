package com.example.petproject;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.example.petproject.bean.JsonRequest;
import com.example.petproject.utils.ToastUtils;
import com.google.gson.Gson;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class TabFragment3 extends Fragment implements LocationSource,
        AMapLocationListener {
    private static final String TAG = "TabFragment3";
    private View view;
    private MapView mapView;
    private AMap aMap;
    private AMapLocation amapLocation;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    WebSocketClient mWebSocketClient;
    private double longitude = -1;
    private double latitude = -1;
    private Marker marker;

    public TabFragment3() {
        // Required empty public constructor
    }

    public static TabFragment3 newInstance(int id) {
        TabFragment3 fragment = new TabFragment3();
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
        view = inflater.inflate(R.layout.fragment_tab3, container, false);
        view.findViewById(R.id.cl_location).setOnClickListener(view -> {
            Log.d(TAG, "onCreateView: " + amapLocation);
            if (amapLocation != null) {
                Log.d(TAG, "btn_locate: ");
                aMap.setMyLocationEnabled(true);
                mHandler.removeCallbacks(mRunnable);
                ToastUtils.continuousToast(getContext(), "宠物定位中...");
                mHandler.postDelayed(mRunnable, 3000);
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(amapLocation.getLatitude() + 0.0001, amapLocation.getLongitude() + 0.0001), 15)); // yourLatitude和yourLongitude是定位得到的经纬度信息
            } else {
                ToastUtils.continuousToast(getContext(), "设备定位中请稍后...");
            }
        });

        view.findViewById(R.id.cl_location_device).setOnClickListener(view -> {
            Log.d(TAG, "onCreateView: " + amapLocation);
            if (amapLocation != null) {
                aMap.setMyLocationEnabled(true);
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()), 15)); // yourLatitude和yourLongitude是定位得到的经纬度信息
                mHandler.removeCallbacks(mRunnable);
                ToastUtils.continuousToast(getContext(), "本机定位中...");
            } else {
                ToastUtils.continuousToast(getContext(), "设备定位中请稍后...");
            }
        });

        view.findViewById(R.id.cl_only).setOnClickListener(view -> {
            ToastUtils.continuousToast(getContext(), "功能升级中");
        });
        view.findViewById(R.id.cl_market).setOnClickListener(view -> {
            ToastUtils.continuousToast(getContext(), "功能升级中");
        });
        view.findViewById(R.id.cl_fence).setOnClickListener(view -> {
            ToastUtils.continuousToast(getContext(), "功能升级中");
        });
        view.findViewById(R.id.cl_find).setOnClickListener(view -> {
            ToastUtils.continuousToast(getContext(), "功能升级中");
        });

        mapView = (MapView) view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
        return view;
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            setPosition(latitude, longitude);
            ToastUtils.continuousToast(getContext(), "定位成功");
        }
    };

    private Runnable mRunnable2 = new Runnable() {
        @Override
        public void run() {
            startWebSocket();
        }
    };

    /**
     * 初始化AMap对象
     */
    private void init() {
        aMap = mapView.getMap();
        setUpMap();
        //startWebSocket();
        mHandler.removeCallbacks(mRunnable2);
        mHandler.postDelayed(mRunnable2, 2000);//先显示定位蓝点后，再定位宠物
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        // 自定义系统定位小蓝点
        AMapLocationClient.updatePrivacyAgree(getContext(), true);
        AMapLocationClient.updatePrivacyShow(getContext(), true, true);
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        //myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);//定位一次，且将视角移动到地图中心点。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动。

        //myLocationStyle.interval(2000);//设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.mipmap.location_marker));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
//        myLocationStyle.anchor(0.5f,0.5f);//设置小蓝点的锚点
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        myLocationStyle.showMyLocation(true);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);// aMap.setLocationSource(this)中包含两个回调，activate(OnLocationChangedListener)和deactivate()。
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        // 去除缩放按钮
        aMap.getUiSettings().setZoomControlsEnabled(false);
        // 去除定位按钮
        aMap.getUiSettings().setMyLocationButtonEnabled(false);
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false
        // aMap.setMyLocationType()
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if(null != mlocationClient){
            mlocationClient.onDestroy();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        Log.d(TAG, "onLocationChanged: ");
        if (mListener != null && amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                this.amapLocation = amapLocation;
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                //Log.d("1111", amapLocation.toString());
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                //Log.e("1111",errText);
            }
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        Log.d(TAG, "activate: ");
        mListener = listener;
        if (mlocationClient == null) {

            try {
                mlocationClient = new AMapLocationClient(getContext());
            } catch (Exception e) {
                e.printStackTrace();
            }
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除

            mlocationClient.startLocation();//启动定位
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    public void startWebSocket() {
        mWebSocketClient = new WebSocketClient(URI.create("ws://139.186.13.82:3003/terminal/realtime")) {

            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                // 连接成功时的处理
                if (mWebSocketClient != null) {
                    JsonRequest request = new JsonRequest();
                    request.setRequestType(1);

                    JsonRequest.Data data = new JsonRequest.Data();
                    data.setTerminalID("10069096400");
                    data.setSubscribe(true);

                    request.setData(data);
                    List<JsonRequest> jsonRequests = new ArrayList<>();
                    jsonRequests.add(request);
                    // 初始化 Gson 对象
                    Gson gson = new Gson();

                    // 将对象转换为 JSON 字符串
                    String jsonString = gson.toJson(jsonRequests);
                    mWebSocketClient.send(jsonString);
                }
            }

            @Override
            public void onMessage(String s) {
                Log.d(TAG, "onMessage1111: " + s);
                try {
                    // 解析 JSON 数组
                    JSONArray jsonArray = new JSONArray(s);
                    Log.d(TAG, "1111: " + jsonArray.length());
                    // 遍历 JSON 数组
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        // 获取 JSON 对象中的字段
                        JSONObject dataObject = jsonObject.getJSONObject("Data");
                        longitude = dataObject.getDouble("Longitude");
                        latitude = dataObject.getDouble("Latitude");
                        Log.d(TAG, "1111: " + "Longitude: " + longitude + ", Latitude: " + latitude);
                        // 输出字段值
                        setPosition(latitude, longitude);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                // 连接关闭时的处理
            }

            @Override
            public void onError(Exception e) {
                // 连接失败时的处理
            }
        };

        mWebSocketClient.connect();
    }


    private void setPosition(double latitude, double longitude) {
        if (latitude == -1 || longitude == -1) {
            ToastUtils.customToast(getContext(), "等待设备连接，请稍后");
            return;
        }
        CoordinateConverter converter = new CoordinateConverter(getContext());
// CoordType.GPS 待转换坐标类型
        converter.from(CoordinateConverter.CoordType.GPS);
// sourceLatLng待转换坐标点 LatLng类型
        LatLng latLng = new LatLng(latitude, longitude);
        try {
            converter.coord(new DPoint(latLng.latitude, latLng.longitude));
// 执行转换操作
            DPoint dPoint = converter.convert();
            // 创建经纬度对象
            // 在指定的经纬度位置添加标记
            if (marker != null) {
                marker.remove();
            }
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(dPoint.getLatitude(), dPoint.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.dog_icon)); // 设置自定义图标资源;
            marker = aMap.addMarker(markerOptions);
            // for (Marker marker : aMap.getMapScreenMarkers()) {
            //    marker.remove();
            // }
            // 将地图视图移动到指定的经纬度位置
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15)); // 设置缩放级别为 15
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}