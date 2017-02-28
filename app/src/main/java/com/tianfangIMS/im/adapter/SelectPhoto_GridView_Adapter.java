package com.tianfangIMS.im.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
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
//        ViewHodler hodler = new ViewHodler();
        convertView = View.inflate(mContext, R.layout.item_select_gridview_photo, null);
        ImageView img = (ImageView) convertView.findViewById(R.id.select_imageView_GridView);
//        Glide.with(mContext).load("http://content.52pk.com/files/100623/2230_102437_1_lit.jpg")
//                .bitmapTransform(new CropCircleTransformation(mContext))
//                .into(img);
        Log.e("选择照片的Adapter：", "---:" + list.get(position));
        Picasso.with(mContext)
                .load(list.get(position))
                .resize(500, 500)
                .placeholder(R.mipmap.default_photo)
                .error(R.mipmap.default_photo)
                .into(img);
        return convertView;
    }

//    class ViewHodler {
//        ImageView img;
//    }
}
