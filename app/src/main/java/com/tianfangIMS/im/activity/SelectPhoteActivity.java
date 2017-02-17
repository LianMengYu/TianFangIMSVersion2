package com.tianfangIMS.im.activity;

/**
 * Created by LianMengYu on 2017/2/14.
 */
public class SelectPhoteActivity extends BaseActivity {
//    private Context mContext;
//    private GridView gridView;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.selectphont_layout);
//        mContext = this;
//        gridView = (GridView)this.findViewById(R.id.grid);
//        List alist = (List<Object>) getIntent().getSerializableExtra("photouri");
//        List<Uri> list = alist;
//        SelectPhoto_GridView_Adapter adapter = new SelectPhoto_GridView_Adapter(list,mContext);
//        SettingGridView(list);
//        gridView.setAdapter(adapter);
//        gridView.deferNotifyDataSetChanged();
//
//    }
//
//    //对GridView 显示的宽高经行设置
//    private void SettingGridView(List<Uri> list) {
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        float density = dm.density;
//        int size = list.size();//要显示数据的个数
//        //gridview的layout_widht,要比每个item的宽度多出2个像素，解决不能完全显示item的问题
//        int allWidth = (int) (82 * size * density);
//        //int allWidth = (int) ((width / 3 ) * size + (size-1)*3);//也可以这样使用，item的总的width加上horizontalspacing
//        int itemWidth = (int) (65 * density);//每个item宽度
//        LinearLayout.LayoutParams params = new
//                LinearLayout.LayoutParams(allWidth, LinearLayout.LayoutParams.MATCH_PARENT);
//        gridView.setLayoutParams(params);
//        gridView.setColumnWidth(itemWidth);
//        gridView.setHorizontalSpacing(3);
//        gridView.setStretchMode(GridView.NO_STRETCH);
//        gridView.setNumColumns(size);
//    }
}
