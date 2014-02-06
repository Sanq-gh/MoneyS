package com.sanq.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import com.sanq.ds.DS;
import com.sanq.exception.DBException;
import com.sanq.exception.SavingException;
import com.sanq.moneys.R;
import com.sanq.utils.SLog;
import com.sanq.utils.Utils;

/**
 * Created with IntelliJ IDEA.
 * User: sanq
 * Date: 05.06.13
 * Time: 23:08
 * To change this template use File | Settings | File Templates.
 */
public abstract class DAO {
    protected Context context;

    public DAO(Context context) {
        this.context = context;
    }

    public SQLiteDatabase getDb() {
        SQLiteDatabase  res = null;
        try {
            res = DS.getInstance().getDB(context);
        } catch (DBException e) {
            Utils.procMessage(context, context.getString(R.string.mes_db_error) + " " + e.getMessage());
        } catch (SavingException e) {
            Utils.procMessage(context, context.getString(R.string.mes_db_error) + " " + e.getMessage());
        }
        return res;
    }

//    protected void safeCloseDB() {
//        if (db != null) {
//            if (!db.inTransaction()) {
//                if (db.isOpen()) {
//                   db.close();
//                } ;
//            }
//        }
//    }

//    protected void finalCloseDb(){
//         if (db != null && db.inTransaction()) {
//             db.endTransaction();
//             db.close();
//         }
//     }

    protected void cachSqlException(SQLiteException ex) {
        SLog.d(ex.getMessage());
        throw new SQLiteException(ex.getMessage());
    }


}
