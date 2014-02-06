package com.sanq.charts;

import android.content.Context;
import com.sanq.core.Report;
import com.sanq.entity.Account;
import com.sanq.entity.Category;
import com.sanq.entity.Payord;
import com.sanq.entity.Period;
import com.sanq.utils.DatePeriod;
import com.sanq.utils.Utils;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: sanq
 * Date: 15.10.13
 * Time: 21:35
 * To change this template use File | Settings | File Templates.
 */
public class DataChart {
    Context context;

    public DataChart(Context context) {
        this.context = context;
    }


    public Map<Category, Double> getAmountByGroupCategory(String currency, DatePeriod period, Payord.Type payType) {
        Map<Category, Double> res = new TreeMap<Category, Double>();
        List<Account> accounts = Account.getAllByCurrency(context, currency);
        for (Account acc : accounts) {
            Map<Category, Double> amounts = getAmountByGroupCategory(acc, period, payType);
            for (Map.Entry<Category, Double> el : amounts.entrySet()) {
                res.put(el.getKey(), res.containsKey(el.getKey()) ? res.get(el.getKey()) + el.getValue() : el.getValue());
            }
        }
        return res;
    }


    public Map<Category, Double> getAmountByGroupCategory(Account acc, DatePeriod period, Payord.Type payType) {
        Map<Category, Double> res = new TreeMap<Category, Double>();
        Report rep = new Report(context);
        List<Category> cats = Category.getAllLinked(context);
        for (Category cat : cats) {
            if (cat.isGroup()) {
                int amount = rep.getAmountByCategory(acc.getId(), cat.getId(), period, payType);
                List<Category> childs = cat.getChilds();
                for (Category child : childs) {
                    amount += rep.getAmountByCategory(acc.getId(), child.getId(), period, payType);
                }
                if (amount != 0) {
                    res.put(cat, Utils.moneyIntToDouble(amount));
                }
            }

        }
        return res;
    }


    public Map<Date, Double> getBalance(String currency, DatePeriod dtPeriod, Period.Type typeInterval) {
        Map<Date, Double> res = new TreeMap<Date, Double>();
        List<Account> accounts = Account.getAllByCurrency(context, currency);
        for (Account acc : accounts) {
            Map<Date, Double> balance = getBalance(acc, dtPeriod, typeInterval);
            for (Map.Entry<Date, Double> bal : balance.entrySet()) {
                res.put(bal.getKey(), res.containsKey(bal.getKey()) ? res.get(bal.getKey()) + bal.getValue() : bal.getValue());
            }
        }
        return res;
    }


    public Map<Date, Double> getBalance(Account acc, DatePeriod dtPeriod, Period.Type typeInterval) {
        Map<Date, Double> res = new TreeMap<Date, Double>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dtPeriod.dateFrom);
        Date curDt = dtPeriod.dateFrom;
        int amount;
        Report rep = new Report(context);
        while (cal.getTime().getTime() <= dtPeriod.dateTo.getTime()) {
            curDt = cal.getTime();
            amount = rep.getSaldoOnDate(acc.getId(), curDt);
            res.put(cal.getTime(), Utils.moneyIntToDouble(amount));
            cal.add(Period.getCalendarField(typeInterval), typeInterval == Period.Type.QUART ? 3 : 1); //потому что в календаре кварталов нет
            if ((cal.getTime().getTime() > dtPeriod.dateTo.getTime()) & (curDt.getTime() < dtPeriod.dateTo.getTime())) {
                res.put(cal.getTime(), Utils.moneyIntToDouble(rep.getSaldoOnDate(acc.getId(), dtPeriod.dateTo)));
            }
        }
        return res;
    }


    public Map<DatePeriod, Double> getSummary(String currency, DatePeriod dtPeriod, Period.Type typeInterval, Payord.Type payType) {
        Map<DatePeriod, Double> res = new TreeMap<DatePeriod, Double>();
        List<Account> accounts = Account.getAllByCurrency(context, currency);
        for (Account acc : accounts) {
            Map<DatePeriod, Double> summary = getSummary(acc, dtPeriod, typeInterval, payType);
            for (Map.Entry<DatePeriod, Double> bal : summary.entrySet()) {
                res.put(bal.getKey(), res.containsKey(bal.getKey()) ? res.get(bal.getKey()) + bal.getValue() : bal.getValue());
            }
        }
        return res;
    }


    public Map<DatePeriod, Double> getSummary(Account acc, DatePeriod dtPeriod, Period.Type typeInterval, Payord.Type payType) {
        Map<DatePeriod, Double> res = new TreeMap<DatePeriod, Double>();
        int amount;
        Report rep = new Report(context);
        Calendar cal = Calendar.getInstance();
        cal.setTime(dtPeriod.dateFrom);
        while (cal.getTime().getTime() <= dtPeriod.dateTo.getTime()) {
            DatePeriod curPeriod = new DatePeriod();
            curPeriod.dateFrom = cal.getTime();
            cal.add(Period.getCalendarField(typeInterval), typeInterval == Period.Type.QUART ? 3 : 1); //потому что в календаре кварталов нет
            if ((cal.getTime().getTime() > dtPeriod.dateTo.getTime()) & (curPeriod.dateTo.getTime() <= dtPeriod.dateTo.getTime())) {
                curPeriod.dateTo = dtPeriod.dateTo;
            } else {
                curPeriod.dateTo = cal.getTime();
            }
            if (Period.getCalendarField(typeInterval) == Calendar.DAY_OF_MONTH) {
                curPeriod.dateTo = curPeriod.dateFrom;
            }
            amount = rep.getAmountOnPeriod(acc.getId(), payType, curPeriod);
            res.put(curPeriod, Utils.moneyIntToDouble(amount));
        }
        return res;
    }


}
