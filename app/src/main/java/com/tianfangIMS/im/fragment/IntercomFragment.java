package com.tianfangIMS.im.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.BitmapCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.squareup.picasso.Picasso;
import com.tianfangIMS.im.ConstantValue;
import com.tianfangIMS.im.R;
import com.tianfangIMS.im.bean.OneGroupBean;
import com.tianfangIMS.im.dialog.LoadDialog;
import com.tianfangIMS.im.utils.NToast;

import net.qiujuer.genius.blur.StackBlur;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentSkipListMap;

import io.rong.common.RLog;
import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import io.rong.ptt.JoinSessionCallback;
import io.rong.ptt.PTTClient;
import io.rong.ptt.PTTSession;
import io.rong.ptt.PTTSessionStateListener;
import io.rong.ptt.RequestToSpeakCallback;
import okhttp3.Call;
import okhttp3.Response;

import static com.xiaomi.push.service.aa.C;
import static com.xiaomi.push.service.aa.f;

/**
 * Created by LianMengYu on 2017/2/9.
 * 对讲fragment
 */

public class IntercomFragment extends BaseFragment implements View.OnClickListener, PTTSessionStateListener {
    private PTTClient pttClient;
    public static IntercomFragment Instance = null;
    private List<String> participants;

    public static IntercomFragment getInstance() {
        if (Instance == null) {
            Instance = new IntercomFragment();
        }
        return Instance;
    }

    ImageView main_call_blur;
    ImageView main_call_header;
    private Conversation.ConversationType mConversationType;
    ImageView main_call_free, main_call_flash, main_call_talk;
    private String userid;
    private UserInfo userInfo;
    private TextView intercom_name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.intercom_layout, container, false);
        Intent intent = getActivity().getIntent();
        main_call_blur = (ImageView) view.findViewById(R.id.main_call_blur);
        main_call_header = (ImageView) view.findViewById(R.id.main_call_header);
        main_call_free = (ImageView) view.findViewById(R.id.main_call_free);
        main_call_flash = (ImageView) view.findViewById(R.id.main_call_flash);
        main_call_talk = (ImageView) view.findViewById(R.id.main_call_talk);
        intercom_name = (TextView) view.findViewById(R.id.intercom_name);
        setListener();

//        main_call_blur.setImageBitmap(blur(getBitmapFromUri(userInfo.getPortraitUri()), 25f));
        mConversationType = Conversation.ConversationType.valueOf(intent.getData()
                .getLastPathSegment().toUpperCase(Locale.getDefault()));

        userid = intent.getData().getQueryParameter("targetId");

        //获取userinfo
        if (mConversationType == Conversation.ConversationType.PRIVATE) {
            userInfo = RongUserInfoManager.getInstance().getUserInfo(userid);
            if (userInfo != null) {
                Log.e("已经不等于空了：", "---:" + userInfo);
                intercom_name.setText(userInfo.getName());
                Log.e("intercom", "确实是否执行：" + userInfo.getName());
                getBitmap(userInfo.getPortraitUri().toString());
            }
        }
        if (mConversationType == Conversation.ConversationType.GROUP) {
            userid = intent.getData().getQueryParameter("targetId");
            GetGroupUserInfo();
            Log.e("intercom", "群组id" + userid);
        }
        RongExtension extension = new RongExtension(getActivity());
        pttClient = PTTClient.getInstance();
        PTTSession pttSession = pttClient.getCurrentPttSession();
        userid = RongIM.getInstance().getCurrentUserId();
        pttClient.joinSession(mConversationType, userid, new JoinSessionCallback() {
            @Override
            public void onSuccess(List<String> list) {
                Log.e("OnSuccess", "测试对讲是否连接成功");
            }

            @Override
            public void onError(String s) {
                Log.e("OnSuccess", "测试对讲是否连接成功");
            }
        });
        setListener();
        main_call_talk.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return requestToSpeak(v, event);
            }
        });
        return view;
    }

    private void GetGroupUserInfo() {
        OkGo.post(ConstantValue.GETONEGROUPINFO)
                .tag(this)
                .connTimeOut(10000)
                .readTimeOut(10000)
                .writeTimeOut(10000)
                .params("groupid", userid)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if (!TextUtils.isEmpty(s) && !s.equals("{}")) {
                            Gson gson = new Gson();
                            OneGroupBean oneGroupBean = gson.fromJson(s, OneGroupBean.class);
                            intercom_name.setText(oneGroupBean.getText().getName());
                            getBitmap(ConstantValue.ImageFile + oneGroupBean.getText().getLogo());
                            Log.e("intercom", "群组成员都有什么：" + oneGroupBean.getText().getName());
                        } else {
                            Log.e("intercom", "没有获取数据：" + s);
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Log.e("intercom", "返回数据错误" + call);
                    }
                });

    }

    private void getBitmap(String path) {
        OkGo.post(path)
                .tag(this)
                .execute(new BitmapCallback() {
//                    @Override
//                    public void onBefore(BaseRequest request) {
//                        super.onBefore(request);
//                        LoadDialog.show(getActivity());
//                    }

                    @Override
                    public void onSuccess(Bitmap bitmap, Call call, Response response) {
                        LoadDialog.dismiss(getActivity());
                        if (bitmap != null) {
                            Bitmap newBitmap = StackBlur.blur(bitmap, (int) 20, false);
                            main_call_blur.setImageBitmap(newBitmap);
                            main_call_header.setImageBitmap(bitmap);
                        }
                    }
                });
    }

    private void setListener() {
        main_call_free.setOnClickListener(this);
        main_call_flash.setOnClickListener(this);
        main_call_talk.setOnClickListener(this);
    }

