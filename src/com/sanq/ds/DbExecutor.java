package com.sanq.ds;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created with IntelliJ IDEA.
 * User: sanq
 * Date: 02.06.13
 * Time: 20:25
 * To change this template use File | Settings | File Templates.
 */
public interface DbExecutor<T> {
    public void execute(Context context, SQLiteDatabase db);
    public T getResult();


}
