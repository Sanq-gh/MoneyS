package com.sanq.ui.dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import com.sanq.utils.Cnt;

import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 18.06.13
 * Time: 18:14
 */
public class TimeDialog extends DialogFragment {
    Context context;

    TimePickerDialog.OnTimeSetListener listener;
    private int hour, min;

    public TimeDialog(Context context, TimePickerDialog.OnTimeSetListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        hour = args.getInt("hour");
        min = args.getInt("min");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
         return new TimePickerDialog( getActivity(), listener, hour, min, Cnt.IS_24_TIME_FORMAT );
    }



    public static  void showTimePicker(FragmentActivity activity,  TimePickerDialog.OnTimeSetListener listener, Date time) {
        TimeDialog dateDialog = new TimeDialog(activity.getApplicationContext(), listener);
        Calendar calender = Calendar.getInstance();
        if (time != null) {
            calender.setTime(time);
        }
        Bundle args = new Bundle();
        args.putInt("hour", calender.get(Calendar.HOUR_OF_DAY));
        args.putInt("min", calender.get(Calendar.MINUTE));
        dateDialog.setArguments(args);
        dateDialog.show(activity.getSupportFragmentManager(), "time_picker");
    }

}
