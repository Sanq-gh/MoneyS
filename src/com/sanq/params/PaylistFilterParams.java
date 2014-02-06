package com.sanq.params;

import android.os.Bundle;
import com.sanq.entity.Account;
import com.sanq.entity.Payord;
import com.sanq.utils.DatePeriod;

/**
 * Created with IntelliJ IDEA.
 * User: sanq
 * Date: 07.11.13
 * Time: 22:23
 * To change this template use File | Settings | File Templates.
 */
public interface PaylistFilterParams {
   // Account filterAcc = new Account();
    //    DatePeriod filterDatePeriod = new DatePeriod();
//    Payord.Type filterPayType = Payord.Type.UNDEFINED;

    public void  initParams(Account account, DatePeriod datePeriod, Payord.Type payType);

    public Account getAccount()  ;
    public void setAccount(Account account) ;

    public DatePeriod getDatePeriod() ;
    public void  setDatePeriod(DatePeriod per) ;

    public Payord.Type getPayType() ;
    public void setPayType(Payord.Type payType);

    public Bundle getBundle();

}
