package com.tianfangIMS.im.location;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.tianfangIMS.im.activity.BaseActivity;
import com.tianfangIMS.im.utils.NToast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.rong.imageloader.core.DisplayImageOptions;
import io.rong.imageloader.core.ImageLoader;
import io.rong.imageloader.core.assist.LoadedFrom;
import io.rong.imageloader.core.display.CircleBitmapDisplayer;
import io.rong.imageloader.core.imageaware.ImageViewAware;
import io.rong.imkit.R;
import io.rong.imkit.plugin.location.IUserInfoProvider;
import io.rong.imkit.plugin.location.LocationManager;
import io.rong.imkit.utilities.PromptPopupDialog;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

public class AMapRealTimeActivity extends BaseActivity implements
        AMapLocationListener,
        IUserInfoProvider.UserInfoCallback {

    private static final String TAG = "AMapRealTimeActivity";

    private MapView mAMapView;
    private ViewGroup mTitleBar;
    private TextView mUserText;
    private Handler mHandler;
    private AMap mAMap;
    private Map<String, UserTarget> mUserTargetMap;
    private ArrayList<String> mParticipants;
    private boolean mHasAnimate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_location_real_time_activity);
        mHandler = new Handler();
        mUserTargetMap = new HashMap<>();
        mAMapView = (MapView) findViewById(R.id.rc_ext_amap);
        mAMapView.onCreate(savedInstanceState);
        View exitView = findViewById(R.id.rc_toolbar_close);
        exitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PromptPopupDialog dialog = PromptPopupDialog.newInstance(v.getContext(), "", getString(R.string.rc_ext_exit_location_sharing), getString(R.string.rc_ext_exit_confirm));
                dialog.setPromptButtonClickedListener(new PromptPopupDialog.OnPromptButtonClickedListener() {
                    @Override
                    public void onPositiveButtonClicked() {
                        LocationManager.getInstance().quitLocationSharing();
                        finish();
                    }
                });
                dialog.show();
            }
        });
        View closeView = findViewById(R.id.rc_toolbar_hide);
        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleBar = (ViewGroup) findViewById(R.id.rc_user_icons);
        mUserText = (TextView) findViewById(R.id.rc_user_text);

        mParticipants = getIntent().getStringArrayListExtra("participants");
        if (mParticipants == null) {
            mParticipants = new ArrayList<>();
            mParticipants.add(RongIMClient.getInstance().getCurrentUserId());
            Log.e("mParticipants","---:"+mParticipants);
        }

        initMap();
        LocationManager.getInstance().updateMyLocationInLoop(5);
    }

    private void initMap() {
        mAMap = mAMapView.getMap();
        mAMap.getUiSettings().setMyLocationButtonEnabled(false);
        mAMap.setMapType(AMap.MAP_TYPE_NORMAL);

        for (String userId : mParticipants) {
            UserTarget userTarget = createUserTargetById(userId);
            mUserTargetMap.put(userId, userTarget);
            LocationManager.getInstance().getUserInfo(userId, this);
            Log.e("mParticipants","userTarget---:"+userTarget.targetMarker.getPosition()+"--userId:"+userId);
            updateParticipantTitleText();
        }
//        CameraUpdate zoom = CameraUpdateFactory.zoomTo(17f);
//        mAMap.animateCamera(zoom, null);
    }

    @Override
    public void onGotUserInfo(UserInfo userInfo) {
        String userId = userInfo.getUserId();
        UserTarget userTarget = mUserTargetMap.get(userId);
        if (userTarget != null) {
            setAvatar(userTarget.targetView, userInfo.getPortraitUri().toString());

            View iconView = LayoutInflater.from(AMapRealTimeActivity.this).inflate(R.layout.rc_icon_rt_location_marker, null);
            ImageView imageView = (ImageView) iconView.findViewById(android.R.id.icon);
            ImageView locImageView = (ImageView) iconView.findViewById(android.R.id.icon1);
            setAvatar(imageView, userInfo.getPortraitUri().toString());
            if (userId.equals(RongIMClient.getInstance().getCurrentUserId())) {
                locImageView.setImageResource(R.drawable.rc_rt_loc_myself);
            } else {
                locImageView.setImageResource(R.drawable.rc_rt_loc_other);
            }
            userTarget.targetMarker.setIcon(BitmapDescriptorFactory.fromView(iconView));
        }
    }

    private DisplayImageOptions createDisplayImageOptions(int defaultResId) {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        if (defaultResId != 0) {
            Drawable defaultDrawable = getResources().getDrawable(defaultResId);
            builder.showImageOnLoading(defaultDrawable);
            builder.showImageForEmptyUri(defaultDrawable);
            builder.showImageOnFail(defaultDrawable);
        }
        builder.displayer(new CircleBitmapDisplayer());
        DisplayImageOptions options = builder.resetViewBeforeLoading(false)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        return options;
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    private void setAvatar(ImageView imageView, String url) {
        DisplayImageOptions options = createDisplayImageOptions(url == null ? R.drawable.rc_ext_realtime_default_avatar : 0);
        ImageViewAware imageViewAware = new ImageViewAware(imageView);
        if (url == null) {
            Drawable drawable = options.getImageForEmptyUri(null);
            try {
                Bitmap bitmap = drawableToBitmap(drawable);
                options.getDisplayer().display(bitmap, imageViewAware, LoadedFrom.DISC_CACHE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            File file = ImageLoader.getInstance().getDiskCache().get(url);
            try {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inSampleSize = 1;
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), opt);
                options.getDisplayer().display(bitmap, imageViewAware, LoadedFrom.DISC_CACHE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy()");
        mAMapView.onDestroy();
        LocationManager.getInstance().setLocationChangedListener(null);
        super.onDestroy();
    }

    private UserTarget createUserTargetById(final String userId) {
        if (mUserTargetMap.get(userId) != null) {
            return mUserTargetMap.get(userId);
        }
        UserTarget userTarget = new UserTarget();

        // set target icon view.
        userTarget.targetView = new ImageView(this);
        userTarget.targetView.setTag(userId);
        userTarget.targetView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = (String) v.getTag();
                UserTarget user = mUserTargetMap.get(userId);
                LatLng latLng = user.targetMarker.getPosition();
                CameraUpdate update = CameraUpdateFactory.changeLatLng(latLng);
                mAMap.animateCamera(update, null);
            }
        });
        float scale = Resources.getSystem().getDisplayMetrics().density;
        int hw = (int) (40 * scale + 0.5f);
        int pd = (int) (2 * scale + 0.5f);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(hw, hw);
        userTarget.targetView.setLayoutParams(lp);
        userTarget.targetView.setPadding(pd, pd, pd, pd);
        setAvatar(userTarget.targetView, null);
        mTitleBar.addView(userTarget.targetView);

        // set target aMap marker.
        View iconView = LayoutInflater.from(AMapRealTimeActivity.this).inflate(R.layout.rc_icon_rt_location_marker, null);
        ImageView locImageView = (ImageView) iconView.findViewById(android.R.id.icon1);

        ImageView avatar = (ImageView) iconView.findViewById(android.R.id.icon);
        setAvatar(avatar, null);
        if (userId.equals(RongIMClient.getInstance().getCurrentUserId())) {
            locImageView.setImageResource(R.drawable.rc_rt_loc_myself);
        } else {
            locImageView.setImageResource(R.drawable.rc_rt_loc_other);
        }
        MarkerOptions markerOptions = new MarkerOptions().anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.fromView(iconView));
        userTarget.targetMarker = mAMap.addMarker(markerOptions);
        return userTarget;
    }

    private void removeParticipantTitleIcon(final UserTarget userTarget) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mTitleBar.removeView(userTarget.targetView);
            }
        });
    }

    private void updateParticipantTitleText() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mUserTargetMap.size() == 0) {
                    return;
                } else if (mUserTargetMap.size() == 1) {
                    mUserText.setText(getResources().getString(R.string.rc_you_are_sharing_location));
                } else {
                    mUserText.setText(String.format(getResources().getString(R.string.rc_others_are_sharing_location), mUserTargetMap.size()));
                }
            }
        });
    }

    private void updateParticipantMarker(final double latitude, final double longitude, final String userId) {
        UserTarget target = mUserTargetMap.get(userId);
        if (target == null) {
            target = createUserTargetById(userId);
            mUserTargetMap.put(userId, target);
            LocationManager.getInstance().getUserInfo(userId, this);
            if (!mParticipants.contains(userId)) {
                mParticipants.add(userId);
            }
        }
        target.targetMarker.setPosition(new LatLng(latitude, longitude));
        updateParticipantTitleText();
        if (userId.equals(RongIMClient.getInstance().getCurrentUserId())
                && !mHasAnimate
                && latitude != 0
                && longitude != 0) {
            CameraUpdate update = CameraUpdateFactory.changeLatLng(new LatLng(latitude, longitude));
            mAMap.animateCamera(update, null);
            mHasAnimate = true;
        }
    }
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                Double latitude = aMapLocation.getLatitude();
                Double longitude = aMapLocation.getLongitude();
                Log.e("AmapOnSuccess", "latitude:"
                        + latitude + ", longitude:"
                        + longitude);
//                LatLng latLng = new LatLng(latitude, longitude);
                updateParticipantMarker(latitude, longitude, "10006");
            }

            if (aMapLocation.getErrorCode() == 12) {
                NToast.longToast(mContext, "请开启手机APP定位权限");
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
}

    private class UserTarget {
        public ImageView targetView;
        public Marker targetMarker;
    }
}
