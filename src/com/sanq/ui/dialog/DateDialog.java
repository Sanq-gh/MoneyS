package com.sanq.ui.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 18.06.13
 * Time: 18:14
 */
public class DateDialog extends DialogFragment {
    Context context;

    DatePickerDialog.OnDateSetListener listener;
    private int year, month, day;

    public DateDialog(Context context,DatePickerDialog.OnDateSetListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        year = args.getInt("year");
        month = args.getInt("month");
        day = args.getInt("day");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
         return new DatePickerDialog(getActivity(), listener, year, month, day);
    }


    public static  void showDatePicker(FragmentActivity activity, DatePickerDialog.OnDateSetListener listener, Date dt) {
        DateDialog dateDialog = new DateDialog(activity.getApplicationContext(), listener);
        Calendar calender = Calendar.getInstance();
        if (dt != null) {
            calender.setTime(dt);
        }
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        dateDialog.setArguments(args);
        dateDialog.show(activity.getSupportFragmentManager(), "date_picker");
    }

}
