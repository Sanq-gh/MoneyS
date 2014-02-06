package com.sanq.utils;

import android.content.Context;
import android.database.Cursor;
import com.sanq.dao.AccountDAO;
import com.sanq.exception.DateCheckedException;
import com.sanq.moneys.R;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 12.07.13
 * Time: 16:56
 */
public class DatePeriod implements Comparable<DatePeriod> , Serializable {

    //static section ************************************************
    public static String getBundleKey(){
        return "DATE_PERIOD";
    }

    public static DatePeriod getAllPeriod(Context context){
        DatePeriod res = new DatePeriod(new Date(), new Date());
        Cursor cur;
        cur = new AccountDAO(context).getRawQuery(context.getString(R.string.SQL_GetMinDt), null);
        if (cur.moveToFirst()) {
            do {
                res.dateFrom  = new java.sql.Date(cur.getLong(cur.getColumnIndex("dt")));
            } while (cur.moveToNext());
        }
        cur.close();
        cur = new AccountDAO(context).getRawQuery(context.getString(R.string.SQL_GetMaxDt), null);
        if (cur.moveToFirst()) {
            do {
                res.dateTo = new java.sql.Date(cur.getLong(cur.getColumnIndex("dt")));
            } while (cur.moveToNext());
        }
        cur.close();
        return res;
    }



    //************************************************

    public Date dateFrom;
    public Date dateTo;


    public DatePeriod() {
        this.dateFrom  =new Date();
        this.dateTo = new Date();
    }

    public DatePeriod(Date dateFrom, Date dateTo) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public DatePeriod(String dateFrom, String dateTo,String format) throws DateCheckedException {
        SimpleDateFormat  formatter = new SimpleDateFormat(format);
        try {
            this.dateFrom = formatter.parse(dateFrom);
            this.dateTo =   formatter.parse(dateTo);
        } catch (ParseException e) {
            throw new DateCheckedException("Wrong date format: '" + format + "' (" +  e.getMessage()+")");
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatePeriod that = (DatePeriod) o;
        if (!dateFrom.equals(that.dateFrom)) return false;
        if (!dateTo.equals(that.dateTo)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = dateFrom.hashCode();
        result = 31 * result + dateTo.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "DatePeriod{" +
                " dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                '}';
    }

    @Override
    public int compareTo(DatePeriod another) {
        return dateFrom .compareTo(another.dateFrom) + dateTo .compareTo(another.dateTo);
    }
}
