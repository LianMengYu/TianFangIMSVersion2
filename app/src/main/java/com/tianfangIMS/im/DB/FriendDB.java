package com.tianfangIMS.im.DB;

import android.util.Log;

import com.tianfangIMS.im.bean.Friend;
import com.tianfangIMS.im.utils.DatabaseOpenHelper;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

/**
 * Created by LianMengYu on 2017/2/18.
 */

public class FriendDB {
    public static final String DB_NAME = "friend.db";

    //数据库名
    public static final int VERSION = 1;
    //数据库版本号
    private static FriendDB friendDB;
    private static DbManager db;
    //接收构造方法初始化的DbManager对象
    private FriendDB(){
        DatabaseOpenHelper databaseOpenHelper = new DatabaseOpenHelper(DB_NAME,VERSION);
        db = x.getDb(databaseOpenHelper.getDaoConfig());
    }
    //构造方法私有化,拿到DbManager对象
    public synchronized static FriendDB getIntance(){
        if (friendDB == null){
            friendDB = new FriendDB();
        }
        return friendDB;
    }

    //******************************************************************************************
    //获取PersonDB实例

    /****************************************************************************************/
    //写两个测试方法，也就是常用的数据库操作
    public void savePerson(Friend friend){
        try {
            db.save(friend);
            Log.d("xyz","save succeed!");
        } catch (DbException e) {
            Log.d("xyz",e.toString());
        }
    }
    //将Person实例存进数据库
    public List<Friend> loadPerson(){
        List<Friend> list = null;
        try {
            list = db.selector(Friend.class).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return list;
    }
    //读取所有Person信息
}
