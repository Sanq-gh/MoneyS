package com.sanq.dao;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 29.05.13
 * Time: 12:14
 */
public interface Extractor<T> {
    public T extractCurrent(Cursor cur);
    public List<T> extractAll(Cursor cur);
    public ContentValues pack(T entity);

}
