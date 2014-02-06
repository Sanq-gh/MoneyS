package com.sanq.utils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import com.sanq.entity.Period;
import com.sanq.entity.Transfer;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: sanq
 * Date: 23.11.13
 * Time: 8:17
 * To change this template use File | Settings | File Templates.
 */
public class RemindManager {

    public static void cancelAlarmTransfAll(Context context) {
        Map<Integer, Date> alarmTransf = Transfer.getAllScheduled(context, new Date());
        List<Integer> idsTransf = new ArrayList<Integer>();
        for (Integer el : alarmTransf.keySet()) {
            idsTransf.add(el);
        }
        cancelAlarmTransf(context, idsTransf);
    }

    public static void setAlarmTransfAll(Context context, Date fromDate) {
        Map<Integer, Date> alarmTransf = Transfer.getAllScheduled(context, fromDate);
        setAlarmTransf(context, alarmTransf);
    }

    public static void scheduleNextBackup(Context context, Preferences prefs) {
        if (prefs.isBackupSchedule()) {
            Date dt = Utils.concatTimeDate(Period.calcNextDate(prefs.getBackupLastTime(), prefs.getBackupInterval()), prefs.getBackupTime());
            //Date dt = Utils.concatTimeDate(new Date(), prefs.getBackupTime());
            setAlarmBackup(context, dt);
        } else {
            cancelAlarmBackup(context);
        }
    }

    public static void setAlarmBackup(Context context, Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        Intent intent = new Intent(Cnt.INTENT_ACTION_BACKUP, null, context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        android.app.AlarmManager alarmManager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(android.app.AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
    }

    public static void cancelAlarmBackup(Context context) {
        Intent intent = new Intent(Cnt.INTENT_ACTION_BACKUP, null, context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        android.app.AlarmManager alarmManager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        sender.cancel();
        alarmManager.cancel(sender);
    }

    public static boolean isAlarmBackupSet(Context context) {
        return (PendingIntent.getBroadcast(context, 0
                , new Intent(Cnt.INTENT_ACTION_BACKUP)
                , PendingIntent.FLAG_NO_CREATE) != null);
    }

    public static void setAlarmTransf(Context context, Map<Integer, Date> alarms) {
        for (Map.Entry<Integer, Date> el : alarms.entrySet()) {
            setAlarmTransf(context, el.getKey(), el.getValue());
        }
    }

    public static void setAlarmTransf(Context context, int reqCode, Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        Intent intent = new Intent(Cnt.INTENT_ACTION_TRANSFER, null, context, AlarmReceiver.class);
        intent.putExtra(Cnt.BUNDLE_KEY_ALARM_TRANSACTION, reqCode);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, reqCode, intent, 0);
        android.app.AlarmManager alarmManager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // Log.d(Cnt.TAG, "set: " + String.valueOf(reqCode) + " "  + Utils.dateToString(dt, "dd.MM.yyyy hh:mm:ss"));
        alarmManager.set(android.app.AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
    }


    public static void cancelAlarmTransf(Context context, List<Integer> alarmRegCodes) {
        for (Integer el : alarmRegCodes) {
            cancelAlarmTransf(context, el);
        }
    }

    public static void cancelAlarmTransf(Context context, int reqCode) {
        Intent intent = new Intent(Cnt.INTENT_ACTION_TRANSFER, null, context, AlarmReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, reqCode, intent, 0);
        android.app.AlarmManager alarmManager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //Log.d(Cnt.TAG, "canceled: " + String.valueOf(reqCode));
        sender.cancel();
        alarmManager.cancel(sender);
    }


    public static boolean isAlarmTransfSet(Context context, int reqCode) {
        return (PendingIntent.getBroadcast(context, reqCode
                , new Intent(Cnt.INTENT_ACTION_TRANSFER, null, context, AlarmReceiver.class)
                , PendingIntent.FLAG_NO_CREATE) != null);
    }

}
