package com.tianfangIMS.im.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianfangIMS.im.R;

/**
 * Created by LianMengYu on 2017/2/10.
 */

public class FriendPersonInfoActivity extends BaseActivity {
    private ImageView iv_friendinfo_photo;
    private TextView tv_friendinfo_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendpersoninfo_activity);
        init();
    }

    private void init() {
        iv_friendinfo_photo = (ImageView) this.findViewById(R.id.iv_friendinfo_photo);
        tv_friendinfo_name = (TextView) this.findViewById(R.id.tv_friendinfo_name);
    }
}
