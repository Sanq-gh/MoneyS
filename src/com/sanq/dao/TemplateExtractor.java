package com.sanq.dao;

import android.content.ContentValues;
import android.database.Cursor;
import com.sanq.entity.Template;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 29.05.13
 * Time: 12:17
 */
public class TemplateExtractor extends AbstractExtractor<Template> {

    @Override
    public Template extractCurrent(Cursor cur) {
        Template res = new Template();
        res.setId(cur.getInt(cur.getColumnIndex("_id")));
        res.setName(cur.getString(cur.getColumnIndex("name")));
        res.setIdPayord(cur.getInt(cur.getColumnIndex("id_payord")));
        res.setActive(cur.getInt(cur.getColumnIndex("active"))==1);
        res.setDt_create(new java.sql.Date(cur.getLong(cur.getColumnIndex("dt_create"))));
        res.setDt_update(new java.sql.Date(cur.getLong(cur.getColumnIndex("dt_update"))));
        return res;
    }

    @Override
    public ContentValues pack(Template entity) {
        ContentValues cv = new ContentValues();
        cv.put("name" , entity.getName() );
        cv.put("id_payord", entity.getIdPayord());
        cv.put("active" , (entity.isActive() ? 1 : 0 ));
        return cv;
    }

}
