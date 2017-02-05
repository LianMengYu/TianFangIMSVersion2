package com.tianfangIMS.im.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.tianfangIMS.im.ConstantValue;
import com.tianfangIMS.im.R;
import com.tianfangIMS.im.adapter.TopContactsAdapter;
import com.tianfangIMS.im.bean.LoginBean;
import com.tianfangIMS.im.bean.TopContactsBean;
import com.tianfangIMS.im.dialog.LoadDialog;
import com.tianfangIMS.im.utils.CommUtils;
import com.tianfangIMS.im.utils.NToast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by LianMengYu on 2017/2/3.
 * 常用联系人
 */

public class MineTopContactsActivity extends BaseActivity {
    private static final String TAG = "MineTopContactsActivity";
    private Context mContext;
    private ListView lv_topContacts;
    private TopContactsAdapter topContactsAdapter;
    private List<TopContactsBean> topContactsList = new ArrayList<TopContactsBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_topcontacts_activity);
        mContext = this;
        initView();
        GetTopContacts();
    }

    private void initView() {
        lv_topContacts = (ListView) this.findViewById(R.id.lv_topcontacts);
    }

    private void GetTopContacts() {
        Gson gson = new Gson();
        LoginBean loginBean = gson.fromJson(CommUtils.getUserInfo(mContext), LoginBean.class);
        String UID = loginBean.getText().getAccount();
        OkGo.post(ConstantValue.GETCONTACTSLIST)
                .tag(this)
                .connTimeOut(10000)
                .readTimeOut(10000)
                .writeTimeOut(10000)
                .params("account", UID)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        LoadDialog.show(mContext);
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LoadDialog.dismiss(mContext);
                        if (!TextUtils.isEmpty(s)) {
                            Type listType = new TypeToken<List<TopContactsBean>>() {
                            }.getType();
                            Gson gson = new Gson();
                            topContactsList = gson.fromJson(s, listType);
                            topContactsAdapter = new TopContactsAdapter(mContext, topContactsList);
                            lv_topContacts.setAdapter(topContactsAdapter);
                            topContactsAdapter.notifyDataSetChanged();
                        } else {
                            return;
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        LoadDialog.dismiss(mContext);
                        NToast.shortToast(mContext, "请求失败");
                        return;
                    }
                });
    }
}
