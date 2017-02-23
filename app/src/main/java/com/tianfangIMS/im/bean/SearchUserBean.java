package com.tianfangIMS.im.bean;

import java.io.Serializable;

/**
 * Created by LianMengYu on 2017/2/23.
 */

public class SearchUserBean implements Serializable {
    private String name;
    private String id;
    private String phoneNumber;
    private String logo;

    public SearchUserBean(String id, String name, String phoneNumber, String logo) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.logo = logo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public String toString() {
        return "SearchUserBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", logo='" + logo + '\'' +
                '}';
    }
}
