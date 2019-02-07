package com.example.zxcbn.qrpay;

import java.util.ArrayList;

/**
 * Created by zxcbn on 2018-09-22.
 */

public class Order {
    /*ArrayList<String> arrayname = new ArrayList<String>();
    ArrayList<String> arraymenu = new ArrayList<String>();
    ArrayList<String> arraycount = new ArrayList<String>();
    ArrayList<String> arrayoid = new ArrayList<String>();*/
    String oid, name, menu, count;

    public Order(String oid, String name, String menu, String count) {
        this.oid = oid;
        this.name = name;
        this.menu = menu;
        this.count = count;
    }


/*public void setOrder(String oid, String name, String menu, String count){
        arrayname.add(name);
        arraymenu.add(menu);
        arraycount.add(count);
        arrayoid.add(oid);
    }*/


}
