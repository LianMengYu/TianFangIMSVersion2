package com.tianfangIMS.im.activity;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.tianfangIMS.im.R;
import com.tianfangIMS.im.dialog.LoadDialog;

/**
 * Created by LianMengYu on 2017/1/16.
 */

public class ConversationListActivity extends BaseActivity{
    private static final String TAG = ConversationListActivity.class.getSimpleName();
    private LoadDialog mDialog;
    private SharedPreferences sp;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversationlist_activity);
        mContext = this;
//        sp = getSharedPreferences("config", MODE_PRIVATE);
////        mDialog = new LoadDialog(mContext);
//        Intent intent = getIntent();
//
//        //push
//        if (intent.getData().getScheme().equals("rong") && intent.getData().getQueryParameter("push") != null) {
//
//            //通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
//            if (intent.getData().getQueryParameter("push").equals("true")) {
//                enterActivity();
//            }
//
//        } else {//通知过来
//            //程序切到后台，收到消息后点击进入,会执行这里
//            if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
//                enterActivity();
//            } else {
//                startActivity(new Intent(ConversationListActivity.this, MainActivity.class));
//                finish();
//            }
//        }
    }

//    private void enterActivity() {
//        String token = sp.getString("loginToken", "");
//        if (token.equals("default")) {
//            startActivity(new Intent(ConversationListActivity.this, LoginActivity.class));
//            finish();
//        } else {
//            if (mDialog != null && !mDialog.isShowing()) {
//                mDialog.show();
//            }
//            reconnect(token);
//        }
//    }
//    private void reconnect(String token) {
//        RongIM.connect(token, new RongIMClient.ConnectCallback() {
//            @Override
//            public void onTokenIncorrect() {
//                Log.e(TAG, "---onTokenIncorrect--");
//            }
//
//            @Override
//            public void onSuccess(String s) {
//                Log.i(TAG, "---onSuccess--" + s);
//                if (mDialog != null)
//                    mDialog.dismiss();
//                startActivity(new Intent(ConversationListActivity.this, MainActivity.class));
//                finish();
//            }
//
//            @Override
//            public void onError(RongIMClient.ErrorCode e) {
//                Log.e(TAG, "---onError--" + e);
//            }
//        });
//
//    }
//    @Override
//    public void initView() {
//    }
}
