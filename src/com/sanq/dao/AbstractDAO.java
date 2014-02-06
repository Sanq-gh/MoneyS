package com.sanq.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import com.sanq.entity.Entity;
import com.sanq.utils.SLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 29.05.13
 * Time: 11:35
 */
public abstract class AbstractDAO<T extends Entity> extends DAO  {

    public AbstractDAO(Context context) {
        super(context);
    }



    protected abstract String getTableName();
    protected abstract Extractor<T> getExtractor();



    protected List<String> getColumnsAsList(String tableName) {
        List<String> res = new ArrayList<String>();
        Cursor cur = getDb().rawQuery("PRAGMA table_info(" + tableName + ")", null);
        if (cur.moveToFirst()) {
            do {
                res.add(cur.getString(1));
            } while (cur.moveToNext());
        }
        cur.close();
        return res;
    }

    protected String[] getColumns(String tableName){
        return this.getColumnsAsList(tableName).toArray(new String[0]);
    }

    //-------- SELECT ------------------
    public Cursor getRawQuery(String sql, String[] selectionArgs){
       return getDb().rawQuery(sql, selectionArgs);
    }

    public T getById(long id ) {
        return getById(id, false);
    }

    public T getById(long id , boolean withDeleted) {
        T res = null;
        String selection = withDeleted ? "_id = ? " :  "_id = ? and active = 1" ;
        Cursor cur = getDb().query(false, this.getTableName(), this.getColumns(this.getTableName()),selection , new String[]{String.valueOf(id)}, null, null, null, null);
        if (cur.moveToFirst()) {
            res = getExtractor().extractCurrent(cur);
        }
        cur.close();
        return res;
    }

    public List<T> getAll() {
        return  getAll(false);
    }

    public List<T> getAll( boolean withDeleted) {
        List<T> res;
        String selection = withDeleted ? "" : "active = 1" ;
        Cursor cur = getDb().query(false, this.getTableName(), this.getColumns(this.getTableName()), selection , null , null, null, null, null);
        res = getExtractor().extractAll(cur);
        cur.close();
        return res;
    }


     public List<T> getByCondition(String selection, String[] selectionArgs) {
        List<T> res;
        selection = selection.isEmpty() ? "active = 1" : selection + " and active = 1" ;
        //Log.d(Cnt.TAG, "selection - " + selection + "  args - " + Arrays.asList(selectionArgs).toString()  );
        Cursor cur = getDb().query(false, this.getTableName(), this.getColumns(this.getTableName()), selection , selectionArgs, null, null, null, null);
        res = getExtractor().extractAll(cur);
        cur.close();
        return res;
    }


    //-------- MODIFY ------------------

    public T add(T entity) throws SQLiteException{
        try{
        long id = getDb().insertOrThrow(getTableName(), null, getExtractor().pack(entity));
        return  this.getById(id);
       }catch (SQLiteException ex){
         String mes  = "Failed insert: " +  entity.getClass().getName() + " : " + ex.getMessage();
         throw  new SQLiteException(mes);
       }
    }

 /**
  * Recommended call addAll(List<T> ents) in transaction.
  */
    public void addAll(List<T> ents){
       for (T ent : ents) {
         add(ent);
        }
    }

    public void deleteById(long id){
        T ent =  getById(id);
       delete(ent);
    }

    public void delete(T ent){
        ent.setActive(false);
        update(ent);
    }

    public void update(T entity) throws SQLiteException {
        int res = 0;
        int conflictAlgorithm = SQLiteDatabase.CONFLICT_NONE;
        if (getDb().inTransaction()) {
            conflictAlgorithm = SQLiteDatabase.CONFLICT_ROLLBACK;
        }
        res = getDb().updateWithOnConflict(this.getTableName(), getExtractor().pack(entity), "_id = ? ", new String[]{String.valueOf(entity.getId())}, conflictAlgorithm);
        if (res != 1) {
            String mes = "Failed update. id = '" + entity.getId() + "' " + entity.getClass().getName();
            SLog.d(mes);
            throw new SQLiteException(mes);
        }
    }

    /**
     * Physical delete record.
     */
    @Deprecated
    public int physicsDeleteById(long id) throws SQLiteException{
        int res;
        res = getDb().delete(this.getTableName(), "_id = ?", new String[]{String.valueOf(id)});
        if (res != 1) {
            String mes = "Failed physics delete. id = '" + id + "' " ;
            SLog.d(mes);
            throw new SQLiteException(mes);
        }
        return res;
    }


    public int physicsDeleteNotActive() {
       return getDb().delete(this.getTableName(), "active = ?", new String[]{String.valueOf(0)});
    }




}
