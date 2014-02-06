package com.sanq.params;

import android.os.Bundle;
import com.sanq.entity.Account;
import com.sanq.entity.Category;
import com.sanq.entity.Payord;
import com.sanq.entity.Period;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: sanq
 * Date: 07.11.13
 * Time: 22:23
 * To change this template use File | Settings | File Templates.
 */
public interface PayordFilterParams extends Serializable {

    public void  initParams(Account account, Payord filterPay, Category filterCat, Period period, boolean saveAsTemplate);

    public Account getAccount()  ;
    public void setAccount(Account account) ;

    public Payord getPayord()  ;
    public void setPayord(Payord payord) ;

    public Category getCategory()  ;
    public void setCategory(Category category) ;

    public  Period getPeriod() ;
    public void  setPeriod( Period per) ;

    public boolean getSaveAsTemplate() ;
    public void setSaveAsTemplate(boolean saveAsTemplate) ;


    public Bundle getBundle();

    public Bundle getBundleSelf();

    public String toString();
}
