package com.tianfangIMS.im.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.tianfangIMS.im.ConstantValue;
import com.tianfangIMS.im.R;
import com.tianfangIMS.im.bean.LoginBean;
import com.tianfangIMS.im.bean.MineGroupChildBean;
import com.tianfangIMS.im.bean.MineGroupParentBean;
import com.tianfangIMS.im.dialog.LoadDialog;
import com.tianfangIMS.im.utils.CommUtils;
import com.tianfangIMS.im.utils.NToast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by LianMengYu on 2017/1/11.
 * 我的群组
 */

public class MineGroupActivity extends BaseActivity {
    private static final String TAG = "MineGroupActivity";
    private Context mContext;
    private List<MineGroupChildBean> mList = new ArrayList<MineGroupChildBean>();
    private ListView mListView;
    private List<Map<String, String>> ListGroup = new ArrayList<Map<String, String>>();
    private ExpandableListView exlv_MineGroup;
    private List<Map<String, String>> Listitem = new ArrayList<Map<String, String>>();
    private ListView minegroup_list_ICreate;
    private ListView minegroup_list_Ijoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_activity);
        setData();
        mContext = this;
        setTitle("我的群组");
        initView();
        GetGroupList();

    }

    public void initView() {
//        mListView = (ListView) this.findViewById(R.id.minegroup_list);

//        minegroup_list_ICreate = (ListView) this.findViewById(R.id.minegroup_list_ICreate);
//        minegroup_list_Ijoin = (ListView)this.findViewById(R.id.minegroup_list_Ijoin);
        exlv_MineGroup = (ExpandableListView) this.findViewById(R.id.exlv_MineGroup);
    }

    private void setData() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("title", "IJoin");
        ListGroup.add(map);

        map = new HashMap<String, String>();
        map.put("title", "IJoin");
        ListGroup.add(map);
    }

    private void GetGroupList() {
        Gson gson = new Gson();
        final LoginBean loginBean = gson.fromJson(CommUtils.getUserInfo(mContext), LoginBean.class);
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
//                            Type listType = new TypeToken<Map<String, Object>>() {
//                            }.getType();
//
//                            Gson gson = new Gson();
//
//                            Map<String, Object> map = gson.fromJson(s, listType);
//                            Map<String,String> map1 = (Map<String,String>)map.get("text");
//                            List<Map<String,String>> list =new ArrayList<Map<String,String>>();
//                            list.add(map1);
//
//
//                            for (int i = 0; i < list.size(); i++) {
//                                Log.e(TAG, "解析有没有成功" + list.get(i));
//                            }
                            List<MineGroupParentBean> list = new ArrayList<MineGroupParentBean>();
                            Gson gson = new Gson();
                            Type listType = new TypeToken<List<MineGroupParentBean>>() {
                            }.getType();
                            MineGroupParentBean bean = gson.fromJson(s, listType);
                            list = gson.fromJson(s, listType);
                            Log.e(TAG, "看看解析成功没有:::" + bean);
                            for (int i = 0; i < list.size(); i++) {
                                Log.e(TAG, "看看解析成功没有:::" + list.get(i).getText().getICreate().get(i).getFullname());
                            }

//                            List<Map<String,String>> Plist =(List<Map<String,String>>)map.get("text");

//                            List<Map<String,String>> list = new ArrayList<Map<String, String>>();
//                            list =  (List<Map<String,String>>)map1.get("ICreate");
//                            list =  (List<Map<String,String>>)map1.get("IJoin");

//                            //首先把我创建的添加到ListGroup 和listitem中
//                            Map<String, String> mp = new HashMap<String, String>();
//                            mp.put("title","ICreate");
//                            Listitem.add(mp);//向子类里边添加
//                            ListGroup.add(mp);//想父类里边添加
//                            //把我创建的所有数据添加到子类
//                            Map<String, String> map12 = (Map<String, String>)map1.get("ICreate");
//                            Listitem.add(map12);
//                            mp = new HashMap<String, String>();
//
//                            //然后把我加入的添加到ListGroup 和listitem中
//                            mp.put("title","IJoin");
//                            Listitem.add(mp);//向子类里边添加
//                            ListGroup.add(mp);//想父类里边添加
//                            Map<String, String> map13 = (Map<String, String>)map1.get("IJoin");
//                            Listitem.add(map13);
//
//                            MineGroupListAdapter adapter = new MineGroupListAdapter(ListGroup,list,mContext);
//                            mListView.setAdapter(adapter);
//                            adapter.notifyDataSetChanged();

//
//
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
