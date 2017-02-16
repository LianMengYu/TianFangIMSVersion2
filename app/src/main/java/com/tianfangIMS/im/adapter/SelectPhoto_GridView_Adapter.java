package com.tianfangIMS.im.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.tianfangIMS.im.R;

import java.util.List;

/**
 * Created by LianMengYu on 2017/2/15.
 */

public class SelectPhoto_GridView_Adapter extends BaseAdapter {
    private Context mContext;
    private List<Uri> list;

    public SelectPhoto_GridView_Adapter(List<Uri> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler hodler = null;
        if (convertView == null) {
            hodler = new ViewHodler();
            convertView = View.inflate(mContext, R.layout.item_select_gridview_photo, null);
            hodler.imgeview = (ImageView)convertView.findViewById(R.id.image_item);
            convertView.setTag(hodler);
        }else{
            convertView.getTag();
        }
//        CommonUtil.getImageBitmap(mContext,list.get(position).toString(),hodler.imgeview);
        Picasso.with(mContext)
                .load(list.get(position))
                .into(hodler.imgeview);
        return convertView;
    }
    class ViewHodler{
        ImageView imgeview;
    }
}
