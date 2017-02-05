package com.tianfangIMS.im.activity;

import android.os.Bundle;

import com.tianfangIMS.im.R;

import io.rong.imlib.model.Conversation;

/**
 * Created by LianMengYu on 2017/1/21.
 */
public class PrivateChatDetailActivity extends BaseActivity{
    private Conversation.ConversationType mConversationType;
    private String fromConversationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privatechatdetail_activity);
        setTitle("聊天信息");
        fromConversationId = getIntent().getStringExtra("TargetId");
        mConversationType = (Conversation.ConversationType) getIntent().getSerializableExtra("conversationType");
    }
}
