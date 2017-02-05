package com.tianfangIMS.im.dialog;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.tianfangIMS.im.R;
import com.tianfangIMS.im.activity.AddTopContacts_Activity;

/**
 * Created by LianMengYu on 2017/1/19.
 */

public class MainPlusDialog extends PopupWindow implements View.OnClickListener {

    private Context mContext;
    private RelativeLayout rl_mainplus_chatroom, rl_mainplus_topcontacts;

    public MainPlusDialog(Context context) {
        super(context);
        this.mContext = context;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.mianplus_dialog, null);
        setContentView(view);
        init(view);
    }

    private void jump(){
        mContext.startActivity(new Intent(mContext,AddTopContacts_Activity.class));
        this.dismiss();

    }
    private void init(View view) {
        rl_mainplus_chatroom = (RelativeLayout) view.findViewById(R.id.rl_mainplus_chatroom);
        rl_mainplus_topcontacts = (RelativeLayout) view.findViewById(R.id.rl_mainplus_topcontacts);

        rl_mainplus_topcontacts.setOnClickListener(this);
        rl_mainplus_chatroom.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_mainplus_chatroom:

                break;
            case R.id.rl_mainplus_topcontacts:
                jump();
                break;
        }
    }
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent);
        } else {
            this.dismiss();
        }
    }
}
