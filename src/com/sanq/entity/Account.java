package com.sanq.entity;

import android.content.Context;
import com.sanq.core.Report;
import com.sanq.dao.AccountDAO;
import com.sanq.utils.Utils;

import java.io.Serializable;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 29.05.13
 * Time: 11:04
 */
public class Account extends AbstractEntity implements Entity, Serializable {

    ///static .................................
    public static String getBundleKey() {
        return "ACCOUNT";
    }

    public static List<String> getAllAccNames(Context context) {
        List<String> res = new ArrayList<String>();
        List<Account> acc = new AccountDAO(context).getAll();
        for (Account el : acc) {
            res.add(el.getNameAndCurrency());
        }
        return res;
    }

    public static List<String> getUsedCurrencies(Context context) {
        List<String> res = new ArrayList<String>();
        Set<String> tmpSet = new TreeSet<String>();
        List<Account> accounts = new AccountDAO(context).getAll();
        for (Account el : accounts) {
            tmpSet.add(el.getCurrency());
        }
        res.addAll(tmpSet);
        return res;
    }


    public static List<Account> getAllByCurrency(Context context, String currency) {
        List<Account> res = new ArrayList<Account>();
        List<Account> acc = new AccountDAO(context).getAll();
        for (Account el : acc) {
            if (el.getCurrency().equals(currency)) {
                res.add(el);
            }
        }
        return res;
    }

    ///static .................................


    private int id;
    private String name;
    private int saldo;
    private String currency;
    private boolean active = true;
    private Date dt_create;
    private Date dt_update;
    private int lastCalcSaldo;

    public Account() {
    }

    public Account(String name, int saldo, String currency) {
        this.name = name;
        this.saldo = saldo;
        this.currency = currency;
    }

    public int getCurrentSaldo(Context context) {
        return calcSaldo(context, Utils.zeroTime(new Date()));
    }

    public int calcSaldo(Context context, Date dt) {
       int res = 0;
        if (getId() > 0) {
            res = new Report(context).getSaldoOnDate(this.getId(), dt);
        }
        lastCalcSaldo = res;
        return res;
    }



    public int getLastCalcSaldo() {
        return lastCalcSaldo;
    }



    public String getNameAndCurrency() {
        return name + " (" + getCurrency() + ")";
    }


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public String getName() {
        return name;
    }

    public int getSaldo() {
        return saldo;
    }

    public Date getDt_create() {
        return dt_create;
    }

    public Date getDt_update() {
        return dt_update;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        if (active != account.active) return false;
        if (saldo != account.saldo) return false;
        if (name != null ? !name.equals(account.name) : account.name != null) return false;
        if (currency != null ? !currency.equals(account.currency) : account.currency != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "\nAccount{" +
                "\nid=" + id +
                "\n, name='" + name + '\'' +
                "\n, currency='" + currency + '\'' +
                "\n, saldo=" + saldo +
                "\n, dt_create=" + dt_create +
                "\n, dt_update=" + dt_update +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDt_create(Date dt_create) {
        this.dt_create = dt_create;
    }

    public void setDt_update(Date dt_update) {
        this.dt_update = dt_update;
    }


}
