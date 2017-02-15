package com.tianfangIMS.im.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.tianfangIMS.im.ConstantValue;
import com.tianfangIMS.im.R;
import com.tianfangIMS.im.adapter.GroupAdapter;
import com.tianfangIMS.im.bean.GroupBean;
import com.tianfangIMS.im.bean.LoginBean;
import com.tianfangIMS.im.bean.MineGroupBean;
import com.tianfangIMS.im.bean.MineGroupChildBean;
import com.tianfangIMS.im.dialog.LoadDialog;
import com.tianfangIMS.im.utils.CommonUtil;
import com.tianfangIMS.im.utils.NToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by LianMengYu on 2017/1/11.
 * 我的群组
 */

public class MineGroupActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "MineGroupActivity";
    private Context mContext;
    private List<MineGroupChildBean> mList = new ArrayList<MineGroupChildBean>();
    private List<Map<String, String>> ListGroup = new ArrayList<Map<String, String>>();
    private TextView tv_groupIsNull;
    ListView activity_group_lv_data;

    MineGroupBean mMineGroupBean;
    List<GroupBean> mGroupBeen;

    GroupAdapter mGroupAdapter;

    Gson mGson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        mContext = this;
        setTitle("我的群组");
        initView();
        GetGroupList();

    }

    public void initView() {
        activity_group_lv_data = (ListView) findViewById(R.id.activity_group_lv_data);
        activity_group_lv_data.setOnItemClickListener(this);
        tv_groupIsNull = (TextView) findViewById(R.id.tv_groupIsNull);
//        mListView = (ListView) this.findViewById(R.id.minegroup_list);

//        minegroup_list_ICreate = (ListView) this.findViewById(R.id.minegroup_list_ICreate);
//        minegroup_list_Ijoin = (ListView) this.findViewById(R.id.minegroup_list_Ijoin);
//        exlv_MineGroup = (ExpandableListView) this.findViewById(R.id.exlv_MineGroup);
    }

    private void GetGroupList() {
        Gson gson = new Gson();
        final LoginBean loginBean = gson.fromJson(CommonUtil.getUserInfo(mContext), LoginBean.class);
        String UID = loginBean.getText().getId();
        OkGo.post(ConstantValue.MINEGROUP)
                .tag(this)
                .connTimeOut(10000)
                .readTimeOut(10000)
                .writeTimeOut(10000)
                .params("userid", UID)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        LoadDialog.show(mContext);
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LoadDialog.dismiss(mContext);
                        Log.e(TAG, "json:---" + s);
                        if (!TextUtils.isEmpty(s)) {
                            Gson gson1 = new Gson();
                            Map<String, Object> map = gson1.fromJson(s, new TypeToken<Map<String, Object>>() {
                            }.getType());
                            if ((map.get("code").toString()).equals("1.0")) {
                                Log.e("打印返回数据", "你是什么:" + s);
                                mGson = new Gson();
                                mGroupBeen = new ArrayList<GroupBean>();
                                mMineGroupBean = mGson.fromJson(s, MineGroupBean.class);
                                GroupBean tmp = new GroupBean();
                                tmp.setName("我建的组");
                                tmp.setGID(String.valueOf(-1));
                                mGroupBeen.add(tmp);
                                mGroupBeen.addAll(mMineGroupBean.getText().getICreate());
                                tmp = new GroupBean();
                                tmp.setName("我加入的");
                                tmp.setGID(String.valueOf(-1));
                                mGroupBeen.add(tmp);
                                mGroupBeen.addAll(mMineGroupBean.getText().getIJoin());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mGroupAdapter = new GroupAdapter(mGroupBeen, mContext);
                                        activity_group_lv_data.setAdapter(mGroupAdapter);

                                    }
                                });
                            } else {
                                tv_groupIsNull.setText("群组为空");
                            }
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
        RongIM.getInstance().startGroupChat(mContext, mGroupBeen.get(position).getGID(), mGroupBeen.get(position).getName());
    }
}
