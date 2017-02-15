package com.tianfangIMS.im.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.tianfangIMS.im.ConstantValue;
import com.tianfangIMS.im.R;
import com.tianfangIMS.im.bean.UserInfoBean;
import com.tianfangIMS.im.utils.CommonUtil;

import io.rong.imlib.model.Conversation;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by LianMengYu on 2017/2/10.
 */

public class FriendPersonInfoActivity extends BaseActivity {
    private static final String TAG = "FriendPersonInfoActivity";
    private Conversation.ConversationType mConversationType;
    private String fromConversationId;
    private ImageView iv_friendinfo_photo;
    private TextView tv_friendinfo_name;
    private String userID;
    private UserInfoBean userInfoBean;
    private TextView friendinfo_email, tx_frienduserinfo_phonenumber, iv_friendinfo_phone,
            friendinfo_company, friendinfo_address, friendinfo_chanpin, friendinfo_jingli;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friendpersoninfo_activity);
        setLoactionButtonVisibility(View.INVISIBLE);
        setViewPagerTagVisibiliy(View.INVISIBLE);
        setPersonContactButtonVisibility(View.INVISIBLE);
        mContext = this;
        init();
        userID = getIntent().getStringExtra("userId");
        GetUserInfoSync(userID);
    }

    private void GetUserInfoSync(String userID) {
        OkGo.post(ConstantValue.GETONEPERSONINFO)
                .tag(this)
                .connTimeOut(10000)
                .readTimeOut(10000)
                .writeTimeOut(10000)
                .params("userid", userID)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Gson gson = new Gson();
                        userInfoBean = gson.fromJson(s, UserInfoBean.class);
                        setTitle(userInfoBean.getName());
                        SetUserInfo(
                                userInfoBean.getName(),
                                userInfoBean.getEmail(),
                                userInfoBean.getMobile(),
                                userInfoBean.getTelephone(),
                                null,
                                userInfoBean.getAddress(),
                                userInfoBean.getName(),
                                userInfoBean.getOrganname());
                        CommonUtil.GetImages(mContext, userInfoBean.getLogo(), iv_friendinfo_photo);
                    }
                });
    }


    private void init() {

        iv_friendinfo_photo = (ImageView) this.findViewById(R.id.iv_friendinfo_photo);
        tv_friendinfo_name = (TextView) this.findViewById(R.id.tv_friendinfo_name);

        friendinfo_email = (TextView) this.findViewById(R.id.friendinfo_email);
        tx_frienduserinfo_phonenumber = (TextView) this.findViewById(R.id.tx_frienduserinfo_phonenumber);
        iv_friendinfo_phone = (TextView) this.findViewById(R.id.iv_friendinfo_phone);
        friendinfo_company = (TextView) this.findViewById(R.id.friendinfo_company);
        friendinfo_address = (TextView) this.findViewById(R.id.friendinfo_address);
        friendinfo_chanpin = (TextView) this.findViewById(R.id.friendinfo_chanpin);
        friendinfo_jingli = (TextView) this.findViewById(R.id.friendinfo_jingli);
    }

    private void SetUserInfo(String uesrname,String eMail, String phone, String telephone, String company, String address, String chanpin, String jingli) {
        tv_friendinfo_name.setText(uesrname);
        friendinfo_email.setText(eMail);
        tx_frienduserinfo_phonenumber.setText(phone);
        iv_friendinfo_phone.setText(telephone);
        friendinfo_company.setText(company);
        friendinfo_address.setText(address);
        friendinfo_chanpin.setText(chanpin);
        friendinfo_jingli.setText(jingli);
    }
}
