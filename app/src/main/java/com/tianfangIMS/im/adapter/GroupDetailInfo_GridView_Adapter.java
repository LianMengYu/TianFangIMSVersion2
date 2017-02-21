package com.tianfangIMS.im.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tianfangIMS.im.ConstantValue;
import com.tianfangIMS.im.R;
import com.tianfangIMS.im.bean.GroupBean;

import java.util.ArrayList;

/**
 * Created by LianMengYu on 2017/2/17.
 */

public class GroupDetailInfo_GridView_Adapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<GroupBean> mList;
    private boolean isAdded;   //是否额外添加了最后一个图片
    private int maxImgCount;

    public GroupDetailInfo_GridView_Adapter(Context mContext, ArrayList<GroupBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

//    private void setImages(ArrayList<GroupBean> List) {
//        mList = new ArrayList<>();
//        if (getCount() < maxImgCount) {
//            mList.add();
//            isAdded = true;
//        } else {
//            isAdded = false;
//        }
//        notifyDataSetChanged();
//    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Hodler viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.addcontacts_gridview, null);
            viewHolder = new Hodler();
            viewHolder.iv_photo = (ImageView) convertView.findViewById(R.id.iv_addfriend_photo);
            viewHolder.tv_userName = (TextView) convertView.findViewById(R.id.item_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (Hodler) convertView.getTag();
        }
//        if (position == getCount() + 1) {
//            viewHolder.iv_photo.setBackgroundResource(R.mipmap.add);
//        }
        Picasso.with(mContext)
                .load(ConstantValue.ImageFile + mList.get(position).getLogo())
                .into(viewHolder.iv_photo);
        viewHolder.tv_userName.setText(mList.get(position).getFullname());
        return convertView;
    }

    class Hodler {
        ImageView iv_photo;
        TextView tv_userName;
    }
}
