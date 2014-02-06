package com.sanq.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import com.sanq.dao.AccountDAO;
import com.sanq.entity.Account;
import com.sanq.exception.SavingException;
import com.sanq.moneys.R;
import com.sanq.ui.adapter.CurrencyAdapter;
import com.sanq.ui.dialog.CalcDialog;
import com.sanq.utils.Utils;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 14.06.13
 * Time: 14:05
 */
public class AccountAdd extends AbstractFragment implements View.OnClickListener {
    Account updatedAcc = null;

    CurrencyAdapter currAdapter;

    EditText txtAccName;
    Spinner spinCurrency;
    EditText txtAccSaldo;

    Button cmdSave;
    Button cmdCancel;

    public AccountAdd() {
    }

    public AccountAdd(Account updatedAcc) {
        this.updatedAcc = updatedAcc;
    }

    @Override
    protected String getHeadCaption() {
        return context.getResources().getString(R.string.title_acc_activity_add);
    }

    @Override
    protected int getHeadIcon() {
        return R.drawable.visa;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.account_add, null);

        txtAccName = (EditText) v.findViewById(R.id.txtAccAddName);
        txtAccSaldo = (EditText) v.findViewById(R.id.txtAccAddSaldo);
        txtAccSaldo.setOnClickListener(this);

        currAdapter = new CurrencyAdapter(context);

        spinCurrency = (Spinner) v.findViewById(R.id.spinAccAddCurrency);
        spinCurrency.setAdapter(currAdapter);
        spinCurrency.setOnItemSelectedListener(listenerSpinCurrency);

        cmdSave = (Button) v.findViewById(R.id.cmdAccAddSave);
        cmdSave.setOnClickListener(this);
        cmdCancel = (Button) v.findViewById(R.id.cmdAccAddCancel);
        cmdCancel.setOnClickListener(this);

        // fill controls if get cats for update...
        refreshViewData();

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.txtAccAddSaldo:
                float amount = txtAccSaldo.getText().toString().isEmpty() ? 0 : Utils.stringToFloat(context, txtAccSaldo.getText().toString());
                showCalc(amount);
                break;
            case R.id.cmdAccAddSave:
                saveData();
                break;
            case R.id.cmdAccAddCancel:
                goBack();
                break;
        }
    }


    AdapterView.OnItemSelectedListener listenerSpinCurrency = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    protected void refreshViewData() {
        if (updatedAcc != null) {
            txtAccName.setText(updatedAcc.getName());
            txtAccSaldo.setText(Utils.intToMoney(updatedAcc.getSaldo()));
            spinCurrency.setSelection(currAdapter.getPos(updatedAcc.getCurrency()));
        } else {
            spinCurrency.setSelection(currAdapter.getDefaultCurrencyPos());
        }
    }


    private void saveData() {
        String accName = txtAccName.getText().toString().trim();
        try {
            if (accName.isEmpty()) {
                throw new SavingException(context.getResources().getString(R.string.mes_acc_empty_name));
            }

            String curr = currAdapter.getCurrency(spinCurrency.getSelectedItemPosition()).getCurrencyCode();

            int amount = 0;
            if (!txtAccSaldo.getText().toString().isEmpty()) {
                amount = Utils.moneyToInt(context, txtAccSaldo.getText().toString());
            }


            if (updatedAcc == null) {
                Account acc = new Account(accName, amount, curr);
                new AccountDAO(context).add(acc);
            } else {
                updatedAcc.setName(accName);
                updatedAcc.setSaldo(amount);
                updatedAcc.setCurrency(curr);
                new AccountDAO(context).update(updatedAcc);
            }
            goBack();
        } catch (Exception ex) {
            Utils.showMessage(getActivity(),"", ex.getMessage(),null);
        }
    }

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
            txtAccSaldo.setText(Utils.floatToString(result));
        }
    };

}




