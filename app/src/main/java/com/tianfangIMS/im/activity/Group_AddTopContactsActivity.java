package com.tianfangIMS.im.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.tianfangIMS.im.ConstantValue;
import com.tianfangIMS.im.R;
import com.tianfangIMS.im.adapter.AddTopContacts_GridView_Adapter;
import com.tianfangIMS.im.adapter.GroupTopContacts_GridView_Adapter;
import com.tianfangIMS.im.adapter.Group_AddTopContactsAdapter;
import com.tianfangIMS.im.bean.LoginBean;
import com.tianfangIMS.im.bean.TopContactsBean;
import com.tianfangIMS.im.bean.TopContactsRequestBean;
import com.tianfangIMS.im.dialog.LoadDialog;
import com.tianfangIMS.im.utils.CommUtils;
import com.tianfangIMS.im.utils.NToast;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by LianMengYu on 2017/2/5.
 * 用于创建群组是添加常用联系人
 */

public class Group_AddTopContactsActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private static final String TAG = "Group_AddTopContactsActivity";
    private Context mContext;
    private List<TopContactsBean> topContactsList = new ArrayList<TopContactsBean>();
    private ListView lv_topContacts;
    private RelativeLayout rl_selectAddGroupContacts_background;
    private GridView gv_addContacts;
    private List<TopContactsBean> allChecked;
    private Map<Integer, Boolean> checkedMap;
    private TextView tv_addfriend_submit;
    private List<TopContactsBean> list;
    private AddTopContacts_GridView_Adapter gridView_adapter;
    private Group_AddTopContactsAdapter group_addTopContactsAdapter;
    private GroupTopContacts_GridView_Adapter groupTopContacts_gridView_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtopcpntacts_activity);
        mContext = this;
        setTitle("选择群组联系人");
        init();
        GetCheckTopContacts();
    }

    private void init() {
        lv_topContacts = (ListView) this.findViewById(R.id.lv_addtopcontacts);
        rl_selectAddGroupContacts_background = (RelativeLayout) this.findViewById(R.id.rl_selectAddContacts_background);
        gv_addContacts = (GridView) this.findViewById(R.id.gv_addContacts);
        tv_addfriend_submit = (TextView) this.findViewById(R.id.tv_addfriend_submit);

        tv_addfriend_submit.setOnClickListener(this);
        lv_topContacts.setOnItemClickListener(this);
    }


    private void GetCheckTopContacts() {
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
                            group_addTopContactsAdapter = new Group_AddTopContactsAdapter(mContext, topContactsList);
                            lv_topContacts.setAdapter(group_addTopContactsAdapter);
                            group_addTopContactsAdapter.notifyDataSetChanged();

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
        Group_AddTopContactsAdapter.ViewHodler hodler = (Group_AddTopContactsAdapter.ViewHodler) view.getTag();
        hodler.cb_addfrien.toggle();
        rl_selectAddGroupContacts_background.setVisibility(View.VISIBLE);
        getCount();
    }

    //对GridView 显示的宽高经行设置
    private void SettingGridView(List<TopContactsBean> list) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;
        int size = list.size();//要显示数据的个数
        //gridview的layout_widht,要比每个item的宽度多出2个像素，解决不能完全显示item的问题
        int allWidth = (int) (82 * size * density);
        //int allWidth = (int) ((width / 3 ) * size + (size-1)*3);//也可以这样使用，item的总的width加上horizontalspacing
        int itemWidth = (int) (65 * density);//每个item宽度
        LinearLayout.LayoutParams params = new
                LinearLayout.LayoutParams(allWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        gv_addContacts.setLayoutParams(params);
        gv_addContacts.setColumnWidth(itemWidth);
        gv_addContacts.setHorizontalSpacing(3);
        gv_addContacts.setStretchMode(GridView.NO_STRETCH);
        gv_addContacts.setNumColumns(size);
    }

    private void getCount() {
        checkedMap = group_addTopContactsAdapter.getCheckedMap();//获取选中的人，true是选中的，false是没选中的
        allChecked = new ArrayList<TopContactsBean>();//创建一个存储选中的人的集合
        for (int i = 0; i < checkedMap.size(); i++) {
            if (checkedMap.get(i) == null) {    //防止出现空指针,如果为空,证明没有被选中
                continue;
            } else if (checkedMap.get(i)) {
                TopContactsBean testCheckBean = topContactsList.get(i);
                allChecked.add(testCheckBean);
                if (allChecked.size() == 0) {
                    tv_addfriend_submit.setText("添加（0）");
                } else {
                    tv_addfriend_submit.setText("添加（" + (allChecked.size()) + "）");
                }

            }
        }
        groupTopContacts_gridView_adapter = new GroupTopContacts_GridView_Adapter(mContext, allChecked);
        SettingGridView(allChecked);
        gv_addContacts.setAdapter(groupTopContacts_gridView_adapter);
        groupTopContacts_gridView_adapter.notifyDataSetChanged();
//        gridView_adapter = new AddTopContacts_GridView_Adapter(mContext, allChecked);
//        SettingGridView(allChecked);
//        gv_addContacts.setAdapter(gridView_adapter);
//        gridView_adapter.notifyDataSetChanged();
    }

    private void AddGroup() {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < allChecked.size(); i++) {
            list.add(allChecked.get(i).getId().toString());
        }
        Gson gson = new Gson();
        LoginBean loginBean = gson.fromJson(CommUtils.getUserInfo(mContext), LoginBean.class);
        list.add(loginBean.getText().getId());
        String UID = loginBean.getText().getId();
        String aa = list.toString();
        System.out.println("aaaaaaaaaaaaa:" + aa);
        OkGo.post(ConstantValue.CREATEGROUP)
                .tag(this)
                .connTimeOut(10000)
                .readTimeOut(10000)
                .writeTimeOut(10000)
                .params("userid", UID)
                .params("groupids", aa)
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
                            Gson gson = new Gson();
                            TopContactsRequestBean bean = gson.fromJson(s, TopContactsRequestBean.class);
                            if (bean.getCode().equals("200")) {
                                NToast.shortToast(mContext, "创建成功");
                                RongIM.getInstance().startGroupChat(mContext, bean.getText().getId(), bean.getText().getName());
//                                RongIM.getInstance().getRongIMClient().joinGroup(bean.getText().getId(), bean.getText().getName(), new RongIMClient.OperationCallback() {
//                                    @Override
//                                    public void onSuccess() {
//
//                                    }
//                                    @Override
//                                    public void onError(RongIMClient.ErrorCode errorCode) {
//
//                                    }
//                                });
                            } else {
                                NToast.shortToast(mContext, "创建失败");
                            }
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_addfriend_submit:
                AddGroup();
                break;
        }
    }
}
