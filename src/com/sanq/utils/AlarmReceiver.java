package com.sanq.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import com.sanq.dao.TransferDAO;
import com.sanq.ds.DbUtils;
import com.sanq.entity.Payord;
import com.sanq.entity.Transfer;
import com.sanq.exception.DBException;
import com.sanq.moneys.MainSlidingMenu;
import com.sanq.moneys.R;

import java.util.Date;

//http://startandroid.ru/ru/uroki/vse-uroki-spiskom/204-urok-119-pendingintent-flagi-requestcode-alarmmanager.html
//am broadcast -a android.intent.action.BOOT_COMPLETED -c android.intent.category.HOME -n com.blubuk/.AppReciever

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Cnt.INTENT_ACTION_WIDGET_UPDATE)) {          // from widget

            WidgetProvider.updateWidgetViews(context, ++WidgetProvider.idxCurrAcc);
//            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.widget);
//            remoteViews.setTextViewText(R.id.txtWidgetMain, "Test message from receiver.");
//            remoteViews.setOnClickPendingIntent(R.id.cmdWidgetRefresh, WidgetProvider.buildButtonPendingIntent(context));
//            WidgetProvider.pushWidgetUpdate(context.getApplicationContext(),remoteViews);

        } else if (intent.getAction().equals(Cnt.INTENT_ACTION_BOOT_COMPLETED)) {   //from system
            //recreating all  alarms from now()
            RemindManager.setAlarmTransfAll(context, Utils.zeroTime(new Date()));
            RemindManager.scheduleNextBackup(context, new Preferences(context));
        } else if (intent.getAction().equals(Cnt.INTENT_ACTION_BACKUP)) {           //from backup
            try {
                DbUtils.cycleBackupDb(context);
            } catch (DBException e) {
                Utils.procMessage(context, context.getResources().getString(R.string.mes_backup_schedule_error) + " " + e.getMessage());
            }
            new Preferences(context).setBackupLastTime(new Date());
            RemindManager.scheduleNextBackup(context, new Preferences(context));
        } else if (intent.getAction().equals(Cnt.INTENT_ACTION_TRANSFER)) {          //from scheduled trans start
            int reqCode;
            if ((reqCode = intent.getIntExtra(Cnt.BUNDLE_KEY_ALARM_TRANSACTION, -1)) != -1) {
                Payord pay = new TransferDAO(context).getById(reqCode).getPayord(context);
                if (pay != null) {
                    String curr = pay.getAccount(context).getCurrency();
                    Transfer transf = new TransferDAO(context).getById(reqCode);
                    String rimindDate = transf != null ? Utils.dateToString(context, transf.getDt()) + " " + Utils.floatToTime(pay.getTimeRemind()) : "";
                    String mes = pay.getName() + "  " + rimindDate;
                    Utils.procMessage(context, mes);
                    NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    Intent notificationIntent = new Intent(context, MainSlidingMenu.class);
                    notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    notificationIntent.putExtra(Cnt.BUNDLE_KEY_ALARM_TRANSACTION, reqCode);
                    NotificationCompat.Builder nb = new NotificationCompat.Builder(context)
                            //NotificationCompat.Builder nb = new NotificationBuilder(context) //для версии Android > 3.0
                            .setSmallIcon(R.drawable._app_icon)
                            .setAutoCancel(true)
                            .setContentText(mes)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setVibrate(new long[]{0, 100, 200, 300})
                            .setContentIntent(PendingIntent.getActivity(context, reqCode, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT))
                            .setContentTitle(context.getString(R.string.app_name) + " - " + Utils.intToMoney(pay.getAmount()) + " " + curr);
                    Notification notification = nb.build();
                    manager.notify(reqCode, notification);
                }
            }
        }
    }


}