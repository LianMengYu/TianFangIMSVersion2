package com.tianfangIMS.im.activity;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.location.RealTimeLocationConstant;

/**
 * Created by LianMengYu on 2017/2/16.
 */

public class AMapRealTImeActivity extends BaseActivity implements RongIMClient.RealTimeLocationListener{
    @Override
    public void onError(RealTimeLocationConstant.RealTimeLocationErrorCode realTimeLocationErrorCode) {

    }

    @Override
    public void onStatusChange(RealTimeLocationConstant.RealTimeLocationStatus realTimeLocationStatus) {

    }

    @Override
    public void onReceiveLocation(double v, double v1, String s) {

    }

    @Override
    public void onParticipantsJoin(String s) {

    }

    @Override
    public void onParticipantsQuit(String s) {

    }
}
