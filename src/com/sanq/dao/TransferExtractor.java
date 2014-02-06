package com.sanq.dao;

import android.content.ContentValues;
import android.database.Cursor;
import com.sanq.entity.Transfer;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 29.05.13
 * Time: 12:17
 */
public class TransferExtractor extends AbstractExtractor<Transfer> {

    @Override
    public Transfer extractCurrent(Cursor cur) {
        Transfer res = new Transfer();
        res.setId(cur.getInt(cur.getColumnIndex("_id")));
        res.setDt(new java.sql.Date(cur.getLong(cur.getColumnIndex("dt"))) );
        res.setIdPayord(cur.getInt(cur.getColumnIndex("id_payord")));
        res.setActive(cur.getInt(cur.getColumnIndex("active"))==1);
        res.setImageName(cur.getString(cur.getColumnIndex("imageName")));
        res.setDt_create(new java.sql.Date(cur.getLong(cur.getColumnIndex("dt_create"))));
        res.setDt_update(new java.sql.Date(cur.getLong(cur.getColumnIndex("dt_update"))));
        return res;
    }

    @Override
    public ContentValues pack(Transfer entity) {
        ContentValues cv = new ContentValues();
        cv.put("dt", entity.getDt().getTime());
        cv.put("id_payord", entity.getIdPayord());
        cv.put("imageName", entity.getImageName());
        cv.put("active" , (entity.isActive() ? 1 : 0 ));
        return cv;
    }

}
