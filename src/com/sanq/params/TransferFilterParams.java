package com.sanq.params;

import android.os.Bundle;
import com.sanq.entity.Payord;
import com.sanq.entity.Transfer;

/**
 * Created with IntelliJ IDEA.
 * User: sanq
 * Date: 07.11.13
 * Time: 22:23
 * To change this template use File | Settings | File Templates.
 */
public interface TransferFilterParams {

    public void  initParams(Payord pay, Transfer transf);

    public Payord getPayord()  ;
    public void setPayord(Payord pay) ;

    public Transfer getTransfer()  ;
    public void setTransfer(Transfer transf) ;

    public Bundle getBundle();


}
