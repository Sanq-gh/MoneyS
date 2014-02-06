package com.sanq.dao;

import android.database.Cursor;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 29.05.13
 * Time: 12:19
 */
public abstract class  AbstractExtractor<T> implements Extractor<T>{
    public List<T> extractAll(Cursor cur) {
        List<T> res = new LinkedList<T>();
        if ( cur.moveToFirst() ) {
            do {
                res.add( extractCurrent(cur));
            } while (cur.moveToNext());
        }
        return res;
    }
}
