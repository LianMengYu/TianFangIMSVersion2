package com.tianfangIMS.im.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;
import com.tianfangIMS.im.R;

import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

/**
 * Created by LianMengYu on 2017/1/21.
 * 个人聊天设置页面
 */
public class PrivateChatDetailActivity extends BaseActivity {
    private static final String TAG = "PrivateChatDetailActivity";
    private UserInfo mUserInfo;
    private Conversation.ConversationType mConversationType;
    private String fromConversationId;
    private ImageView iv_user_photo;
    private TextView tv_user_name;
    private Context mContext;
    private ImageView iv_createGroup;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privatechatdetail_activity);
        mContext = this;
        setTitle("聊天信息");
        init();
        fromConversationId = getIntent().getStringExtra("TargetId");
        mConversationType = (Conversation.ConversationType) getIntent().getSerializableExtra("conversationType");
        if (!TextUtils.isEmpty(fromConversationId)) {
            mUserInfo = RongUserInfoManager.getInstance().getUserInfo(fromConversationId);
            updateUI();
        }

//        String str = mUserInfo.getPortraitUri().toString();
//        System.out.println("看看userinfo里都有什么:"+);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void init() {
        iv_user_photo = (ImageView) this.findViewById(R.id.iv_user_photo);
        tv_user_name = (TextView) this.findViewById(R.id.tv_conversationdetail_username);
        iv_createGroup = (ImageView) this.findViewById(R.id.iv_createGroup);
    }

    private void updateUI() {
        if (mUserInfo != null) {
            Picasso.with(mContext)
                    .load(mUserInfo.getPortraitUri())
                    .into(iv_user_photo);
//            iv_user_photo.setImageURI(mUserInfo.getPortraitUri());
            tv_user_name.setText(mUserInfo.getName());
//            initData();
//            getState(mUserInfo.getUserId());
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("PrivateChatDetail Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
