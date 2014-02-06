package com.sanq.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 17.06.13
 * Time: 15:42
 */

//Toast.makeText(context, "cancel clicked ", Toast.LENGTH_LONG).show();

//Bundle args = new Bundle();
//args.putInt(DELETE_ID_KEY, v.getId());
//...
//private Dialog createDeleteDialog(Bundle args) {
//final int toDeleteId = args.getInt(DELETE_ID_KEY) ;



public class ConfirmDialog extends DialogFragment {
   Context context;
    ConfirmDialogListener listener;
    String message;


    public ConfirmDialog(Context context, ConfirmDialogListener listener, int mes ) {
        this(context,listener , context.getResources().getString(mes));
    }

    public ConfirmDialog(Context context, ConfirmDialogListener listener,String mes) {
       this.context = context;
       this.listener = listener;
       this.message = mes;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, listener) ;
        builder.setNegativeButton(android.R.string.cancel, listener);
        return builder.create();
    }
}
