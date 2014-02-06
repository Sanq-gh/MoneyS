package com.sanq.dao;

import android.content.ContentValues;
import android.database.Cursor;
import com.sanq.entity.Period;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 29.05.13
 * Time: 12:17
 */

public class PeriodExtractor extends AbstractExtractor<Period> {
    @Override
    public Period extractCurrent(Cursor cur) {
        Period res = new Period();
        res.setId(cur.getInt(cur.getColumnIndex("_id")));
        res.setName(cur.getString(cur.getColumnIndex("name")));
        res.setType(Period.Type.fromInt(cur.getInt(cur.getColumnIndex("type"))));
        res.setCount(cur.getInt(cur.getColumnIndex("count")));
        res.setActive(cur.getInt(cur.getColumnIndex("active"))==1);
        res.setDt_create(new java.sql.Date(cur.getLong(cur.getColumnIndex("dt_create"))));
        res.setDt_update(new java.sql.Date(cur.getLong(cur.getColumnIndex("dt_update"))));
        return res;
    }

    @Override
    public ContentValues pack(Period cat) {
        ContentValues cv = new ContentValues();
        cv.put("name" , cat.getName());
        cv.put("type",  cat.getType().getInt()  );
        cv.put("count" , cat.getCount());
        cv.put("active" , (cat.isActive() ? 1 : 0 ));
        return cv;
    }
}
