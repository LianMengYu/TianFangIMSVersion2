package com.tianfangIMS.im.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.tianfangIMS.im.R;

/**
 * Created by LianMengYu on 2017/1/20.
 */
public class VideoActivity extends BaseActivity {
    private ImageView video_image_background;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_activity);
        setTitle("录音");
        init();
    }
    private void init(){
        video_image_background = (ImageView)this.findViewById(R.id.video_image_background);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.base_slide_right_out);
    }
}
