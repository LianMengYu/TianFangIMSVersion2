package io.rong.ptt.kit;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.rong.common.RLog;
import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.utilities.PermissionCheckUtil;
import io.rong.ptt.JoinSessionCallback;
import io.rong.ptt.PTTClient;

public class PTTActionPlugin implements IPluginModule {

    private static final String TAG = "PTTActionPlugin";

    @Override
    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.rc_ic_files_selector);
    }

    @Override
    public String obtainTitle(Context context) {
        return context.getString(R.string.rce_ptt);
    }

    @Override
    public void onClick(Fragment currentFragment, final RongExtension extension) {
        if (!PermissionCheckUtil.checkPermissions(currentFragment.getActivity(), new String[]{android.Manifest.permission.RECORD_AUDIO})) {
            PermissionCheckUtil.requestPermissions(currentFragment, new String[]{Manifest.permission.RECORD_AUDIO});
            return;
        }

        final Context context = currentFragment.getActivity();

        PTTClient pttKitManager = PTTClient.getInstance();
        pttKitManager.joinSession(extension.getConversationType(), extension.getTargetId(), new JoinSessionCallback() {
            @Override
            public void onSuccess(List<String> users) {
                Intent intent = new Intent(context, PTTSessionActivity.class);
                intent.putStringArrayListExtra("users", users == null ? null : new ArrayList<String>(users));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

            @Override
            public void onError(final String msg) {
                RLog.e(TAG, msg);
                if (extension != null) {
                    Toast.makeText(context, "JoinSession failed: " + msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }
}
