package com.openapi_test_app.testdraft;

public class ListViewItem {
    private String address;
    private String resLon;
    private String resLat;

    public void setAddress(String address_name){
        address = address_name;
    }
    public void setResLon(String resLon_name){
        resLon = resLon_name;
    }
    public void setResLat(String resLat_name){
        resLat = resLat_name;
    }

    public String getAddress(){
        return this.address;
    }
    public String getResLon(){
        return this.resLon;
    }
    public String getResLat(){
        return this.resLat;
    }
}
