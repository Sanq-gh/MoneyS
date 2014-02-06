package com.sanq.params;

import android.os.Bundle;
import com.sanq.entity.Account;
import com.sanq.entity.Payord;
import com.sanq.entity.Period;
import com.sanq.utils.DatePeriod;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: sanq
 * Date: 07.11.13
 * Time: 22:23
 * To change this template use File | Settings | File Templates.
 */
public interface ChartFilterParams extends Serializable {

    public void  initParams(Account account, String currency, DatePeriod datePeriod, Payord.Type payType, Period.Type typePeriod, boolean byAccount);

    public Payord.Type getPayType() ;

    public void setPayType(Payord.Type payType);

    public DatePeriod getDatePeriod() ;

    public void  setDatePeriod(DatePeriod per) ;

    public Account getAccount()  ;

    public void setAccount(Account account) ;

    public String getCurrency();

    public void setCurrency(String currency);

    public Period.Type getTypePeriod() ;

    public void setTypePeriod(Period.Type typePeriod) ;

    public boolean isByAccount() ;

    public Bundle getBundle();

}
