package com.sanq.dao;

import android.content.ContentValues;
import android.database.Cursor;
import com.sanq.entity.Payord;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 29.05.13
 * Time: 12:17
 */
public class PayordExtractor extends AbstractExtractor<Payord> {

    @Override
    public Payord extractCurrent(Cursor cur) {
        Payord res = new Payord();
        res.setId(cur.getInt(cur.getColumnIndex("_id")));
        res.setAmount(cur.getInt(cur.getColumnIndex("amount")));
        res.setDt(new java.sql.Date(cur.getLong(cur.getColumnIndex("dt"))) );
        res.setDt_end(new java.sql.Date(cur.getLong(cur.getColumnIndex("dt_end"))));
        res.setId_acc(cur.getInt(cur.getColumnIndex("id_acc")));
        res.setId_cat(cur.getInt(cur.getColumnIndex("id_cat")));
        res.setId_link(cur.getInt(cur.getColumnIndex("id_link")));
        res.setName(cur.getString(cur.getColumnIndex("name")));
        res.setId_period(cur.getInt(cur.getColumnIndex("id_period")));
        res.setPermanent(cur.getInt(cur.getColumnIndex("permanent"))==1);
        res.setRemind(cur.getInt(cur.getColumnIndex("remind"))==1);
        res.setTimeRemind(cur.getFloat(cur.getColumnIndex("time_remind")));
        res.setType(Payord.Type.fromInt(cur.getInt(cur.getColumnIndex("type"))));
        res.setActive(cur.getInt(cur.getColumnIndex("active"))==1);
        res.setDt_create(new java.sql.Date(cur.getLong(cur.getColumnIndex("dt_create"))));
        res.setDt_update(new java.sql.Date(cur.getLong(cur.getColumnIndex("dt_update"))));
        return res;
    }

    @Override
    public ContentValues pack(Payord pay) {
        ContentValues cv = new ContentValues();
        cv.put("name" , pay.getName());
        cv.put("amount" , pay.getAmount());
        cv.put("dt" , pay.getDt().getTime());
        cv.put("dt_end" , pay.getDt_end().getTime());
        cv.put("id_acc" , pay.getId_acc());
        cv.put("id_cat" , pay.getId_cat());
        cv.put("id_link" , pay.getId_link());
        cv.put("id_period" , pay.getId_period());
        cv.put("permanent",(pay.isPermanent() ? 1 : 0 ));
        cv.put("remind" , (pay.isRemind() ? 1 : 0));
        cv.put("time_remind" , pay.getTimeRemind());
        cv.put("type",  pay.getType().getInt()  );
        cv.put("active" , (pay.isActive() ? 1 : 0 ));
        return cv;
    }
}
