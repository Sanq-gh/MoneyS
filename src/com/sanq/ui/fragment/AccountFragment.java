package com.sanq.ui.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.*;
import android.widget.*;
import com.sanq.core.Report;
import com.sanq.dao.AccountDAO;
import com.sanq.entity.Account;
import com.sanq.entity.Payord;
import com.sanq.entity.Period;
import com.sanq.loader.BackgroundLoader;
import com.sanq.loader.BackgroundTask;
import com.sanq.moneys.MainSlidingMenu;
import com.sanq.moneys.R;
import com.sanq.params.AccountFilterParams;
import com.sanq.params.FilterParams;
import com.sanq.ui.adapter.AccountAdapter;
import com.sanq.ui.dialog.ConfirmDialog;
import com.sanq.ui.dialog.ConfirmDialogListener;
import com.sanq.ui.dialog.DateDialog;
import com.sanq.utils.Cnt;
import com.sanq.utils.Preferences;
import com.sanq.utils.Utils;
import yuku.iconcontextmenu.IconContextMenu;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 14.06.13
 * Time: 14:05
 */

public class AccountFragment extends AbstractFragment implements View.OnClickListener {
    private final int LOADER_ID = 400;


    private final int RESULT_PARAM_ID_ACCOUNTS = 0;
    private final int RESULT_PARAM_ID_SALDO = 1;


    AccountAdapter mAdapter;
    AccountFilterParams filterParams;

    View layButtonFew;
//    View layButtonOne;

    ListView listView;
    TextView txtTotalSaldo;
    ViewGroup layProgress;
    ViewGroup layTotalSaldo;


    Button cmdDate;

    Button cmdDtLeft;
    Button cmdDtRight;

    Button cmdNew;
    Button cmdTransfer;
    // Button cmdBack;

    int idContextMenu = R.menu.context_account;

    CallbackFragment callbackFragment;

    //ProgressDialog progressDialogInfin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        filterParams = new FilterParams();
        filterParams.initParams(new Date());
        mAdapter = new AccountAdapter(context);
    }

    @Override
    protected String getHeadCaption() {
        return context.getResources().getString(R.string.title_acc_fragment);
    }

    @Override
    protected int getHeadIcon() {
        return R.drawable.visa;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.account, null);

        layButtonFew = (View) v.findViewById(R.id.layAccButttonFew);
//        layButtonOne = (View) v.findViewById(R.id.layAccButtonOne);
//

        listView = (ListView) v.findViewById(R.id.lvAccount);
        listView.setAdapter(mAdapter);

        txtTotalSaldo = (TextView) v.findViewById(R.id.txtAccTotalSaldo);

        layProgress = (ViewGroup) v.findViewById(R.id.layAccProgress);

        layTotalSaldo= (ViewGroup) v.findViewById(R.id.layAccTotalSaldo);

        cmdDate = (Button) v.findViewById(R.id.cmdAccDt);
        cmdDate.setOnClickListener(this);
        cmdDate.setText(Utils.dateToString(context, filterParams.getDate()));

        cmdDtLeft = (Button) v.findViewById(R.id.cmdAccDtLeft);
        cmdDtLeft.setOnClickListener(this);

        cmdDtRight = (Button) v.findViewById(R.id.cmdAccDtRight);
        cmdDtRight.setOnClickListener(this);

        cmdNew = (Button) v.findViewById(R.id.cmdAccNew);
        cmdNew.setOnClickListener(this);
        cmdTransfer = (Button) v.findViewById(R.id.cmdAccTransfer);
        cmdTransfer.setOnClickListener(this);
