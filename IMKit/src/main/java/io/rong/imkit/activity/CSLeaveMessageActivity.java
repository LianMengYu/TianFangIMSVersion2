package io.rong.imkit.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.rong.common.RLog;
import io.rong.eventbus.EventBus;
import io.rong.imkit.R;
import io.rong.imkit.RongIM;
import io.rong.imkit.utilities.RongUtils;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.CSLMessageItem;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.CSLeaveMessage;
import io.rong.message.InformationNotificationMessage;

public class CSLeaveMessageActivity extends Activity {
    private static final String TAG = "CSLeaveMessageActivity";
    private ArrayList<EditText> mEditList = new ArrayList<>();
    private String mTargetId;
    private ArrayList<CSLMessageItem> mItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rc_cs_leave_message);

        mTargetId = getIntent().getStringExtra("targetId");
        if (getIntent().getBooleanExtra("csTerminal", false)) {
            finish();
            return;
        }
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mItemList = bundle.getParcelableArrayList("itemList");
        }

        TextView cancelBtn = (TextView) findViewById(R.id.rc_btn_cancel);

        LinearLayout container = (LinearLayout) findViewById(R.id.rc_content);
        addItemToContainer(container);

        TextView submitBtn = (TextView) findViewById(R.id.rc_submit_message);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isContentValid()) {
                    Map<String, String> map = new HashMap<>();
                    for (EditText editText : mEditList) {
                        String name = (String) editText.getTag();
                        map.put(name, editText.getText().toString());
                    }
                    CSLeaveMessage message = new CSLeaveMessage();
                    message.setDataSet(map);
                    RongIMClient.getInstance().sendMessage(Message.obtain(mTargetId, Conversation.ConversationType.CUSTOMER_SERVICE, message), null, null, new IRongCallback.ISendMediaMessageCallback() {
                        @Override
                        public void onProgress(Message message, int progress) {

                        }

                        @Override
                        public void onCanceled(Message message) {

                        }

                        @Override
                        public void onAttached(Message message) {

                        }

                        @Override
                        public void onSuccess(Message message) {
                            InformationNotificationMessage notificationMessage = new InformationNotificationMessage(getResources().getString(R.string.rc_cs_message_submited));
                            RongIM.getInstance().insertMessage(Conversation.ConversationType.CUSTOMER_SERVICE, mTargetId, RongIMClient.getInstance().getCurrentUserId(), notificationMessage, null);
                            finish();
                        }

                        @Override
                        public void onError(Message message, RongIMClient.ErrorCode errorCode) {

                        }
                    });
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addItemToContainer(LinearLayout parent) {
        LinearLayout.LayoutParams params;
        if (mItemList == null)
            return;

        for (int i = 0; i < mItemList.size(); i++) {
            CSLMessageItem item = mItemList.get(i);
            LinearLayout itemContainer = new LinearLayout(this);
            if (item.getType().equals("text")) {
                params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, RongUtils.dip2px(45));
                itemContainer.setOrientation(LinearLayout.HORIZONTAL);
            } else {
                params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                itemContainer.setOrientation(LinearLayout.VERTICAL);
            }
            if (i > 0) {
                params.setMargins(0, RongUtils.dip2px(1), 0, 0);
            }
            itemContainer.setBackgroundColor(Color.WHITE);
            itemContainer.setLayoutParams(params);

            TextView view = new TextView(this);
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, RongUtils.dip2px(45));
            params.setMargins(RongUtils.dip2px(14), 0, 0, 0);
            view.setLayoutParams(params);
            view.setTextColor(getResources().getColor(R.color.rc_text_color_primary));
            view.setTextSize(16);
            view.setGravity(Gravity.CENTER_VERTICAL);
            view.setText(item.getTitle());
            itemContainer.addView(view);


            EditText editText = new EditText(this);
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            editText.setHint(item.getDefaultText());
            editText.setBackgroundColor(0);
            if (item.getType().equals("text")) {
                params.setMargins(RongUtils.dip2px(10), 0, RongUtils.dip2px(14), 0);
                editText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                editText.setMaxLines(1);
                editText.setMaxEms(20);
                editText.setSingleLine();
            } else {
                params.setMargins(0, 0, 0, 0);
                editText.setGravity(Gravity.TOP | Gravity.LEFT);
                editText.setPadding(RongUtils.dip2px(14), 0, 0, 0);
                editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                editText.setMinLines(3);
                editText.setMaxEms(item.getMax());
                editText.setVerticalScrollBarEnabled(true);
                editText.setMaxLines(3);
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(item.getMax())});
            }
            editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.rc_conversation_item_name_size));
            editText.setTextColor(getResources().getColor(R.color.rc_text_color_primary));
            editText.setTag(item.getName());
            editText.setLayoutParams(params);
            mEditList.add(editText);
            itemContainer.addView(editText);

            parent.addView(itemContainer);
        }
    }

    /**
     * 检查输入的内容是否符合要求。
     */
    public boolean isContentValid() {
        for (EditText editText : mEditList) {
            String tag = (String) editText.getTag();
            if (tag == null) {
                RLog.i(TAG, "tag is null !");
                return false;
            }
            CSLMessageItem config = getItemConfig(tag);
            if (config == null) {
                RLog.i(TAG, "config is null !");
                return false;
            }
            if (config.isRequired() && TextUtils.isEmpty(editText.getText().toString())) {
                Toast.makeText(getBaseContext(), config.getMessage().get(CSLMessageItem.RemindType.EMPTY.getName()), Toast.LENGTH_SHORT).show();
                return false;
            } else if (config.getVerification() != null && editText.getText().length() > 0) {
                boolean isValid = true;
                if (config.getVerification().equals("phone")) {
                    isValid = isMobile(editText.getText().toString());
                } else if (config.getVerification().equals("email")) {
                    isValid = isEmail(editText.getText().toString());
                }
                if (!isValid) {
                    Toast.makeText(getBaseContext(), config.getMessage().get(CSLMessageItem.RemindType.WRONG_FORMAT.getName()), Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else if (config.getMax() > 0 && editText.length() > config.getMax()) {
                Toast.makeText(getBaseContext(), config.getMessage().get(CSLMessageItem.RemindType.OVER_LENGTH.getName()), Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public CSLMessageItem getItemConfig(String name) {
        for (CSLMessageItem item : mItemList) {
            if (item.getName().equals(name))
                return item;
        }
        return null;
    }

    /**
     * 通过正则验证是否是合法手机号码
     *
     * @param phoneNumber
     * @return
     */
    private boolean isMobile(String phoneNumber) {
        String MOBLIE_PHONE_PATTERN = "^((13[0-9])|(15[0-9])|(18[0-9])|(14[7])|(17[0|6|7|8]))\\d{8}$";
        Pattern p = Pattern.compile(MOBLIE_PHONE_PATTERN);
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
    }

    /**
     * 通过正则验证邮箱格式是否正确
     *
     * @param email 邮箱地址
     * @return 验证结果
     */
    public boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.getBooleanExtra("csTerminal", false)) {
            String text = intent.getStringExtra("msg");
            showDialog(text);
        }
        super.onNewIntent(intent);
    }

    private void showDialog(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.rc_cs_alert_warning);
        TextView tv = (TextView) window.findViewById(R.id.rc_cs_msg);
        tv.setText(text);
        window.findViewById(R.id.rc_btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
            }
        });
    }
}
