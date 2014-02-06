package com.sanq.params;

import android.os.Bundle;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: sanq
 * Date: 07.11.13
 * Time: 22:23
 * To change this template use File | Settings | File Templates.
 */
public interface AccountFilterParams {

    public void  initParams(Date date);

    public Date getDate()  ;

    public void setDate(Date date) ;

    public Bundle getBundle();


}
