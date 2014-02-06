package com.sanq.dao;

import android.content.ContentValues;
import android.database.Cursor;
import com.sanq.entity.Category;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 29.05.13
 * Time: 12:17
 */
public class CategoryExtractor extends AbstractExtractor<Category> {

    @Override
    public Category extractCurrent(Cursor cur) {
        Category res = new Category();
        res.setId(cur.getInt(cur.getColumnIndex("_id")));
        res.setName(cur.getString(cur.getColumnIndex("name")));
        res.setId_parent(cur.getInt(cur.getColumnIndex("id_parent")));
        res.setIsgroup(cur.getInt(cur.getColumnIndex("isgroup"))==1);
        res.setActive(cur.getInt(cur.getColumnIndex("active"))==1);
        res.setDt_create(new java.sql.Date(cur.getLong(cur.getColumnIndex("dt_create"))));
        res.setDt_update(new java.sql.Date(cur.getLong(cur.getColumnIndex("dt_update"))));
        return res;
    }

    @Override
    public ContentValues pack(Category cat) {
        ContentValues cv = new ContentValues();
        cv.put("name" , cat.getName() );
        cv.put("id_parent", cat.getId_parent() );
        cv.put("isgroup" , (cat.isGroup() ? 1 : 0 ));
        cv.put("active" , (cat.isActive() ? 1 : 0 ));
        return cv;
    }

}