//        cmdBack = (Button) v.findViewById(R.id.cmdAccBack);
//        cmdBack.setOnClickListener(this);

        registerForContextMenu(listView);

        switchTypeStart(typeStart);
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callbackFragment = (CallbackFragment) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement CallbackFragment");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshViewData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cmdAccDt:
                DateDialog.showDatePicker(getActivity(), ondateListener, Utils.getDateFromString(context, cmdDate.getText().toString()));
                break;
            case R.id.cmdAccDtLeft:
                filterParams.setDate(moveDateFilter(cmdDate, -1));
                refreshViewData();
                break;
            case R.id.cmdAccDtRight:
                filterParams.setDate(moveDateFilter(cmdDate, 1));
                refreshViewData();
                break;
            case R.id.cmdAccNew:
                MainSlidingMenu.getInstance().switchContent(new AccountAdd());
                refreshViewData();
                break;
            case R.id.cmdAccTransfer:
                MainSlidingMenu.getInstance().switchContent(new AccountTransferFragment());
                break;
//            case R.id.cmdAccBack:
//                goBack();
//                break;
        }
    }

    protected void refreshViewData() {
        refreshLoader(LOADER_ID, filterParams.getBundle(), loaderCallback);
    }


    LoaderManager.LoaderCallbacks<Map<Integer, Object>> loaderCallback = new LoaderManager.LoaderCallbacks<Map<Integer, Object>>() {
        @Override
        public Loader<Map<Integer, Object>> onCreateLoader(int id, Bundle args) {
            switchVisibility(true, new View[]{layProgress}, new View[]{listView, layTotalSaldo});
            return new BackgroundLoader(context, backgroundTaskLoad, args);
        }

        @Override
        public void onLoadFinished(Loader<Map<Integer, Object>> listLoader, Map<Integer, Object> resultParams) {
            if (listLoader.getId() == LOADER_ID) {
                StringBuilder str = new StringBuilder();
                Map<String, Integer> saldo = (Map<String, Integer>) resultParams.get(RESULT_PARAM_ID_SALDO);
                for (String key : saldo.keySet()) {
                    str.append(key + ": " + Utils.intToMoney(saldo.get(key)) + "  ");
                }
                txtTotalSaldo.setText(str.toString());
                txtTotalSaldo.setSelected(true);
                List<Account> accounts = (List<Account>) resultParams.get(RESULT_PARAM_ID_ACCOUNTS);

                mAdapter.reloadData(accounts);
                switchVisibility(false, new View[]{layProgress}, new View[]{listView, layTotalSaldo});
            }
        }

        @Override
        public void onLoaderReset(Loader<Map<Integer, Object>> listLoader) {
        }
    };

    BackgroundTask backgroundTaskLoad = new BackgroundTask<Map<Integer, Object>>() {
        @Override
        public Map<Integer, Object> load(Bundle args) {
            Date date = (Date) (args.getSerializable(Date.class.toString()));
            Map<Integer, Object> resultParams = new HashMap<Integer, Object>();
            List<Account> accounts = new AccountDAO(context).getAll();
            Map<String, Integer> saldo = new Report(context).getSaldoOnDate(date);
            for (Account el : accounts) {
                el.calcSaldo(context, date);
            }
            resultParams.put(RESULT_PARAM_ID_ACCOUNTS, accounts);
            resultParams.put(RESULT_PARAM_ID_SALDO, saldo);
            return resultParams;
        }
    };


    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        getActivity().getMenuInflater().inflate(idContextMenu, menu);
        IconContextMenu cm = new IconContextMenu(getActivity(), idContextMenu);
        cm.setOnIconContextItemSelectedListener(iconMenuiListener);
        cm.setInfo(((AdapterView.AdapterContextMenuInfo) menuInfo).position);
        cm.show();
    }


    IconContextMenu.IconContextItemSelectedListener iconMenuiListener = new IconContextMenu.IconContextItemSelectedListener() {
        @Override
        public void onIconContextItemSelected(MenuItem item, Object info) {
            Account clickedAcc = mAdapter.getAccount((Integer) info);
            switch (item.getItemId()) {
                case R.id.ctxNewPay:
                    callbackFragment.onFragmentEvent(R.string.title_acc_fragment, clickedAcc);
                    MainSlidingMenu.getInstance().switchContent(new PayordFragment());
                    break;
                case R.id.ctxNewTransfer:
                    callbackFragment.onFragmentEvent(R.string.title_acc_fragment, clickedAcc);
                    MainSlidingMenu.getInstance().switchContent(new AccountTransferFragment());
                    break;
                case R.id.ctxEdit:
                    MainSlidingMenu.getInstance().switchContent(new AccountAdd(clickedAcc));
                    refreshViewData();
                    break;
                case R.id.ctxDelete:
                    String mes = context.getResources().getString(R.string.mes_confirm_delete);
                    confirmListener.onSetParam(clickedAcc.getId());
                    new ConfirmDialog(context, confirmListener, String.format(mes, clickedAcc.getName())).show(getActivity().getSupportFragmentManager(), "");
                    break;
                case R.id.ctxSelect:
                    selectAndPostBack(clickedAcc);
                    break;

            }

        }
    };


    private void switchTypeStart(int typeStart) {
        switch (typeStart) {
            case AbstractFragment.TYPE_START_SELECT:
                layButtonFew.setVisibility(View.GONE);
//                layButtonOne.setVisibility(View.VISIBLE);
                idContextMenu = R.menu.context_select;
                listView.setOnItemClickListener(new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Account clickedAcc = mAdapter.getAccount(position);
                        selectAndPostBack(clickedAcc);
                    }
                });
                break;
            default:
                layButtonFew.setVisibility(View.VISIBLE);
