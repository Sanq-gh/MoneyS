package com.sanq.ui.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sanq.core.InvoiceAccount;
import com.sanq.entity.Account;
import com.sanq.entity.Payord;
import com.sanq.entity.Period;
import com.sanq.exception.DateCheckedException;
import com.sanq.exception.SavingException;
import com.sanq.moneys.MainSlidingMenu;
import com.sanq.moneys.R;
import com.sanq.ui.dialog.CalcDialog;
import com.sanq.ui.dialog.DateDialog;
import com.sanq.ui.dialog.TimeDialog;
import com.sanq.utils.Cnt;
import com.sanq.utils.Preferences;
import com.sanq.utils.Utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 14.06.13
 * Time: 14:05
 */
public class AccountTransferFragment extends AbstractFragment implements View.OnClickListener {
    private Account selectedAccFrom;
    private Account selectedAccTo;
    private Payord selectedPay;      //фиктивная платежка для хранения данных формы
    private Period selectedPer;

    private TypeAccSelected currentAccSelected = TypeAccSelected.ACC_FROM;
    ArrayAdapter<Period.Type> adapterTypePeriod;

    ScrollView scrollView;

    Button cmdAccFrom;
    Button cmdAccTo;

    EditText txtAmount;
    TextView txtDate;
    EditText txtTransName;
    Button cmdCalc;
    TextView txtTimeRemind;

    LinearLayout layRepeat;
    LinearLayout layRemind;

    Spinner spinTypePeriod;
    EditText txtRepeatCount;

    Button cmdRepeat;
    Button cmdRemind;

    ImageButton imgRepCountPlus;
    ImageButton imgRepCountMinus;

    Button cmdSave;
    Button cmdCancel;

    private enum TypeAccSelected {
        ACC_FROM, ACC_TO
    }

    @Override
    protected String getHeadCaption() {
        return context.getResources().getString(R.string.title_transacc_fragment);
    }

    @Override
    protected int getHeadIcon() {
        return R.drawable.transfer;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.account_transfer, null);
        scrollView = (ScrollView) v.findViewById(R.id.scrollTransAcc);

        cmdAccFrom = (Button) v.findViewById(R.id.cmdTransAccFrom);
        cmdAccFrom.setOnClickListener(dictionaryOnClickListener);

        cmdAccTo = (Button) v.findViewById(R.id.cmdTransAccTo);
        cmdAccTo.setOnClickListener(dictionaryOnClickListener);

        txtDate = (TextView) v.findViewById(R.id.txtTransAccDt);
        Calendar cal = Calendar.getInstance();
        txtDate.setText(Utils.dateToString(context, cal.getTime()));
        txtDate.setOnClickListener(this);

        txtAmount = (EditText) v.findViewById(R.id.txtTransAccAmount);
        txtAmount.setOnClickListener(this);

        txtTransName = (EditText) v.findViewById(R.id.txtTransAccName);

        txtTimeRemind = (TextView) v.findViewById(R.id.txtTransAccRemindTime);
        txtTimeRemind.setOnClickListener(this);

        cmdCalc = (Button) v.findViewById(R.id.cmdTransAccCalc);
        cmdCalc.setOnClickListener(this);


        cmdRepeat = (Button) v.findViewById(R.id.cmdTransAccRepeat);
        cmdRepeat.setOnClickListener(this);

        cmdRemind = (Button) v.findViewById(R.id.cmdTransAccRemind);
        cmdRemind.setOnClickListener(this);

        layRepeat = (LinearLayout) v.findViewById(R.id.layTransAccRepeat);
        layRemind = (LinearLayout) v.findViewById(R.id.layTransAccRemind);

        spinTypePeriod = (Spinner) v.findViewById(R.id.spinTransAccTypePeriod);
        adapterTypePeriod = new ArrayAdapter<Period.Type>(context, R.layout.template_spin_center, Period.Type.values());
        adapterTypePeriod.setDropDownViewResource(R.layout.my_simple_spinner_dropdown_item);
        spinTypePeriod.setAdapter(adapterTypePeriod);


        txtRepeatCount = (EditText) v.findViewById(R.id.txtTransAccRepeatCount);

        imgRepCountPlus = (ImageButton) v.findViewById(R.id.imgTransAccRepCountPlus);
        imgRepCountPlus.setOnClickListener(repCountOnClickListener);
        imgRepCountMinus = (ImageButton) v.findViewById(R.id.imgTransAccRepCountMinus);
        imgRepCountMinus.setOnClickListener(repCountOnClickListener);

