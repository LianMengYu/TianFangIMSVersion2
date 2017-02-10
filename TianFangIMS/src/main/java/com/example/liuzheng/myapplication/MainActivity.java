package com.example.liuzheng.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.liuzheng.myapplication.model.ParentModel;
import com.example.liuzheng.myapplication.utils.JSONUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {
    public static JSONUtils jsonUtils;
    private ArrayAdapter<String> adapter;
    private List<String> companyNames;
    private List<ParentModel> parentList;
    private ListView listView;

    public static MainActivity mainActivity;

    public static MainActivity getInstance() {
        return mainActivity;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivity = this;
        listView = (ListView) findViewById(R.id.lv);
        listView.setOnItemClickListener(this);
        initData();

    }

    private void initData() {
        jsonUtils = new JSONUtils();
        /**显示公司级别*/
        //获取公司的
        getCompanyName(jsonUtils.parentModelList);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, companyNames);
        listView.setAdapter(adapter);
    }

    private void getCompanyName(List<ParentModel> parentModelList) {
        companyNames = new ArrayList<>();
        parentList = new ArrayList<>();

        for (int i = 0; i < parentModelList.size(); i++) {
            if (parentModelList.get(i).getPid() == -1) {
                companyNames.add(parentModelList.get(i).getName());
                parentList.add(parentModelList.get(i));
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        intent.putExtra("name", parentList.get(position).getName());
        intent.putExtra("pid", parentList.get(position).getPid());
        Log.e("TAG", "onItemClick: " + parentList.get(position).getName());
        startActivity(intent);
    }
}
