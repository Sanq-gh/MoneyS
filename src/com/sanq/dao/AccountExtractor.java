package com.sanq.dao;

import android.content.ContentValues;
import android.database.Cursor;
import com.sanq.entity.Account;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 29.05.13
 * Time: 12:17
 */
public class AccountExtractor extends AbstractExtractor<Account> {

    @Override
    public Account extractCurrent(Cursor cur) {
        Account res = new Account();
        res.setId(cur.getInt(cur.getColumnIndex("_id")));
        res.setName(cur.getString(cur.getColumnIndex("name")));
        res.setSaldo(cur.getInt(cur.getColumnIndex("saldo")));
        res.setCurrency(cur.getString(cur.getColumnIndex("currency")));
        res.setActive(cur.getInt(cur.getColumnIndex("active"))==1);
        res.setDt_create(new java.sql.Date(cur.getLong(cur.getColumnIndex("dt_create"))));
        res.setDt_update(new java.sql.Date(cur.getLong(cur.getColumnIndex("dt_update"))));
        return res;
    }

    @Override
    public ContentValues pack(Account acc) {
        ContentValues cv = new ContentValues();
        cv.put("name" , acc.getName());
        cv.put("saldo", acc.getSaldo());
        cv.put("currency", acc.getCurrency());
        cv.put("active" , (acc.isActive() ? 1 : 0 ));
        return cv;
    }
}