        cmdSave = (Button) v.findViewById(R.id.cmdTransAccSave);
        cmdSave.setOnClickListener(this);
        cmdCancel = (Button) v.findViewById(R.id.cmdTransAccCancel);
        cmdCancel.setOnClickListener(this);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        Preferences preferences = new Preferences(context);
        Payord payEmpty = new Payord(0, 0, 0, 0, "", new Date(), Payord.Type.UNDEFINED, 0, false, false, null, preferences.getDefaultReminderTime());

        selectedPay = Utils.nvl(selectedPay, payEmpty);

        Account accDef = preferences.getDefaultAcc();
        Account accOutFrom = null;
        Account accOutTo = null;

        switch (currentAccSelected) {
            case ACC_FROM:
                accOutFrom = parseInputData(R.string.title_acc_fragment, Account.class);
                break;
            case ACC_TO:
                accOutTo = parseInputData(R.string.title_acc_fragment, Account.class);
                break;
        }

        selectedAccFrom = Utils.nvl(accOutFrom, selectedAccFrom, accDef);
        selectedAccTo = Utils.nvl(accOutTo, selectedAccTo, accDef);
        Period perDef = new Period("", Period.Type.MONTH, 12);
        selectedPer = Utils.nvl(selectedPer, perDef);

        refreshViewData();
    }


    @Override
    public void onPause() {
        super.onPause();
        saveControlsData();
    }


    protected void refreshViewData() {
        cmdAccFrom.setText(selectedAccFrom.getNameAndCurrency());
        cmdAccTo.setText(selectedAccTo.getNameAndCurrency());

        txtAmount.setText(Utils.intToMoney(selectedPay.getAmount()));
        txtDate.setText(Utils.dateToString(context, selectedPay.getDt()));
        txtTransName.setText(selectedPay.getName());

        setCmdSelection(cmdRepeat, selectedPay.isPermanent());
        setCmdSelection(cmdRemind, selectedPay.isRemind());
    }


    private void saveControlsData() {
        int amount = 0;
        try {
            amount = (int) (Utils.stringToFloat(context, txtAmount.getText().toString()) * 100);
        } catch (Exception e) {/*NOP*/}
        selectedPay.setAmount(amount);
        selectedPay.setDt(Utils.getDateFromString(context, txtDate.getText().toString()));
        selectedPay.setName(txtTransName.getText().toString());

        selectedPay.setPermanent(cmdRepeat.isSelected());
        selectedPay.setRemind(cmdRemind.isSelected());

        if (selectedPay.isPermanent()) {
            selectedPer.setType(Period.Type.fromInt(spinTypePeriod.getSelectedItemPosition()));
            int perCnt = 0;
            try {
                perCnt = Integer.parseInt(txtRepeatCount.getText().toString());
            } catch (NumberFormatException e) {/*NOP*/}
            selectedPer.setCount(perCnt);
        }
        if (selectedPay.isRemind()) {

            try {
                selectedPay.setTimeRemind(Utils.timeToFloat(txtTimeRemind.getText().toString()));
            } catch (DateCheckedException e) {
                Utils.showMessage(getActivity(), "", context.getString(R.string.mes_wrong_time_format), null);
            }

        }
    }


    @Override
    public void onClick(View v) {
        float amount = txtAmount.getText().toString().isEmpty() ? 0 : Utils.stringToFloat(context, txtAmount.getText().toString());
        switch (v.getId()) {
            case R.id.cmdTransAccSave:
                if (saveTransfer()) {
                    Toast.makeText(context, R.string.txt_transacc_save_ok, Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
                break;
            case R.id.cmdTransAccCancel:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.txtTransAccDt:
                DateDialog.showDatePicker(getActivity(), ondateListener, Utils.getDateFromString(context, txtDate.getText().toString()));
                break;
            case R.id.cmdTransAccCalc:
                showCalc(amount);
                break;
            case R.id.txtTransAccAmount:
                showCalc(amount);
                break;
            case R.id.cmdTransAccRepeat:
                setCmdSelection(cmdRepeat, !cmdRepeat.isSelected());
                break;
            case R.id.cmdTransAccRemind:
                setCmdSelection(cmdRemind, !cmdRemind.isSelected());
                break;
            case R.id.txtTransAccRemindTime:
                try {
                    TimeDialog.showTimePicker(getActivity(), onTimeRemindListener, Utils.timeToDate(txtTimeRemind.getText().toString()));
                } catch (DateCheckedException e) {
                    Utils.showMessage(getActivity(), "", context.getString(R.string.mes_wrong_time_format), null);
                }
                break;
        }
    }

    private boolean saveTransfer() {
        boolean res = false;
        try {
            saveControlsData();
            if ((selectedAccFrom == null) | (selectedAccTo == null) | ((selectedAccFrom.equals(selectedAccTo)))) {
                throw new SavingException(context.getResources().getString(R.string.mes_transacc_invalid_account));
            }

            if (!selectedAccFrom.getCurrency().equals(selectedAccTo.getCurrency())) {
                throw new SavingException(context.getResources().getString(R.string.mes_illegal_trans_acc));

            }

            if (selectedPay.getAmount() <= 0) {
                throw new SavingException(context.getResources().getString(R.string.mes_pay_invalid_amount));
            }

            if (selectedPay.isPermanent()) {
                if (selectedPer.getCount() == 0) {
                    throw new SavingException(context.getResources().getString(R.string.mes_pay_invalid_repeat_count));
                }
            } else {
                selectedPer = null;
            }

            new InvoiceAccount(context).transferBetweenAcc(selectedAccFrom, selectedAccTo, selectedPay.getName(), selectedPay.getAmount(), selectedPay.getDt(), selectedPer, selectedPay.isRemind(), selectedPay.getTimeRemind());

            res = true;
        } catch (Exception ex) {
            res = false;
            Utils.showMessage(getActivity(),"", ex.getMessage(), null);
        }
        return res;
    }


    View.OnClickListener dictionaryOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cmdTransAccFrom:
                    currentAccSelected = TypeAccSelected.ACC_FROM;
                    break;
                case R.id.cmdTransAccTo:
                    currentAccSelected = TypeAccSelected.ACC_TO;
                    break;
            }
            AbstractFragment  fragment = new AccountFragment();
            Bundle args = new Bundle();
            args.putInt(Cnt.BUNDLE_KEY_TYPE_START, AbstractFragment.TYPE_START_SELECT);
            fragment.setArguments(args);
            MainSlidingMenu.getInstance().switchContent(fragment);
        }
    };

    TimePickerDialog.OnTimeSetListener onTimeRemindListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            selectedPay.setTimeRemind(Utils.timeToFloat(hourOfDay, minute));
            txtTimeRemind.setText(Utils.floatToTime(selectedPay.getTimeRemind()));
        }
    };

    DatePickerDialog.OnDateSetListener ondateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar cal = Calendar.getInstance();
            cal.set(year, monthOfYear, dayOfMonth);
            txtDate.setText(Utils.dateToString(context, cal.getTime()));
        }
    };

    private void showCalc(float totalValue) {
        CalcDialog calcDialog = new CalcDialog(context, calcListener);
        Bundle args = new Bundle();
        args.putFloat("TOTAL_VALUE", totalValue);
        calcDialog.setArguments(args);
        calcDialog.show(getActivity().getSupportFragmentManager(), "calc_dialog");
    }

    CalcDialog.CalcListener calcListener = new CalcDialog.CalcListener() {
        @Override
        public void onTotalClick(float result) {
            txtAmount.setText(Utils.floatToString(result));
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
                case R.id.imgTransAccRepCountMinus:
                    repCnt--;
                    break;
                case R.id.imgTransAccRepCountPlus:
                    repCnt++;
                    break;
            }
            repCnt = repCnt < 1 ? 1 : repCnt;
            txtRepeatCount.setText(String.valueOf(repCnt));
        }
    };

    private void setCmdSelection(Button cmd, boolean state) {
        Drawable draw = null;
        cmd.setSelected(state);
        switch (cmd.getId()) {
            case R.id.cmdTransAccRepeat:
                selectedPay.setPermanent(state);
                if (cmd.isSelected()) {
                    draw = getResources().getDrawable(R.drawable.calendar);
                    layRepeat.setVisibility(View.VISIBLE);

                    if (selectedPer != null) {
                        spinTypePeriod.setSelection(selectedPer.getType().getInt());
                        txtRepeatCount.setText(String.valueOf(selectedPer.getCount()));
                    }
                    Utils.scrollToEnd(scrollView);
                } else {
                    draw = getResources().getDrawable(R.drawable.calendar_white);
                    layRepeat.setVisibility(View.GONE);
                }
                break;
            case R.id.cmdTransAccRemind:
                selectedPay.setRemind(state);
                if (cmd.isSelected()) {
                    draw = getResources().getDrawable(R.drawable.alert_blue);
                    layRemind.setVisibility(View.VISIBLE);
                    txtTimeRemind.setText(Utils.floatToTime(selectedPay.getTimeRemind()));
                    Utils.scrollToEnd(scrollView);
                } else {
                    draw = getResources().getDrawable(R.drawable.alert_white);
                    layRemind.setVisibility(View.GONE);
                }
                break;
        }
        cmd.setCompoundDrawablesWithIntrinsicBounds(draw, null, null, null);
    }

}
