package com.sanq.ui.fragment;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import com.sanq.entity.Transfer;
import com.sanq.moneys.R;
import com.sanq.ui.dialog.TimeDialog;
import com.sanq.utils.RemindManager;
import com.sanq.utils.SLog;
import com.sanq.utils.Utils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 19.06.13
 * Time: 16:41
 */
public class TestFragment extends AbstractFragment implements View.OnClickListener {
    TimePicker myTimePicker;
    Button cmdCreateAlarm;
    Button cmdCancelAlarm;
    Button cmdThrowException;
    Button  cmdOther;

    Button cmdCheckAlarms;
    TextView txtCheckAlarms;


    View lay;

    int testReqCode = 0;


    @Override
    protected String getHeadCaption() {
        return context.getResources().getString(R.string.title_test_activity);
    }

    @Override
    protected int getHeadIcon() {
        return R.drawable.octocat_s;
    }

    @Override
    protected void refreshViewData() {
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.test, null);

        lay = (View) v.findViewById(R.id.layTest);

        cmdCreateAlarm = (Button) v.findViewById(R.id.cmdTestCreateAlarmAll);
        cmdCreateAlarm.setOnClickListener(this);
        cmdCancelAlarm = (Button) v.findViewById(R.id.cmdTestCancelAlarmAll);
        cmdCancelAlarm.setOnClickListener(this);

        cmdThrowException = (Button) v.findViewById(R.id.cmdTestThrowException);
        cmdThrowException.setOnClickListener(this);

        cmdCheckAlarms = (Button) v.findViewById(R.id.cmdTestTestCheckAlarms);
        cmdCheckAlarms.setOnClickListener(this);

        cmdOther = (Button) v.findViewById(R.id.cmdTestOther);
        cmdOther.setOnClickListener(this);

        txtCheckAlarms = (TextView) v.findViewById(R.id.txtTestCheckAlarms);



        return v;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.cmdTestOther:

                SLog.d(context.getApplicationInfo().dataDir);
                SLog.d(Environment.getExternalStorageDirectory().toString());

                break;
            case R.id.cmdTestThrowException:
                try {
                    try {
                        throw new Exception("a");
                    } finally {
                        if (true) {
                            throw new IOException("b");
                        }
                       SLog.d("c");
                    }
                } catch (IOException ex) {
                    SLog.d(ex.getMessage());
                } catch (Exception ex) {
                    SLog.d("d");
                    SLog.d(ex.getMessage());
                }





                break;
            case R.id.cmdTestCreateAlarmAll:
                RemindManager.setAlarmTransfAll(context, new Date());
                break;
            case R.id.cmdTestCancelAlarmAll:
                RemindManager.cancelAlarmTransfAll(context);
                break;
            case R.id.cmdTestTestCheckAlarms:
                Map<Integer, Date> alarms = Transfer.getAllScheduled(context, new Date());
                txtCheckAlarms.setText("");
                for (Map.Entry<Integer, Date> el : alarms.entrySet()) {
                    String mes = "";
                    if (RemindManager.isAlarmTransfSet(context, el.getKey())) {
                        mes = "present:   " + el.getKey() + "  " + Utils.dateToString(el.getValue(), "dd.MM.yyyy hh:mm:ss");
                    } else {
                        mes = "not found: " + el.getKey() + "  " + Utils.dateToString(el.getValue(), "dd.MM.yyyy hh:mm:ss");
                    }
                    SLog.d(mes);
                 txtCheckAlarms.setText(txtCheckAlarms.getText() + "\n" +  mes);
                }
                break;
        }
    }

    private void openTimePickerDialog() {
        TimeDialog.showTimePicker(getActivity(), onTimeSetListener, new Date());

    }

    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar calSet = Calendar.getInstance();
            calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calSet.set(Calendar.MINUTE, minute);
            calSet.set(Calendar.SECOND, 0);
            calSet.set(Calendar.MILLISECOND, 0);

            calSet.set(Calendar.SECOND, calSet.get(Calendar.SECOND) + 10);
            RemindManager.setAlarmTransf(context, 5, calSet.getTime());
            //   calSet.set(Calendar.MINUTE, calSet.get(Calendar.MINUTE) + 1);
            RemindManager.setAlarmTransf(context, 6, calSet.getTime());
            //  calSet.set(Calendar.MINUTE, calSet.get(Calendar.MINUTE) + 1);
            RemindManager.setAlarmTransf(context, 7, calSet.getTime());
            // calSet.set(Calendar.MINUTE, calSet.get(Calendar.MINUTE) + 1);
            RemindManager.setAlarmTransf(context, 8, calSet.getTime());


        }
    };


   /*
    private void setAlarmTransf(Calendar targetCal) {



        Intent intent = new Intent(getActivity().getBaseContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getBaseContext(), RQS_1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);

    }

    */

}