package com.sanq.ui.dialog;

import android.content.DialogInterface;

/**
 * Created with IntelliJ IDEA.
 * User: sanq
 * Date: 05.11.13
 * Time: 22:55
 * To change this template use File | Settings | File Templates.
 */
public interface ConfirmDialogListener extends DialogInterface.OnClickListener {
    public void onSetParam(int param);
}
