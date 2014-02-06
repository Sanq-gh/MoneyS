package com.sanq.ds;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.sanq.exception.SavingException;
import com.sanq.moneys.R;
import com.sanq.utils.Cnt;
import com.sanq.utils.SLog;
import com.sanq.utils.Utils;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 27.05.13
 * Time: 11:33
 */
public class DbHelper extends SQLiteOpenHelper {

    private Context context;

    public DbHelper(Context context) throws SavingException {
       //super(context,Cnt.DB_NAME , null, Cnt.DB_VERSION);
       super(context,Utils.getDBFileName(context), null, Cnt.DB_VERSION);

        this.context = context;


       SLog.d(" --- onCreate helper --- ");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //transaction are implemented in super class  ...
        SLog.d(" --- onCreate database --- ");

        try {
            SLog.d(" --- meta data --- ");
            for (String el : Utils.getSqlAsList(context, R.raw.meta)) {
                db.execSQL(el);
            }

            SLog.d(" --- filling data --- ");
            for (String el : Utils.getSqlAsList(context, R.raw.data)) {
                db.execSQL(el);
            }

        } catch (SQLException ex) {
           SLog.d("++++++++++++++++ onCreate - SQLException:  :" +  ex.getMessage());
           throw  new SQLException(ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        SLog.d(" --- onUpgrade database --- ");
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
           SLog.d(" --- onOpen database --- ");
        }
    }

    @Override
     public void close(){
     super.close();
        SLog.d(" --- close database --- ");
     }


}
