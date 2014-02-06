package com.sanq.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ScrollView;
import android.widget.Toast;
import com.sanq.exception.DateCheckedException;
import com.sanq.exception.SavingException;
import com.sanq.moneys.MainSlidingMenu;
import com.sanq.moneys.R;

import java.io.*;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.text.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 27.05.13
 * Time: 11:48
 */
public class Utils {

    public static void deleteImagesNA(Context context, List<Integer> exclude) {
        try {
            File imgDir = new File(getAppPath(context, TypePath.Images) + "/");
            if (imgDir.isDirectory()) {
                for (File f : imgDir.listFiles()) {
                    try {
                        Integer intName = Integer.parseInt(f.getName().replace(Cnt.PHOTO_EXT, ""));
                        if (!exclude.contains(intName)) {
                            f.delete();
                        }
                    } catch (NumberFormatException e) {
                   /*NOP*/
                    }
                }
            }
        } catch (SavingException e) {
        /*NOP*/
        }
    }


    public static enum SlidingMenuOrientation {
        LEFT(0),
        RIGHT(1);
        private final int ent;

        private SlidingMenuOrientation(int ent) {
            this.ent = ent;
        }

        public int getInt() {
            return ent;
        }

        public static SlidingMenuOrientation fromInt(int x) {
            switch (x) {
                case 0:
                    return LEFT;
                case 1:
                    return RIGHT;
                default:
                    return null;
            }
        }

        @Override
        public String toString() {
            Context context = MainSlidingMenu.getAppContext();
            switch (ent) {
                case 0:
                    return context.getResources().getString(R.string.name_slide_orient_left);
                case 1:
                    return context.getResources().getString(R.string.name_slide_orient_right);
                default:
                    return "";
            }
        }

    }


