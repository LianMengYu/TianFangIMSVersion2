package com.tianfangIMS.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.tianfangIMS.im.R;

/**
 * Created by LianMengYu on 2017/1/7.
 */

public class Settings_Activity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout rl_newMessage, rl_resetPwd;//新消息通知

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_fragment);
        setTitle("设置");
        init();
    }

    private void init() {
        rl_newMessage = (RelativeLayout) this.findViewById(R.id.rl_newMessage);
        rl_resetPwd = (RelativeLayout) this.findViewById(R.id.rl_resetPwd);

        rl_newMessage.setOnClickListener(this);
        rl_resetPwd.setOnClickListener(this);
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
        }
    }

//    @Override
//    public void initView() {
//
//    }
}
