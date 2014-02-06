package com.sanq.params;

import android.os.Bundle;
import com.sanq.entity.*;
import com.sanq.utils.DatePeriod;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 21.10.13
 * Time: 17:39
 */
public class FilterParams implements ChartFilterParams, TransferFilterParams, PaylistFilterParams, AccountFilterParams, PayordFilterParams {

    //static ========================================
    public static String getBundleKey() {
        return "FILTER_PARAMS";
    }

    //==============================================

    private Account account;
    private String currency;
    private DatePeriod datePeriod;
    private Period.Type typePeriod;
    private Payord.Type payType;
    private boolean byAccount;
    private Payord payord;
    private Transfer transf;
    private Period period;
    private Category category;
    private Date date;
    private boolean saveAsTemplate;

    @Override
    public Period getPeriod() {
        return period;
    }

    @Override
    public void setPeriod(Period period) {
        this.period = period;
    }

    @Override
    public boolean getSaveAsTemplate() {
        return saveAsTemplate;
    }

    @Override
    public void setSaveAsTemplate(boolean saveAsTemplate) {
        this.saveAsTemplate = saveAsTemplate;
    }

    public Bundle  getBundleSelf(){
        Bundle res = new Bundle();
        res.putSerializable(getBundleKey(),this);
      return res;
    }

    @Override
    public Bundle getBundle() {
        Bundle res = new Bundle();
        if (this.account != null) {
            res.putSerializable(Account.getBundleKey(), this.account);
        }
        if (this.payord != null) {
            res.putSerializable(Payord.getBundleKey(), this.payord);
        }
        if (this.transf != null) {
            res.putSerializable(Transfer.getBundleKey(), this.transf);
        }
        if (this.category != null) {
            res.putSerializable(Category.getBundleKey(), this.category);
        }
        if (this.period!= null) {
            res.putSerializable(Period.getBundleKey(), this.period);
        }
        if (this.payType != null) {
            res.putSerializable(Payord.Type.getBundleKey(), this.payType);
        }
        if (this.datePeriod != null) {
            res.putSerializable(DatePeriod.getBundleKey(), this.datePeriod);
        }
        if (this.typePeriod != null) {
            res.putSerializable(Period.Type.getBundleKey(), this.typePeriod);
        }
        if (this.date != null) {
            res.putSerializable(Date.class.toString(), this.date);
        }

        res.putSerializable(Boolean.class.toString(), this.saveAsTemplate);

        return res;
    }

    @Override
    public void initParams(Account account, String currency, DatePeriod datePeriod, Payord.Type payType, Period.Type typePeriod, boolean byAccount) {
        this.account = account;
        this.currency = currency;
        this.datePeriod = datePeriod;
        this.payType = payType;
        this.typePeriod = typePeriod;
        this.byAccount = byAccount;
    }

    @Override
    public void initParams(Account account, DatePeriod datePeriod, Payord.Type payType) {
        this.account = account;
        this.datePeriod = datePeriod;
        this.payType = payType;
    }

    @Override
    public void initParams(Account account, Payord payord, Category category, Period period, boolean saveAsTemplate) {
        this.account = account;
        this.payord = payord;
        this.category = category;
        this.period = period;
        this.saveAsTemplate = saveAsTemplate;
    }


    @Override
    public void initParams(Date date) {
        this.date = date;
    }

    public Payord.Type getPayType() {
        return payType;
    }

    public void setPayType(Payord.Type payType) {
        this.payType = payType;
    }

    public DatePeriod getDatePeriod() {
        return datePeriod;
    }

    @Override
    public void setDatePeriod(DatePeriod per) {
        this.datePeriod = per;
    }


    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        byAccount = true;
        this.account = account;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        byAccount = false;
        this.currency = currency;
    }

    public Period.Type getTypePeriod() {
        return typePeriod;
    }

    public void setTypePeriod(Period.Type typePeriod) {
        this.typePeriod = typePeriod;
    }

    public boolean isByAccount() {
        return byAccount;
    }


    @Override
    public void initParams(Payord pay, Transfer transf) {
        this.payord = pay;
        this.transf = transf;
    }

    @Override
    public Payord getPayord() {
        return payord;
    }

    @Override
    public void setPayord(Payord pay) {
        this.payord = pay;
    }

    @Override
    public Transfer getTransfer() {
        return transf;
    }

    @Override
    public void setTransfer(Transfer transf) {
        this.transf= transf;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    @Override
    public String toString() {

        return  account!=null?"\naccount: " + account.toString() : ""  +
                currency!=null?"\ncurrency: " + currency : "" +
                payType!=null?"\npayType: " + payType : ""  +
                datePeriod!=null?"\ndatePeriod: " + datePeriod.toString() : "" +
                typePeriod!=null?"\ntypePeriod: " + typePeriod.toString() : "" +
                payord!=null?"\npayord: " + payord.toString() : ""+
                transf!=null?"\ntransf: " + transf.toString() : ""+
                period!=null?"\nperiod: " + period.toString() : ""+
                category!=null?"\ncategory: " + category.toString() : ""+
                date!=null?"\ndate: " + date.toString() : ""+
                "\nbyAccount: " +  ((Boolean) byAccount).toString() +
                "\nsaveAsTemplate: " + ((Boolean) saveAsTemplate).toString()  +
                "\nbyAccount: " + ((Boolean) byAccount).toString();


    }

}
