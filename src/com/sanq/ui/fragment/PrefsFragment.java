package com.sanq.ui.fragment;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sanq.dao.AccountDAO;
import com.sanq.ds.DbUtils;
import com.sanq.entity.Account;
import com.sanq.entity.Period;
import com.sanq.entity.Transfer;
import com.sanq.exception.DBException;
import com.sanq.exception.DateCheckedException;
import com.sanq.exception.SavingException;
import com.sanq.loader.BackgroundLoader;
import com.sanq.loader.BackgroundTask;
import com.sanq.moneys.R;
import com.sanq.ui.adapter.AccountAdapter;
import com.sanq.ui.adapter.FileListAdapter;
import com.sanq.ui.dialog.ConfirmDialog;
import com.sanq.ui.dialog.ConfirmDialogListener;
import com.sanq.ui.dialog.FileListDialog;
import com.sanq.ui.dialog.TimeDialog;
import com.sanq.utils.Cnt;
import com.sanq.utils.Preferences;
import com.sanq.utils.RemindManager;
import com.sanq.utils.Utils;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 14.06.13
 * Time: 15:00
 */
public class PrefsFragment extends AbstractFragment implements View.OnClickListener {

    private final int LOADER_ID = 800;

    Preferences preferences;

    ViewGroup layProgressAcc;
    ViewGroup layBackupSchedule;

    AccountAdapter adapterAcc;
    ArrayAdapter<Period.ReportType> adapterTypePeriod;
    ArrayAdapter<Utils.SlidingMenuOrientation> adapterMenuOrient;
    FileListAdapter adapterBackupFiles;
    ArrayAdapter<Period.Type> adapterTypeInterval;

    ScrollView scrollView;

    Spinner spinAcc;
    Spinner spinMenuOrient;
    Spinner spinTypePeriod;
    Spinner spinBackupScheduleInterval;

    TextView txtTimeRemind;
    TextView txtCompressValue;
    TextView txtBackUpScheduleTime;
    TextView txtBackupScheduleCnt;

    SeekBar seekCompress;
    SeekBar seekBackupScheduleCnt;

    Button cmdBackup;
    Button cmdRestore;
    Button cmdBackupSchedule;
    Button cmdDbClear;
    ImageView imgDbClearInfo;

