package com.tianfangIMS.im.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.tianfangIMS.im.ConstantValue;
import com.tianfangIMS.im.R;
import com.tianfangIMS.im.bean.GroupBean;
import com.tianfangIMS.im.bean.GroupListBean;
import com.tianfangIMS.im.bean.LoginBean;
import com.tianfangIMS.im.bean.TopContactsBean;
import com.tianfangIMS.im.dialog.MainPlusDialog;
import com.tianfangIMS.im.fragment.Contacts_Fragment;
import com.tianfangIMS.im.fragment.Jobs_Fragment;
import com.tianfangIMS.im.fragment.Message_Fragment;
import com.tianfangIMS.im.fragment.Mine_Fragment;
import com.tianfangIMS.im.utils.CommUtils;
import com.tianfangIMS.im.utils.NToast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by LianMengYu on 2017/1/3.
 * 主要作为所有fragment的基类来使用
 */

public class MainActivity extends BaseActivity implements View.OnClickListener, RongIM.UserInfoProvider, RongIM.GroupInfoProvider {
    private static final String TAG = "MainActivity";
    private LinearLayout ly_tab_menu_msg, ly_tab_menu_job, ly_tab_menu_contacts, ly_tab_menu_me;
    private TextView tv_tab_menu_msg, tv_tab_menu_job, tv_tab_menu_contacts, tv_tab_menu_me;
    private TextView tv_tab_menu_msg_num, tv_tab_menu_job_num, tv_tab_menu_contacts_num;
    private ImageView img_tab_menu_setting_partner;
    private FrameLayout fragment_container;

    private LinearLayout ly_set_firstFragment;

    private ConversationListDynamicActivtiy conversationListDynamicActivtiy;
    private Message_Fragment message_fragment;
    private Jobs_Fragment Jobs_Fragment;
    private Contacts_Fragment Contacts_Fragment;
    private Mine_Fragment Me_Fragment;
    private ImageView main_plus;//首页“+”号
    //会话列表
    private Fragment mConversationListFragment = null;
    private Fragment mConversationList;
    //会话列表Activity
    private boolean isDebug;
    private Conversation.ConversationType[] mConversationsTypes = null;
    private boolean ischeck = true;
    //    FragmentManager fragmentManager;
    private android.support.v4.app.FragmentTransaction transaction;
    private android.support.v4.app.FragmentManager fragmentManager;
    private List<LoginBean> mLoginBeanList;
    private ImageView main_button;
    private TopContactsBean topContactsBean;
    private List<TopContactsBean> topContactsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mian_activity);
        GetFriendInfo();//持久化所有好友信息
        GetGroupInfo();//持久化所有群组信息
        setHeadVisibility(View.GONE);
        SystemBarTranslucentType(this);//改变状态栏的沉浸样式
        init();//初始化控件
        initFM();//初始化fragment
        mLoginBeanList = new ArrayList<LoginBean>();
        mLoginBeanList.add(GetUesrBean());
        RongIM.setUserInfoProvider(this, true);
        RongIM.setGroupInfoProvider(this, true);
        RongIM.getInstance().setMessageAttachedUserInfo(true);
        RongIM.setConversationBehaviorListener(new MyConversationBehaviorListener());
        RongIM.setConversationListBehaviorListener(new MyConversationListBehaviorListener());

//        RongIM.startActivity
//        PTTClient pttClient = PTTClient.getInstance();


//        startActivity(new Intent(MainActivity.this, ConversationListDynamicActivtiy.class));
//        CommUtils.GetImage(this,GetUesrBean().getText().getLogo());
    }

