package com.example.zxcbn.qrpay;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by zxcbn on 2018-10-30.
 */

public class ListThread extends Activity {
    ListDB db;
    ComDBCash cash;
    String cid;
    boolean power;
    public ListThread(final ListView olv, final Context context, final TextView otv, final String cid) {
        this.cid = cid;
        power = true;
        new Thread(){
            @Override
            public void run() {
                while (power){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            db = new ListDB(context,olv);
                            cash = new ComDBCash(context,otv);
                            db.execute("http://ec2-18-222-167-250.us-east-2.compute.amazonaws.com/list_DB.php", cid);
                            cash.execute("http://ec2-18-222-167-250.us-east-2.compute.amazonaws.com/com_DBCash.php", cid);
                        }
                    });
                    SystemClock.sleep(2000);
                }
            }
        }.start();
    }

    public void setPower(){
        power = false;
    }

    public void setOid(int oid){
        db.setOid(oid);
    }

    public String getOid(){
        return db.getOid();
    }

    public void resetAdp() {
        db.resetAdp();
    }
}
