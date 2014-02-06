package com.sanq.ds;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import com.sanq.dao.*;
import com.sanq.exception.DBException;
import com.sanq.exception.SavingException;
import com.sanq.utils.Cnt;
import com.sanq.utils.Preferences;
import com.sanq.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 04.11.13
 * Time: 9:47
 */
public class DbUtils {


    public static void clearDb(Context context) throws DBException {
        //physics clear tables
        SQLiteDatabase db = null;
        try {
            db = DS.getInstance().getDB(context);
            if (db != null && db.inTransaction()) {
                db.endTransaction();
            }
            db.beginTransaction();
            new TransferDAO(context).physicsDeleteNotActive();
            new TemplateDAO(context).physicsDeleteNotActive();
            new PayordDAO(context).physicsDeleteNotActive();
            new PeriodDAO(context).physicsDeleteNotActive();
            new AccountDAO(context).physicsDeleteNotActive();
            if (db.inTransaction()) {
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            throw new DBException("Error clear DB: " + e.getMessage());
        } finally {
            if (db != null && db.inTransaction()) {
                db.endTransaction();
            }
        }
    }

    public static void vacuumDb(Context context) throws DBException {
        SQLiteDatabase db = null;
        try {
            db = DS.getInstance().getDB(context);
            db.execSQL("VACUUM;");
        } catch (Exception e) {
          throw  new DBException("Error VACUUM DB: " + e.getMessage());
        }
    }


    public static String getDefaultBackupName(Context context) throws DBException {
        try {
            return Utils.getAppPath(context, Utils.TypePath.BackUp) + "//" + Cnt.DB_NAME;
        } catch (SavingException e) {
            throw new DBException(e.getMessage());
        }
    }

    private static File getDBFile(Context context) throws DBException {
        try {
            return context.getDatabasePath(Utils.getDBFileName(context));
        } catch (SavingException e) {
            throw new DBException(e.getMessage());
        }
    }

    /**
     * Backup DB with next name
     */
    public static void cycleBackupDb(Context context) throws DBException {
        File dbFile = getDBFile(context);
        Preferences prefs = new Preferences(context);
        String bakupFileName = null;
        try {
            bakupFileName = Utils.getAppPath(context, Utils.TypePath.BackUp) + "//" + Cnt.DB_NAME + "." + Cnt.DB_BACKUP_EXT + ".";
        } catch (SavingException e) {
            throw new DBException(e.getMessage());
        }
        if (prefs.getBackupCurrent() >= prefs.getMaxBackupCount()) {
            int i = Cnt.INIT_BACKUP_CURRENT;
            new File(bakupFileName + i).delete();
            for (++i; i <= prefs.getMaxBackupCount(); i++) {
                new File(bakupFileName + i).renameTo(new File(bakupFileName + (i - 1)));
            }
            bakupFileName = bakupFileName + prefs.getMaxBackupCount();
        } else {
            bakupFileName = bakupFileName + (prefs.getBackupCurrent() + 1);
        }

        File backupFile = new File(bakupFileName);
        backupDb(dbFile, backupFile);

        if (prefs.getBackupCurrent() < prefs.getMaxBackupCount()) {
            prefs.setBackupCurrent(prefs.getBackupCurrent() + 1);
        }
    }


    /**
     * Backup DB with default name and path (Utils.getAppPath + Cnt.Cnt.DB_NAME)
     */
    public static void backupDb(Context context) throws DBException {
        File dbFile = null;
        try {
            dbFile = getDBFile(context);
            File backupFile = new File(getDefaultBackupName(context));
            backupDb(dbFile, backupFile);
        } catch (DBException e) {
            throw new DBException(e.getMessage());
        }
    }

    /**
     * Restore DB from default path ans name (Utils.getAppPath + Cnt.Cnt.DB_NAME)
     */
    public static void restoreDb(Context context) throws DBException {
        File dbFile = getDBFile(context);
        File backupFile = new File(getDefaultBackupName(context));
        restoreDb(context, dbFile, backupFile);
    }

    public static void backupDb(File dbFile, File backupFile) throws DBException {
        try {
            backupFile.createNewFile();
            Utils.copyFile(dbFile, backupFile);
        } catch (IOException e) {
            throw new DBException(e.getMessage());
        }
    }

    public static void restoreDb(Context context, File backupFile) throws DBException {
        restoreDb(context, getDBFile(context), backupFile);
    }

    public static void restoreDb(Context context, File dbFile, File backupFile) throws DBException {
        if (!backupFile.exists()) {
            throw new DBException("File" + backupFile.getAbsolutePath() + " does not exist");
        }
        if (!checkDbIsValid(context, backupFile)) {
            throw new DBException("DB is not valid.");
        }
        try {
            dbFile.createNewFile();
            Utils.copyFile(backupFile, dbFile);
        } catch (IOException e) {
            throw new DBException(e.getMessage());
        }

    }

    public static boolean checkDbIsValid(Context context, File db) {
        try {
            SQLiteDatabase sqlDb = SQLiteDatabase.openDatabase(db.getPath(), null, SQLiteDatabase.OPEN_READONLY);
            HashMap<String, List<String>> tables = Utils.getTablesDDL(context);

            for (Map.Entry<String, List<String>> entry : tables.entrySet()) {
                Cursor cursor = sqlDb.query(true, entry.getKey(), null, null, null, null, null, null, null);
                for (String column : entry.getValue()) {
                    if (cursor.getColumnIndex(column) == -1) {
                        cursor.close();
                        sqlDb.close();
                        throw new IllegalArgumentException("Database valid but not the right type");
                    }
                }
                cursor.close();
            }
            sqlDb.close();
        } catch (IllegalArgumentException e) {
            Utils.procMessage(context, e.getMessage(),false);
            return false;
        } catch (SQLiteException e) {
            Utils.procMessage(context, "Database file is invalid." + e.getMessage(),false);
            return false;
        } catch (Exception e) {
            Utils.procMessage(context, e.getMessage(),false);
            return false;
        }
        return true;
    }


    /** Imports the file at IMPORT_FILE **/
//    protected static boolean importIntoDb(Context ctx){
//        if( ! SdIsPresent() ) return false;
//
//        File importFile = IMPORT_FILE;
//
//        if( ! checkDbIsValid(importFile) ) return false;
//
//        try{
//            SQLiteDatabase sqlDb = SQLiteDatabase.openDatabase
//                    (importFile.getPath(), null, SQLiteDatabase.OPEN_READONLY);
//
//            Cursor cursor = sqlDb.query(true, DATABASE_TABLE,
//                    null, null, null, null, null, null, null
//            );
//
//            DbAdapter dbAdapter = new DbAdapter(ctx);
//            dbAdapter.open();
//
//            final int titleColumn = cursor.getColumnIndexOrThrow("title");
//            final int timestampColumn = cursor.getColumnIndexOrThrow("timestamp");
//
//            // Adds all items in cursor to current database
//            cursor.moveToPosition(-1);
//            while(cursor.moveToNext()){
//                dbAdapter.createQuote(
//                        cursor.getString(titleColumn),
//                        cursor.getString(timestampColumn)
//                );
//            }
//
//            sqlDb.close();
//            cursor.close();
//            dbAdapter.close();
//        } catch( Exception e ){
//            e.printStackTrace();
//            return false;
//        }

//        return true;
//    }

}