    private final int CNT_SPINNER = 3;
    private int cntSpinnerInit = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapterAcc = new AccountAdapter(context, AccountAdapter.TypeControl.SPINNER);
        preferences = new Preferences(context);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.prefs, null);
        cntSpinnerInit = 0;

        layProgressAcc = (ViewGroup) v.findViewById(R.id.layPrefsProgressAcc);
        layBackupSchedule = (ViewGroup) v.findViewById(R.id.layPrefsBackupSchedule);

        scrollView = (ScrollView) v.findViewById(R.id.scrollPrefs);

        spinAcc = (Spinner) v.findViewById(R.id.spinPrefsDefAcc);
        spinAcc.setAdapter(adapterAcc);
        spinAcc.setOnItemSelectedListener(spinOnItemSelectedListener);


        spinTypePeriod = (Spinner) v.findViewById(R.id.spinPrefsDefPeriod);
        adapterTypePeriod = new ArrayAdapter<Period.ReportType>(context, R.layout.template_spin, Period.ReportType.values());
        adapterTypePeriod.setDropDownViewResource(R.layout.my_simple_spinner_dropdown_item);
        spinTypePeriod.setAdapter(adapterTypePeriod);
        spinTypePeriod.setOnItemSelectedListener(spinOnItemSelectedListener);


        spinMenuOrient = (Spinner) v.findViewById(R.id.spinPrefsMenuOrient);
        adapterMenuOrient = new ArrayAdapter<Utils.SlidingMenuOrientation>(context, R.layout.template_spin, Utils.SlidingMenuOrientation.values());
        adapterMenuOrient.setDropDownViewResource(R.layout.my_simple_spinner_dropdown_item);
        spinMenuOrient.setAdapter(adapterMenuOrient);
        spinMenuOrient.setOnItemSelectedListener(spinOnItemSelectedListener);


        spinBackupScheduleInterval = (Spinner) v.findViewById(R.id.spinPrefsBackupScheduleInterval);
        adapterTypeInterval = new ArrayAdapter<Period.Type>(context, R.layout.template_spin, Period.Type.values());
        adapterTypeInterval.setDropDownViewResource(R.layout.my_simple_spinner_dropdown_item);
        spinBackupScheduleInterval.setAdapter(adapterTypeInterval);
        spinBackupScheduleInterval.setOnItemSelectedListener(spinOnItemSelectedListener);


        txtTimeRemind = (TextView) v.findViewById(R.id.txtPrefsDefTime);
        txtTimeRemind.setOnClickListener(this);
        txtBackUpScheduleTime = (TextView) v.findViewById(R.id.txtPrefsBackupScheduleTime);
        txtBackUpScheduleTime.setOnClickListener(this);

        txtCompressValue = (TextView) v.findViewById(R.id.txtPrefsCompressValue);
        txtBackupScheduleCnt = (TextView) v.findViewById(R.id.txtPrefsBackupScheduleCnt);

        seekCompress = (SeekBar) v.findViewById(R.id.seekPrefsCompress);
        seekCompress.setOnSeekBarChangeListener(seekChangeListener);

        seekBackupScheduleCnt = (SeekBar) v.findViewById(R.id.seekPrefsBackupScheduleCnt);
        seekBackupScheduleCnt.setOnSeekBarChangeListener(seekChangeListener);

        cmdBackup = (Button) v.findViewById(R.id.cmdPrefsBackup);
        cmdBackup.setOnClickListener(this);
        cmdRestore = (Button) v.findViewById(R.id.cmdPrefsRestore);
        cmdRestore.setOnClickListener(this);
        cmdBackupSchedule = (Button) v.findViewById(R.id.cmdPrefsBackupSchedule);
        cmdBackupSchedule.setOnClickListener(this);
        cmdDbClear = (Button) v.findViewById(R.id.cmdPrefsDbClear);
        cmdDbClear.setOnClickListener(this);
        imgDbClearInfo = (ImageView) v.findViewById(R.id.imgPrefsDbClearInfo);
        imgDbClearInfo.setOnClickListener(this);

        refreshLoader(LOADER_ID, null, loaderCallback);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshViewData();
    }

    @Override
    protected String getHeadCaption() {
        return context.getResources().getString(R.string.title_prefs_fragment);
    }

    @Override
    protected int getHeadIcon() {
        return R.drawable.settings;
    }


    @Override
    protected void refreshViewData() {
        spinAcc.setSelection(adapterAcc.getPosition(preferences.getDefaultAcc()));
        spinMenuOrient.setSelection(adapterMenuOrient.getPosition(preferences.getMenuOrient()));
        spinTypePeriod.setSelection(adapterTypePeriod.getPosition(preferences.getDefaultTypeReportPeriod()));
        txtTimeRemind.setText(Utils.floatToTime(preferences.getDefaultReminderTime()));
        seekCompress.setProgress((preferences.getCompressValue() - 50) * 2);
        txtCompressValue.setText(String.valueOf(seekCompress.getProgress()));
        switchCmdSchedule();
        spinBackupScheduleInterval.setSelection(adapterTypeInterval.getPosition(preferences.getBackupInterval()));
        txtBackUpScheduleTime.setText(Utils.floatToTime(preferences.getBackupTime()));
        seekBackupScheduleCnt.setProgress(preferences.getMaxBackupCount());
        txtBackupScheduleCnt.setText(String.valueOf(preferences.getMaxBackupCount()));
    }

    LoaderManager.LoaderCallbacks<List<Account>> loaderCallback = new LoaderManager.LoaderCallbacks<List<Account>>() {
        @Override
        public Loader<List<Account>> onCreateLoader(int id, Bundle args) {
            switchVisibility(true, layProgressAcc, spinAcc);
            return new BackgroundLoader(context, backgroundTaskLoad, args);
        }

        @Override
        public void onLoadFinished(Loader<List<Account>> listLoader, List<Account> accounts) {
            if (listLoader.getId() == LOADER_ID) {
                adapterAcc.reloadData(accounts);
                spinAcc.setSelection(adapterAcc.getPosition(preferences.getDefaultAcc()));
                switchVisibility(false, layProgressAcc, spinAcc);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Account>> listLoader) {
        }
    };

    BackgroundTask backgroundTaskLoad = new BackgroundTask<List<Account>>() {
        @Override
        public List<Account> load(Bundle args) {
            List<Account> accounts = new AccountDAO(context).getAll();
            for (Account el : accounts) {
                el.calcSaldo(context, new Date());
            }
            return accounts;
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtPrefsDefTime:
                try {
                    TimeDialog.showTimePicker(getActivity(), onTimeRemindListener, Utils.timeToDate(txtTimeRemind.getText().toString()));
                } catch (DateCheckedException e) {
                    Utils.showMessage(getActivity(), "", context.getString(R.string.mes_wrong_time_format), null);
                }
                break;
            case R.id.cmdPrefsBackup:
                try {
                    DbUtils.backupDb(context);
                    String mes = String.format(context.getResources().getString(R.string.mes_backup_create_successfully), DbUtils.getDefaultBackupName(context));
                    Utils.showMessage(getActivity(), "", mes, null);
                } catch (DBException e) {
                    Utils.showMessage(getActivity(), "", context.getResources().getString(R.string.mes_backup_create_fail) + " " + e.getMessage(), null);
                }
                break;
            case R.id.cmdPrefsRestore:
                try {
                    adapterBackupFiles = new FileListAdapter(context, Utils.getAppPath(context, Utils.TypePath.BackUp));
                    new FileListDialog(adapterBackupFiles, backupFileDialogListener).show(getActivity().getSupportFragmentManager(), "");
                } catch (SavingException e) {
                    Utils.showMessage(getActivity(), "", context.getString(R.string.mes_db_restore_fail) + " " + e.getMessage(), null);
                }
                break;
            case R.id.cmdPrefsBackupSchedule:
                preferences.setBackupSchedule(!preferences.isBackupSchedule());
                switchCmdSchedule();
                if (preferences.isBackupSchedule()) {
                    Utils.scrollToEnd(scrollView);
                }
                scheduleBackup();
                break;
            case R.id.txtPrefsBackupScheduleTime:
                try {
                    TimeDialog.showTimePicker(getActivity(), onTimeBackupScheduleListener, Utils.timeToDate(txtBackUpScheduleTime.getText().toString()));
                } catch (DateCheckedException e) {
                    Utils.showMessage(getActivity(), "", context.getString(R.string.mes_wrong_time_format), null);
                }
                break;
            case R.id.imgPrefsDbClearInfo:
                Utils.showMessage(getActivity(), "", context.getString(R.string.mes_db_clear_info), null);
                break;
            case R.id.cmdPrefsDbClear:
                new ConfirmDialog(context, confirmClearListener, context.getString(R.string.mes_confirm_clear)).show(getActivity().getSupportFragmentManager(), "");
                break;
        }
    }


    AdapterView.OnItemSelectedListener spinOnItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // эта хрень нужна т.к. при  инициализации каждого спиннера для него вызывается Spinner.OnItemSelectedListener
            if (cntSpinnerInit < CNT_SPINNER) {
                cntSpinnerInit++;
            } else {
                switch (parent.getId()) {
                    case R.id.spinPrefsDefAcc:
                        preferences.setDefaultAcc(adapterAcc.getAccount(position));
                        break;
                    case R.id.spinPrefsMenuOrient:
                        preferences.setMenuOrient(Utils.SlidingMenuOrientation.fromInt(position));
                        break;
                    case R.id.spinPrefsDefPeriod:
                        preferences.setDefaultTypeReportPeriod(Period.ReportType.fromInt(position));
                        break;
                    case R.id.spinPrefsBackupScheduleInterval:
                        preferences.setBackupInterval(Period.Type.fromInt(position));
                        scheduleBackup();
                        break;
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    TimePickerDialog.OnTimeSetListener onTimeRemindListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            preferences.setDefaultReminderTime(Utils.timeToFloat(hourOfDay, minute));
            txtTimeRemind.setText(Utils.floatToTime(preferences.getDefaultReminderTime()));
        }
    };
    TimePickerDialog.OnTimeSetListener onTimeBackupScheduleListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            preferences.setBackupTime(Utils.timeToFloat(hourOfDay, minute));
            txtBackUpScheduleTime.setText(Utils.floatToTime(preferences.getBackupTime()));
            scheduleBackup();
        }
    };

    SeekBar.OnSeekBarChangeListener seekChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            //compress must be 50-100
            switch (seekBar.getId()) {
                case R.id.seekPrefsCompress:
                    txtCompressValue.setText(String.valueOf(seekBar.getProgress()));
                    preferences.setCompressValue((seekBar.getProgress() / 2) + 50);
                    break;
                case R.id.seekPrefsBackupScheduleCnt:
                    txtBackupScheduleCnt.setText(String.valueOf(seekBar.getProgress()));
                    preferences.setMaxBackupCount(seekBar.getProgress());
                    break;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

    DialogInterface.OnClickListener backupFileDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int idFile) {
            confirmRestoreListener.onSetParam(idFile);
            String mes = context.getResources().getString(R.string.mes_confirm_restore);
            String fileName = adapterBackupFiles.getFile(idFile).getName() + " (" + Utils.dateToString(context, new Date(adapterBackupFiles.getFile(idFile).lastModified())) + ")";
            new ConfirmDialog(context, confirmRestoreListener, String.format(mes, fileName)).show(getActivity().getSupportFragmentManager(), "");
        }
    };

    private void switchCmdSchedule() {
        layBackupSchedule.setVisibility(preferences.isBackupSchedule() ? View.VISIBLE : View.GONE);
        Drawable draw = null;
        if (preferences.isBackupSchedule()) {
            draw = getResources().getDrawable(R.drawable.db_right);
        } else {
            draw = getResources().getDrawable(R.drawable.db_white);
        }
        cmdBackupSchedule.setCompoundDrawablesWithIntrinsicBounds(draw, null, null, null);
    }

    ConfirmDialogListener confirmClearListener = new ConfirmDialogListener() {
        int id;

        @Override
        public void onSetParam(int param) {
            this.id = param;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case android.content.DialogInterface.BUTTON_POSITIVE:
                    progressDialogInfin = ProgressDialog.show(getActivity(), "", "", true, false);
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                handlerDbClear.sendEmptyMessage(Cnt.HANDLER_MES_DB_BACKUP);
                                DbUtils.backupDb(context);
                                Thread.sleep(500);
                                handlerDbClear.sendEmptyMessage(Cnt.HANDLER_MES_DB_CLEAR);
                                DbUtils.clearDb(context);
                                Thread.sleep(500);
                                handlerDbClear.sendEmptyMessage(Cnt.HANDLER_MES_DB_VACUUM);
                                DbUtils.vacuumDb(context);
                                Thread.sleep(500);
                                handlerDbClear.sendEmptyMessage(Cnt.HANDLER_MES_IMAGES_CLEAR);
                                Utils.deleteImagesNA(context, Transfer.getActiveWithImage(context));
                                Thread.sleep(500);
                                handlerDbClear.sendEmptyMessage(Cnt.HANDLER_MES_FINISH);
                            } catch (InterruptedException e) {
                          /*NOP*/
                            } catch (Exception e) {
                                handlerDafault.sendMessage(createHandlerMessageString(Cnt.HANDLER_MES_ERROR, context.getResources().getString(R.string.mes_db_clear_fail) + " " + e.getMessage()));
                            }
                        }
                    }).start();
                    break;
                case android.content.DialogInterface.BUTTON_NEGATIVE:
                   /*NOP*/
                    break;
            }
        }
    };

    Handler handlerDbClear = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (progressDialogInfin != null) {
                switch (msg.what) {
                    case Cnt.HANDLER_MES_DB_BACKUP:
                        progressDialogInfin.setMessage(context.getResources().getString(R.string.mes_db_backup));
                        break;
                    case Cnt.HANDLER_MES_DB_CLEAR:
                        progressDialogInfin.setMessage(context.getResources().getString(R.string.mes_db_clearing));
                        break;
                    case Cnt.HANDLER_MES_DB_VACUUM:
                        progressDialogInfin.setMessage(context.getResources().getString(R.string.mes_db_vacuum));
                        break;
                    case Cnt.HANDLER_MES_IMAGES_CLEAR:
                        progressDialogInfin.setMessage(context.getResources().getString(R.string.mes_delete_images));
                        break;
                    case Cnt.HANDLER_MES_FINISH:
                        if (progressDialogInfin.isShowing()) {
                            progressDialogInfin.dismiss();
                        }
                        Utils.showMessage(getActivity(), "", context.getResources().getString(R.string.mes_db_clear_successfully), null);
                        break;
                    case Cnt.HANDLER_MES_ERROR:
                        if (progressDialogInfin.isShowing()) {
                            progressDialogInfin.dismiss();
                        }
                        Utils.showMessage(getActivity(), "", context.getString(R.string.mes_error_occurs) + " " + msg.getData().getString(Cnt.BUNDLE_KEY_HANDLER_MES), null);
                        break;
                }
            }
        }
    };

    ConfirmDialogListener confirmRestoreListener = new ConfirmDialogListener() {
        int idFile;

        @Override
        public void onSetParam(int param) {
            this.idFile = param;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case android.content.DialogInterface.BUTTON_POSITIVE:
                    RemindManager.cancelAlarmTransfAll(context);
                    try {
                        DbUtils.restoreDb(context, adapterBackupFiles.getFile(idFile));
                        Utils.showMessage(getActivity(), "", context.getString(R.string.mes_db_restore_successfully), null);
                    } catch (DBException e) {
                        Utils.showMessage(getActivity(), "", context.getString(R.string.mes_db_restore_fail) + " " + e.getMessage(), null);
                    }
                    break;
                case android.content.DialogInterface.BUTTON_NEGATIVE:
                   /*NOP*/
                    break;
            }
        }
    };

    private void scheduleBackup() {
        RemindManager.scheduleNextBackup(context, preferences);
    }

}
