package com.tianfangIMS.im.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.tianfangIMS.im.ConstantValue;
import com.tianfangIMS.im.R;
import com.tianfangIMS.im.bean.TopFiveUserInfoBean;
import com.tianfangIMS.im.bean.TreeInfo;
import com.tianfangIMS.im.dialog.CleanAllChatDialog;
import com.tianfangIMS.im.dialog.LoadDialog;
import com.tianfangIMS.im.dialog.SignOutDialog;
import com.tianfangIMS.im.service.FloatService;
import com.tianfangIMS.im.utils.CommonUtil;
import com.tianfangIMS.im.utils.NToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by LianMengYu on 2017/1/7.
 */

public class Settings_Activity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "Settings_Activity";
    private RelativeLayout rl_newMessage, rl_resetPwd, rl_setting_signout;//新消息通知
    private Context mContext;
    private CompoundButton sw_sttings_notfaction;
    private ArrayList<TreeInfo> mTreeInfos;
    private RelativeLayout settting_clear_conversation;
    Intent mIntent;
    ArrayList<String> strList;

    ReentrantLock lock = new ReentrantLock();
    private List<TopFiveUserInfoBean> data = new ArrayList<TopFiveUserInfoBean>(5);
    private int sum = 5;
    private List<TopFiveUserInfoBean> resultdata = new ArrayList<TopFiveUserInfoBean>(5);
    private SharedPreferences.Editor editor;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_fragment);
        mContext = this;
        setTitle("设置");
        init();
        getNew5Data();
        setCahce();
        sp = getSharedPreferences("config", MODE_PRIVATE);
        editor = sp.edit();
        boolean flag = sp.getBoolean("isOpen", false);
        sw_sttings_notfaction.setChecked(flag);
    }

    private void init() {
        rl_newMessage = (RelativeLayout) this.findViewById(R.id.rl_newMessage);
        rl_resetPwd = (RelativeLayout) this.findViewById(R.id.rl_resetPwd);
        rl_setting_signout = (RelativeLayout) this.findViewById(R.id.rl_setting_signout);
        sw_sttings_notfaction = (CompoundButton) this.findViewById(R.id.sw_sttings_notfaction);
        settting_clear_conversation = (RelativeLayout) this.findViewById(R.id.settting_clear_conversation);

        rl_newMessage.setOnClickListener(this);
        rl_resetPwd.setOnClickListener(this);
        rl_setting_signout.setOnClickListener(this);
        settting_clear_conversation.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= 23) {
            if(!Settings.canDrawOverlays(Settings_Activity.this)){
                NToast.longToast(mContext,"开启悬浮球权限");
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 10);
            }
        }else {
            sw_sttings_notfaction.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (buttonView.isChecked()) {
                        editor.putBoolean("isOpen", true);
                        editor.apply();
                        mTreeInfos = new ArrayList<>();
                        Log.e("settingResultData：", "打印传递值的数量----:" + data.size());
                        for (int i = 0; i < data.size(); i++) {
                            TreeInfo mInfo = new TreeInfo();
                            mInfo.setLogo(data.get(i).getLogo());
                            mInfo.setName(data.get(i).getName());
                            if (resultdata.get(i).getConversationType() == Conversation.ConversationType.PRIVATE) {
                                mInfo.setGroup(false);
                            } else if (resultdata.get(i).getConversationType() == Conversation.ConversationType.GROUP) {
                                mInfo.setGroup(true);
                            }
                            mTreeInfos.add(mInfo);
                        }
                        mIntent = new Intent(Settings_Activity.this, FloatService.class);
                        mIntent.putExtra("data", mTreeInfos);
                        startService(mIntent);
                    } else {
                        editor.putBoolean("isOpen", false);
                        editor.apply();
                        mIntent = new Intent(Settings_Activity.this, FloatService.class);
//                    mIntent.putStringArrayListExtra("data",mTreeInfos);
                        stopService(mIntent);

                    }
                }
            });
        }
    }

    public void getNew5Data() {
        List<Conversation> list = RongIMClient.getInstance().getConversationList();
        if (list != null && list.size() > 0) {
            if (list.size() < 5) {
                for (int i = 0; i < list.size(); i++) {
                    resultdata.add(new TopFiveUserInfoBean(list.get(i).getConversationType(), list.get(i).getTargetId(), null, null));
                }

            }
            if (list.size() > 5 || list.size() == 5) {
                for (int i = 0; i < 5; i++) {
                    resultdata.add(new TopFiveUserInfoBean(list.get(i).getConversationType(), list.get(i).getTargetId(), null, null));
                }
            }
        }
    }

    private void setCahce() {
        for (int i = 0; i < resultdata.size(); i++) {
            if (resultdata.get(i).getConversationType() == Conversation.ConversationType.PRIVATE) {
                GetPrivate(resultdata.get(i).getId());
            } else if (resultdata.get(i).getConversationType() == Conversation.ConversationType.GROUP) {
                GetGroup(resultdata.get(i).getId());
            }
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
                SignOutDialog signoutdialog = new SignOutDialog(mContext);
                signoutdialog.getWindow().setBackgroundDrawable(new ColorDrawable());
                signoutdialog.show();
                CommonUtil.SetCleanDialogStyle(signoutdialog);
                break;
            case R.id.settting_clear_conversation:
                CleanAllChatDialog dialog = new CleanAllChatDialog(mContext);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable());
                dialog.show();
                CommonUtil.SetCleanDialogStyle(dialog);
                break;
        }
    }

    private void GetPrivate(String id) {
        OkGo.post(ConstantValue.GETONEPERSONINFO)
                .tag(this)
                .connTimeOut(10000)
                .readTimeOut(10000)
                .writeTimeOut(10000)
                .params("userid", id)
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
                            Log.e("Serivce", "s:" + s);
                            Gson gson = new Gson();
                            final Map<String, Object> map = gson.fromJson(s, new TypeToken<Map<String, Object>>() {
                            }.getType());
                            Log.e("APP", "----图片地址:" + ConstantValue.ImageFile + map.get("logo"));
                            if (!TextUtils.isEmpty(map.get("logo").toString())) {
                                Log.e("Serivce", "Private缓存成功");
                                data.add(new TopFiveUserInfoBean(Conversation.ConversationType.PRIVATE, map.get("id").toString(),
                                        map.get("name").toString(), ConstantValue.ImageFile + map.get("logo").toString()));
                            } else {
                                data.add(new TopFiveUserInfoBean(Conversation.ConversationType.PRIVATE, map.get("id").toString(),
                                        map.get("name").toString(), ConstantValue.ImageFile + map.get("logo").toString()));
                            }
                        }
                    }
                });
    }

    private void GetGroup(final String id) {
        OkGo.post(ConstantValue.GETONEGROUPINFO)
                .tag(this)
                .connTimeOut(10000)
                .readTimeOut(10000)
                .writeTimeOut(10000)
                .params("groupid", id)
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
                            Log.e("Serivce", "缓存群组s:" + s);
                            Gson gson = new Gson();
                            final Map<String, Object> map = gson.fromJson(s, new TypeToken<Map<String, Object>>() {
                            }.getType());
                            Object object = map.get("text");
                            final Map<String, String> map1 = (Map<String, String>) object;
                            Log.e("Serivce", "Group缓存失败" + map1.get("logo"));
                            if (!TextUtils.isEmpty(map1.get("logo"))) {
                                data.add(new TopFiveUserInfoBean(Conversation.ConversationType.GROUP,
                                        id,
                                        map1.get("name").toString(),
                                        ConstantValue.ImageFile + map1.get("logo")));
                            } else {
                                data.add(new TopFiveUserInfoBean(Conversation.ConversationType.GROUP,
                                        id,
                                        map1.get("name").toString(),
                                        ConstantValue.ImageFile + map1.get("logo")));
                            }
                        }
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(Settings_Activity.this, "not granted", Toast.LENGTH_SHORT);
            }
        }
    }
}