//    @Override
//    public void initView() {
//
//    }


    private void init() {
        ly_set_firstFragment = (LinearLayout) this.findViewById(R.id.main_ly_tab_menu_msg_new);
        ly_tab_menu_job = (LinearLayout) this.findViewById(R.id.ly_tab_menu_job);
        ly_tab_menu_contacts = (LinearLayout) this.findViewById(R.id.ly_tab_menu_contacts);
        ly_tab_menu_me = (LinearLayout) this.findViewById(R.id.ly_tab_menu_me);

        tv_tab_menu_msg = (TextView) this.findViewById(R.id.tv_tabmenu_msg);
        tv_tab_menu_job = (TextView) this.findViewById(R.id.tv_tabmenu_job);
        tv_tab_menu_contacts = (TextView) this.findViewById(R.id.tv_tabmenu_contacts);
        tv_tab_menu_me = (TextView) this.findViewById(R.id.tv_tabmenu_me);

        tv_tab_menu_msg_num = (TextView) this.findViewById(R.id.tv_tab_menu_msg_num);
        tv_tab_menu_job_num = (TextView) this.findViewById(R.id.tv_tab_menu_job_num);
        tv_tab_menu_contacts_num = (TextView) this.findViewById(R.id.tv_tab_menu_contacts_num);
        img_tab_menu_setting_partner = (ImageView) this.findViewById(R.id.img_tab_menu_setting_partner);
        main_plus = (ImageView) this.findViewById(R.id.main_plus);


        ly_set_firstFragment.setOnClickListener(this);
        ly_tab_menu_job.setOnClickListener(this);
        ly_tab_menu_contacts.setOnClickListener(this);
        ly_tab_menu_me.setOnClickListener(this);
        main_plus.setOnClickListener(this);
        main_button = (ImageView) this.findViewById(R.id.main_plus);
        fragment_container = (FrameLayout) this.findViewById(R.id.fragment_container);
    }

    //获取所有好友信息
    private void GetFriendInfo() {
        Gson gson = new Gson();
        LoginBean loginBean = gson.fromJson(CommUtils.getUserInfo(mContext), LoginBean.class);
        String UID = loginBean.getText().getAccount();
        OkGo.post(ConstantValue.GETCONTACTSLIST)
                .tag(this)
                .connTimeOut(10000)
                .readTimeOut(10000)
                .writeTimeOut(10000)
                .cacheKey("getfriendinfo")
                .params("account", UID)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if (!TextUtils.isEmpty(s)) {
//                            Type listType = new TypeToken< List<TopContactsBean> >() {
//                            }.getType();
//                            Gson gson = new Gson();
//                            topContactsList = gson.fromJson(s, listType);
//
//                            Log.e(TAG,"保存好友："+topContactsList);
                            CommUtils.saveFrientUserInfo(mContext, s);
                        } else {
                            return;
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        NToast.shortToast(mContext, "好友请求失败");
                        return;
                    }
                });
    }

    //获取所有群组信息
    private void GetGroupInfo() {
        Gson gson = new Gson();
        LoginBean loginBean = gson.fromJson(CommUtils.getUserInfo(mContext), LoginBean.class);
        String UID = loginBean.getText().getId();
        OkGo.post(ConstantValue.GETALLGROUP)
                .tag(this)
                .connTimeOut(10000)
                .readTimeOut(10000)
                .writeTimeOut(10000)
                .params("userid", UID)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if (!TextUtils.isEmpty(s)) {
//                            Type listType = new TypeToken< List<GroupListBean> >() {
//                            }.getType();
//                            Gson gson = new Gson();
//                            topContactsList = gson.fromJson(s, listType);
//
//                            Log.e(TAG,"保存好友："+topContactsList);

                            Log.e(TAG, "有没有执行" + s);
                            CommUtils.saveGroupUserInfo(mContext, s);
                        } else {
                            return;
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        NToast.shortToast(mContext, "好友请求失败");
                        return;
                    }
                });
    }

    //重置所有文本的选中状态
    private void setSelected() {
        tv_tab_menu_msg.setSelected(false);
        tv_tab_menu_job.setSelected(false);
        tv_tab_menu_contacts.setSelected(false);
        tv_tab_menu_me.setSelected(false);


        tv_tab_menu_msg.setTextColor(this.getResources().getColor(R.color.colorNavigation));
        tv_tab_menu_job.setTextColor(this.getResources().getColor(R.color.colorNavigation));
        tv_tab_menu_contacts.setTextColor(this.getResources().getColor(R.color.colorNavigation));
        tv_tab_menu_me.setTextColor(this.getResources().getColor(R.color.colorNavigation));
    }

    private LoginBean GetUesrBean() {
        Gson gson = new Gson();
//        TopContactsBean bean = gson.fromJson(CommUtils.getFrientUserInfo(mContext), TopContactsBean.class);
        LoginBean bean = gson.fromJson(CommUtils.getUserInfo(mContext), LoginBean.class);
        return bean;
    }

    private class MyConversationBehaviorListener implements RongIM.ConversationBehaviorListener {
        @Override
        public boolean onMessageClick(Context context, View view, Message message) {
            return false;
        }

        @Override
        public boolean onUserPortraitClick(Context context, Conversation.ConversationType mConversationType, UserInfo userInfo) {
//            NToast.longToast(context, "点击了头像" + userInfo.getUserId() + "会话类型" + mConversationType.getValue());
            String userID = userInfo.getUserId().toString();
//            if (mConversationType.equals(Conversation.ConversationType.GROUP)) {
//                Intent intentGroup = new Intent(mContext, FriendPersonInfoActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("userId", userID);
//                startActivity(intentGroup);
//            }
//            if (mConversationType.equals(Conversation.ConversationType.PRIVATE)) {
                Intent intent = new Intent(mContext, FriendPersonInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("userId", userID);
                intent.putExtras(bundle);
                intent.putExtra("conversationType", Conversation.ConversationType.PRIVATE);
                startActivity(intent);

//            }
            return true;
        }

        @Override
        public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
            return false;
        }

        @Override
        public boolean onMessageLinkClick(Context context, String s) {
            return false;
        }

        @Override
        public boolean onMessageLongClick(Context context, View view, Message message) {
//            startActivity(new Intent(MainActivity.this,ConversationViewpagerActivity.class));
//            Log.e(TAG,"执行点击会话列表消息界面");
            return false;
        }
    }

    private class MyConversationListBehaviorListener implements RongIM.ConversationListBehaviorListener {

        @Override
        public boolean onConversationClick(Context context, View view, UIConversation conversation) {
//            startActivity(new Intent(MainActivity.this, ConversationActivity.class));
//            Log.e(TAG, "执行点击会话列表消息界面");
            return false;
        }

        @Override
        public boolean onConversationPortraitClick(Context context, Conversation.ConversationType conversationType, String targetId) {
            return false;
        }

        @Override
        public boolean onConversationPortraitLongClick(Context context, Conversation.ConversationType conversationType, String targetId) {
            return false;
        }

        @Override
        public boolean onConversationLongClick(Context context, View view, UIConversation conversation) {
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_ly_tab_menu_msg_new:
                setSelected();
                tv_tab_menu_msg.setSelected(true);
                tv_tab_menu_msg_num.setVisibility(View.INVISIBLE);
                tv_tab_menu_msg.setTextColor(this.getResources().getColor(R.color.colorNaviationClick));
                SelectFragment(1);
                break;
            case R.id.ly_tab_menu_job:
                setSelected();
                tv_tab_menu_job.setSelected(true);
                tv_tab_menu_job_num.setVisibility(View.INVISIBLE);
                tv_tab_menu_job.setTextColor(this.getResources().getColor(R.color.colorNaviationClick));
                SelectFragment(2);
                break;
            case R.id.ly_tab_menu_contacts:
                setSelected();
                tv_tab_menu_contacts.setSelected(true);
                tv_tab_menu_contacts_num.setVisibility(View.INVISIBLE);
                tv_tab_menu_contacts.setTextColor(this.getResources().getColor(R.color.colorNaviationClick));
                SelectFragment(3);
                break;
            case R.id.ly_tab_menu_me:
                setSelected();
                tv_tab_menu_me.setSelected(true);
                tv_tab_menu_me.setTextColor(this.getResources().getColor(R.color.colorNaviationClick));
                SelectFragment(4);
                break;
            case R.id.main_plus:
                MainPlusDialog mainPlusDialog = new MainPlusDialog(this);
                mainPlusDialog.update();
                mainPlusDialog.setBackgroundDrawable(new ColorDrawable(0000000000));
                mainPlusDialog.setOutsideTouchable(true);
                mainPlusDialog.setTouchable(true);
                mainPlusDialog.setFocusable(true);
                mainPlusDialog.showPopupWindow(main_plus);
                break;
        }
    }

    private void SelectFragment(int i) {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        hideAll(transaction);
        switch (i) {
            case 1:
                if (mConversationList == null) {
//                    message_fragment = new Message_Fragment();
                    mConversationList = initConversationList();
                    transaction.add(R.id.fragment_container, mConversationList);
                } else {
                    transaction.show(mConversationList);
                }

                Log.i("TAG", "进入message");
                break;
            case 2:
                if (Jobs_Fragment == null) {
                    Jobs_Fragment = new Jobs_Fragment();
//                    transaction.hide(Jobs_Fragment);
                    transaction.add(R.id.fragment_container, Jobs_Fragment);
                } else {
                    transaction.show(Jobs_Fragment);
                }

                Log.i("TAG", "进入jobs");
                break;
            case 3:
                if (Contacts_Fragment == null) {
                    Contacts_Fragment = new Contacts_Fragment();
                    transaction.add(R.id.fragment_container, Contacts_Fragment);
                } else {
                    transaction.show(Contacts_Fragment);
                }

                Log.i("TAG", "进入jobs");
                break;
            case 4:
                if (Me_Fragment == null) {
                    Me_Fragment = new Mine_Fragment();
                    transaction.add(R.id.fragment_container, Me_Fragment);
                } else {
                    transaction.show(Me_Fragment);
                }

                Log.i("TAG", "进入jobs");
                break;
        }
        transaction.commit();
    }

    private void hideAll(android.support.v4.app.FragmentTransaction ft) {
        if (mConversationList != null) {
            ft.hide(mConversationList);
        }
        if (Jobs_Fragment != null) {
            ft.hide(Jobs_Fragment);
        }
        if (Contacts_Fragment != null) {
            ft.hide(Contacts_Fragment);
        }
        if (Me_Fragment != null) {
            ft.hide(Me_Fragment);
        }
    }

    //
    private void SetIconIsTrue() {
        tv_tab_menu_msg.setSelected(ischeck);
        tv_tab_menu_msg_num.setVisibility(View.INVISIBLE);
        tv_tab_menu_msg.setTextColor(this.getResources().getColor(R.color.colorNaviationClick));
    }

    //设置进入的首页
    private void initFM() {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
//        mConversationList = initConversationList();

        if (mConversationList == null) {
            mConversationList = initConversationList();
//            mConversationList = new Message_Fragment();
            transaction.add(R.id.fragment_container, mConversationList);
            Log.i("TAG", "主Fragment");
            SetIconIsTrue();
        } else {
            transaction.show(mConversationList);
        }
        transaction.commit();
    }

    //将Android状态栏改变为沉浸样式
    private void SystemBarTranslucentType(Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);

        }

    }

    private Fragment initConversationList() {
        if (mConversationListFragment == null) {
            ConversationListFragment listFragment = new ConversationListFragment();
//            listFragment.setAdapter(new ConversationAdapter(RongContext.getInstance()));
            Uri uri;
//            if (isDebug) {
            uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                    .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                    .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//系统
                    .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "true")
                    .build();
//                mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
//                        Conversation.ConversationType.GROUP,
//                        Conversation.ConversationType.PUBLIC_SERVICE,
//                        Conversation.ConversationType.APP_PUBLIC_SERVICE,
//                        Conversation.ConversationType.SYSTEM,
//                        Conversation.ConversationType.DISCUSSION
//                };
//            } else {
//                uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
//                        .appendPath("conversationlist")
//                        .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
//                        .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
//                        .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
//                        .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
//                        .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
//                        .build();
//                mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
//                        Conversation.ConversationType.GROUP,
//                        Conversation.ConversationType.PUBLIC_SERVICE,
//                        Conversation.ConversationType.APP_PUBLIC_SERVICE,
//                        Conversation.ConversationType.SYSTEM
//                };
//            }
            listFragment.setUri(uri);
//            mConversationListFragment = listFragment;
            return listFragment;
        } else {
            return mConversationListFragment;
        }
    }

    @Override
    public UserInfo getUserInfo(String s) {
        Log.e(TAG, "用户新提供的S：" + s);
        Type listType = new TypeToken<List<TopContactsBean>>() {
        }.getType();
        Gson gson = new Gson();
        topContactsList = gson.fromJson(CommUtils.getFrientUserInfo(mContext), listType);
        if (topContactsList != null && topContactsList.size() > 0) {
            for (TopContactsBean i :
                    topContactsList) {
                Log.e(TAG, "内容提供者是否实现:" + i.getId() + "---s:" + s);
                if (i.getId().equals(s)) {
                    Uri uri = Uri.parse(ConstantValue.ImageFile + i.getLogo());
                    Log.e(TAG, "内容提供者URI:" + uri);
                    return new UserInfo(i.getId(), i.getFullname(), Uri.parse(ConstantValue.ImageFile + i.getLogo()));
                }
            }
        }
        return null;
    }

    @Override
    public Group getGroupInfo(String groupId) {
        Log.e(TAG, "获取群组ID：" + groupId);
        Type listType = new TypeToken<GroupListBean>() {
        }.getType();
        Gson gson = new Gson();
        String str = CommUtils.getGroupUserInfo(mContext);
        Log.e(TAG, "数据又出问题了：" + str);
        GroupListBean GroupAllBean = gson.fromJson(CommUtils.getGroupUserInfo(mContext), listType);
        ArrayList<GroupBean> GroupBeanList = GroupAllBean.getText();
        if (GroupBeanList != null && GroupBeanList.size() > 0) {
            for (GroupBean i : GroupBeanList) {
                if (i.getGID().equals(groupId)) {
                    return new Group(i.getGID(), i.getName(), Uri.parse(ConstantValue.ImageFile + i.getLogo()));
                }
            }
        }
        return null;
    }
}

