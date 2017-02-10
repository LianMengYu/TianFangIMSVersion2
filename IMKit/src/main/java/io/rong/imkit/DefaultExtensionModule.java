package io.rong.imkit;

import android.view.KeyEvent;
import android.widget.EditText;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import io.rong.common.RLog;
import io.rong.imkit.emoticon.EmojiTab;
import io.rong.imkit.emoticon.IEmojiItemClickListener;
import io.rong.imkit.emoticon.IEmoticonTab;
import io.rong.imkit.plugin.CombineLocationPlugin;
import io.rong.imkit.plugin.DefaultLocationPlugin;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.plugin.ImagePlugin;
import io.rong.imkit.manager.InternalModuleManager;
import io.rong.imkit.widget.provider.FilePlugin;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

public class DefaultExtensionModule implements IExtensionModule {
    private final static String TAG = DefaultExtensionModule.class.getSimpleName();
    private EditText mEditText;

    public DefaultExtensionModule() {
    }

    @Override
    public void onInit(String appKey) {

    }

    @Override
    public void onConnect(String token) {

    }

    @Override
    public void onAttachedToExtension(RongExtension extension) {
        mEditText = extension.getInputEditText();
    }

    @Override
    public void onDetachedFromExtension() {
        mEditText = null;
    }

    @Override
    public void onReceivedMessage(Message message) {

    }

    @Override
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
        List<IPluginModule> pluginModuleList = new ArrayList<>();

        IPluginModule image = new ImagePlugin();
        IPluginModule file = new FilePlugin();

        pluginModuleList.add(image);
        try {
            String clsName = "com.amap.api.netlocation.AMapNetworkLocationClient";
            Class<?> locationCls = Class.forName(clsName);
            if (locationCls != null) {
                IPluginModule combineLocation = new CombineLocationPlugin();
                IPluginModule locationPlugin = new DefaultLocationPlugin();
                if (conversationType.equals(Conversation.ConversationType.PRIVATE)) {
                    pluginModuleList.add(combineLocation);
                } else {
                    pluginModuleList.add(locationPlugin);
                }
            }
        } catch (Exception e) {
            RLog.i(TAG, "Not include AMap");
            e.printStackTrace();
        }
        if (conversationType.equals(Conversation.ConversationType.GROUP) ||
                conversationType.equals(Conversation.ConversationType.DISCUSSION) ||
                conversationType.equals(Conversation.ConversationType.PRIVATE)) {
            pluginModuleList.addAll(InternalModuleManager.getInstance().getExternalPlugins(conversationType));
        }
        pluginModuleList.add(file);

        try {
            String clsName = "com.iflytek.cloud.SpeechUtility";
            Class<?> cls = Class.forName(clsName);
            if(cls != null) {
                cls = Class.forName("io.rong.recognizer.RecognizePlugin");
                Constructor<?> constructor = cls.getConstructor();
                IPluginModule recognizer = (IPluginModule) constructor.newInstance();
                pluginModuleList.add(recognizer);
            }
        } catch (Exception e) {
            RLog.i(TAG, "Not include Recognizer");
            e.printStackTrace();
        }

        return pluginModuleList;
    }

    @Override
    public List<IEmoticonTab> getEmoticonTabs() {
        EmojiTab emojiTab = new EmojiTab();
        emojiTab.setOnItemClickListener(new IEmojiItemClickListener() {
            @Override
            public void onEmojiClick(String emoji) {
                int start = mEditText.getSelectionStart();
                mEditText.getText().insert(start, emoji);
            }

            @Override
            public void onDeleteClick() {
                mEditText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
            }
        });
        List<IEmoticonTab> list = new ArrayList<>();
        list.add(emojiTab);
        return list;
    }

    @Override
    public void onDisconnect() {

    }
}
