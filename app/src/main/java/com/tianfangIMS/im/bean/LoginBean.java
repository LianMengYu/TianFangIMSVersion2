package com.tianfangIMS.im.bean;

import java.io.Serializable;

/**
 * Created by LianMengYu on 2017/1/16.
 */

public class LoginBean implements Serializable {


    private int code;
    private Text text;


    public static class Text implements Serializable {
        private String account;
        private String address;
        private String birthday;
        private String email;
        private String fullname;
        private String groupmax;
        private String groupuse;
        private String id;
        private String intro;
        private String logo;
        private String mobile;
        private String password;
        private String pinyin;
        private String sex;
        private String telephone;
        private String workno;
        private String token;

        public Text(String account, String address, String birthday, String email, String fullname, String groupmax, String groupuse, String id, String intro, String logo, String mobile, String password, String pinyin, String sex, String telephone, String token, String workno) {
            this.account = account;
            this.address = address;
            this.birthday = birthday;
            this.email = email;
            this.fullname = fullname;
            this.groupmax = groupmax;
            this.groupuse = groupuse;
            this.id = id;
            this.intro = intro;
            this.logo = logo;
            this.mobile = mobile;
            this.password = password;
            this.pinyin = pinyin;
            this.sex = sex;
            this.telephone = telephone;
            this.token = token;
            this.workno = workno;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public String getGroupmax() {
            return groupmax;
        }

        public void setGroupmax(String groupmax) {
            this.groupmax = groupmax;
        }

        public String getGroupuse() {
            return groupuse;
        }

        public void setGroupuse(String groupuse) {
            this.groupuse = groupuse;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPinyin() {
            return pinyin;
        }

        public void setPinyin(String pinyin) {
            this.pinyin = pinyin;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getWorkno() {
            return workno;
        }

        public void setWorkno(String workno) {
            this.workno = workno;
        }

        @Override
        public String toString() {
            return "Text{" +
                    "account='" + account + '\'' +
                    ", address='" + address + '\'' +
                    ", birthday='" + birthday + '\'' +
                    ", email='" + email + '\'' +
                    ", fullname='" + fullname + '\'' +
                    ", groupmax='" + groupmax + '\'' +
                    ", groupuse='" + groupuse + '\'' +
                    ", id='" + id + '\'' +
                    ", intro='" + intro + '\'' +
                    ", logo='" + logo + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", password='" + password + '\'' +
                    ", pinyin='" + pinyin + '\'' +
                    ", sex='" + sex + '\'' +
                    ", telephone='" + telephone + '\'' +
                    ", workno='" + workno + '\'' +
                    ", token='" + token + '\'' +
                    '}';
        }

//        @Override
//        public int describeContents() {
//            return 0;
//        }
//
//        @Override
//        public void writeToParcel(Parcel dest, int flags) {
//            dest.writeString(account);
//            dest.writeString(address);
//            dest.writeString(birthday);
//            dest.writeString(email);
//            dest.writeString(fullname);
//            dest.writeString(groupmax);
//            dest.writeString(groupuse);
//            dest.writeString(id);
//            dest.writeString(intro);
//            dest.writeString(logo);
//            dest.writeString(mobile);
//            dest.writeString(password);
//            dest.writeString(pinyin);
//            dest.writeString(sex);
//            dest.writeString(telephone);
//            dest.writeString(workno);
//            dest.writeString(token);
//        }
//
//        public static final Parcelable.Creator<Text> CREATOR = new Creator<Text>() {
//            @Override
//            public Text createFromParcel(Parcel source) {
//                return new Text(source);
//            }
//
//            @Override
//            public Text[] newArray(int size) {
//                return new Text[size];
//            }
//        };
//
//        public Text(Parcel source) {
//            account = source.readString();
//            address = source.readString();
//            birthday = source.readString();
//            email = source.readString();
//            fullname = source.readString();
//            groupmax = source.readString();
//            groupuse = source.readString();
//            id = source.readString();
//            intro = source.readString();
//            logo = source.readString();
//            mobile = source.readString();
//            password = source.readString();
//            pinyin = source.readString();
//            sex = source.readString();
//            workno = source.readString();
//            token = source.readString();
//        }
    }

    public LoginBean(int code, Text text) {
        this.code = code;
        this.text = text;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(code);
//        dest.writeParcelable(Text);
//    }
//
}
