package com.example.hci_project.bean;

public class School2 {

    private String addr;
    private String name;
    private String type;
    private String tel;
    private double x;
    private double y;


    public School2(String addr, String name, String type, String tel, double x, double y) {
        this.addr = addr;
        this.name = name;
        this.type = type;
        this.tel = tel;
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
