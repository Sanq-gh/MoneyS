package com.sanq.core;

import android.content.Context;
import com.sanq.dao.DAO;

/**
 * Created with IntelliJ IDEA.
 * User: sanq
 * Date: 17.06.13
 * Time: 22:37
 * To change this template use File | Settings | File Templates.
 */
public abstract class InvoiceAbstract extends DAO {
    protected InvoiceAbstract(Context context) {
        super(context);
     }
}
