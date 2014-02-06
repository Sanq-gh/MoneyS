package com.sanq.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.sanq.dao.AccountDAO;
import com.sanq.entity.Account;
import com.sanq.entity.Period;
import com.sanq.moneys.R;

import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: sanq
 * Date: 03.11.13
 * Time: 13:06
 * To change this template use File | Settings | File Templates.
 */
public class Preferences {
    private  Context context;
    private  SharedPreferences preferences ;

    public Preferences(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(Cnt.PREFS_NAME, Context.MODE_PRIVATE);
    }

    private enum Type {
        DEFAULT_ID_ACC,
        MENU_ORIENT,
        DEFAULT_TYPE_REPORT_PERIOD,
        DEFAULT_REMINDER_TIME,
        COMPRESS_VALUE,
        BACKUP_CURRENT ,
        BACKUP_MAX_COUNT  ,
        BACKUP_IS_SCHEDULE,
        BACKUP_LAST_TIME,
        BACKUP_TIME_SCHEDULE,
        BACKUP_INTERVAL_SCHEDULE


    }


    //===============================  Defaults

    public  Account getDefaultAcc() {
        int idAcc = getPref(Type.DEFAULT_ID_ACC, -1);
        AccountDAO accDAO = new AccountDAO(context);
        Account res = accDAO.getById(idAcc);
        if (res == null) {
            List<Account> accounts = accDAO.getAll();
            if (accounts.size() > 0) {
                res = accounts.get(0);
            } else {
                //если нифига нету - создаем
                Account acc = new Account(context.getResources().getString(R.string.default_account_name), 0, java.util.Currency.getInstance(Locale.getDefault()).getCurrencyCode());
                res = accDAO.add(acc);
            }
            setDefaultAcc(res);
        }
        return res;
    }
    public  void setDefaultAcc(Account acc) {
        setPref(Type.DEFAULT_ID_ACC, acc.getId());
    }

    public   Period.ReportType getDefaultTypeReportPeriod() {
        return Period.ReportType.fromInt(getPref(Type.DEFAULT_TYPE_REPORT_PERIOD, Cnt.INIT_TYPE_REPORT_PERIOD));
    }
    public   void setDefaultTypeReportPeriod(Period.ReportType periodReportType) {
        setPref(Type.DEFAULT_TYPE_REPORT_PERIOD, periodReportType.getInt());
    }

    public   float getDefaultReminderTime() {
        return getPref(Type.DEFAULT_REMINDER_TIME, Cnt.INIT_TIME_REMIND);
    }
    public   void setDefaultReminderTime(float reminderTime) {
        setPref(Type.DEFAULT_REMINDER_TIME, reminderTime);
    }


//===============================  Interface

    public  Utils.SlidingMenuOrientation getMenuOrient() {
        return Utils.SlidingMenuOrientation.fromInt(getPref(Type.MENU_ORIENT, Cnt.INIT_SLIDE_MENU_ORIENTATION));
    }
    public   void setMenuOrient(Utils.SlidingMenuOrientation menuOrient) {
        setPref(Type.MENU_ORIENT, menuOrient.getInt());
    }

    public   int getCompressValue() {
        return getPref(Type.COMPRESS_VALUE, Cnt.INIT_PHOTO_QUALITY_COMPRESS);
    }
    public   void setCompressValue(int compressValue) {
        setPref(Type.COMPRESS_VALUE, compressValue);
    }


//===============================  Schedule

    public   boolean isBackupSchedule() {
        return getPref(Type.BACKUP_IS_SCHEDULE, Cnt.INIT_BACKUP_IS_SCHEDULE);
    }
    public   void setBackupSchedule(boolean isSchedule) {
        setPref(Type.BACKUP_IS_SCHEDULE, isSchedule);
    }

    public Date getBackupLastTime() {
        return new Date(getPref(Type.BACKUP_LAST_TIME,  new Date().getTime()));
    }
    public   void setBackupLastTime(Date lastTime) {
        setPref(Type.BACKUP_LAST_TIME, lastTime.getTime());
    }

    public   float getBackupTime() {
        return getPref(Type.BACKUP_TIME_SCHEDULE, Cnt.INIT_BACKUP_TIME_SCHEDULE);
    }
    public   void setBackupTime(float scheduleTime) {
        setPref(Type.BACKUP_TIME_SCHEDULE, scheduleTime);
    }


    public   Period.Type getBackupInterval() {
        return Period.Type.fromInt(getPref(Type.BACKUP_INTERVAL_SCHEDULE, Cnt.INIT_BACKUP_INTERVAL_SCHEDULE));
    }
    public   void setBackupInterval(Period.Type interval) {
        setPref(Type.BACKUP_INTERVAL_SCHEDULE, interval.getInt());
    }


    public   int getMaxBackupCount() {
        return getPref(Type.BACKUP_MAX_COUNT, Cnt.INIT_BACKUP_MAX_COUNT);
    }
    public   void setMaxBackupCount(int maxBackupCount) {
        setPref(Type.BACKUP_MAX_COUNT, maxBackupCount);
    }

    public   int getBackupCurrent() {
        return getPref(Type.BACKUP_CURRENT, Cnt.INIT_BACKUP_CURRENT);
    }
    public   void setBackupCurrent(int backupCurrent) {
        setPref(Type.BACKUP_CURRENT, backupCurrent);
    }










//===============================  private
    private   int getPref(Type pref, int defValue) {
        return preferences.getInt(pref.toString(), defValue);
    }

    private   float getPref(Type pref, float defValue) {
        return preferences.getFloat(pref.toString(), defValue);
    }

    private   String getPref(Type pref, String defValue) {
        return preferences.getString(pref.toString(), defValue);
    }

    private   boolean getPref(Type pref, boolean defValue) {
        return preferences.getBoolean(pref.toString(), defValue);
    }
    private   long getPref(Type pref, long defValue) {
        return preferences.getLong(pref.toString(), defValue);
    }

    private   void setPref(Type pref, int value) {
        SharedPreferences.Editor ed = preferences.edit();
        ed.putInt(pref.toString(), value);
        ed.commit();
    }

    private   void setPref(Type pref, float value) {
        SharedPreferences.Editor ed = preferences.edit();
        ed.putFloat(pref.toString(), value);
        ed.commit();
    }

    private   void setPref(Type pref, String value) {
        SharedPreferences.Editor ed = preferences.edit();
        ed.putString(pref.toString(), value);
        ed.commit();
    }
    private   void setPref(Type pref, boolean value) {
        SharedPreferences.Editor ed = preferences.edit();
        ed.putBoolean(pref.toString(), value);
        ed.commit();
    }

    private   void setPref(Type pref, long value) {
        SharedPreferences.Editor ed = preferences.edit();
        ed.putLong(pref.toString(), value);
        ed.commit();
    }

}
