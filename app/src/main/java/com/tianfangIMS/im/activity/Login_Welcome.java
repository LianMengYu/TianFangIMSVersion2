package com.tianfangIMS.im.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.tianfangIMS.im.R;


/**
 * Created by LianMengYu on 2016/12/29.
 * 加载页
 */

public class Login_Welcome extends Activity {
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //定义全屏参数
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        mContext = this;
        //获取当前窗体
        Window window = Login_Welcome.this.getWindow();
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
        setContentView(R.layout.activity_welcome);
        //启动一个handler来限定3秒，然后调整Activity
        new Handler().postDelayed(runnable, 5000);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            SharedPreferences sharedPreferences = getSharedPreferences("config",
                    Activity.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "");
            String userPwd = sharedPreferences.getString("userpass", "");
            String token = sharedPreferences.getString("token", "");
            if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(userPwd)) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
//                if (!TextUtils.isEmpty(token)) {
//                    RongIM.connect(token, new RongIMClient.ConnectCallback() {
//                        @Override
//                        public void onTokenIncorrect() {
//
//                        }
//
//                        @Override
//                        public void onSuccess(String s) {
//
//                        }
//
//                        @Override
//                        public void onError(RongIMClient.ErrorCode errorCode) {
//                            NToast.shortToast(mContext,"Connect连接失败");
//                            return;
//                        }
//                    });
//                }
            } else {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

}
