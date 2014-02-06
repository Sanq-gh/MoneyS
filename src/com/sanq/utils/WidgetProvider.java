package com.sanq.utils;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import com.sanq.core.Report;
import com.sanq.dao.AccountDAO;
import com.sanq.entity.Account;
import com.sanq.moneys.R;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 04.12.13
 * Time: 17:20
 */
public class WidgetProvider extends AppWidgetProvider {
    public static int idxCurrAcc = -1;

    @Override
    public void onEnabled(Context context) {
        Account defAcc = new Preferences(context).getDefaultAcc();
        List<Account> accounts = new AccountDAO(context).getAll();
        idxCurrAcc = accounts.lastIndexOf(defAcc);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        SLog.d("widget - on update");
        final int N = appWidgetIds.length;
        for (int i=0; i<N; i++) {
            updateWidgetViews(context, idxCurrAcc);
        }
    }


    public static void updateWidgetViews(Context context, int idxAccount) {

        Date onDate = Utils.zeroTime(new Date());


        Map<String, Integer> saldo = new Report(context).getSaldoOnDate(onDate);
        StringBuilder strSaldo = new StringBuilder();
        for (String key : saldo.keySet()) {
            strSaldo.append(key + ": " + Utils.intToMoney(saldo.get(key)) + "  ");
        }

        List<Account> accounts = new AccountDAO(context).getAll();

        if (idxAccount < 0 || idxAccount > accounts.size() - 1) {
            WidgetProvider.idxCurrAcc = 0;
            idxAccount = 0;
        }

        Account acc = accounts.get(idxAccount);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

        if (acc != null) {
            remoteViews.setTextViewText(R.id.txtWidgetAcc, acc.getName());

            int amount = acc.getCurrentSaldo(context);
            remoteViews.setTextColor(R.id.txtWidgetAmount, amount < 0 ? context.getResources().getColor(R.color.dark_red) : context.getResources().getColor(R.color.xdark_green));
            remoteViews.setTextViewText(R.id.txtWidgetAmount, Utils.intToMoney(amount));
            remoteViews.setTextViewText(R.id.txtWidgetCurrency, acc.getCurrency());
        } else {
            remoteViews.setTextViewText(R.id.txtWidgetAcc, "account not found");
        }


        remoteViews.setOnClickPendingIntent(R.id.cmdWidgetNextAcc, buildButtonPendingIntent(context));
        remoteViews.setTextViewText(R.id.txtWidgetFooter, strSaldo);
        pushWidgetUpdate(context, remoteViews);
    }


    private static PendingIntent buildButtonPendingIntent(Context context) {
        //++MyWidgetIntentReceiver.clickCount;
        Intent intent = new Intent();
        intent.setAction(Cnt.INTENT_ACTION_WIDGET_UPDATE);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
        ComponentName myWidget = new ComponentName(context, WidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget, remoteViews);
    }


}
