package com.tianfangIMS.im.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.tianfangIMS.im.ConstantValue;
import com.tianfangIMS.im.R;
import com.tianfangIMS.im.activity.MineGroupActivity;
import com.tianfangIMS.im.activity.MineTopContactsActivity;
import com.tianfangIMS.im.activity.SecondActivity;
import com.tianfangIMS.im.dialog.LoadDialog;
import com.tianfangIMS.im.utils.JSONUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by LianMengYu on 2017/1/4.
 */

public class Contacts_Fragment extends BaseFragment implements View.OnClickListener {
    private RelativeLayout rl_mine_contacts;
    private LinearLayout ly_company_name;
    private RelativeLayout rl_mine_topcontacts;
    private TextView tv_company_name;
    public static JSONUtils jsonUtils;
    private String name;
    private int pid;
    public static Contacts_Fragment contacts_fragment;


    public static Contacts_Fragment getInstance() {
        return contacts_fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contacts_fragment, container, false);
        initView(view);
        GetData();
        contacts_fragment = this;
        jsonUtils = new JSONUtils();
        return view;
    }

    private void initView(View view) {
        rl_mine_contacts = (RelativeLayout) view.findViewById(R.id.rl_mine_contacts);
        ly_company_name = (LinearLayout) view.findViewById(R.id.ly_company_name);
        rl_mine_topcontacts = (RelativeLayout) view.findViewById(R.id.rl_mine_topcontacts);
        tv_company_name = (TextView) view.findViewById(R.id.tv_company_name);

        rl_mine_topcontacts.setOnClickListener(this);
        ly_company_name.setOnClickListener(this);
        rl_mine_contacts.setOnClickListener(this);
    }

    private void GetData() {
        OkGo.post(ConstantValue.DEPARTMENTPERSON)
                .tag(this)
                .connTimeOut(10000)
                .readTimeOut(10000)
                .writeTimeOut(10000)
                .execute(new StringCallback() {
                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        LoadDialog.show(getActivity());
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LoadDialog.dismiss(getActivity());
                        if (!TextUtils.isEmpty(s)) {
                            Gson gson = new Gson();
                            List<Map<String, String>> list = gson.fromJson(s, new TypeToken<ArrayList<Map<String, String>>>() {
                            }.getType());
                            for (int i = 0; i < list.size(); i++) {
                                if ((list.get(i).get("pid")).equals("-1")) {
                                    tv_company_name.setText(list.get(i).get("name"));
                                    name = list.get(i).get("name");
                                    String str = list.get(i).get("pid");
                                    pid = Integer.parseInt(str);
                                }
                            }
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_mine_contacts:
                startActivity(new Intent(getActivity(), MineGroupActivity.class));
                break;
            case R.id.ly_company_name:
//                startActivity(new Intent(getActivity(), Contacts_DepartmentActivity.class));
                Intent intent = new Intent(getActivity(), SecondActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("pid", pid);
                startActivity(intent);
                break;
            case R.id.rl_mine_topcontacts:
                startActivity(new Intent(getActivity(), MineTopContactsActivity.class));
                break;
        }
    }
}
