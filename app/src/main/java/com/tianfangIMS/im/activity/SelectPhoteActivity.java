package com.tianfangIMS.im.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.widget.GridView;

import com.tianfangIMS.im.R;
import com.tianfangIMS.im.adapter.SelectPhoto_GridView_Adapter;

import java.util.List;

/**
 * Created by LianMengYu on 2017/2/14.
 */
public class SelectPhoteActivity extends BaseActivity {
    private Context mContext;
    private GridView gridView;
    SelectPhoto_GridView_Adapter adapter;
//    private ImageView image_view1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectphont_layout);
        setTitle("聊天文件");
        mContext = this;
        gridView = (GridView) this.findViewById(R.id.grid);
//        image_view1 = (ImageView) this.findViewById(R.id.image_view1);
        List alist = (List<Object>) getIntent().getSerializableExtra("photouri");
        if (alist.size() > 0 && alist != null) {
            List<Uri> list = alist;
            adapter = new SelectPhoto_GridView_Adapter(list, mContext);
            gridView.setAdapter(adapter);
//            SettingGridView(list);
            gridView.deferNotifyDataSetChanged();
        } else {
            return;
        }
    }
}
