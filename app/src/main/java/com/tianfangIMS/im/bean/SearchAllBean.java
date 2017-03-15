package com.tianfangIMS.im.bean;

import java.io.Serializable;

/**
 * Created by LianMengYu on 2017/3/11.
 */

public class SearchAllBean implements Serializable {
    private String id;
    private String name;
    private String position;
    private String Logo;
    private boolean flag;//true为单聊，false为群聊

    public SearchAllBean() {}

    public SearchAllBean(String id, String name, String position, String logo, boolean flag) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.Logo = logo;
        this.flag = flag;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }

    @Override
    public String toString() {
        return "SearchAllBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", Logo='" + Logo + '\'' +
                '}';
    }
}
