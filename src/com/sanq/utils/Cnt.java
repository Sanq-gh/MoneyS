package com.sanq.utils;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.sanq.entity.Period;

/**
 * Created with IntelliJ IDEA.
 * User: pospelov
 * Date: 27.05.13
 * Time: 11:35
 * To change this template use File | Settings | File Templates.
 */
public interface Cnt {
    public static final boolean DEBUG = true;

    public static final String TAG =  "mLog";
    public static final String DEF_CHAR_SET = "UTF-8";

    //  db
    public static final String DB_NAME = "pay.db";
    public static final int    DB_VERSION = 1;
    public static final String DB_DELIMITER_STATEMENT = ";;";
    public static final String DB_BACKUP_EXT = "bkp";



    //bundle keys
    public static final String  BUNDLE_KEY_TYPE_START = "BUNDLE_KEY_TYPE_START" ;
    public static final String BUNDLE_KEY_ALARM_TRANSACTION = "BUNDLE_KEY_ALARM_TRANSACTION" ;
    public static final String BUNDLE_KEY_HANDLER_MES = "BUNDLE_KEY_HANDLER_MES" ;


    //other
    public static  final String PHOTO_TMP_NAME = "img.tmp";
    public static  final String PHOTO_EXT = ".jpeg";
    public static final boolean IS_24_TIME_FORMAT = true ;
    public static  final int CHART_MAX_X = 4 ;


    //prefs
    public static final String  PREFS_NAME = "APP_PREFS" ;
    public static final int INIT_SLIDE_MENU_ORIENTATION = SlidingMenu.RIGHT;
    public static  final float INIT_TIME_REMIND = 12.0f;
    public static  final int INIT_PHOTO_QUALITY_COMPRESS = 75;  //50-100,  в процентах 0% - 100%
    public static  final int INIT_TYPE_REPORT_PERIOD = Period.ReportType.THIS_MONTH.getInt();
    public static  final boolean INIT_BACKUP_IS_SCHEDULE = true;
    public static  final float INIT_BACKUP_TIME_SCHEDULE = 0.0f;
    public static  final int INIT_BACKUP_INTERVAL_SCHEDULE = Period.Type.WEEK.getInt();
    public static  final int INIT_BACKUP_MAX_COUNT = 5;
    public static  final int INIT_BACKUP_CURRENT = 1;


    //alarm
    public static final String INTENT_ACTION_BACKUP =   "com.sanq.moneys.INTENT_ACTION_BACKUP";
    public static final String INTENT_ACTION_TRANSFER = "com.sanq.moneys.INTENT_ACTION_TRANSFER";
    //its  android action!
    public static final String INTENT_ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";


    // handler code
    public final int HANDLER_MES_ERROR = -1;
    public final int HANDLER_MES_FINISH = 0;
    public final int HANDLER_MES_START = 1;
    public final int HANDLER_MES_DB_CLEAR = 2;
    public final int HANDLER_MES_DB_VACUUM = 3;
    public final int HANDLER_MES_DB_BACKUP = 4;
    public final int HANDLER_MES_IMAGES_CLEAR = 5;


    //widget
    public static final String  INTENT_ACTION_WIDGET_UPDATE = "com.sanq.moneys.INTENT_ACTION_WIDGET_UPDATE";



}
