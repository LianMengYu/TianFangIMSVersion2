package com.tianfangIMS.im.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianfangIMS.im.R;
import com.tianfangIMS.im.bean.TopContactsBean;
import com.tianfangIMS.im.utils.CommUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LianMengYu on 2017/2/3.
 *
 */

public class TopContactsAdapter extends BaseAdapter {
    private Context mContext;
    private List<TopContactsBean> mList;
    //存储CheckBox状态的集合
    private Map<Integer,Boolean> checkedMap;
    public TopContactsAdapter(Context mContext, List<TopContactsBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
        checkedMap = new HashMap<>();
        initCheckBox(false);
    }
    /**
     * 初始化Map集合
     * @param isChecked   CheckBox状态
     */
    public void initCheckBox(boolean isChecked) {
        for (int i = 0; i<mList.size();i++) {
            checkedMap.put(i,isChecked);
        }
    }
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler;
        if (convertView == null){
            convertView = View.inflate(mContext, R.layout.contacts_person_item, null);
            viewHodler =new ViewHodler();
            viewHodler.img = (ImageView)convertView.findViewById(R.id.iv_person_photo);
            viewHodler.name = (TextView)convertView.findViewById(R.id.tv_person_departmentName);
            viewHodler.level = (TextView)convertView.findViewById(R.id.tv_person_departmentTxt);
            convertView.setTag(viewHodler);
        }else {
            viewHodler = (ViewHodler)convertView.getTag();
        }
        CommUtils.GetImages(mContext,mList.get(position).getLogo(),viewHodler.img);
        viewHodler.name.setText(mList.get(position).getFullname());
        viewHodler.level.setText(mList.get(position).getSex());
        return convertView;
    }
    public class ViewHodler{
        ImageView img;
        TextView name;
        TextView level;
    }
    /**
     * 得到勾选状态的集合
     * @return
     */
    public Map<Integer,Boolean> getCheckedMap() {
        return checkedMap;
    }
}
