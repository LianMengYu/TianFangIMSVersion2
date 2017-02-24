package com.tianfangIMS.im.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.tianfangIMS.im.R;
import com.tianfangIMS.im.bean.TreeInfo;
import com.tianfangIMS.im.service.FloatService;
import com.tianfangIMS.im.utils.NToast;

import java.util.ArrayList;

/**
 * Created by LianMengYu on 2017/1/7.
 */

public class Settings_Activity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout rl_newMessage, rl_resetPwd, rl_setting_signout;//新消息通知
    private Context mContext;
    private CompoundButton sw_sttings_notfaction;
    private ArrayList<TreeInfo> mTreeInfos;
    Intent mIntent;
    Boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_fragment);
        mContext = this;
        setTitle("设置");
        init();
        sw_sttings_notfaction.setChecked(flag);
    }

    private void init() {
        rl_newMessage = (RelativeLayout) this.findViewById(R.id.rl_newMessage);
        rl_resetPwd = (RelativeLayout) this.findViewById(R.id.rl_resetPwd);
        rl_setting_signout = (RelativeLayout) this.findViewById(R.id.rl_setting_signout);
        sw_sttings_notfaction = (CompoundButton) this.findViewById(R.id.sw_sttings_notfaction);

        rl_newMessage.setOnClickListener(this);
        rl_resetPwd.setOnClickListener(this);
        rl_setting_signout.setOnClickListener(this);

        sw_sttings_notfaction.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    mTreeInfos = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                        TreeInfo mInfo = new TreeInfo();
                        mInfo.setLogo("http://www.qqzhi.com/uploadpic/2014-09-26/153011818.jpg");
                        mInfo.setName(String.valueOf(i * 100));
                        mTreeInfos.add(mInfo);
                    }
                    mIntent = new Intent(Settings_Activity.this, FloatService.class);
                    mIntent.putExtra("data", mTreeInfos);
                    startService(mIntent);
                    flag = true;
                } else {
                    mIntent = new Intent(Settings_Activity.this, FloatService.class);
                    stopService(mIntent);
                    flag = false;
                }
            }
        });

    }

    //预留登出接口
    private void Signout() {
//        OkGo.post(ConstantValue.SINGOUTUSER)
//                .tag(this)
//                .connTimeOut(10000)
//                .readTimeOut(10000)
//                .writeTimeOut(10000)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(String s, Call call, Response response) {
//                        if()
//                    }
//                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_newMessage:
                startActivity(new Intent(this, NewMessageNotice_Activity.class));
                break;
            case R.id.rl_resetPwd:
                startActivity(new Intent(this, ResetPassword_Activity.class));
                break;
            case R.id.rl_setting_signout:
//                Signout();
                NToast.shortToast(mContext, "退出账号登录");
                SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
    }

//    @Override
//    public void initView() {
//
//    }
}
