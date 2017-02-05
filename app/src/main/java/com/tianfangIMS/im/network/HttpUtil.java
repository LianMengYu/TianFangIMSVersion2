package com.tianfangIMS.im.network;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.PostRequest;
import com.tianfangIMS.im.callback.IDataHandle;

import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by Administrator on 2017/1/11 0011.
 */
public class HttpUtil {

    public static void  post(String url, Map<String,String> param, final IDataHandle dataHandle){
        PostRequest postRequest = OkGo.post(url)
                .tag(dataHandle)
                .connTimeOut(10000)      // 设置当前请求的连接超时时间
                .readTimeOut(10000)      // 设置当前请求的读取超时时间
                .writeTimeOut(10000);     // 设置当前请求的写入超时时间
        if(null!=param){
            Set<String> kes = param.keySet();
            for (String item :kes){
                postRequest.params(item,param.get(item));  // 循环添加参数
            }
        }
        try {
            postRequest.execute(new StringCallback() {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    dataHandle.handleData(s);  // 处理返回
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
