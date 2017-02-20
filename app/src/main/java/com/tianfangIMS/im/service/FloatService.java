package com.tianfangIMS.im.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tianfangIMS.im.bean.TreeInfo;
import com.tianfangIMS.im.view.FloatView;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Titan on 2017/2/19.
 */

public class FloatService extends Service {

    private static final String TAG = "FloatService";

    WindowManager mWindowManager;
    FloatView mFloatView;

    List<TreeInfo> mTreeInfos;

    WindowManager.LayoutParams wl;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mFloatView == null) {
            final int density = (int) getResources().getDisplayMetrics().density;
            mTreeInfos = (List<TreeInfo>) intent.getSerializableExtra("data");
            mWindowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
            mFloatView = new FloatView(getApplicationContext());
            mFloatView.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mFloatView.mCustomView.getVisibility() == View.GONE) {
                        mFloatView.mCustomView.setVisibility(View.VISIBLE);
//                        mFloatView.setBackgroundColor(Color.GRAY);
//                        mFloatView.setAlpha(0.1f);
//                        wl.gravity = Gravity.CENTER;
                        wl.width = WindowManager.LayoutParams.WRAP_CONTENT;
                        wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        wl.gravity = Gravity.TOP | Gravity.LEFT;
                        wl.dimAmount = 0.3f;
                        wl.x = getResources().getDisplayMetrics().widthPixels - 300 * density / 2;
                        wl.y = (getResources().getDisplayMetrics().heightPixels - 300 * density) / 2;
                        wl.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_DIM_BEHIND | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(50 * density, 50 * density);
                        lp.addRule(RelativeLayout.CENTER_VERTICAL);
                        lp.leftMargin = 150 * density - 70 * density;
                        mFloatView.btn.setLayoutParams(lp);
//                        wl.width = getResources().getDisplayMetrics().widthPixels;
//                        wl.height = getResources().getDisplayMetrics().heightPixels;
                    } else {
                        mFloatView.mCustomView.setVisibility(View.GONE);
//                        wl.width = 66 * density;
//                        wl.height = 66 * density;
//                        wl.gravity = Gravity.CENTER | Gravity.RIGHT;
                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(50 * density, 50 * density);
                        lp.rightMargin = 25 * density;
                        mFloatView.btn.setLayoutParams(lp);
                        wl.width = WindowManager.LayoutParams.WRAP_CONTENT;
                        wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        wl.gravity = Gravity.TOP | Gravity.LEFT;
                        wl.dimAmount = 0.3f;
                        wl.x = getResources().getDisplayMetrics().widthPixels - 75 * density / 2;
                        wl.y = (getResources().getDisplayMetrics().heightPixels - 50 * density) / 2;
                        wl.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_DIM_BEHIND | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
                        wl.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//                        mFloatView.setBackgroundColor(Color.TRANSPARENT);
                    }
                    mWindowManager.updateViewLayout(mFloatView, wl);
                }
            });
            Glide.with(getApplicationContext()).load("http://www.qqzhi.com/uploadpic/2014-09-26/153011818.jpg").bitmapTransform(new CropCircleTransformation(getApplicationContext())).into(mFloatView.btn);
            for (int i = 0; i < mTreeInfos.size(); i++) {
                ImageView mImageView = new ImageView(this);
                mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(50 * density, 50 * density);
                mImageView.setLayoutParams(lp);
                mImageView.setId(View.NO_ID);
                mImageView.setTag(mImageView.getId(), mTreeInfos.get(0));
                mImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TreeInfo mInfo = (TreeInfo) v.getTag(v.getId());
                        Toast.makeText(getApplicationContext(), mInfo.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
                mFloatView.mCustomView.addView(mImageView);
            }
            wl = new WindowManager.LayoutParams();
            wl.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            wl.format = PixelFormat.RGBA_8888;
            wl.width = WindowManager.LayoutParams.WRAP_CONTENT;
            wl.height = WindowManager.LayoutParams.WRAP_CONTENT;
            wl.gravity = Gravity.TOP | Gravity.LEFT;
            wl.dimAmount = 0.3f;
            wl.x = getResources().getDisplayMetrics().widthPixels - 300 * density / 2;
            wl.y = (getResources().getDisplayMetrics().heightPixels - 300 * density) / 2;
            wl.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_DIM_BEHIND | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(50 * density, 50 * density);
            lp.addRule(RelativeLayout.CENTER_VERTICAL);
            lp.leftMargin = 150 * density - 70 * density;
            mFloatView.btn.setLayoutParams(lp);
            mWindowManager.addView(mFloatView, wl);
            Log.d(TAG, "添加完成");
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatView != null) {
            mWindowManager.removeView(mFloatView);
        }
    }
}
