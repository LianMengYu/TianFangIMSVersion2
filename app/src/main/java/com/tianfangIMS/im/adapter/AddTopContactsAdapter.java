package com.tianfangIMS.im.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianfangIMS.im.R;
import com.tianfangIMS.im.bean.AddFriendBean;
import com.tianfangIMS.im.utils.CommUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LianMengYu on 2017/1/23.
 */

public class AddTopContactsAdapter extends BaseAdapter {

    private Context mContext;
    private List<AddFriendBean> mList;
    public static HashMap<Integer, Boolean> isSelectedCheck;
    private String itemString = null; // 记录每个item中textview的值
    private String keyString[] = null;
    //存储CheckBox状态的集合
    private Map<Integer,Boolean> checkedMap;

    public AddTopContactsAdapter(Context mContext, List<AddFriendBean> mList) {
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
        Holder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.addtopcontacts_item, null);
            holder = new Holder();
            holder.cb_addfrien = (CheckBox) convertView.findViewById(R.id.cb_addfrien);
            holder.AddFriendPhoto = (ImageView) convertView.findViewById(R.id.iv_addfriend_photo);
            holder.AddFriendName = (TextView) convertView.findViewById(R.id.tv_addfriend_Name);
            holder.AddFriendLevel = (TextView) convertView.findViewById(R.id.tv_addfriend_Txt);
            convertView.setTag(holder);
        } else {

            holder = (Holder) convertView.getTag();
        }
        holder.cb_addfrien.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 当勾选框状态发生改变时,重新存入map集合
                checkedMap.put(position,isChecked);
            }
        });
        holder.cb_addfrien.setChecked(checkedMap.get(position));
        CommUtils.GetImages(mContext, mList.get(position).getLogo(), holder.AddFriendPhoto);
        holder.AddFriendName.setText(mList.get(position).getName());
        holder.AddFriendLevel.setText(mList.get(position).getSex());
        return convertView;
    }

    public class Holder {
        public CheckBox cb_addfrien;//选择
        ImageView AddFriendPhoto;//好友头像
        TextView AddFriendName;//好友名字
        TextView AddFriendLevel;//还有级别
    }
    /**
     * 得到勾选状态的集合
     * @return
     */
    public Map<Integer,Boolean> getCheckedMap() {
        return checkedMap;
    }
}
