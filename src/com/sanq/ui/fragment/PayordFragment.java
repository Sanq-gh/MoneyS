package com.sanq.ui.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sanq.core.InvoicePayord;
import com.sanq.entity.*;
import com.sanq.exception.DateCheckedException;
import com.sanq.exception.SavingException;
import com.sanq.loader.BackgroundLoader;
import com.sanq.loader.BackgroundTask;
import com.sanq.moneys.MainSlidingMenu;
import com.sanq.moneys.R;
import com.sanq.params.FilterParams;
import com.sanq.params.PayordFilterParams;
import com.sanq.ui.dialog.CalcDialog;
import com.sanq.ui.dialog.DateDialog;
import com.sanq.ui.dialog.TimeDialog;
import com.sanq.utils.Cnt;
import com.sanq.utils.Preferences;
import com.sanq.utils.Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 14.06.13
 * Time: 14:05
 */
public class PayordFragment extends AbstractFragment implements View.OnClickListener {
    private final int LOADER_ID = 700;
    private final int RESULT_PARAM_ID_SUCCESS = 0;
    private final int RESULT_PARAM_ID_MESSAGE = 1;


    private ProgressDialog progressDialogInfin;

    private PayordFilterParams filterParams;

    ArrayAdapter<Period.Type> adapterTypePeriod;

    ScrollView scrollPay;
    Button cmdTemplate;
    Button cmdAccount;
    EditText txtAmount;
    TextView txtDate;
    EditText txtPayName;
    Button cmdCat;
    Button cmdCalc;

    ImageButton imgDebit;
    ImageButton imgCredit;


    LinearLayout layRepeat;
    LinearLayout layRemind;

    Spinner spinTypePeriod;
    EditText txtRepeatCount;
    TextView txtTimeRemind;

    Button cmdRepeat;
    Button cmdRemind;
    Button cmdSaveAsTemplate;

    ImageButton imgRepCountPlus;
    ImageButton imgRepCountMinus;

    Button cmdSave;
    Button cmdCancel;


    @Override
    protected String getHeadCaption() {
        return context.getResources().getString(R.string.title_pay_faragment);
    }

    @Override
    protected int getHeadIcon() {
        return R.drawable.transact;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filterParams = new FilterParams();
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.payord, null);

        scrollPay = (ScrollView) v.findViewById(R.id.scrollPay);

        cmdTemplate = (Button) v.findViewById(R.id.cmdPayTemplate);
        cmdTemplate.setOnClickListener(dictionaryOnClickListener);

        cmdAccount = (Button) v.findViewById(R.id.cmdPayAcc);
        cmdAccount.setOnClickListener(dictionaryOnClickListener);


        txtDate = (TextView) v.findViewById(R.id.txtPayDt);
        Calendar cal = Calendar.getInstance();
        txtDate.setText(Utils.dateToString(context, cal.getTime()));
        txtDate.setOnClickListener(this);

        txtAmount = (EditText) v.findViewById(R.id.txtPayAmount);
        txtAmount.setOnClickListener(this);

        txtPayName = (EditText) v.findViewById(R.id.txtPayName);

        txtPayName.addTextChangedListener(new MultiTextWatcher(txtPayName));


        imgDebit = (ImageButton) v.findViewById(R.id.imgPayTypeDebit);
        imgDebit.setOnClickListener(typePayOnClickListener);
        imgCredit = (ImageButton) v.findViewById(R.id.imgPayTypeCredit);
        imgCredit.setOnClickListener(typePayOnClickListener);

        cmdCat = (Button) v.findViewById(R.id.cmdPayCat);
        cmdCat.setOnClickListener(dictionaryOnClickListener);

        cmdCalc = (Button) v.findViewById(R.id.cmdPayCalc);
        cmdCalc.setOnClickListener(this);


        cmdRepeat = (Button) v.findViewById(R.id.cmdPayRepeat);
        cmdRepeat.setOnClickListener(this);

        cmdRemind = (Button) v.findViewById(R.id.cmdPayRemind);
        cmdRemind.setOnClickListener(this);

        cmdSaveAsTemplate = (Button) v.findViewById(R.id.cmdPaySaveAsTempl);
        cmdSaveAsTemplate.setOnClickListener(this);

