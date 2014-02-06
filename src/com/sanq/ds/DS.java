package com.sanq.ds;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.sanq.exception.DBException;
import com.sanq.exception.SavingException;

/**
 * Created with IntelliJ IDEA.
 * User: sanq
 * Date: 17.06.13
 * Time: 22:10
 * To change this template use File | Settings | File Templates.
 */
public class DS {
    private static DS instance = new DS();
    private SQLiteDatabase db;
    private DbHelper helper;
    private Context context;

    public static DS getInstance() {
        return instance;
    }

    private DS() {
    }

    public SQLiteDatabase getDB(Context context) throws DBException, SavingException {
        if (context == null) {
            throw new DBException("DB cannot be accessible with null context.");
        }
        if ((!context.equals(this.context)) | helper == null) {
            closeDb();
            this.context = context;
            helper = new DbHelper(context);
            db = helper.getWritableDatabase();
        }
        return db;
    }

    public void closeDb() {
        if (helper != null)
            helper.close();
        helper = null;
    }
}
