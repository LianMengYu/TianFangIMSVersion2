package io.rong.ptt.kit;

import android.Manifest;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.emoticon.IEmoticonTab;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.utilities.PermissionCheckUtil;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.ptt.JoinSessionCallback;
import io.rong.ptt.PTTClient;
import io.rong.ptt.PTTSession;
import io.rong.ptt.PTTStateListener;

public class PTTExtensionModule implements IExtensionModule, PTTStateListener {

    private RongExtension rongExtension;
    private View currentNotificationView;
    private PTTClient pttClient;
    private Context context;

    private boolean sendExtraVoiceMessage;
    private long durationPerVoiceMessage;

    /**
     * @param context
     * @param sendExtraVoiceMessage   语音对讲的过程中，是否发送额外的语音消息
     * @param durationPerVoiceMessage 需要发送额外的语音消息时，单条语音消息的时长，但我是毫秒
     */
    public PTTExtensionModule(Context context, boolean sendExtraVoiceMessage, long durationPerVoiceMessage) {
        this.context = context.getApplicationContext();
        this.sendExtraVoiceMessage = sendExtraVoiceMessage;
        this.durationPerVoiceMessage = durationPerVoiceMessage;
    }

    @Override
    public void onInit(String appKey) {
        RongIM.registerMessageTemplate(new PTTStartMessageItemProvider());
        RongIM.registerMessageTemplate(new PTTEndMessageItemProvider());
        pttClient = PTTClient.getInstance();
    }

    @Override
    public void onConnect(String token) {
        pttClient.init(context, sendExtraVoiceMessage, durationPerVoiceMessage);
    }

    @Override
    public void onAttachedToExtension(RongExtension extension) {
        this.rongExtension = extension;
        if (pttClient == null) {
            return;
        }
        pttClient.setPttStateListener(this);
        PTTSession pttSession = pttClient.getPttSession(rongExtension.getConversationType(), rongExtension.getTargetId());
        if (pttSession != null) {
            showPTTNotificationView(pttSession);
        }
    }

    @Override
    public void onReceivedMessage(Message message) {
        // do nothing....
    }

    private void showPTTNotificationView(@NonNull final PTTSession pttSession) {
        if (rongExtension == null) {
            return;
        }
        final ConversationFragment conversationFragment = (ConversationFragment) rongExtension.getFragment(); // the cast is always successful
        rongExtension.post(new Runnable() {
            @Override
            public void run() {
                currentNotificationView = conversationFragment.inflateNotificationView(R.layout.rc_conversation_notification_container);
                TextView textView = (TextView) currentNotificationView.findViewById(R.id.rc_conversation_notification_textView);
                textView.setText(context.getString(R.string.rce_ptt_session_info, pttSession.getParticipantIds() == null ? 0 : pttSession.getParticipantIds().size()));

                currentNotificationView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {

                        if (!PermissionCheckUtil.checkPermissions(rongExtension.getContext(), new String[]{android.Manifest.permission.RECORD_AUDIO})) {
                            PermissionCheckUtil.requestPermissions(rongExtension.getFragment(), new String[]{Manifest.permission.RECORD_AUDIO});
                            return;
                        }
                        pttClient.joinSession(rongExtension.getConversationType(), rongExtension.getTargetId(), new JoinSessionCallback() {
                            @Override
                            public void onSuccess(List<String> users) {
                                if (rongExtension == null) {
                                    return;
                                }
//                                Context context = rongExtension.getContext();
////                                Intent intent = new Intent(context, PTTSessionActivity.class);
//                                Intent intent = new Intent();
//                                intent.setAction("com.android.action.MY_ACTION");
//                                intent.putExtra("pages",1);
//                                intent.putStringArrayListExtra("users", (ArrayList<String>) users);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                context.startActivity(intent);
//                                Toast.makeText(context,"-----------",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(String msg) {
                                // TODO
                            }
                        });
                    }
                });
                conversationFragment.showNotificationView(currentNotificationView);
            }
        });
    }

    private void updatePTTNotificationView(final PTTSession pttSession) {
        if (rongExtension == null || pttSession == null) {
            return;
        }
        rongExtension.post(new Runnable() {
            @Override
            public void run() {
                if (currentNotificationView == null) {
                    showPTTNotificationView(pttSession);
                    return;
                }
                TextView textView = (TextView) currentNotificationView.findViewById(R.id.rc_conversation_notification_textView);
                textView.setText(context.getString(R.string.rce_ptt_session_info, pttSession.getParticipantIds() == null ? 0 : pttSession.getParticipantIds().size()));
            }
        });
    }

    private void hideNotificationView() {
        if (rongExtension == null) {
            return;
        }
        rongExtension.post(new Runnable() {
            @Override
            public void run() {
                ConversationFragment conversationFragment = (ConversationFragment) rongExtension.getFragment(); // the cast is always successful
                conversationFragment.hideNotificationView(currentNotificationView);
                currentNotificationView = null;
            }
        });

    }

    @Override
    public void onDetachedFromExtension() {
        PTTSession pttSession = pttClient.getActiveSession();
        if (pttSession != null
                && pttSession.getConversationType() == rongExtension.getConversationType()
                && rongExtension.getTargetId().equals(pttSession.getTargetId())) {
            pttClient.leaveSession();
            Toast.makeText(context, context.getString(R.string.rce_ptt_end), Toast.LENGTH_SHORT).show();
        }
        pttClient.setPttStateListener(null);
        this.rongExtension = null;
        this.currentNotificationView = null;
    }

    @Override
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
        List<IPluginModule> pluginModules = new ArrayList<>();
        pluginModules.add(new PTTActionPlugin());
        return pluginModules;
    }

    @Override
    public List<IEmoticonTab> getEmoticonTabs() {
        return null;
    }

    @Override
    public void onDisconnect() {
        pttClient.unInit();
    }

    @Override
    public void onSessionStart(PTTSession pttSession) {
        if (shouldHandlePTTSession(pttSession)) {
            showPTTNotificationView(pttSession);
        }
    }

    @Override
    public void onSessionTerminated(PTTSession pttSession) {
        if (shouldHandlePTTSession(pttSession)) {
            hideNotificationView();
        }
    }

    @Override
    public void onParticipantChanged(PTTSession pttSession, List<String> userIds) {
        if (shouldHandlePTTSession(pttSession)) {
            updatePTTNotificationView(pttSession);
        }
    }

    @Override
    public void onMicHolderChanged(PTTSession pttSession, String holderUserId) {
        if (shouldHandlePTTSession(pttSession)) {
            updatePTTNotificationView(pttSession);
        }

    }

    @Override
    public void onNetworkError(String msg) {

    }

    /**
     * 只处理和当前消息会话相关的pttSession
     *
     * @param pttSession
     * @return
     */
    private boolean shouldHandlePTTSession(PTTSession pttSession) {
        if (rongExtension != null &&
                PTTClient.genPttSessionKey(rongExtension.getConversationType(), rongExtension.getTargetId()).equals(pttSession.key())) {
            return true;
        }

        return false;
    }

}
