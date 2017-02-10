package com.tianfangIMS.im.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.tianfangIMS.im.ConstantValue;
import com.tianfangIMS.im.R;
import com.tianfangIMS.im.bean.LoginBean;
import com.tianfangIMS.im.bean.SetSyncUserBean;
import com.tianfangIMS.im.dialog.LoadDialog;
import com.tianfangIMS.im.utils.AMUtils;
import com.tianfangIMS.im.utils.CommUtils;
import com.tianfangIMS.im.utils.MD5;
import com.tianfangIMS.im.utils.NToast;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by LianMengYu on 2016/12/29.
 * 登录页
 */

public class LoginActivity extends Activity implements View.OnClickListener, RongIM.UserInfoProvider {
    private final static String TAG = "LoginActivity";
    private static final int LOGIN = 5;
    private static final int GET_TOKEN = 6;
    private static final int SYNC_USER_INFO = 9;

    private TextView tx_forgetpassword;
    private String phoneString;
    private String passwordString;
    private ImageView img_login_clean_user, img_login_clean_password;
    private EditText et_login_user, et_login_password;
    private Button btn_login;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private Context mContext;
    private String loginToken;
    private String connectResultId;
    private LoginBean user;
    private List<LoginBean> mLoginBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mContext = this;
        SystemBarTranslucentType();//将Android状态栏改变为沉浸样式
        sp = getSharedPreferences("config", MODE_PRIVATE);
        editor = sp.edit();
        init();//初始化控件
    }

    private void SetSyncUserGroup(LoginBean bean) {
        String id = bean.getText().getId();
        OkGo.post(ConstantValue.SYNCUSERGROUP)
                .tag(this)
                .connTimeOut(10000)
                .readTimeOut(10000)
                .writeTimeOut(10000)
                .params("userid", id)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if (!TextUtils.isEmpty(s)) {
                            Gson gson = new Gson();
                            SetSyncUserBean syncUserBean = gson.fromJson(s, SetSyncUserBean.class);
                            if (syncUserBean.getCode().equals("200")) {
                                Log.e("Login", "返回消息：" + syncUserBean.getText());
                            } else {
                                NToast.shortToast(mContext, "同步群组失败");
                            }
                        }
                    }
                });
    }

    //初始化控件
    private void init() {
        tx_forgetpassword = (TextView) this.findViewById(R.id.tx_forgetpassword);
        img_login_clean_user = (ImageView) this.findViewById(R.id.img_login_clean_user_et);
        img_login_clean_password = (ImageView) this.findViewById(R.id.img_login_clean_password);
        et_login_user = (EditText) this.findViewById(R.id.et_login_user);
        et_login_password = (EditText) this.findViewById(R.id.et_login_password);
        btn_login = (Button) this.findViewById(R.id.btn_login);


        //根据Edittext输入的改变，隐藏或者显示输入框右边的清空button
        et_login_user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    img_login_clean_user.setVisibility(View.VISIBLE);
                } else {
                    img_login_clean_user.setVisibility(View.INVISIBLE);
                }

                if (s.length() == 11) {
                    AMUtils.onInactive(getApplicationContext(), et_login_user);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        String oldPhone = sp.getString(ConstantValue.SEALTALK_LOGING_PHONE, "");
        String oldPassword = sp.getString(ConstantValue.SEALTALK_LOGING_PASSWORD, "");
        if (!TextUtils.isEmpty(oldPhone) && !TextUtils.isEmpty(oldPassword)) {
            et_login_user.setText(oldPhone);
            et_login_password.setText(oldPassword);
        }

        if (getIntent().getBooleanExtra("kickedByOtherClient", false)) {
//            final AlertDialog dlg = new AlertDialog().Builder(LoginActivity.this).create();
//            dlg.show();
            //  注释掉的为掉线逻辑，现在暂未实现，保留后续
//            Window window = dlg.getWindow();
//            window.setContentView(R.layout.other_devices);
            Toast.makeText(getApplicationContext(), "您的账号在其他设备登录", Toast.LENGTH_SHORT).show();

//            TextView text = (TextView) window.findViewById(R.id.ok);
//            text.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dlg.cancel();
//                }
//            });
        }

        et_login_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.length() > 0) {
                    img_login_clean_password.setVisibility(View.VISIBLE);
                } else {
                    img_login_clean_password.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    img_login_clean_password.setVisibility(View.VISIBLE);
                } else {
                    img_login_clean_password.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //设置单机事件
        tx_forgetpassword.setOnClickListener(this);
        img_login_clean_user.setOnClickListener(this);
        img_login_clean_password.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        et_login_password.setOnClickListener(this);
        et_login_user.setOnClickListener(this);
        CommUtils.FilePath();//创建项目文件
    }

    private void setLogin() {
        phoneString = et_login_user.getText().toString().trim();
        passwordString = et_login_password.getText().toString().trim();

        if (TextUtils.isEmpty(phoneString)) {
            NToast.shortToast(getApplicationContext(), R.string.phone_number_is_null);
            return;
        }
        if (TextUtils.isEmpty(passwordString)) {
            NToast.shortToast(getApplicationContext(), R.string.password_is_null);
            return;
        }
        if (passwordString.contains(" ")) {
            NToast.shortToast(getApplicationContext(), R.string.password_cannot_contain_spaces);
            return;
        }
//                LoadDialog.show(mContext);
        editor.putBoolean("exit", false);
        editor.apply();
        String oldPhone = sp.getString(ConstantValue.SEALTALK_LOGING_PHONE, "");
        OkGo.post(ConstantValue.AFTERLOGIN)
                .tag(this)
                .connTimeOut(10000)
                .readTimeOut(10000)
                .writeTimeOut(10000)
                .params("account", et_login_user.getText().toString().trim())
                .params("userpwd", MD5.encrypt(et_login_password.getText().toString().trim()))
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        LoadDialog.show(mContext);
                    }

                    @Override
                    public void onAfter(String s, Exception e) {
                        super.onAfter(s, e);
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        if (!TextUtils.isEmpty(s)) {
                            Log.e("OnSuccess", "访问成功" + s);
                            Gson gson = new Gson();
                            user = gson.fromJson(s, LoginBean.class);
                            loginToken = user.getText().getToken();
                            CommUtils.saveUserInfo(mContext, gson.toJson(user));
                            if (user.getCode() == 1) {
                                if (!TextUtils.isEmpty(loginToken)) {
                                    RongIM.connect(loginToken, new RongIMClient.ConnectCallback() {
                                        @Override
                                        public void onTokenIncorrect() {
                                        }
                                        @Override
                                        public void onSuccess(String s) {
                                            LoadDialog.dismiss(mContext);
                                            SetSyncUserGroup(user);
                                            Log.e("OnSuccess", "访问成功" + s);
                                            connectResultId = s;
                                            Toast.makeText(getApplicationContext(), "成功", Toast.LENGTH_SHORT).show();
                                            Intent intent_login = new Intent();
                                            intent_login.setClass(LoginActivity.this, MainActivity.class);
                                            startActivity(intent_login);
                                        }

                                        @Override
                                        public void onError(RongIMClient.ErrorCode errorCode) {
                                            NToast.shortToast(mContext, "获取token失败");
                                            return;
                                        }
                                    });
                                } else {
                                    NToast.shortToast(mContext, "访问失败");
                                    return;
                                }

                            }
                            if (user.getCode() == 0) {
                                LoadDialog.dismiss(mContext);
                                Toast.makeText(getApplicationContext(), "账号密码错误，请重新输入", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        }
                        if (TextUtils.isEmpty(s)) {
                            LoadDialog.dismiss(mContext);
                            Toast.makeText(getApplicationContext(), "请求超时", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        LoadDialog.dismiss(mContext);
                        Toast.makeText(getApplicationContext(), "登录失败", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tx_forgetpassword://跳转至找回密码Activity
                Intent intent_forget = new Intent();
                intent_forget.setClass(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent_forget);
                break;
            case R.id.img_login_clean_user_et:
                et_login_user.getText().clear();
                break;
            case R.id.img_login_clean_password:
                et_login_password.getText().clear();
                break;
            case R.id.btn_login:
                setLogin();
                break;
            case R.id.et_login_user:

                break;
            case R.id.et_login_password:

                break;
        }
    }

    //将Android状态栏改变为沉浸样式
    private void SystemBarTranslucentType() {
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

    /**
     * 点击空白处隐藏键盘
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideKeyboard(View view, MotionEvent event) {
        if (view != null && (view instanceof EditText)) {
            int[] l = {0, 0};
            view.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + view.getHeight(),
                    right = left + view.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private LoginBean GetUesrBean() {
        Gson gson = new Gson();
        LoginBean bean = gson.fromJson(CommUtils.getUserInfo(mContext), LoginBean.class);
        return bean;
    }

    @Override
    public UserInfo getUserInfo(String s) {
        mLoginBeanList.add(GetUesrBean());
        if (mLoginBeanList != null && mLoginBeanList.size() > 0) {
            for (LoginBean i :
                    mLoginBeanList) {
                if (i.getText().getId().equals(s)) {
                    return new UserInfo(i.getText().getId(), i.getText().getFullname(), Uri.parse(ConstantValue.ImageFile + i.getText().getLogo()));
                }
            }
        }
        return null;
    }
}
