package com.sanq.entity;

import android.content.Context;
import com.sanq.moneys.MainSlidingMenu;
import com.sanq.moneys.R;
import com.sanq.utils.DatePeriod;
import com.sanq.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 29.05.13
 * Time: 11:17
 */
public class Period extends AbstractEntity implements Entity, Serializable {
    //static ..................................
    public static String getBundleKey() {
        return "PERIOD";
    }

    private static final int TYPE_SCROLL_ADD = 1;
    private static final int TYPE_SCROLL_ROLL = -1;


    public static Date calcNextDate(Date dt, Type type) {
        return calcNextDate(dt, type, 1);
    }

    public static Date calcPrevDate(Date dt, Type type) {
        return calcPrevDate(dt, type, 1);
    }

    public static Date calcNextDate(Date dt, Type type, int count) {
        return calcDate(TYPE_SCROLL_ADD, dt, type, count);
    }

    public static Date calcPrevDate(Date dt, Type type, int count) {
        return calcDate(TYPE_SCROLL_ROLL, dt, type, count);
    }

    private static Date calcDate(int typeScroll, Date startDate, Type type, int count) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        count *= typeScroll;
        switch (type) {
            case DAY:
                cal.add(Calendar.DATE, 1 * count);
                break;
            case WEEK:
                cal.add(Calendar.DATE, 7 * count);
                break;
            case MONTH:
                cal.add(Calendar.MONTH, 1 * count);
                break;
            case QUART:
                cal.add(Calendar.MONTH, 3 * count);
                break;
            case YEAR:
                cal.add(Calendar.YEAR, 1 * count);
                break;
        }
        return cal.getTime();
    }


    public static enum Type implements Serializable {
        DAY(0),
        WEEK(1),
        MONTH(2),
        QUART(3),
        YEAR(4);

        private final int ent;

        private Type(int ent) {
            this.ent = ent;
        }

        public int getInt() {
            return ent;
        }

        public static Type fromInt(int x) {
            switch (x) {
                case 0:
                    return DAY;
                case 1:
                    return WEEK;
                case 2:
                    return MONTH;
                case 3:
                    return QUART;
                case 4:
                    return YEAR;
                default:
                    return null;
            }
        }

        @Override
        public String toString() {
            Context context = MainSlidingMenu.getAppContext();
            switch (ent) {
                case 0:
                    return context.getResources().getString(R.string.name_period_type_day);
                case 1:
                    return context.getResources().getString(R.string.name_period_type_week);
                case 2:
                    return context.getResources().getString(R.string.name_period_type_month);
                case 3:
                    return context.getResources().getString(R.string.name_period_type_quart);
                case 4:
                    return context.getResources().getString(R.string.name_period_type_year);
                default:
                    return "";
            }
        }

        public static String getBundleKey() {
            return "TYPE_INTERVAL";
        }
    }


    public static enum ReportType {
        TODAY(0),
        THIS_WEEK(1),
        THIS_MONTH(2),
        THIS_YEAR(3),
        NEXT_WEEK(4),
        NEXT_MONTH(5),
        NEXT_YEAR(6),
        PREV_WEEK(7),
        PREV_MONTH(8),
        PREV_YEAR(9),
        ALL_TIME(10),
        CUSTOM(11);


        private final int ent;

        private ReportType(int ent) {
            this.ent = ent;
        }

        public int getInt() {
            return ent;
        }

        public static ReportType fromInt(int x) {
            switch (x) {
                case 0:
                    return TODAY;
                case 1:
                    return THIS_WEEK;
                case 2:
                    return THIS_MONTH;
                case 3:
                    return THIS_YEAR;
                case 4:
                    return NEXT_WEEK;
                case 5:
                    return NEXT_MONTH;
                case 6:
                    return NEXT_YEAR;
                case 7:
                    return PREV_WEEK;
                case 8:
                    return PREV_MONTH;
                case 9:
                    return PREV_YEAR;
                case 10:
                    return ALL_TIME;
                case 11:
                    return CUSTOM;
                default:
                    return null;
            }
        }

        public static String[] getStringValues() {
            List<String> res = new ArrayList<String>();
            for (ReportType el : ReportType.values()) {
                res.add(el.toString());
            }
            return res.toArray(new String[0]);
        }

        @Override
        public String toString() {
            Context context = MainSlidingMenu.getAppContext();
            switch (ent) {
                case 0:
                    return context.getResources().getString(R.string.name_period_report_type_today);
                case 1:
                    return context.getResources().getString(R.string.name_period_report_type_this_week);
                case 2:
                    return context.getResources().getString(R.string.name_period_report_type_this_month);
                case 3:
                    return context.getResources().getString(R.string.name_period_report_type_this_year);
                case 4:
                    return context.getResources().getString(R.string.name_period_report_type_next_week);
                case 5:
                    return context.getResources().getString(R.string.name_period_report_type_next_month);
                case 6:
                    return context.getResources().getString(R.string.name_period_report_type_next_year);
                case 7:
                    return context.getResources().getString(R.string.name_period_report_type_prev_week);
                case 8:
                    return context.getResources().getString(R.string.name_period_report_type_prev_month);
                case 9:
                    return context.getResources().getString(R.string.name_period_report_type_prev_year);
                case 10:
                    return context.getResources().getString(R.string.name_period_report_type_all_time);
                case 11:
                    return context.getResources().getString(R.string.name_period_report_type_custom);
                default:
                    return "";
            }
        }

    }

    public static int getCalendarField(Period.Type typeInterval) {
        int res = Calendar.DAY_OF_MONTH;
        switch (typeInterval) {
            case DAY:
                res = Calendar.DAY_OF_MONTH;
                break;
            case WEEK:
                res = Calendar.WEEK_OF_MONTH;
                break;
            case MONTH:
                res = Calendar.MONTH;
                break;
            case QUART:
                res = Calendar.MONTH;
                break;
            case YEAR:
                res = Calendar.YEAR;
                break;
        }
        return res;
    }


    public static Period.ReportType encodeSelectDateRange(Context context, DatePeriod dtPeriod) {
        dtPeriod = Utils.zeroTime(dtPeriod);
        Period.ReportType res = Period.ReportType.CUSTOM;
        for (Period.ReportType el : Period.ReportType.values()) {
            if (dtPeriod.equals(calcSelectDateRange(context, el))) {
                return el;
            }
        }
        return res;
    }


    public static DatePeriod calcSelectDateRange(Context context, Period.ReportType typePeriodReport) {
        DatePeriod res = new DatePeriod();
        Calendar calFrom = Calendar.getInstance();
        Calendar calTo = Calendar.getInstance();
        switch (typePeriodReport) {
            case TODAY:
            /*NOP*/
                break;
            case THIS_WEEK:
                calFrom.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                calTo.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                break;
            case THIS_MONTH:
                calFrom.set(Calendar.DATE, 1);
                calTo.setTime(calFrom.getTime());
                calTo.set(Calendar.DATE, calFrom.getActualMaximum(Calendar.DAY_OF_MONTH));
                break;
            case THIS_YEAR:
                calFrom.set(Calendar.DAY_OF_YEAR, 1);
                calTo.setTime(calFrom.getTime());
                calTo.set(Calendar.DAY_OF_YEAR, calFrom.getActualMaximum(Calendar.DAY_OF_YEAR));
                break;
            case NEXT_WEEK:
                calFrom.add(Calendar.WEEK_OF_MONTH, 1);
                calFrom.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                calTo.setTime(calFrom.getTime());
                calTo.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                break;
            case NEXT_MONTH:
                calFrom.add(Calendar.MONTH, 1);
                calFrom.set(Calendar.DATE, 1);
                calTo.setTime(calFrom.getTime());
                calTo.set(Calendar.DATE, calFrom.getActualMaximum(Calendar.DAY_OF_MONTH));
                break;
            case NEXT_YEAR:
                calFrom.add(Calendar.YEAR, 1);
                calFrom.set(Calendar.DAY_OF_YEAR, 1);
                calTo.setTime(calFrom.getTime());
                calTo.set(Calendar.DAY_OF_YEAR, calFrom.getActualMaximum(Calendar.DAY_OF_YEAR));
                break;
            case PREV_WEEK:
                calFrom.add(Calendar.WEEK_OF_MONTH, -1);
                calFrom.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                calTo.setTime(calFrom.getTime());
                calTo.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                break;
            case PREV_MONTH:
                calFrom.add(Calendar.MONTH, -1);
                calFrom.set(Calendar.DATE, 1);
                calTo.setTime(calFrom.getTime());
                calTo.set(Calendar.DATE, calFrom.getActualMaximum(Calendar.DAY_OF_MONTH));
                break;
            case PREV_YEAR:
                calFrom.add(Calendar.YEAR, -1);
                calFrom.set(Calendar.DAY_OF_YEAR, 1);
                calTo.setTime(calFrom.getTime());
                calTo.set(Calendar.DAY_OF_YEAR, calFrom.getActualMaximum(Calendar.DAY_OF_YEAR));
                break;
            case ALL_TIME:
                DatePeriod per = DatePeriod.getAllPeriod(context);
                calFrom.setTime(per.dateFrom);
                calTo.setTime(per.dateTo);
                break;
            case CUSTOM:
            /*NOP*/
                break;
        }
        res.dateFrom = calFrom.getTime();
        res.dateTo = calTo.getTime();
        res = Utils.zeroTime(res);
        return res;
    }

    //...................................................

    private int id;
    private String name;
    private Type type;
    private int count;
    private boolean active = true;
    private Date dt_create;
    private Date dt_update;


    public Period() {
    }

    public Period(String name, Type type, int value) {
        this.name = name;
        this.type = type;
        this.count = value;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getDt_create() {
        return dt_create;
    }

    public void setDt_create(Date dt_create) {
        this.dt_create = dt_create;
    }

    public Date getDt_update() {
        return dt_update;
    }

    public void setDt_update(Date dt_update) {
        this.dt_update = dt_update;
    }


    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Period period = (Period) o;
        if (active != period.active) return false;
        if (count != period.count) return false;
        if (name != null ? !name.equals(period.name) : period.name != null) return false;
        if (type != period.type) return false;
        return true;
    }

    @Override
    public String toString() {
        return "Period{" +
                "\nid=" + id +
                "\n, name='" + name + '\'' +
                "\n, type=" + type +
                "\n, count=" + count +
                "\n, dt_create=" + dt_create +
                "\n, dt_update=" + dt_update +
                '}';
    }


}

