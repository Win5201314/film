package com.administrator.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

public class Film extends BmobObject implements Serializable {
    //片子名字
    private String name;
    //片子分享地址
    private String path;
    //分享地址密码
    private String password;

    public Film(String name, String path, String password) {
        this.name = name;
        this.path = path;
        this.password = password;
    }

    public Film() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Film{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
