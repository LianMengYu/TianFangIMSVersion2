package com.tianfangIMS.im.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newmessage_activity);
        setTitle("新消息通知设置");
        SharedPreferences sp = getSharedPreferences("newmessage",MODE_PRIVATE);
        editor = sp.edit();
        boolean isOpenDisturb = sp.getBoolean("isOpenDisturb", true);
        init();
        newMessage_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_OpenOr.getText().equals("已开启")){
                    tv_OpenOr.setText("已关闭");
                    RongIM.getInstance().removeNotificationQuietHours(new RongIMClient.OperationCallback() {
                        @Override
                        public void onSuccess() {
                            newMessage_tishi.setVisibility(View.GONE);
                            editor.putBoolean("isOpenDisturb",true);
                            editor.apply();
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }
                    });
                }else {
                    tv_OpenOr.setText("已开启");
                    RongIM.getInstance().setNotificationQuietHours("00:00:00", 1439, new RongIMClient.OperationCallback() {
                        @Override
                        public void onSuccess() {
                            newMessage_tishi.setVisibility(View.VISIBLE);
                            editor.putBoolean("isOpenDisturb",false);
                            editor.apply();
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }
                    });
                }
            }
        });
        if(isOpenDisturb){
            tv_OpenOr.setText("已关闭");
            newMessage_tishi.setVisibility(View.GONE);
        }else {
            tv_OpenOr.setText("已开启");
            newMessage_tishi.setVisibility(View.VISIBLE);
        }
    }
    private void init(){
        tv_OpenOr = (TextView)this.findViewById(R.id.tv_OpenOr);
        newMessage_tishi = (LinearLayout)this.findViewById(R.id.newMessage_tishi);
        newMessage_rl = (RelativeLayout)this.findViewById(R.id.newMessage_rl);
    }

}
