package com.tianfangIMS.im.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tianfangIMS.im.R;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by LianMengYu on 2017/1/8.
 */

public class NewMessageNotice_Activity extends BaseActivity {
    private TextView tv_OpenOr;
    private SharedPreferences.Editor editor;
    private LinearLayout newMessage_tishi;
    private RelativeLayout newMessage_rl;
    private CompoundButton sw_newmessage_disturb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newmessage_activity);
        setTitle("新消息通知设置");
        SharedPreferences sp = getSharedPreferences("newmessage", MODE_PRIVATE);
        editor = sp.edit();
        boolean isOpenDisturb = sp.getBoolean("isOpenDisturb", true);
        init();
        if (isOpenDisturb) {
            tv_OpenOr.setText("已开启");
        } else {
            tv_OpenOr.setText("已关闭");
        }
        sw_newmessage_disturb.setChecked(isOpenDisturb);
    }

    private void init() {
        tv_OpenOr = (TextView) this.findViewById(R.id.tv_OpenOr);
        newMessage_tishi = (LinearLayout) this.findViewById(R.id.newMessage_tishi);
        newMessage_rl = (RelativeLayout) this.findViewById(R.id.newMessage_rl);
        sw_newmessage_disturb = (CompoundButton) this.findViewById(R.id.sw_newmessage_disturb);
        sw_newmessage_disturb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tv_OpenOr.setText("已开启");
                if (buttonView.isChecked()) {
                    RongIM.getInstance().setNotificationQuietHours("00:00:00", 1439, new RongIMClient.OperationCallback() {
                        @Override
                        public void onSuccess() {
                            newMessage_tishi.setVisibility(View.VISIBLE);
                            editor.putBoolean("isOpenDisturb", true);
                            editor.apply();
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }
                    });
                } else {
                    tv_OpenOr.setText("已关闭");
                    RongIM.getInstance().setNotificationQuietHours("00:00:00", 1439, new RongIMClient.OperationCallback() {
                        @Override
                        public void onSuccess() {
                            newMessage_tishi.setVisibility(View.VISIBLE);
                            editor.putBoolean("isOpenDisturb", false);
                            editor.apply();
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }
                    });
                }
            }
        });

    }

}