    public static String fileToMD5(String filePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
            byte[] buffer = new byte[1024];
            MessageDigest digest = MessageDigest.getInstance("MD5");
            int numRead = 0;
            while (numRead != -1) {
                numRead = inputStream.read(buffer);
                if (numRead > 0)
                    digest.update(buffer, 0, numRead);
            }
            byte[] md5Bytes = digest.digest();
            return convertHashToString(md5Bytes);
        } catch (Exception e) {
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                }
            }
        }
    }

    private static String convertHashToString(byte[] md5Bytes) {
        String returnVal = "";
        for (int i = 0; i < md5Bytes.length; i++) {
            returnVal += Integer.toString((md5Bytes[i] & 0xff) + 0x100, 16).substring(1);
        }
        return returnVal.toUpperCase();
    }


    public static void scrollToEnd(final ScrollView scrollView) {
        scrollView.post(new Runnable() {
            public void run() {
                scrollView.scrollTo(0, scrollView.getBottom());
            }
        });

    }


    public static int GetPixelFromDips(Context context, float pixels) {
        // Get the screen's density scale
        final float scale = context.getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }


    public static ArrayList<File> GetFiles(String DirectoryPath) {
        ArrayList<File> resFiles = new ArrayList<File>();
        File f = new File(DirectoryPath);

        f.mkdirs();
        File[] files = f.listFiles();

        for (int i = 0; i < files.length; i++) {
            resFiles.add(files[i]);
        }
        return resFiles;
    }


    public static void copyFile(File src, File dst) throws IOException {
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }
    }

    /**
     * Returns whether an SD card is present and writable *
     */
    public static boolean isSdCardPresent() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }


    public static int getRandomColor(Random rnd) {
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }


    public static void disableEnableControls(boolean enable, ViewGroup vg) {
        List<View> controls = getControls(vg);
        for (View el : controls) {
            if (el instanceof ViewGroup) {
                disableEnableControls(enable, (ViewGroup) el);
            } else {
                el.setEnabled(enable);
            }
        }
    }

    private static List<View> getControls(ViewGroup vg) {
        List<View> res = new ArrayList<View>();
        for (int i = 0; i < vg.getChildCount(); i++) {
            res.add(vg.getChildAt(i));
        }
        return res;
    }


    /**
     * @param size
     * @param maxSize
     * @return ArrayList<Integer>indexes  (+ first and last indexes; thereby, res.size == maxSize(-+1)+2 )
     */
    public static List<Integer> averIndexes(int size, int maxSize) {
        ArrayList<Integer> res = new ArrayList<Integer>();
        if (maxSize < 2) {
            maxSize = 2;
        }
        int step = maxSize >= size ? 1 : size / maxSize;
        for (int i = step; i < size; i += step) {
            res.add(i);
        }
        res.add(0, 0);
        res.add(res.size(), size);
        return res;
    }


    public static Date addDays(Date dt, int count) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.add(Calendar.DAY_OF_MONTH, count);
        return cal.getTime();
    }

    public static DatePeriod zeroTime(DatePeriod dtPer) {
        return new DatePeriod(Utils.zeroTime(dtPer.dateFrom), Utils.zeroTime(dtPer.dateTo));
    }

    public static Date zeroTime(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();

    }

    public static void anim(View view) {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500);
        anim.setStartOffset(0);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        view.startAnimation(anim);
    }

    public static Date concatTimeDate(Date dt, float time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.set(Calendar.HOUR_OF_DAY, getTimePart(Calendar.HOUR_OF_DAY, time));
        cal.set(Calendar.MINUTE, getTimePart(Calendar.MINUTE, time));
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }


    public static int getTimePart(int partTime, float time) {
        int res = -1;
        int hour = (int) time;
        int min = Math.round((time - hour) * 100);
        try {
            checkTimeField(hour, min);
            switch (partTime) {
                case Calendar.HOUR:
                    res = hour;
                    break;
                case Calendar.HOUR_OF_DAY:
                    res = hour;
                    break;
                case Calendar.MINUTE:
                    res = min;
                    break;
            }
        } catch (DateCheckedException e) {
            /*NOP*/
        }
        return res;
    }


    public static String floatToTime(float time) {
        String res = "";
        int hour = (int) time;
        int min = Math.round((time - hour) * 100);
        try {
            checkTimeField(hour, min);
            res = String.format("%02d:%02d", hour, min);
        } catch (DateCheckedException e) {
            /*NOP*/
        }
        return res;
    }

    public static float timeToFloat(String time) throws DateCheckedException {
        float res = Float.MIN_VALUE;
        try {
            int hour = Integer.parseInt(time.split(":", 2)[0]);
            int min = Integer.parseInt(time.split(":", 2)[1]);
            res = timeToFloat(hour, min);
        } catch (Exception e) {
            throw new DateCheckedException("Wrong time format");
        }
        return res;
    }

    public static float timeToFloat(int hour, int min) {
        try {
            checkTimeField(hour, min);
            return (float) hour + ((float) min / 100);
        } catch (DateCheckedException e) {
            return -1;
        }
    }

    public static Date timeToDate(String time) throws DateCheckedException {
        Date res;
        try {
            int hour = Integer.parseInt(time.split(":", 2)[0]);
            int min = Integer.parseInt(time.split(":", 2)[1]);
            checkTimeField(hour, min);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, hour);
            cal.set(Calendar.MINUTE, min);
            res = cal.getTime();
        } catch (Exception e) {
            throw new DateCheckedException("Wrong time format");
        }
        return res;
    }

    private static void checkTimeField(int hour, int min) throws DateCheckedException {
        if (hour < 0 | hour > 59 | min < 0 | min > 59) {
            throw new DateCheckedException("Wrong time format");
        }
    }


    public static <T> T nvl(T... items) {
        for (T i : items) if (i != null) return i;
        return null;
    }


    //  app path ==========================================================

    public enum TypePath {
        Data, Images, Temp, BackUp
    }

    public static String getDBFileName(Context context) throws SavingException {
        return Utils.getAppPath(context, Utils.TypePath.Data) + "/" + Cnt.DB_NAME;
    }

    private static String getAppPath(Context context) throws SavingException {
        String rootPath;
        if (!isSdCardPresent()) {
            //throw new SavingException(context.getString(R.string.mes_sdcard_not_found));
            rootPath = context.getApplicationInfo().dataDir; //get app path
        } else {
            rootPath = Environment.getExternalStorageDirectory().toString();
        }
        File appDir = new File(rootPath + "//" + context.getResources().getString(R.string.app_name));
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        return appDir.getAbsolutePath();
    }

    public static String getAppPath(Context context, TypePath typePath) throws SavingException {
        File dir = new File(getAppPath(context) + "//" + typePath.toString());
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir.getAbsolutePath();
    }
    // ==========================================================


    public static String getSystemDelimiter() {
        DecimalFormat format = (DecimalFormat) DecimalFormat.getInstance(Locale.getDefault());
        DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
        return String.valueOf(symbols.getDecimalSeparator());
    }

    public static int moneyToInt(Context context, String strFloatValue) throws NumberFormatException {
        return Math.round(stringToFloat(context, strFloatValue) * 100);
    }

    public static String intToMoney(int intValue) {
        float flt = (float) intValue / 100;
        return floatToString(flt);
    }

    public static Double moneyIntToDouble(int intValue) {
        return ((double) intValue) / (double) 100;
    }

    public static float stringToFloat(Context context, String s) {
        try {
            return Float.parseFloat(s);
        } catch (NumberFormatException e) {
            String mes = context.getResources().getString(R.string.mes_pay_invalid_amount);
            throw new NumberFormatException(mes);
        }
    }

    public static String floatToString(float d) {
        DecimalFormatSymbols s = new DecimalFormatSymbols();
        s.setDecimalSeparator('.');
        DecimalFormat f = new DecimalFormat("#0.00", s);
        return f.format(d);
    }


    public static Date getDateFromString(Context context, String dt) {
        Format dateFormat = android.text.format.DateFormat.getDateFormat(context);
        Date res = null;
        try {
            res = (Date) dateFormat.parseObject(dt);
        } catch (ParseException e) {
            Utils.procMessage(context, e.getMessage());
        }
        return res;
    }


    public static String dateToString(Date dt, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(dt);
    }

    public static String dateToString(Context context, Date dt) {
        Format dateFormat = android.text.format.DateFormat.getDateFormat(context);
        return dateFormat.format(dt);
    }

    public static String getStringFromTime(Date dt) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(dt.getTime());
    }


    public static <T> int getObjectPos(T obj, List<T> lst) {
        int i = 0;
        if (obj != null & lst != null) {
            for (T o : lst) {
                if (o.equals(obj)) {
                    return i;
                }
                i++;
            }
        }
        return -1;
    }


    public static void showMessage(Activity activity, String title, String mes, DialogInterface.OnClickListener listener) {
//        if (!mes.isEmpty()) {
//            Log.d(Cnt.TAG, mes);
//        }

        if (listener == null) {
            listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                /*NOP*/
                }
            };
        }

        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(mes)
                .setPositiveButton(R.string.ok, listener)
                .show();
    }


    public static void procMessage(Context context, String mes) {
        procMessage(context, mes, true);
    }


    public static void procMessage(Context context, String mes, boolean isShowDialog) {
        if (!mes.isEmpty()) {
            SLog.e(mes);
        }
        if (isShowDialog) {
            Toast.makeText(context, mes, Toast.LENGTH_LONG).show();
        }
    }

    public static void procMessage(Context context, int idMes) {
        procMessage(context, context.getResources().getString(idMes));
    }

    public static String[] getArrayString(String... str) {
        List<String> buf = new ArrayList<String>();
        for (String s : str) {
            buf.add(s);
        }
        return buf.toArray(new String[0]);
    }

    public static String getTxtFile(Context context, int idRaw) {
        String res = "";
        Resources r = context.getResources();
        BufferedReader br = null;
        try {
            String line = "";
            br = new BufferedReader(new InputStreamReader(r.openRawResource(idRaw), Cnt.DEF_CHAR_SET));
            while ((line = br.readLine()) != null) {
                res += line.trim();
            }
        } catch (IOException e) {
            throw new SQLException("Can not parse sql: " + e.getMessage());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ignore) {/*NOP*/}
            }
        }
        return res;
    }


    public static HashMap<String, List<String>> getTablesDDL(Context context) {
        HashMap<String, List<String>> res = new HashMap<String, List<String>>();
        List<String> ddlScript = Utils.getSqlAsList(context, R.raw.meta);
        for (String sql : ddlScript) {
            if (sql.trim().startsWith("CREATE TABLE")) {
                Pattern p = Pattern.compile("\"([^\"]*)\"");
                Matcher m = p.matcher(sql);
                List<String> columnNames = new ArrayList<String>();
                String tableName = "";
                int i = 0;
                while (m.find()) {
                    if (i == 0) {
                        tableName = m.group(1);
                    } else {
                        columnNames.add(m.group(1));
                    }
                    if (!tableName.isEmpty()) {
                        res.put(tableName, columnNames);
                    }
                    i++;
                }
            }
        }
        return res;
    }


    public static List<String> getSqlAsList(Context context, int idRaw) {
        List<String> res = new ArrayList<String>();
        Resources r = context.getResources();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(r.openRawResource(idRaw), Cnt.DEF_CHAR_SET));
            String line = null;
            String sql = "";
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.startsWith("--")) {  //exclude remark
                    sql += " " + line + " ";
                    if (sql.indexOf(Cnt.DB_DELIMITER_STATEMENT) >= 0) {
                        sql = sql.replaceAll(Cnt.DB_DELIMITER_STATEMENT, "");
                        res.add(sql);
                        sql = "";
                    }
                }
            }
        } catch (IOException e) {
            throw new SQLException("Can not parse sql: " + e.getMessage());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ignore) {/*NOP*/}
            }
        }
        return res;
    }


}