//                layButtonOne.setVisibility(View.GONE);
                idContextMenu = R.menu.context_account;
                listView.setOnItemClickListener(new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Account clickedAcc = mAdapter.getAccount(position);
                        PaylistFragment paylistFragment = new PaylistFragment();
                        Preferences prefs = new Preferences(context);
                        paylistFragment.filterParams = new FilterParams();
                        paylistFragment.filterParams.initParams(clickedAcc, Period.calcSelectDateRange(context, prefs.getDefaultTypeReportPeriod()), Payord.Type.UNDEFINED);
                        MainSlidingMenu.getInstance().switchContent(paylistFragment);
                   }
                });
                break;
        }
    }

    private Date moveDateFilter(Button cmd, int cntDay) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(Utils.getDateFromString(context, cmd.getText().toString()));
        cal.add(Calendar.DATE, cntDay);
        cmd.setText(Utils.dateToString(context, cal.getTime()));
        return cal.getTime();
    }

//
//    ListView.OnItemClickListener listenerOnItemClickListener = new ListView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            Account clickedAcc = mAdapter.getAccount(position);
//            selectAndPostBack(clickedAcc);
//        }
//    };
//
//



    private void selectAndPostBack(Account clickedAcc) {
        callbackFragment.onFragmentEvent(R.string.title_acc_fragment, clickedAcc);
        getActivity().getSupportFragmentManager().popBackStack();
    }

    DatePickerDialog.OnDateSetListener ondateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar cal = Calendar.getInstance();
            cal.set(year, monthOfYear, dayOfMonth);
            cmdDate.setText(Utils.dateToString(context, cal.getTime()));
            filterParams.setDate(cal.getTime());
            refreshViewData();
        }
    };


    ConfirmDialogListener confirmListener = new ConfirmDialogListener() {
        int deleteId;

        @Override
        public void onSetParam(int param) {
            deleteId = param;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {

            switch (which) {
                case android.content.DialogInterface.BUTTON_POSITIVE:
                    progressDialogInfin = ProgressDialog.show(getActivity(), "", "", true, false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                handlerDafault.sendMessage(createHandlerMessageString(Cnt.HANDLER_MES_START, context.getResources().getString(R.string.mes_deleting)));
                                new AccountDAO(context).deleteById(deleteId);
                                handlerDafault.sendEmptyMessage(Cnt.HANDLER_MES_FINISH);
                            } catch (Exception e) {
                                handlerDafault.sendMessage(createHandlerMessageString(Cnt.HANDLER_MES_ERROR, e.getMessage()));
                            }
                        }
                    }).start();
                    break;
                case android.content.DialogInterface.BUTTON_NEGATIVE:  /*NOP*/
                    break;
            }
        }
    };


}
