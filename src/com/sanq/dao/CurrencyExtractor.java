package com.sanq.dao;

import android.content.ContentValues;
import android.database.Cursor;
import com.sanq.entity.Currency;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 29.05.13
 * Time: 12:17
 */
@Deprecated
public class CurrencyExtractor extends AbstractExtractor<Currency> {

    @Override
    public Currency extractCurrent(Cursor cur) {
        Currency res = new Currency();
        res.setId(cur.getInt(cur.getColumnIndex("_id")));
        res.setName(cur.getString(cur.getColumnIndex("name")));
        res.setActive(cur.getInt(cur.getColumnIndex("active"))==1);
        res.setDt_create(new java.sql.Date(cur.getLong(cur.getColumnIndex("dt_create"))));
        res.setDt_update(new java.sql.Date(cur.getLong(cur.getColumnIndex("dt_update"))));
        return res;
    }

    @Override
    public ContentValues pack(Currency entity) {
        ContentValues cv = new ContentValues();
        cv.put("name" , entity.getName() );
        cv.put("active" , (entity.isActive() ? 1 : 0 ));
        return cv;
    }

}
