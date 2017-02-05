package com.tianfangIMS.im.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianfangIMS.im.R;
import com.tianfangIMS.im.utils.CommUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by LianMengYu on 2017/2/4.
 * 我的群组，根据不同数据加载不同布局
 */

public class MineGroupListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Map<String, String>> ListGroup;
    private List<Map<String, String>> listItem;

    public MineGroupListAdapter(List<Map<String, String>> listGroup, List<Map<String, String>> listItem, Context mContext) {
        this.ListGroup = listGroup;
        this.listItem = listItem;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return listItem.size();
    }

    @Override
    public Object getItem(int position) {
        return listItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEnabled(int position) {
        if(ListGroup.contains(listItem.get(position))){
            return false;
        }
        return super.isEnabled(position);
    }
    //
//    @Override
//    public int getItemViewType(int position) {
//        int type = Integer.parseInt(mapList.get(position).get("flag"));
//        return type;
//    }
//
//    @Override
//    public int getViewTypeCount() {
//        return 2;
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChildHodler childHodler = null;
        ParentHolder parentHolder = null;
        if(convertView == null){
            childHodler = new ChildHodler();
            parentHolder = new ParentHolder();
            if(ListGroup.contains(listItem.get(position))){
                convertView = View.inflate(mContext,R.layout.mine_group_parent,null);
                childHodler.name = (TextView)convertView.findViewById(R.id.tv_parent_name);
            }else {
                convertView = View.inflate(mContext,R.layout.mine_group_child,null);
                childHodler.img = (ImageView) convertView.findViewById(R.id.iv_group_photo);
                childHodler.name = (TextView) convertView.findViewById(R.id.tv_group_name);
                convertView.setTag(childHodler);
            }
        }else{
            childHodler = (ChildHodler) convertView.getTag();
        }
//        parentHolder.name.setText("我的群组");
        CommUtils.GetImages(mContext, listItem.get(position).get("logo"), childHodler.img);
        childHodler.name.setText(listItem.get(position).get("name"));
        return convertView;
    }
    class ParentHolder{
        TextView name;
    }
    class ChildHodler {
        ImageView img;
        TextView name;
    }
}
