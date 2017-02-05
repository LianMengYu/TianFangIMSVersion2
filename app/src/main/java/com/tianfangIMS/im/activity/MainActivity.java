package com.tianfangIMS.im.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianfangIMS.im.ConstantValue;
import com.tianfangIMS.im.R;
import com.tianfangIMS.im.bean.LoginBean;
import com.tianfangIMS.im.dialog.MainPlusDialog;
import com.tianfangIMS.im.fragment.Contacts_Fragment;
import com.tianfangIMS.im.fragment.Jobs_Fragment;
import com.tianfangIMS.im.fragment.Message_Fragment;
import com.tianfangIMS.im.fragment.Mine_Fragment;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

/**
 * Created by LianMengYu on 2017/1/3.
 * 主要作为所有fragment的基类来使用
 */

public class MainActivity extends BaseActivity implements View.OnClickListener, RongIM.UserInfoProvider {
    private LinearLayout ly_tab_menu_msg, ly_tab_menu_job, ly_tab_menu_contacts, ly_tab_menu_me;
    private TextView tv_tab_menu_msg, tv_tab_menu_job, tv_tab_menu_contacts, tv_tab_menu_me;
    private TextView tv_tab_menu_msg_num, tv_tab_menu_job_num, tv_tab_menu_contacts_num;
    private ImageView img_tab_menu_setting_partner;
    private FrameLayout fragment_container;

    private LinearLayout ly_set_firstFragment;

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
    //    FragmentTransaction transaction;
//    FragmentManager fragmentManager;
    private android.support.v4.app.FragmentTransaction transaction;
    private android.support.v4.app.FragmentManager fragmentManager;

    private List<LoginBean> mLoginBeanList;

    private ImageView main_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mian_activity);
        setHeadVisibility(View.GONE);
        SystemBarTranslucentType(this);//改变状态栏的沉浸样式
        init();//初始化控件
        initFM();//初始化fragment
        RongIM.setUserInfoProvider(this, true);
        mLoginBeanList = new ArrayList<LoginBean>();
        mLoginBeanList.add(GetUesrBean());
        RongIM.setUserInfoProvider(this, true);

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
        Intent intent = this.getIntent();
        LoginBean bean = (LoginBean) intent.getSerializableExtra("loginBean");
        return bean;
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
//                tv_tab_menu_me_num.setVisibility(View.INVISIBLE);
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
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//群组
                    .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                    .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
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
        for (LoginBean i :
                mLoginBeanList) {
            if (i.getText().getId().equals(s)) {
                return new UserInfo(i.getText().getId(), i.getText().getFullname(), Uri.parse(ConstantValue.ImageFile + i.getText().getLogo()));
            }
        }
        return null;
    }

}

