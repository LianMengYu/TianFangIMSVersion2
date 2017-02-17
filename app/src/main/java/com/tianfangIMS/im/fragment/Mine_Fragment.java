package com.tianfangIMS.im.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.tianfangIMS.im.ConstantValue;
import com.tianfangIMS.im.R;
import com.tianfangIMS.im.activity.Settings_Activity;
import com.tianfangIMS.im.activity.UserInfo_Activity;
import com.tianfangIMS.im.bean.LoginBean;
import com.tianfangIMS.im.utils.CommonUtil;

/**
 * Created by LianMengYu on 2017/1/4.
 */

public class Mine_Fragment extends BaseFragment implements View.OnClickListener {
    private RelativeLayout rl_me_use, rl_mine_settings;
    private TextView tv_me_username, tv_mine_company, tv_mine_department, tv_position;
    private ImageView iv_sex;
    private Context mContext;
    private ImageView iv_me_icon_photo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_fragment, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        rl_me_use = (RelativeLayout) view.findViewById(R.id.rl_me_use);
        rl_mine_settings = (RelativeLayout) view.findViewById(R.id.rl_mine_settings);
        tv_me_username = (TextView) view.findViewById(R.id.tv_me_username);
        iv_sex = (ImageView) view.findViewById(R.id.iv_sex);
        tv_mine_company = (TextView) view.findViewById(R.id.tv_mine_company);
        tv_mine_department = (TextView) view.findViewById(R.id.tv_mine_department);
        tv_position = (TextView) view.findViewById(R.id.tv_position);
        iv_me_icon_photo = (ImageView) view.findViewById(R.id.iv_setting_photo);

        rl_mine_settings.setOnClickListener(this);
        rl_me_use.setOnClickListener(this);

        Gson gson = new Gson();
        LoginBean loginBean = gson.fromJson(CommonUtil.getUserInfo(getActivity()), LoginBean.class);
        Picasso.with(getActivity())
                .load(ConstantValue.ImageFile + loginBean.getText().getLogo())
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(iv_me_icon_photo);
        tv_me_username.setText(loginBean.getText().getFullname());
        tv_mine_company.setText(" ");
        if (loginBean.getText().getSex().equals(1)) {
            iv_sex.setImageResource(R.mipmap.me_sexicon_nan);
        } else {
            iv_sex.setImageResource(R.mipmap.me_sexicon_nv);
        }
        tv_mine_department.setText(loginBean.getText().getIntro());
        tv_position.setText(loginBean.getText().getWorkno());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_me_use:
                startActivity(new Intent(getActivity(), UserInfo_Activity.class));
                break;
            case R.id.rl_mine_settings:
                startActivity(new Intent(getActivity(), Settings_Activity.class));
                break;
        }
    }
}
