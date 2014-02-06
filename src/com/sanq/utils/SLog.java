package com.sanq.utils;


import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 28.11.13
 * Time: 15:01
 */
public class SLog {
    public static void d(String log) {
        if (Cnt.DEBUG) {
            Log.d(Cnt.TAG, getCallMethod() +  log);
        }
    }

    public static void e(String log) {
        Log.e(Cnt.TAG, getCallMethod()  + log);
    }

    private static String getCallMethod() {
        String res = "";
        if (Thread.currentThread().getStackTrace().length >=5){
        StackTraceElement stackTraceElement =  Thread.currentThread().getStackTrace()[4];
            res = stackTraceElement.getClassName() + "::" + stackTraceElement.getMethodName() + ":: ";
        }
        return res;
    }

}
