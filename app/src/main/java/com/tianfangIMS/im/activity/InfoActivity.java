package com.tianfangIMS.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tianfangIMS.im.R;
import com.tianfangIMS.im.adapter.InfoAdapter;
import com.tianfangIMS.im.bean.TreeInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imkit.RongIM;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by LianMengYu on 2017/2/7.
 * 组织结构列表页
 */

public class InfoActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    Gson mGson;
    OkHttpClient mClient;
    Request mRequest;

    HashMap<Integer, HashMap<Integer, TreeInfo>> maps;
    List<TreeInfo> mTreeInfos;
    List<Integer> childCount;
    HashMap<Integer, TreeInfo> map;

    ListView activity_info_lv_part;

    InfoAdapter mAdapter;

    //树节点深度
    int currentLevel, parentLevel;
    Boolean flag;
    List<TreeInfo> clickHistory;
    LinearLayout activity_info_ll_indicator;

    int workerCount;

    TreeInfo mTreeInfo;

    LinearLayout activity_info_ll_header;
    TextView activity_info_tv_header;

    Button activity_info_btn_tree;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            transfer();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        maps = (HashMap<Integer, HashMap<Integer, TreeInfo>>) getIntent().getSerializableExtra("maps");
        currentLevel = getIntent().getIntExtra("currentLevel", -1);
        parentLevel = getIntent().getIntExtra("parentLevel", -1);
        flag = getIntent().getBooleanExtra("IsBoolean",false);
        Log.e("打印传递数据:","aaaaa:"+flag);
        activity_info_btn_tree = (Button) findViewById(R.id.activity_info_btn_tree);
        activity_info_btn_tree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(InfoActivity.this, TreeActivity.class);
                mIntent.putExtra("map", maps);
                startActivityForResult(mIntent, 100);
            }
        });

        activity_info_lv_part = (ListView) findViewById(R.id.activity_info_lv_part);
        activity_info_ll_indicator = (LinearLayout) findViewById(R.id.activity_info_ll_indicator);
        activity_info_ll_header = (LinearLayout) findViewById(R.id.activity_info_ll_header);
        activity_info_tv_header = (TextView) findViewById(R.id.activity_info_tv_header);
        activity_info_tv_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        activity_info_lv_part.setOnItemClickListener(this);

        clickHistory = new ArrayList<TreeInfo>();
        mTreeInfos = new ArrayList<>();
        childCount = new ArrayList<Integer>();
        mAdapter = new InfoAdapter(InfoActivity.this, mTreeInfos, childCount,flag);
        if(flag == true){

        }
        activity_info_lv_part.setAdapter(mAdapter);
        for (TreeInfo info : maps.get(parentLevel).values()) {
            if (info.getId() == currentLevel) {
                clickHistory.add(info);
                break;
            }
        }
        transfer();
    }

    private void transfer() {
        //清除适配数据集合
        mTreeInfos.clear();
        childCount.clear();
        //得到下一级部门的数据集合
        map = maps.get(currentLevel);
        //如果没有子部门 直接进行提示
        if (map == null) {
            Toast.makeText(this, "没有子部门", Toast.LENGTH_SHORT).show();
            onBackPressed();
            return;
        }
        //移除位置指示
        activity_info_ll_indicator.removeAllViews();
        float density = getResources().getDisplayMetrics().density;
        if (clickHistory.size() >= 2) {
            activity_info_ll_header.setVisibility(View.GONE);
        } else {
            activity_info_ll_header.setVisibility(View.VISIBLE);
        }
        //重新进行位置指示数据添加
        for (TreeInfo treeInfo : clickHistory) {
            TextView mTextView = new TextView(this);
            mTextView.setText(treeInfo.getName());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins((int) (16 * density), (int) (16 * density), (int) (16 * density), (int) (16 * density));
            mTextView.setLayoutParams(lp);
            mTextView.setTag(treeInfo);
            mTextView.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTreeInfo = (TreeInfo) v.getTag();
                    int index = clickHistory.indexOf(mTreeInfo);
                    if (index == -1) {
                        clickHistory.clear();
                        currentLevel = 0;
                    } else {
                        clickHistory = clickHistory.subList(0, index + 1);
                        currentLevel = mTreeInfo.getId();
                    }
                    transfer();
                }
            });
            activity_info_ll_indicator.addView(mTextView);
            TextView symbol = new TextView(this);
            symbol.setText(">");
            lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER_VERTICAL;
            symbol.setLayoutParams(lp);
            activity_info_ll_indicator.addView(symbol);
        }
        //移除最后的方向指示 && 改变最后(即当前部门)颜色
        if (activity_info_ll_indicator.getChildCount() > 0) {
            activity_info_ll_indicator.removeViewAt(activity_info_ll_indicator.getChildCount() - 1);
            ((TextView) activity_info_ll_indicator.getChildAt(activity_info_ll_indicator.getChildCount() - 1)).setTextColor(getResources().getColor(android
                    .R.color.darker_gray));
        }
        //将Map数据集合转换为List
        for (TreeInfo treeInfo : map.values()) {
            mTreeInfos.add(treeInfo);
        }
        //根据部门编号 进行排序
        Collections.sort(mTreeInfos, new Comparator<TreeInfo>() {
            @Override
            public int compare(TreeInfo o1, TreeInfo o2) {
                return o1.getId() < o2.getId() ? -1 : 1;
            }
        });
        //显示Item后的子部门人数
        for (TreeInfo treeInfo : mTreeInfos) {
            workerCount = 0;
            //员工类型
            if (treeInfo.getFlag() == 1) {
                childCount.add(workerCount);
            } else {
                //部门类型
                calcSum(maps.get(treeInfo.getId()));
                childCount.add(workerCount);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mTreeInfo = mTreeInfos.get(position);
        //点击的选项为员工类型
        if (mTreeInfo.getFlag() == 1) {
            RongIM.getInstance().startPrivateChat(mContext, String.valueOf(mTreeInfo.getId()), mTreeInfo.getName());
            return;
        }
        //记录下一级部门的PID(当前部门的ID即为下一级的PID)
        currentLevel = mTreeInfos.get(position).getId();
        //将点击记录存入回退集合中
        clickHistory.add(mTreeInfo);
        //进行数据抽取
        transfer();
    }

    @Override
    public void onBackPressed() {
        try {
            if (clickHistory.size() == 1) {
                super.onBackPressed();
            } else {
                currentLevel = clickHistory.get(clickHistory.size() - 1).getPid();
                clickHistory.remove(clickHistory.size() - 1);
                transfer();
            }
        } catch (Exception e) {
            Toast.makeText(this, "已经到达结构根节点", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }
    }

    /**
     * 迭代计算传入部门的总人数
     *
     * @param tmp
     */
    private void calcSum(Map<Integer, TreeInfo> tmp) {
        if (tmp != null) {
            for (TreeInfo treeInfo : tmp.values()) {
                workerCount++;
                if (treeInfo.getFlag() == 1) {
                    continue;
                }
                Map<Integer, TreeInfo> child = maps.get(treeInfo.getId());
                if (child != null) {
                    calcSum(child);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 100:
                    clickHistory.clear();
                    clickHistory.addAll((Collection<? extends TreeInfo>) data.getSerializableExtra("clickHistory"));
                    currentLevel = data.getIntExtra("currentLevel", -1);
                    transfer();
                    break;
            }
        }
    }
}
