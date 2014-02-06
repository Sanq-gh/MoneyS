package com.sanq.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import com.sanq.entity.Period;
import com.sanq.moneys.R;

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


public class DateRangeDialog extends DialogFragment {
    Context context;
    DialogInterface.OnClickListener listener;


    public DateRangeDialog(Context context, DialogInterface.OnClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(context.getResources().getString(R.string.title_dialog_template_period));
        builder.setItems(Period.ReportType.getStringValues(), listener);
        return builder.create();
    }


}