        layRepeat = (LinearLayout) v.findViewById(R.id.layPayRepeat);
        layRemind = (LinearLayout) v.findViewById(R.id.layPayRemind);

        spinTypePeriod = (Spinner) v.findViewById(R.id.spinPayTypePeriod);
        adapterTypePeriod = new ArrayAdapter<Period.Type>(context, R.layout.template_spin_center, Period.Type.values());
        adapterTypePeriod.setDropDownViewResource(R.layout.my_simple_spinner_dropdown_item);
        spinTypePeriod.setAdapter(adapterTypePeriod);
        spinTypePeriod.setOnItemSelectedListener(onItemTypePeriodListener);

        txtRepeatCount = (EditText) v.findViewById(R.id.txtPayRepeatCount);
        txtRepeatCount.addTextChangedListener(new MultiTextWatcher(txtRepeatCount));

        txtTimeRemind = (TextView) v.findViewById(R.id.txtPayRemindTime);
        txtTimeRemind.setOnClickListener(this);

        imgRepCountPlus = (ImageButton) v.findViewById(R.id.imgPayRepCountPlus);
        imgRepCountPlus.setOnClickListener(repCountOnClickListener);
        imgRepCountMinus = (ImageButton) v.findViewById(R.id.imgPayRepCountMinus);
        imgRepCountMinus.setOnClickListener(repCountOnClickListener);

        cmdSave = (Button) v.findViewById(R.id.cmdPaySave);
        cmdSave.setOnClickListener(this);
        cmdCancel = (Button) v.findViewById(R.id.cmdPayCancel);
        cmdCancel.setOnClickListener(this);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Preferences preferences = new Preferences(context);
        Payord payTempl = null;
        Payord payOut = null;
        Payord payEmpty = new Payord(0, 0, 0, 0, "", new Date(), Payord.Type.CREDIT, 0, false, false, null, preferences.getDefaultReminderTime());

        Template templ = parseInputData(R.string.title_template_fragment, Template.class);
        if (templ != null) {
            payTempl = templ.getPayord(context);
            payTempl.setDt(new Date());
        }
        if (typeStart == TYPE_START_EDIT) {
            payOut = parseInputData(R.string.title_pay_faragment, Payord.class);
        }
        Payord selectedPay = Utils.nvl(payTempl, payOut, filterParams.getPayord(), payEmpty);

        Account accOut = parseInputData(R.string.title_acc_fragment, Account.class);
        Account accFromPay = selectedPay.getAccount(context);
        Account accDef = preferences.getDefaultAcc();
        Account selectedAcc = Utils.nvl(accOut, accFromPay, filterParams.getAccount(), accDef);

        Category catOut = parseInputData(R.string.title_cat_fragment, Category.class);
        Category catDef = Category.getWithoutCategory(context);
        Category catFromPay = selectedPay.getCategory(context);
        Category selectedCat = Utils.nvl(catOut, catFromPay, filterParams.getCategory(), catDef);

        Period perDef = new Period("", Period.Type.MONTH, 12);
        Period perFromPay = selectedPay.getPeriod(context);
        Period selectedPer = Utils.nvl(perFromPay, filterParams.getPeriod(), perDef);

        filterParams.initParams(selectedAcc, selectedPay, selectedCat, selectedPer, filterParams.getSaveAsTemplate());

