package com.tianfangIMS.im.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tianfangIMS.im.R;
import com.tianfangIMS.im.bean.TreeInfo;

import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Titan on 2017/2/7.
 */

public class InfoAdapter extends BaseAdapter {

    Context mContext;
    List<TreeInfo> mInfos;
    List<Integer> childCount;
    TreeInfo mTreeInfo;
    private Map<Integer, Boolean> checkedMap;

    public InfoAdapter(Context context, List<TreeInfo> treeInfos, List<Integer> childCount) {
        mContext = context;
        this.mInfos = treeInfos;
        this.childCount = childCount;
        initCheckBox(false);
    }

    /**
     * 初始化Map集合
     *
     * @param isChecked CheckBox状态
     */
    public void initCheckBox(boolean isChecked) {
        for (int i = 0; i < mInfos.size(); i++) {
            checkedMap.put(i, isChecked);
        }
    }

    @Override
    public int getCount() {
        return mInfos.size();
    }

    @Override
    public TreeInfo getItem(int position) {
        return mInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return mInfos.get(position).getFlag();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        mTreeInfo = mInfos.get(position);
        BranchHolder mBranchHolder = null;
        WorkerHolder mWorkerHolder = null;
        switch (getItemViewType(position)) {
            case 0:
                if (convertView == null) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_info_item_banch, null);
                    mBranchHolder = new BranchHolder();
                    mBranchHolder.adapter_info_item_branch_name = (TextView) convertView.findViewById(R.id.adapter_info_item_branch_name);
                    mBranchHolder.adapter_info_item_branch_count = (TextView) convertView.findViewById(R.id.adapter_info_item_branch_count);
                    convertView.setTag(mBranchHolder);
                } else {
                    mBranchHolder = (BranchHolder) convertView.getTag();
                }
                mBranchHolder.adapter_info_item_branch_name.setText(getItem(position).getName());
                //部门类型才显示子部门及人员数量
                if (mInfos.get(position).getFlag() == 0) {
                    mBranchHolder.adapter_info_item_branch_count.setVisibility(View.VISIBLE);
                    mBranchHolder.adapter_info_item_branch_count.setText(String.valueOf(childCount.get(position)));
                } else {
                    mBranchHolder.adapter_info_item_branch_count.setVisibility(View.GONE);
                }
                break;
            case 1:
                if (convertView == null) {
                    convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_info_item_worker, null);
                    mWorkerHolder = new WorkerHolder();
                    mWorkerHolder.cb_addfrien = (CheckBox) convertView.findViewById(R.id.cb_addfrien);
                    mWorkerHolder.adapter_info_item_worker_header = (ImageView) convertView.findViewById(R.id.adapter_info_item_worker_header);
                    mWorkerHolder.adapter_info_item_worker_name = (TextView) convertView.findViewById(R.id.adapter_info_item_worker_name);
                    mWorkerHolder.adapter_info_item_worker_job = (TextView) convertView.findViewById(R.id.adapter_info_item_worker_job);
                    convertView.setTag(mWorkerHolder);
                } else {
                    mWorkerHolder = (WorkerHolder) convertView.getTag();
                }
                mWorkerHolder.cb_addfrien.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        // 当勾选框状态发生改变时,重新存入map集合
                        checkedMap.put(position, isChecked);
                    }
                });
                mWorkerHolder.cb_addfrien.setChecked(checkedMap.get(position));
                Glide.with(mContext).load("http://35.164.107.27:8080/im/upload/images/" + mInfos.get(position).getLogo()).bitmapTransform(new CropCircleTransformation(mContext)).into(mWorkerHolder.adapter_info_item_worker_header);
                mWorkerHolder.adapter_info_item_worker_name.setText(mTreeInfo.getName());
                mWorkerHolder.adapter_info_item_worker_job.setText(mTreeInfo.getPostitionname());
                break;
        }
        return convertView;
    }

    private class BranchHolder {
        TextView adapter_info_item_branch_name, adapter_info_item_branch_count;
    }

    private class WorkerHolder {
        public CheckBox cb_addfrien;
        ImageView adapter_info_item_worker_header;
        TextView adapter_info_item_worker_name, adapter_info_item_worker_job;
    }

    /**
     * 得到勾选状态的集合
     *
     * @return
     */
    public Map<Integer, Boolean> getCheckedMap() {
        return checkedMap;
    }
}
