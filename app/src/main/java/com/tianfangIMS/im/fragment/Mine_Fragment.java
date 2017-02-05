package com.tianfangIMS.im.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tianfangIMS.im.R;
import com.tianfangIMS.im.activity.Settings_Activity;
import com.tianfangIMS.im.activity.UserInfo_Activity;

/**
 * Created by LianMengYu on 2017/1/4.
 */

public class Mine_Fragment extends BaseFragment implements View.OnClickListener {
    private RelativeLayout rl_me_use, rl_mine_settings;

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

        rl_mine_settings.setOnClickListener(this);
        rl_me_use.setOnClickListener(this);
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
