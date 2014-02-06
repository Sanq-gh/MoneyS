package com.sanq.entity;

import android.content.Context;
import android.database.Cursor;
import com.sanq.dao.PayordDAO;
import com.sanq.dao.TransferDAO;
import com.sanq.moneys.R;
import com.sanq.utils.Utils;

import java.io.Serializable;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 29.05.13
 * Time: 11:04
 */
public class Transfer extends AbstractEntity implements Entity, Serializable {
    // static .................................

    public static String getBundleKey() {
        return "TRANSFER";
    }

    public static Map<Integer, Date> getAllScheduled(Context context, Date fromDate) {
        Map<Integer, Date> res = new HashMap<Integer, Date>();
        String sql = context.getString(R.string.SQL_GetActualTransferAlarms);
        String[] params = Utils.getArrayString(String.valueOf(fromDate.getTime()));
        Cursor cur = new TransferDAO(context).getRawQuery(sql, params);
        if (cur.moveToFirst()) {
            do {
                Date dateAlarm = Utils.concatTimeDate(new java.sql.Date(cur.getLong(cur.getColumnIndex("dt"))), cur.getFloat(cur.getColumnIndex("time_remind")));
                if (dateAlarm.getTime() > fromDate.getTime()) {
                    res.put(cur.getInt(cur.getColumnIndex("_id")), dateAlarm);
                }
            } while (cur.moveToNext());
        }
        cur.close();
        return res;
    }

    public static List<Integer> getActiveWithImage(Context context) {
        List<Integer> res = new ArrayList<Integer>();
        String sql = context.getString(R.string.SQL_GetWithImage);
        Cursor cur = new TransferDAO(context).getRawQuery(sql, new String[]{});
        if (cur.moveToFirst()) {
            do {
                res.add(cur.getInt(cur.getColumnIndex("_id")));
            } while (cur.moveToNext());
        }
        cur.close();
        return res;
    }
    // .................................


    private int id;
    private int idPayord;
    private Date dt;
    private boolean active = true;
    private String imageName;

    private Date dt_create;
    private Date dt_update;

    public Transfer() {
    }

    public Transfer(int idPayord, Date dt) {
        this.idPayord = idPayord;
        this.dt = dt;
    }

    public Payord getPayord(Context context) {
        return new PayordDAO(context).getById(this.getIdPayord(), true);
    }


    public boolean isActive() {
        return active;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPayord() {
        return idPayord;
    }

    public void setIdPayord(int idPayord) {
        this.idPayord = idPayord;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public Date getDt_create() {
        return dt_create;
    }

    public void setDt_create(Date dt_create) {
        this.dt_create = dt_create;
    }

    public Date getDt_update() {
        return dt_update;
    }

    public void setDt_update(Date dt_update) {
        this.dt_update = dt_update;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transfer transfer = (Transfer) o;
        if (active != transfer.active) return false;
        if (idPayord != transfer.idPayord) return false;
        if (!dt.equals(transfer.dt)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "\nTransfer{" +
                "\nid=" + id +
                "\n, idPayord=" + idPayord +
                "\n, imageName=" + imageName +
                "\n, dt=" + dt +
                "\n, dt_create=" + dt_create +
                "\n, dt_update=" + dt_update +
                '}';
    }
}
