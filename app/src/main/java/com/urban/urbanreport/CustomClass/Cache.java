package com.urban.urbanreport.CustomClass;

import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.RequiresApi;

public class Cache {

    private Map<String, String> dataList=new HashMap<>();
    private Map<String, Boolean> boolData=new HashMap<>();
    private Map<String, ArrayList<String>> map_data=new HashMap<>();
    private ArrayList<String> itemcode=new ArrayList<>();
    private ArrayList<String> itemname=new ArrayList<>();
    private ArrayList<String> itemqty=new ArrayList<>();
    private ArrayList<String> urlgambar=new ArrayList<>();
    private ArrayList<String> stock=new ArrayList<>();
    //
    private ArrayList<String> itemcode_daily=new ArrayList<>();
    private ArrayList<String> itemname_daily=new ArrayList<>();
    private ArrayList<String> itemqty_daily=new ArrayList<>();
    private ArrayList<String> urlgambar_daily=new ArrayList<>();
    private ArrayList<String> stock_daily=new ArrayList<>();

    public Boolean getBoolData(String key){
        Boolean data=false;
        if (this.boolData.get(key)!=null) {
            data=this.boolData.get(key);
        }
        return data;
    }

    public void putBooldata(String key, Boolean data){
        if (this.boolData.get(key)!=null) {
            this.boolData.remove(key);
        }
        this.boolData.put(key, data);
    }

    public void ClearCache(){
        dataList.clear();
        itemcode.clear();
        itemname.clear();
        itemqty.clear();
        urlgambar.clear();
    }

    public void putMapData(String key, ArrayList<String> array) {
        if (this.map_data.get(key)!=null) {
            this.map_data.remove(key);
        }
        this.map_data.put(key,array);
    }

    public void deleteMap(String key) {
        if (isExistMap(key)) {
            this.map_data.remove(key);
        }
    }

    public Boolean isExistMap(String key) {
        Boolean data=false;
        if (this.map_data.get(key)!=null) {
           if (this.map_data.get(key).size()>0) {
               data=true;
           } else {
               data=false;
           }
        } else {
            data=false;
        }
        return data;
    }

    public ArrayList<String> getMapArray(String key) {
        return this.map_data.get(key);
    }

    public Boolean isExist (){
        Boolean ex=false;
        if (this.dataList.get("FIRSTRUN")!=null) {
            if (this.dataList.get("FIRSTRUN").trim().contains("true")) {
                ex=true;
            } else {
                ex=false;
            }
        } else {
            ex=false;
        }
        return ex;
    }

    public Boolean isDailyrecycle (){
        Boolean ex=false;
        if (this.dataList.get("ISDAILY")!=null) {
            if (this.dataList.get("ISDAILY").trim().contains("true")) {
                ex=true;
            } else {
                ex=false;
            }
        } else {
            this.dataList.put("ISDAILY","false");
            ex=false;
        }
        return ex;
    }

    public Boolean isUpdate (){
        Boolean ex=false;
        if (this.dataList.get("UPDATE")!=null) {
            if (this.dataList.get("UPDATE").trim().contains("true")) {
                ex=true;
            } else {
                ex=false;
            }
        } else {
            ex=false;
        }
        return ex;
    }

    public ArrayList<String>  getArrayItemCode() {
        return  this.itemcode;
    }

    public ArrayList<String>  getArrayItemName() {
        return  this.itemname;
    }

    public ArrayList<String>  getArrayItemQty() {
        return  this.itemqty;
    }

    public ArrayList<String>  getArrayurlgambar() {
        return  this.urlgambar;
    }

    public ArrayList<String>  getArraystock() {
        return  this.stock;
    }

    public void putArrayItemCode(ArrayList<String> i) {
        this.itemcode=i;
    }

    public void putArrayItemQty(ArrayList<String> i) {
        this.itemqty=i;
    }

    public void putArrayItemName(ArrayList<String> i) {
        this.itemname=i;
    }

    public void putArrayUrlGambar(ArrayList<String> i) {
        this.urlgambar=i;
    }

    public void putArrayStock(ArrayList<String> i) {
        this.stock=i;
    }
    //
    public ArrayList<String>  getArrayItemCode_daily() {
        return  this.itemcode_daily;
    }

    public ArrayList<String>  getArrayItemName_daily() {
        return  this.itemname_daily;
    }

    public ArrayList<String>  getArrayItemQty_daily() {
        return  this.itemqty_daily;
    }

    public ArrayList<String>  getArrayurlgambar_daily() {
        return  this.urlgambar_daily;
    }

    public ArrayList<String>  getArraystock_daily() {
        return  this.stock_daily;
    }

    public void putArrayItemCode_daily(ArrayList<String> i) {
        this.itemcode_daily=i;
    }

    public void putArrayItemQty_daily(ArrayList<String> i) {
        this.itemqty_daily=i;
    }

    public void putArrayItemName_daily(ArrayList<String> i) {
        this.itemname_daily=i;
    }

    public void putArrayUrlGambar_daily(ArrayList<String> i) {
        this.urlgambar_daily=i;
    }

    public void putArrayStock_daily(ArrayList<String> i) {
        this.stock_daily=i;
    }


    public Boolean getSize(){
        Boolean res = false;
        if (this.dataList.size()>1) {
            res=true;
        } else {
            res=false;
        }
        return res;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void replace (String key, String value) {
        this.dataList.replace(key,value);
    }

    public String getDataList(String key) {
        String datanya="";
        if (this.dataList.get(key)!=null) {
            datanya=this.dataList.get(key);
        } else {
            datanya="";
        }
        try {
//            Log.v("Get : ", this.dataList.get(key));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datanya;
    }

    public void setDataList(String key, String value) {
        if (this.dataList.get(key)!=null) {
            this.dataList.remove(key);
            this.dataList.put(key,value);
        } else {
            this.dataList.put(key, value);
        }
    }

    public Cache() {}

    public static Cache getInstance() {
        if( instance == null ) {
            instance = new Cache();
        }
        return instance;
    }

    public static Cache instance;
}
