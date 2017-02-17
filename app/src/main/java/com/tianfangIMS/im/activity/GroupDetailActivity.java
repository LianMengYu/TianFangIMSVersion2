package com.tianfangIMS.im.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.tianfangIMS.im.ConstantValue;
import com.tianfangIMS.im.R;
import com.tianfangIMS.im.adapter.GroupDetailInfo_GridView_Adapter;
import com.tianfangIMS.im.bean.GroupBean;
import com.tianfangIMS.im.bean.GroupListBean;
import com.tianfangIMS.im.bean.LoginBean;
import com.tianfangIMS.im.bean.OneGroupBean;
import com.tianfangIMS.im.dialog.LoadDialog;
import com.tianfangIMS.im.utils.CommonUtil;
import com.tianfangIMS.im.utils.NToast;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ImageMessage;
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
    private RelativeLayout rl_group_file, rl_breakGroup;
    private Context mContext;
    private GridView gv_userinfo;
    private GroupDetailInfo_GridView_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groupdetail_layout);
        mContext = this;
        init();
        //群组会话界面点进群组详情
        fromConversationId = getIntent().getStringExtra("TargetId");
        mConversationType = (Conversation.ConversationType) getIntent().getSerializableExtra("conversationType");
        if (!TextUtils.isEmpty(fromConversationId)) {

            userInfo = RongUserInfoManager.getInstance().getUserInfo(fromConversationId);
//            updateUI();
            Log.e(TAG, "看看UserInfo有什么：" + fromConversationId);
        }
        GroupInfo();
        GetGroupUserInfo();
    }
    //对GridView 显示的宽高经行设置
    private void SettingGridView(ArrayList<GroupBean> list) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int size = list.size();//要显示数据的个数
        //gridview的layout_widht,要比每个item的宽度多出2个像素，解决不能完全显示item的问题
        int allWidth = (int) (82 * size * density);
        //int allWidth = (int) ((width / 3 ) * size + (size-1)*3);//也可以这样使用，item的总的width加上horizontalspacing
        int itemWidth = (int) (65 * density);//每个item宽度
        LinearLayout.LayoutParams params = new
                LinearLayout.LayoutParams(allWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        gv_userinfo.setLayoutParams(params);
        gv_userinfo.setColumnWidth(itemWidth);
        gv_userinfo.setHorizontalSpacing(3);
        gv_userinfo.setStretchMode(GridView.NO_STRETCH);
        gv_userinfo.setNumColumns(size);
    }

    private void GetGroupUserInfo() {
        OkGo.post(ConstantValue.GROUPALLUSERINFO)
                .tag(this)
                .connTimeOut(10000)
                .readTimeOut(10000)
                .writeTimeOut(10000)
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
                            Type listType1 = new TypeToken<GroupListBean>() {
                            }.getType();
                            Gson gson1 = new Gson();
                            GroupListBean GroupAllBean = gson1.fromJson(s, listType1);
                            ArrayList<GroupBean> GroupBeanList = GroupAllBean.getText();

                            adapter = new GroupDetailInfo_GridView_Adapter(mContext, GroupBeanList);
                            SettingGridView(GroupBeanList);
                            gv_userinfo.setAdapter(adapter);
                            gv_userinfo.deferNotifyDataSetChanged();
                        }
                    }
                });

    }

    private void GetHistoryMessages() {
        List<Message> list = RongIMClient.getInstance().getHistoryMessages(mConversationType, fromConversationId, -1, Integer.MAX_VALUE);
        List<ImageMessage> msg = new ArrayList<>();
        List<Uri> urilist = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
//            Log.e(TAG, "回去消息:" + list.get(i).getObjectName()+"====消息类型id："+list.get(i).get);
            MessageContent messageContent = list.get(i).getContent();
            if (messageContent instanceof ImageMessage) {
                ImageMessage imageMessage = (ImageMessage) messageContent;
                msg.add(imageMessage);
            }
        }
        for (int j = 0; j < msg.size(); j++) {
            Uri aa = msg.get(j).getRemoteUri();
            urilist.add(aa);
        }
        if (urilist != null && urilist.size() > 0) {
            Intent intent = new Intent(mContext, SelectPhoteActivity.class);
            intent.putExtra("photouri", (Serializable) urilist);
            startActivity(intent);
        } else {
            NToast.shortToast(mContext, "没有数据");
        }
    }

    private void init() {
        rl_signout = (RelativeLayout) this.findViewById(R.id.rl_signout);
        tv_group_groupname = (TextView) this.findViewById(R.id.tv_group_groupname);
        rl_changeGroupName = (RelativeLayout) this.findViewById(R.id.rl_changeGroupName);
        rl_group_file = (RelativeLayout) this.findViewById(R.id.rl_group_file);
        rl_breakGroup = (RelativeLayout) this.findViewById(R.id.rl_breakGroup);
        gv_userinfo = (GridView) this.findViewById(R.id.gv_userinfo);

        rl_signout.setOnClickListener(this);
        rl_changeGroupName.setOnClickListener(this);
        rl_group_file.setOnClickListener(this);
        rl_breakGroup.setOnClickListener(this);
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
                            LoginBean loginBean = gson.fromJson(CommonUtil.getUserInfo(mContext), LoginBean.class);
                            if (oneGroupBean.getText().getMid().equals(loginBean.getText().getId())) {
                                rl_breakGroup.setVisibility(View.VISIBLE);
                            } else {
                                rl_breakGroup.setVisibility(View.GONE);
                            }
                        }
                    }
                });
    }

    /**
     * 退出群组
     */
    private void SingOutGroup() {
        final Gson gson = new Gson();
        LoginBean loginBean = gson.fromJson(CommonUtil.getUserInfo(mContext), LoginBean.class);
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

    private void BreakGroupUser() {
        Gson gson = new Gson();
        LoginBean loginBean = gson.fromJson(CommonUtil.getUserInfo(mContext), LoginBean.class);
        String userid = loginBean.getText().getId();
        OkGo.post(ConstantValue.DISSGROUP)
                .tag(this)
                .connTimeOut(10000)
                .readTimeOut(10000)
                .writeTimeOut(10000)
                .params("userid", userid)
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
                                Intent intent = new Intent(mContext, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                NToast.shortToast(mContext, "解散群组失败");
                            }
                        }
                    }
                });
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
            case R.id.rl_group_file:
                GetHistoryMessages();
                break;
            case R.id.rl_breakGroup:
                BreakGroupUser();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            String change01 = data.getStringExtra("change01");
            tv_group_groupname.setText(change01);
        } else {
            return;
        }
    }
}

