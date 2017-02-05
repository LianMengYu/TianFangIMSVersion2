package com.tianfangIMS.im.activity;

import android.os.Bundle;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.tianfangIMS.im.R;

/**
 * Created by LianMengYu on 2017/1/22.
 */
public class AMapForPreviewActivity extends BaseActivity implements AMapLocationListener {
    private MapView mapView = null;
    private AMap aMap;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;


    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    double locationType = aMapLocation.getLocationType();
                    double latitude = aMapLocation.getLatitude();//获取纬度
                    mLocationListener.onLocationChanged(aMapLocation);
                    Log.e("aaaaaaaaaaaaaaaaaa", "定位坐标：" + latitude);
                } else {
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.amapforpreview_activity);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        if (aMap == null) {
            aMap = mapView.getMap();
        }
//        aMap.setTrafficEnabled(true);// 显示实时交通状况

        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);

        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

        mLocationOption.setOnceLocationLatest(true);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
