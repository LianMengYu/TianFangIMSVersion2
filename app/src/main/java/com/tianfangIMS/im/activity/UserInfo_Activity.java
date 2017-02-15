package com.tianfangIMS.im.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tianfangIMS.im.R;
import com.tianfangIMS.im.dialog.UserInfo_Phone_Dialog;

/**
 * Created by LianMengYu on 2017/1/7.
 */

public class UserInfo_Activity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout rl_useinfo_phone, ly_useinfo_photo;
    private TextView mphone;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinfo_fragment);
        setTitle("个人信息");
        init();
    }

    private void init() {
        rl_useinfo_phone = (RelativeLayout) this.findViewById(R.id.rl_useinfo_phone);
        ly_useinfo_photo = (RelativeLayout) this.findViewById(R.id.ly_useinfo_photo);

        mphone = (TextView) this.findViewById(R.id.tx_userinfo_phonenumber);
        rl_useinfo_phone.setOnClickListener(this);
        ly_useinfo_photo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_useinfo_phone:
                String phonenumber = (mphone.getText().toString()).replaceAll(" ", "").trim();
                UserInfo_Phone_Dialog userInfo_phone_dialog = new UserInfo_Phone_Dialog(this, R.style.dialog, phonenumber);
                userInfo_phone_dialog.getWindow().setBackgroundDrawable(new ColorDrawable());
                userInfo_phone_dialog.show();
                break;
            case R.id.ly_useinfo_photo:
                startActivity(new Intent(mContext,SelectPhoteActivity.class));
                break;
        }
    }

}
