package com.sanq.core;

import android.content.Context;
import android.database.Cursor;
import com.sanq.dao.AccountDAO;
import com.sanq.entity.Payord;
import com.sanq.moneys.R;
import com.sanq.utils.DatePeriod;
import com.sanq.utils.Utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 03.06.13
 * Time: 16:28
 */
public class Report {
    Context context;

    public Report(Context context) {
        this.context = context;
    }

    public int getSaldoOnDate(int idAcc, Date onDate) {
        onDate = Utils.zeroTime(onDate);
        int res = 0;
        String sql = context.getString(R.string.SQL_GetSaldoOnDate);
        // 1 - date  2 - id_acc
        String[] params = Utils.getArrayString( String.valueOf(onDate.getTime()), String.valueOf(idAcc));
        Cursor cur = new AccountDAO(context).getRawQuery(sql, params);
        if (cur.moveToFirst()) {
            res = cur.getInt(0);
        }
        cur.close();
        return res;
    }

    public Map<String, Integer> getSaldoOnDate(Date onDate) {
        onDate = Utils.zeroTime(onDate);
        Map<String, Integer> res = new HashMap<String, Integer>();
        String sql = context.getString(R.string.SQL_GetSaldoOnDateAll);
        String[] params = Utils.getArrayString(String.valueOf(onDate.getTime()));
        Cursor cur = new AccountDAO(context).getRawQuery(sql, params);
        if (cur.moveToFirst()) {
            do {
                res.put(cur.getString(0), cur.getInt(1));
            } while (cur.moveToNext());
         }
        cur.close();
        return res;
    }


    public int getAmountOnPeriod(int idAcc, Payord.Type typePayord, DatePeriod dtPer) {
        dtPer = Utils.zeroTime(dtPer);
        dtPer.dateTo = Utils.addDays(dtPer.dateTo , 1);
        int res = 0;
        String    sql = context.getString(R.string.SQL_GetAmountOnPeriod);
        String[]    params = Utils.getArrayString(String.valueOf(idAcc), String.valueOf(typePayord.getInt()), String.valueOf(dtPer.dateFrom.getTime()), String.valueOf(dtPer.dateTo.getTime()));
        Cursor cur = new AccountDAO(context).getRawQuery(sql, params);
        if (cur.moveToFirst()) {
            res = cur.getInt(0);
        }
        cur.close();
        return res;
    }



    public int getAmountByCategory(int idAcc, int idCat,  DatePeriod dtPer, Payord.Type typePayord) {
        dtPer = Utils.zeroTime(dtPer);
        dtPer.dateTo = Utils.addDays(dtPer.dateTo , 1);
        int res = 0;
        String    sql = context.getString(R.string.SQL_GetAmountByCategory);
        String[]  params = Utils.getArrayString(String.valueOf(idAcc), String.valueOf(typePayord.getInt()), String.valueOf(dtPer.dateFrom.getTime()), String.valueOf(dtPer.dateTo.getTime()),String.valueOf(idCat));
        Cursor cur = new AccountDAO(context).getRawQuery(sql, params);
        if (cur.moveToFirst()) {
            res = cur.getInt(0);
        }
        cur.close();
        return res;
    }

}
