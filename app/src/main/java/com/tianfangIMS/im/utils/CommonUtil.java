package com.tianfangIMS.im.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.BitmapCallback;
import com.tianfangIMS.im.ConstantValue;
import com.tianfangIMS.im.TianFangIMSApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by LianMengYu on 2017/1/6.
 */

public class CommonUtil {

    private static final String path = File.separator + File.separator + "sdcard" + File.separator + "TianFangIMS";
    private static final String PathIMGES = path + File.separator + "Images" + File.separator;
    public static void FilePath() {

        File file = new File(path);
        if (!file.exists()) {
            try {
                file.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        File fileImage = new File(path + File.separator + "Images");
        if (!fileImage.exists()) {
            try {
                fileImage.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // dialog改变大小方法
    public static void changeDialogUI(Dialog dialog, WindowManager m, double h, double w) {
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        WindowManager.LayoutParams p = dialog.getWindow().getAttributes(); // 获取对话框当前的参数值
        p.height = (int) h; //
        p.width = (int) w; //
        dialog.getWindow().setAttributes(
                (android.view.WindowManager.LayoutParams) p); // 设置生效
    }

    // 改变dialog位置
    public static void changeDialogPosition(Dialog dialog, int gravity, int xOffset, int yOffset) {
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = gravity;
        wlp.x = xOffset;
        wlp.y = yOffset;
        // wlp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
    }

    public static Bitmap GetImage(final Context context, final String filename) {
        Bitmap mbitmap = null;
        OkGo.post(ConstantValue.ImageFile + filename)
                .tag(context)
                .cacheKey("imagecache")
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(new BitmapCallback() {
                    @Override
                    public void onSuccess(Bitmap bitmap, Call call, Response response) {
                        Bitmap mbitmap = bitmap;
                        Log.e("qqqqqqqqqqqqqqqqqq", "下载方法：" + mbitmap);
                    }
                });
        return mbitmap;

    }

    public static Bitmap GetImages(final Context context, final String filename, final ImageView view) {
        Bitmap mbitmap = null;
        OkGo.post(ConstantValue.ImageFile + filename)
                .tag(context)
                .cacheKey("imagecache")
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(new BitmapCallback() {
                    @Override
                    public void onSuccess(Bitmap bitmap, Call call, Response response) {
                        view.setImageBitmap(bitmap);
                    }
                });
        return mbitmap;
    }

    public static Bitmap getImageBitmap(Context context, final String filename, final ImageView view) {
        Bitmap mbitmap = null;
        OkGo.post(filename)
                .tag(context)
                .cacheKey("imagecache")
                .cacheMode(CacheMode.FIRST_CACHE_THEN_REQUEST)
                .execute(new BitmapCallback() {
                    @Override
                    public void onSuccess(Bitmap bitmap, Call call, Response response) {
                        view.setImageBitmap(bitmap);
                    }
                });
        return mbitmap;
    }

    /**
     * @Description:把list转换为一个用逗号分隔的字符串
     */
    public static String listToString(List list) {
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i < list.size() - 1) {
                    sb.append(list.get(i) + ",");
                } else {
                    sb.append(list.get(i));
                }
            }
        }
        return sb.toString();
    }

    public static String getUserInfo(Context mContext) {
        SharedPreferences pref = mContext.getSharedPreferences(
                "user_login", 0);
        return pref.getString("user_login", "");
    }

    public static boolean saveUserInfo(Context mContext, String info) {
        SharedPreferences pref = mContext.getSharedPreferences(
                "user_login", 0);
        return pref.edit().putString("user_login", info).commit();
    }

    public static boolean saveFrientUserInfo(Context mContext, String info) {
        SharedPreferences pref = mContext.getSharedPreferences(
                "friend_info", 0);
        return pref.edit().putString("friend_info", info).commit();
    }
    public static String getFrientUserInfo(Context mContext) {
        SharedPreferences pref = mContext.getSharedPreferences(
                "friend_info", 0);
        return pref.getString("friend_info", "");
    }
    public static boolean saveGroupUserInfo(Context mContext, String info) {
        SharedPreferences pref = mContext.getSharedPreferences(
                "group_info", 0);
        return pref.edit().putString("group_info", info).commit();
    }
    public static String getGroupUserInfo(Context mContext) {
        SharedPreferences pref = mContext.getSharedPreferences(
                "group_info", 0);
        return pref.getString("group_info", "");
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }



    /**
     * md5加密
     */
    public static String md5(Object object) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(toByteArray(object));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    public static byte[] toByteArray (Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray ();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }



    /**
     * 获取存储路径
     */
    public static String getDataPath() {
        String path;
        if (isExistSDcard())
            path = Environment.getExternalStorageDirectory().getPath() + "/albumSelect";
        else
            path = TianFangIMSApplication.getInstance().getFilesDir().getPath();
        if (!path.endsWith("/"))
            path = path + "/";
        return path;
    }


    /**
     * 检测SDcard是否存在
     *
     * @return
     */
    public static boolean isExistSDcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED))
            return true;
        else {
            return false;
        }
    }

}
