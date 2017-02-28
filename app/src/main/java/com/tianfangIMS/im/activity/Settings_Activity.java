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
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by LianMengYu on 2017/1/7.
 */

public class Settings_Activity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout rl_newMessage, rl_resetPwd, rl_setting_signout;//新消息通知
    private Context mContext;
    private CompoundButton sw_sttings_notfaction;
    private ArrayList<TreeInfo> mTreeInfos;
    private RelativeLayout settting_clear_conversation;
    Intent mIntent;
    Boolean flag = false;
    ArrayList<String> strList;
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
        settting_clear_conversation = (RelativeLayout)this.findViewById(R.id.settting_clear_conversation);

        rl_newMessage.setOnClickListener(this);
        rl_resetPwd.setOnClickListener(this);
        rl_setting_signout.setOnClickListener(this);
        settting_clear_conversation.setOnClickListener(this);

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

    private void clearConversation(){
        List<Conversation> list = RongIM.getInstance().getRongIMClient().getConversationList();
        for (int i = 0; i < list.size(); i++) {
            RongIM.getInstance().clearMessages(list.get(i).getConversationType(), list.get(i).getTargetId(), new RongIMClient.ResultCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
            RongIM.getInstance().removeConversation(list.get(i).getConversationType(), list.get(i).getTargetId(), new RongIMClient.ResultCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean aBoolean) {
                    NToast.shortToast(mContext,"聊天记录删除成功");
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
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
            case R.id.settting_clear_conversation:
                clearConversation();
                break;
        }
    }
}
