package com.tianfangIMS.im.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.tianfangIMS.im.R;

/**
 * Created by LianMengYu on 2017/2/5.
 */

public class AddGroupActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "AddTopContacts_Activity";
    private RelativeLayout rl_group_topcontacts;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addgroup_layout);
        setTitle("选择联系人");
        init();
    }

    private void init() {
        rl_group_topcontacts = (RelativeLayout) this.findViewById(R.id.rl_group_topcontacts);
        rl_group_topcontacts.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_group_topcontacts:
                Intent intent = new Intent(AddGroupActivity.this, Group_AddTopContactsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("MainPlusDialog",TAG);
                intent.putExtras(bundle);
                startActivity(intent);
                break;

        }
    }
}
