package com.tianfangIMS.im.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.tianfangIMS.im.ConstantValue;
import com.tianfangIMS.im.R;
import com.tianfangIMS.im.adapter.TopContactsAdapter;
import com.tianfangIMS.im.bean.LoginBean;
import com.tianfangIMS.im.bean.TopContactsListBean;
import com.tianfangIMS.im.dialog.LoadDialog;
import com.tianfangIMS.im.utils.CommonUtil;
import com.tianfangIMS.im.utils.NToast;

import io.rong.imkit.RongIM;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by LianMengYu on 2017/2/3.
 * 常用联系人
 */

public class MineTopContactsActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "MineTopContactsActivity";
    private Context mContext;
    private ListView lv_topContacts;
    private TopContactsAdapter topContactsAdapter;
    private TopContactsListBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_topcontacts_activity);
        setTitle("选择常用联系人");
        mContext = this;
        initView();
        GetTopContacts();
    }

    private void initView() {
        lv_topContacts = (ListView) this.findViewById(R.id.lv_group_addtopcontacts);
        lv_topContacts.setOnItemClickListener(this);
    }


    private void GetTopContacts() {
        Gson gson = new Gson();
        LoginBean loginBean = gson.fromJson(CommonUtil.getUserInfo(mContext), LoginBean.class);
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
//                            Type listType = new TypeToken<TopContactsListBean>() {
//                            }.getType();
                            Gson gson = new Gson();
                            bean = gson.fromJson(s, TopContactsListBean.class);
//
////                            List<Object> list =(List<Object>) bean.getText();
//
//                            String str = bean.getText().toString();
//                            Gson gson1 = new Gson();
//                            Type listType = new TypeToken<List<Map<String,Object>>>() {
//                            }.getType();
//
//                            List<Map<String,ContactsTopBean>> contactsTopBean = gson1.fromJson(str, listType);
//                            Log.e(TAG, "获取了什么数据:" + contactsTopBean);
                            topContactsAdapter = new TopContactsAdapter(bean, mContext);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RongIM.getInstance().startPrivateChat(mContext, bean.getText().get(position).getId(), bean.getText().get(position).getFullname());
    }
}
