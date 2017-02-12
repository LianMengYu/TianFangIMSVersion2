package com.tianfangIMS.im;

/**
 * Created by Lincktek_Lmy on 2016/12/29.
 * 常量类。
 */

public class ConstantValue {
    //服务器访问地址
    public static String Urls = "http://35.164.107.27:8080/im/";

    public static final String AFTERLOGINUSERINFO = "member!getOneOfMember";//获取登录后用户单独的信息接口

    public static String ImageFile = Urls + "upload/images/";//网络图片路径

    public static final String SEALTALK_LOGING_PHONE = "loginphone";

    public static final String SEALTALK_LOGING_PASSWORD = "loginpassword";

    public static final String AFTERLOGIN = Urls + "system!afterLogin";//登录接口

    public static final String DEPARTMENT = Urls + "branch!getBranchTree";//部门接口

    public static final String REQUESTTEXT = Urls + "system!requestText";//发送短信验证码

    public static final String NEWPASSWORD = Urls + "system!newPassword";//修改密码

    public static final String DEPARTMENTPERSON = Urls + "branch!getBranchTreeAndMember";//部门+人员

    public static final String CONTACTSPERSON = Urls + "branch!getBranchMember";//人员

    public static final String SEARCHFRIEND = Urls + "member!searchUser";//查找联系人

    public static final String ADDTOPCONTACTS = Urls + "friend!addFriend";//添加常用联系人

    public static final String GETCONTACTSLIST = Urls + "friend!getMemberFriends";//获取常用联系人列表

    public static final String MINEGROUP = Urls + "group!groupListWithAction";//我的群组接口

    public static final String CREATEGROUP = Urls + "group!createGroup";//创建群聊

    public static final String SYNCUSERGROUP = Urls + "group!syncUserGroup";//同步用户群列表

    public static final String GETALLGROUP = Urls + "group!groupList";//获取所有群组信息

    public static final String GETONEPERSONINFO = Urls +  "member!getOneOfMember";//获取单用户信息


}
