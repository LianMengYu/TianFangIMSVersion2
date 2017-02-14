package com.tianfangIMS.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.tianfangIMS.im.ConstantValue;
import com.tianfangIMS.im.R;
import com.tianfangIMS.im.bean.LoginBean;
import com.tianfangIMS.im.bean.OneGroupBean;
import com.tianfangIMS.im.dialog.LoadDialog;
import com.tianfangIMS.im.utils.CommUtils;
import com.tianfangIMS.im.utils.NToast;

import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by LianMengYu on 2017/1/21.
 */
public class GroupDetailActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "GroupDetailActivity";
    private String fromConversationId;
    private Conversation.ConversationType mConversationType;
    private UserInfo userInfo;
    private RelativeLayout rl_signout;
    private TextView tv_group_groupname;
    private RelativeLayout rl_changeGroupName;
    private int requestCode;//返回值
    private OneGroupBean oneGroupBean;
    private String GroupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groupdetail_layout);

        init();
        //群组会话界面点进群组详情
        fromConversationId = getIntent().getStringExtra("TargetId");
        mConversationType = (Conversation.ConversationType) getIntent().getSerializableExtra("conversationType");
        if (!TextUtils.isEmpty(fromConversationId)) {

            userInfo = RongUserInfoManager.getInstance().getUserInfo(fromConversationId);
//            updateUI();
            Log.e(TAG, "看看UserInfo有什么：" + userInfo);
        }
        GroupInfo();
    }

    private void init() {
        rl_signout = (RelativeLayout) this.findViewById(R.id.rl_signout);
        tv_group_groupname = (TextView) this.findViewById(R.id.tv_group_groupname);
        rl_changeGroupName = (RelativeLayout) this.findViewById(R.id.rl_changeGroupName);
        rl_signout.setOnClickListener(this);
        rl_changeGroupName.setOnClickListener(this);
    }

    private void GroupInfo() {
        OkGo.post(ConstantValue.GETONEGROUPINFO)
                .tag(this)
                .connTimeOut(10000)
                .readTimeOut(10000)
                .writeTimeOut(10000)
                .params("groupid", fromConversationId)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if (!TextUtils.isEmpty(s) && !s.equals("{}")) {
                            Gson gson = new Gson();
                            oneGroupBean = gson.fromJson(s, OneGroupBean.class);
                            IsLongGroupName(oneGroupBean.getText().getName());
                            setTitle("群信息" + "(" + oneGroupBean.getText().getVolumeuse() + "人)");
                        }
                    }
                });
    }

    /**
     * 退出群组
     */
    private void SingOutGroup() {
        final Gson gson = new Gson();
        LoginBean loginBean = gson.fromJson(CommUtils.getUserInfo(mContext), LoginBean.class);
        String userid = loginBean.getText().getId();
        Log.e("退出群", "要退出的:" + userid + "群组ID" + fromConversationId);
        OkGo.post(ConstantValue.SINGOUTGROUP)
                .tag(this)
                .connTimeOut(10000)
                .readTimeOut(10000)
                .writeTimeOut(10000)
                //要退出群的ＩＤ
                .params("groupids", userid)
                //群组的ID
                .params("groupid", fromConversationId)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        LoadDialog.show(mContext);
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LoadDialog.dismiss(mContext);
                        if (!TextUtils.isEmpty(s) && !s.equals("{}")) {
                            Gson gson1 = new Gson();
                            Map<String, Object> map = gson1.fromJson(s, new TypeToken<Map<String, Object>>() {
                            }.getType());
                            if ((map.get("code").toString()).equals("1.0")) {
                                NToast.shortToast(mContext, "您已经退出群组");
                                RongIM.getInstance().clearMessages(mConversationType, fromConversationId);
                                Intent intent = new Intent(mContext, MainActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("TargetID", fromConversationId);
                                bundle.putSerializable("conversationType", mConversationType);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                            } else {
                                NToast.shortToast(mContext, "退出群组失败");
                            }
                        }
                    }
                });
    }

    //当群名称大于规定数量时，显示未命名
    private void IsLongGroupName(String name) {
        int longName = name.length();
        if (longName > 5) {
            tv_group_groupname.setText("未命名");
        } else {
            tv_group_groupname.setText(name);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_signout:
                SingOutGroup();
                break;
            case R.id.rl_changeGroupName:
                Intent intent = new Intent(mContext, ChangeGroupNameActivity.class);
                requestCode = 0;
                Bundle bundle = new Bundle();
                GroupName = oneGroupBean.getText().getName();
                bundle.putSerializable("GroupBean", oneGroupBean);
                intent.putExtras(bundle);
                startActivityForResult(intent, requestCode);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            String change01 = data.getStringExtra("change01");
            tv_group_groupname.setText(change01);
        }else{
            return;
        }
    }
}