//    private void SetInfos(Bitmap bitmap) {
//        Picasso.with(getActivity())
//        Log.e("intercom", "获取图片:" + userInfo.getPortraitUri());
//
//    }

    private Bitmap blur(Bitmap bitmap, float radius) {
        Bitmap output = Bitmap.createBitmap(bitmap); // 创建输出图片
        RenderScript rs = RenderScript.create(getActivity()); // 构建一个RenderScript对象
        ScriptIntrinsicBlur gaussianBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs)); // 创建高斯模糊脚本
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap); // 创建用于输入的脚本类型
        Allocation allOut = Allocation.createFromBitmap(rs, output); // 创建用于输出的脚本类型
        gaussianBlur.setRadius(radius); // 设置模糊半径，范围0f<radius<=25f
        gaussianBlur.setInput(allIn); // 设置输入脚本类型
        gaussianBlur.forEach(allOut); // 执行高斯模糊算法，并将结果填入输出脚本类型中
        allOut.copyTo(output); // 将输出内存编码为Bitmap，图片大小必须注意
        rs.finish();
        rs.destroy(); // 关闭RenderScript对象，API>=23则使用rs.releaseAllContexts()
        return output;
    }

    @Nullable
    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
    }

    //请求说话，抢麦
    boolean requestToSpeak(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            main_call_talk.setImageResource(R.mipmap.talk_voice_green_connect);
            pttClient.requestToSpeak(new RequestToSpeakCallback() {

                //抢麦成功
                @Override
                public void onReadyToSpeak(long maxDurationMillis) {
//                    updateMicHolder(RongIMClient.getInstance().getCurrentUserId());

                }

                //抢麦失败
                @Override
                public void onFail(String msg) {
                    RLog.e("onFail", "start speak error " + msg);
                }


                //说话超时，通过服务器设定时长，如果超过自动停止说话
                @Override
                public void onSpeakTimeOut() {
                    Toast.makeText(getActivity(), "speak time out", Toast.LENGTH_SHORT).show();
//                    updateMicHolder("");
                }
            });
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
            main_call_talk.setImageResource(R.drawable.talk_voice_normal);
//            micHolderTextView.setText(getString(io.rong.ptt.kit.R.string.rce_ptt_hold_to_request_mic));
//            micHolderImageView.setImageResource(io.rong.ptt.kit.R.drawable.rc_default_portrait);
            pttClient.stopSpeak();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_call_free:
                Toast.makeText(getActivity(), "点击了免提", Toast.LENGTH_SHORT).show();
                break;
            case R.id.main_call_flash:
                Toast.makeText(getActivity(), "点击了Flash", Toast.LENGTH_SHORT).show();
                break;
            case R.id.main_call_talk:
                Toast.makeText(getActivity(), "点击了对讲", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onMicHolderChanged(PTTSession pttSession, String s) {

    }

    @Override
    public void onParticipantChanged(PTTSession pttSession, List<String> list) {

    }

    @Override
    public void onNetworkError(String s) {

    }
}
