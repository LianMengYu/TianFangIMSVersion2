package com.tianfangIMS.im.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.tianfangIMS.im.R;
import com.tianfangIMS.im.utils.NToast;

import java.util.List;
import java.util.Locale;

import io.rong.common.RLog;
import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.ptt.JoinSessionCallback;
import io.rong.ptt.PTTClient;
import io.rong.ptt.PTTSession;
import io.rong.ptt.PTTSessionStateListener;
import io.rong.ptt.RequestToSpeakCallback;
import io.rong.ptt.kit.PTTSessionActivity;

import static com.tianfangIMS.im.R.id.main_call_blur;
import static com.tianfangIMS.im.R.id.main_call_flash;
import static com.tianfangIMS.im.R.id.main_call_free;
import static com.tianfangIMS.im.R.id.main_call_header;
import static com.tianfangIMS.im.R.id.main_call_talk;

/**
 * Created by LianMengYu on 2017/2/9.
 * 对讲fragment
 */

public class IntercomFragment extends BaseFragment implements View.OnClickListener,PTTSessionStateListener {
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
        main_call_blur.setImageBitmap(blur(BitmapFactory.decodeResource(getResources(), R.drawable.heiyan), 25f));
        mConversationType = Conversation.ConversationType.valueOf(intent.getData()
                .getLastPathSegment().toUpperCase(Locale.getDefault()));
        RongExtension extension = new RongExtension(getActivity());
        pttClient = PTTClient.getInstance();
        PTTSession pttSession = pttClient.getCurrentPttSession();
        userid = RongIM.getInstance().getCurrentUserId();
//        pttClient.joinSession(mConversationType, userid, new JoinSessionCallback() {
//            @Override
//            public void onSuccess(List<String> list) {
//                Log.e("OnSuccess","测试对讲是否连接成功");
//            }
//
//            @Override
//            public void onError(String s) {
//                NToast.shortToast(getActivity(),"连接失败");
//            }
//        });
//        setListener();
//        main_call_talk.setOnTouchListener(new View.OnTouchListener(){
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return requestToSpeak(v,event);
//            }
//        });
        return view;
    }

    private void setListener() {
        main_call_free.setOnClickListener(this);
        main_call_flash.setOnClickListener(this);
        main_call_talk.setOnClickListener(this);
    }

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