        refreshViewData();
    }

    protected void refreshViewData() {
        cmdAccount.setText(filterParams.getAccount().getName());
        cmdCat.setText(filterParams.getCategory().getName());
        txtAmount.setText(Utils.intToMoney(filterParams.getPayord().getAmount()));
        txtDate.setText(Utils.dateToString(context, filterParams.getPayord().getDt()));
        txtPayName.setText(filterParams.getPayord().getName());
        switch (filterParams.getPayord().getType()) {
            case CREDIT:
                typePayOnClickListener.onClick(imgCredit);
                break;
            case DEBIT:
                typePayOnClickListener.onClick(imgDebit);
                break;
        }
        setCmdSelection(cmdRepeat, filterParams.getPayord().isPermanent());
        setCmdSelection(cmdRemind, filterParams.getPayord().isRemind());
        setCmdSelection(cmdSaveAsTemplate, filterParams.getSaveAsTemplate());
    }

    @Override
    public void onClick(View v) {
        float amount = txtAmount.getText().toString().isEmpty() ? 0 : Utils.stringToFloat(context, txtAmount.getText().toString());
        switch (v.getId()) {
            case R.id.cmdPaySave:
                savePayord();
                break;
            case R.id.cmdPayCancel:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.txtPayDt:
                DateDialog.showDatePicker(getActivity(), ondateListener, Utils.getDateFromString(context, txtDate.getText().toString()));
                break;
            case R.id.cmdPayCalc:
                showCalc(amount);
                break;
            case R.id.txtPayAmount:
                showCalc(amount);
                break;
            case R.id.cmdPayRepeat:
                setCmdSelection(cmdRepeat, !cmdRepeat.isSelected());
                break;
            case R.id.cmdPayRemind:
                setCmdSelection(cmdRemind, !cmdRemind.isSelected());
                break;
            case R.id.txtPayRemindTime:
                try {
                    TimeDialog.showTimePicker(getActivity(), onTimeRemindListener, Utils.timeToDate(txtTimeRemind.getText().toString()));
                } catch (DateCheckedException e) {
                    Utils.showMessage(getActivity(), "", context.getString(R.string.mes_wrong_time_format), null);
                }
                break;
            case R.id.cmdPaySaveAsTempl:
                setCmdSelection(cmdSaveAsTemplate, !cmdSaveAsTemplate.isSelected());
                break;
        }
    }

    private void savePayord() {
        boolean res;
        InvoicePayord invPay = null;
        try {

            if (filterParams.getAccount() == null) {
                throw new SavingException(context.getResources().getString(R.string.mes_pay_invalid_account));
            }

            if (filterParams.getCategory() == null) {
                throw new SavingException(context.getResources().getString(R.string.mes_pay_invalid_category));
            }

            if (Utils.moneyToInt(context, txtAmount.getText().toString()) <= 0) {
                throw new SavingException(context.getResources().getString(R.string.mes_pay_invalid_amount));
            }

            if (cmdRepeat.isSelected()) {
                try {
                    Integer.parseInt(txtRepeatCount.getText().toString());
                } catch (NumberFormatException ex) {
                    throw new SavingException(context.getResources().getString(R.string.mes_pay_invalid_repeat_count));
                }
            }

            filterParams.getPayord().setId_acc(filterParams.getAccount().getId());
            filterParams.getPayord().setId_cat(filterParams.getCategory().getId());
            filterParams.getPayord().setAmount(Utils.moneyToInt(context, txtAmount.getText().toString()));
            filterParams.getPayord().setDt(Utils.getDateFromString(context, txtDate.getText().toString()));
            filterParams.getPayord().setName(txtPayName.getText().toString());
            filterParams.getPayord().setPermanent(cmdRepeat.isSelected());
            filterParams.getPayord().setRemind(cmdRemind.isSelected());

            if (cmdRepeat.isSelected()) {
                if (filterParams.getPeriod() == null) {
                    filterParams.setPeriod(new Period());
                }
                filterParams.getPeriod().setName("");
                filterParams.getPeriod().setType(Period.Type.fromInt(spinTypePeriod.getSelectedItemPosition()));
                filterParams.getPeriod().setCount(Integer.parseInt(txtRepeatCount.getText().toString()));
            } else {
                filterParams.getPayord().setId_period(0);
                filterParams.setPeriod(null);
            }
            if (filterParams.getPayord().isRemind()) {
                filterParams.getPayord().setTimeRemind(Utils.timeToFloat(txtTimeRemind.getText().toString()));
            }
            refreshLoader(LOADER_ID, filterParams.getBundleSelf(), loaderCallback);
        } catch (Exception ex) {
            Utils.showMessage(getActivity(), "", ex.getMessage(), null);
        }
    }

    LoaderManager.LoaderCallbacks<Map<Integer, Object>> loaderCallback = new LoaderManager.LoaderCallbacks<Map<Integer, Object>>() {
        @Override
        public Loader<Map<Integer, Object>> onCreateLoader(int id, Bundle args) {
            progressDialogInfin = ProgressDialog.show(getActivity(), "", context.getResources().getString(R.string.mes_pay_tray_saving), true, false);
            return new BackgroundLoader(context, backgroundTaskLoad, args);
        }

        @Override
        public void onLoadFinished(Loader<Map<Integer, Object>> listLoader, Map<Integer, Object> resultParams) {
            if (listLoader.getId() == LOADER_ID) {
                if (progressDialogInfin != null && progressDialogInfin.isShowing()) {
                    progressDialogInfin.dismiss();
                }
                boolean resultSuccess = (Boolean) resultParams.get(RESULT_PARAM_ID_SUCCESS);
                String errMessage = (String) resultParams.get(RESULT_PARAM_ID_MESSAGE);
                if (resultSuccess) {
                    DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            MainSlidingMenu.getInstance().switchContent(new PaylistFragment());
                        }
                    };
                    Utils.showMessage(getActivity(), "", context.getResources().getString(R.string.saved_successfully), dialogListener);
                } else {
                    Utils.showMessage(getActivity(), "", errMessage, null);
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Map<Integer, Object>> listLoader) {
        }
    };


    BackgroundTask backgroundTaskLoad = new BackgroundTask<Map<Integer, Object>>() {
        @Override
        public Map<Integer, Object> load(Bundle args) {
            boolean res = false;
            String errMesage = "";
            PayordFilterParams filterParams = (PayordFilterParams) args.getSerializable(FilterParams.getBundleKey());
            Map<Integer, Object> resultParams = new HashMap<Integer, Object>();
            InvoicePayord invPay = null;
            try {
                Payord resultPayord;
                invPay = new InvoicePayord(context);
                if (!invPay.getDb().inTransaction()) {
                    invPay.getDb().beginTransaction();
                }
                if (typeStart == TYPE_START_EDIT) {
                    filterParams.getPayord().clearAlarms(context);
                    resultPayord = invPay.editPayord(true, filterParams.getPayord(), filterParams.getPeriod(), cmdSaveAsTemplate.isSelected());
                } else {
                    resultPayord = invPay.createPayord(true, filterParams.getPayord(), filterParams.getPeriod(), cmdSaveAsTemplate.isSelected());
                }
                if (resultPayord == null) {
                    throw new SavingException(context.getResources().getString(R.string.mes_pay_error_save));
                }
                resultPayord.setAlarm(context);
                if (invPay.getDb().inTransaction()) {
                    invPay.getDb().setTransactionSuccessful();
                }
                res = true;
            } catch (Exception ex) {
                res = false;
                errMesage = "Error while saving: " + ex.getMessage();
            } finally {
                if (invPay != null && invPay.getDb().inTransaction()) {
                    invPay.getDb().endTransaction();
                }
            }

            resultParams.put(RESULT_PARAM_ID_SUCCESS, res);
            resultParams.put(RESULT_PARAM_ID_MESSAGE, errMesage);
            return resultParams;
        }
    };


    View.OnClickListener dictionaryOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AbstractFragment fragment = null;
            switch (v.getId()) {
                case R.id.cmdPayTemplate:
                    fragment = new TemplateFragment();
                    break;
                case R.id.cmdPayAcc:
                    fragment = new AccountFragment();
                    break;
                case R.id.cmdPayCat:
                    fragment = new CategoryFragment();
                    break;
            }
            Bundle args = new Bundle();
            args.putInt(Cnt.BUNDLE_KEY_TYPE_START, AbstractFragment.TYPE_START_SELECT);
            fragment.setArguments(args);
            MainSlidingMenu.getInstance().switchContent(fragment);
        }
    };

    DatePickerDialog.OnDateSetListener ondateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar cal = Calendar.getInstance();
            cal.set(year, monthOfYear, dayOfMonth);
            filterParams.getPayord().setDt(cal.getTime());
            txtDate.setText(Utils.dateToString(context, cal.getTime()));
        }
    };

    TimePickerDialog.OnTimeSetListener onTimeRemindListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            filterParams.getPayord().setTimeRemind(Utils.timeToFloat(hourOfDay, minute));
            txtTimeRemind.setText(Utils.floatToTime(filterParams.getPayord().getTimeRemind()));
        }
    };

    private void showCalc(float totalValue) {
        CalcDialog calcDialog = new CalcDialog(context, calcListener);
        Bundle args = new Bundle();
        args.putFloat("TOTAL_VALUE", totalValue);
        calcDialog.setArguments(args);
        calcDialog.show(getActivity().getSupportFragmentManager(), "CALC_DIALOG");
    }

    CalcDialog.CalcListener calcListener = new CalcDialog.CalcListener() {
        @Override
        public void onTotalClick(float result) {
            filterParams.getPayord().setAmount((int) (result * 100));
            txtAmount.setText(Utils.floatToString(result));
        }
    };

    View.OnClickListener typePayOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Drawable draw = null;
            imgCredit.setImageResource(R.drawable.add_white);
            imgDebit.setImageResource(R.drawable.remove_white);
            switch (v.getId()) {
                case R.id.imgPayTypeDebit:
                    imgDebit.setImageResource(R.drawable.remove);
                    txtAmount.setTextColor(context.getResources().getColor(R.color.red));
                    filterParams.getPayord().setType(Payord.Type.DEBIT);
                    break;
                case R.id.imgPayTypeCredit:
                    imgCredit.setImageResource(R.drawable.add_green);
                    txtAmount.setTextColor(context.getResources().getColor(R.color.xdark_green));
                    filterParams.getPayord().setType(Payord.Type.CREDIT);
                    break;
            }
        }
    };

    View.OnClickListener repCountOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int repCnt = 1;
            try {
                repCnt = Integer.parseInt(txtRepeatCount.getText().toString());
            } catch (NumberFormatException e) {
                txtRepeatCount.setText("1");
            }
            switch (v.getId()) {
                case R.id.imgPayRepCountMinus:
                    repCnt--;
                    break;
                case R.id.imgPayRepCountPlus:
                    repCnt++;
                    break;
            }
            repCnt = repCnt < 1 ? 1 : repCnt;
            filterParams.getPeriod().setCount(repCnt);
            txtRepeatCount.setText(String.valueOf(repCnt));
        }
    };


    private void setCmdSelection(Button cmd, boolean state) {
        Drawable draw = null;
        cmd.setSelected(state);
        switch (cmd.getId()) {
            case R.id.cmdPayRepeat:
                filterParams.getPayord().setPermanent(state);
                if (cmd.isSelected()) {
                    draw = getResources().getDrawable(R.drawable.calendar);
                    layRepeat.setVisibility(View.VISIBLE);
                    if (filterParams.getPeriod() != null) {
                        spinTypePeriod.setSelection(filterParams.getPeriod().getType().getInt());
                        txtRepeatCount.setText(String.valueOf(filterParams.getPeriod().getCount()));
                    }
                } else {
                    draw = getResources().getDrawable(R.drawable.calendar_white);
                    layRepeat.setVisibility(View.GONE);
                }
                break;
            case R.id.cmdPayRemind:
                filterParams.getPayord().setRemind(state);
                if (cmd.isSelected()) {
                    draw = getResources().getDrawable(R.drawable.alert_blue);
                    layRemind.setVisibility(View.VISIBLE);
                    txtTimeRemind.setText(Utils.floatToTime(filterParams.getPayord().getTimeRemind()));
                } else {
                    draw = getResources().getDrawable(R.drawable.alert_white);
                    layRemind.setVisibility(View.GONE);
                }
                break;
            case R.id.cmdPaySaveAsTempl:
                if (cmd.isSelected()) {
                    draw = getResources().getDrawable(R.drawable.puzzle);
                } else {
                    draw = getResources().getDrawable(R.drawable.puzzle_white);
                }
                filterParams.setSaveAsTemplate(state);
                break;
        }
        cmd.setCompoundDrawablesWithIntrinsicBounds(draw, null, null, null);
    }


    AdapterView.OnItemSelectedListener onItemTypePeriodListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            filterParams.getPeriod().setType(Period.Type.fromInt(position));
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };


    private class MultiTextWatcher implements TextWatcher {
        private EditText mEditText;

        public MultiTextWatcher(EditText e) {
            mEditText = e;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            switch (mEditText.getId()) {
                case R.id.txtPayName:
                    filterParams.getPayord().setName(s.toString());
                    break;
                case R.id.txtPayRepeatCount:
                    try {
                        filterParams.getPeriod().setCount(Integer.parseInt(s.toString()));
                    } catch (NumberFormatException e) {
                     /*NOP*/
                    }
                    break;

            }
        }
    }


}
