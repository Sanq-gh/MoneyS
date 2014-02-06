package com.sanq.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.*;
import android.widget.*;
import com.sanq.core.Report;
import com.sanq.dao.PayordDAO;
import com.sanq.dao.TemplateDAO;
import com.sanq.entity.*;
import com.sanq.loader.BackgroundLoader;
import com.sanq.loader.BackgroundTask;
import com.sanq.moneys.MainSlidingMenu;
import com.sanq.moneys.R;
import com.sanq.params.FilterParams;
import com.sanq.params.PaylistFilterParams;
import com.sanq.ui.adapter.PaylistAdapter;
import com.sanq.ui.dialog.ConfirmDialog;
import com.sanq.ui.dialog.ConfirmDialogListener;
import com.sanq.ui.dialog.DateRangeDialog;
import com.sanq.utils.Cnt;
import com.sanq.utils.DatePeriod;
import com.sanq.utils.Preferences;
import com.sanq.utils.Utils;
import yuku.iconcontextmenu.IconContextMenu;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 14.06.13
 * Time: 14:05
 */
public class PaylistFragment extends AbstractFragment implements View.OnClickListener {
    private final int LOADER_ID = 100;

    private final int RESULT_PARAM_ID_PAYLIST = 0;
    private final int RESULT_PARAM_ID_SALDO = 1;

    PaylistAdapter mAdapter;
    PaylistFilterParams filterParams;

    ListView listView;
    ViewGroup layProgressPaylist;
    ViewGroup laySaldo;
    ViewGroup layProgressSaldo;

    Button cmdDtFrom;
    Button cmdDtTo;
    Button cmdPeriod;

    Button cmdAcc;
    Spinner spinTypePay;
    ArrayAdapter<Payord.Type> adapterTypePay;

    //Button cmdNew;
    //Button cmdBack;

    TextView txtTotalInSaldo;
    TextView txtTotalDebit;
    TextView txtTotalCredit;
    TextView txtTotalOutSaldo;

    CallbackFragment callbackFragment;

    @Override
    protected String getHeadCaption() {
        return context.getResources().getString(R.string.title_paylist_faragment);
    }

    @Override
    protected int getHeadIcon() {
        return R.drawable.transact;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new PaylistAdapter(context);
        Preferences prefs = new Preferences(context);
        filterParams = new FilterParams();
        filterParams.initParams(prefs.getDefaultAcc(), Period.calcSelectDateRange(context, prefs.getDefaultTypeReportPeriod()), Payord.Type.UNDEFINED);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.paylist, null);

        listView = (ListView) v.findViewById(R.id.lvPaylist);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(onItemClickListener);
        listView.setVisibility(View.VISIBLE);

        layProgressPaylist = (ViewGroup) v.findViewById(R.id.layPaylistProgress);
        layProgressPaylist.setVisibility(View.GONE);
        laySaldo = (ViewGroup) v.findViewById(R.id.layPaylistSaldo);
        laySaldo.setVisibility(View.VISIBLE);
        layProgressSaldo = (ViewGroup) v.findViewById(R.id.layPaylistProgressSaldo);
        layProgressSaldo.setVisibility(View.GONE);

        cmdDtFrom = (Button) v.findViewById(R.id.cmdPaylistDtFrom);
        cmdDtFrom.setOnClickListener(this);
        cmdDtFrom.setText(Utils.dateToString(context, filterParams.getDatePeriod().dateFrom));

        cmdDtTo = (Button) v.findViewById(R.id.cmdPaylistDtTo);
        cmdDtTo.setOnClickListener(this);
        cmdDtTo.setText(Utils.dateToString(context, filterParams.getDatePeriod().dateTo));

        cmdPeriod = (Button) v.findViewById(R.id.cmdPaylistPeriod);
        cmdPeriod.setOnClickListener(this);

        cmdAcc = (Button) v.findViewById(R.id.cmdPaylistAcc);
        cmdAcc.setOnClickListener(this);
        cmdAcc.setText(filterParams.getAccount().getNameAndCurrency());

        spinTypePay = (Spinner) v.findViewById(R.id.spinPaylistTypePay);
        adapterTypePay = new ArrayAdapter<Payord.Type>(context, R.layout.template_spin, Payord.Type.values());
        adapterTypePay.setDropDownViewResource(R.layout.my_simple_spinner_dropdown_item);


        spinTypePay.setAdapter(adapterTypePay);
        spinTypePay.setOnItemSelectedListener(spinTypePayOnItemClickListener);

//        cmdNew = (Button) v.findViewById(R.id.cmdPayListNew);
//        cmdNew.setOnClickListener(this);
//
//        cmdBack = (Button) v.findViewById(R.id.cmdPayListBack);
//        cmdBack.setOnClickListener(this);

        txtTotalInSaldo = (TextView) v.findViewById(R.id.txtPaylistTotalInSaldo);
        txtTotalDebit = (TextView) v.findViewById(R.id.txtPaylistTotalIn);
        txtTotalCredit = (TextView) v.findViewById(R.id.txtPaylistTotalOut);
        txtTotalOutSaldo = (TextView) v.findViewById(R.id.txtPaylistTotalOutSaldo);

        registerForContextMenu(listView);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.cmdPayListNew:
//                callbackFragment.onFragmentEvent(R.string.title_acc_fragment, filterParams.getAccount());
//                MainSlidingMenu.getInstance().switchContent(new PayordFragment());
//
//                break;
//            case R.id.cmdPayListBack:
//                getActivity().getSupportFragmentManager().popBackStack();
//                break;
            case R.id.cmdPaylistDtFrom:
                setDateByDialog(cmdDtFrom, filterParams.getDatePeriod().dateFrom);
                break;
            case R.id.cmdPaylistDtTo:
                setDateByDialog(cmdDtTo, filterParams.getDatePeriod().dateTo);
                break;
            case R.id.cmdPaylistPeriod:
                new DateRangeDialog(context, dateRangeDialogListener).show(getActivity().getSupportFragmentManager(), "");
                break;
            case R.id.cmdPaylistAcc:
                AbstractFragment fragment = new AccountFragment();
                Bundle args = new Bundle();
                args.putInt(Cnt.BUNDLE_KEY_TYPE_START, AbstractFragment.TYPE_START_SELECT);
                fragment.setArguments(args);
                MainSlidingMenu.getInstance().switchContent(fragment);
                break;
        }
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
        filterParams.setAccount((Account) Utils.nvl(parseInputData(R.string.title_acc_fragment, Account.class), filterParams.getAccount()));
        refreshViewData();
    }


    protected void refreshViewData() {
        refreshLoader(LOADER_ID, filterParams.getBundle(), loaderCallback);

        cmdAcc.setText(filterParams.getAccount().getNameAndCurrency());
        cmdDtFrom.setText(Utils.dateToString(context, filterParams.getDatePeriod().dateFrom));
        cmdDtTo.setText(Utils.dateToString(context, filterParams.getDatePeriod().dateTo));
        cmdPeriod.setText(Period.encodeSelectDateRange(context, filterParams.getDatePeriod()).toString());
        spinTypePay.setSelection(adapterTypePay.getPosition(filterParams.getPayType()));
    }

    //background proc for payord list and saldo =======================================================================

    private enum TypeCalcSaldo implements Serializable {
        IN, DEBIT, CREDIT, OUT
    }

    LoaderManager.LoaderCallbacks<Map<Integer, Object>> loaderCallback = new LoaderManager.LoaderCallbacks<Map<Integer, Object>>() {
        @Override
        public Loader<Map<Integer, Object>> onCreateLoader(int id, Bundle args) {
            switchVisibility(true, new View[]{layProgressPaylist, layProgressSaldo}, new View[]{listView, laySaldo});
            return new BackgroundLoader(context, backgroundTaskLoad, args);
        }

        @Override
        public void onLoadFinished(Loader<Map<Integer, Object>> listLoader, Map<Integer, Object> resultParams) {
            if (listLoader.getId() == LOADER_ID) {
                List<Payord> pays = (List<Payord>) resultParams.get(RESULT_PARAM_ID_PAYLIST);
                Map<TypeCalcSaldo, String> saldos = (HashMap<TypeCalcSaldo, String>) resultParams.get(RESULT_PARAM_ID_SALDO);

                txtTotalInSaldo.setText(saldos.get(TypeCalcSaldo.IN));
                txtTotalDebit.setText("-" + saldos.get(TypeCalcSaldo.DEBIT));
                txtTotalCredit.setText("+" + saldos.get(TypeCalcSaldo.CREDIT));
                txtTotalOutSaldo.setText(saldos.get(TypeCalcSaldo.OUT));
                mAdapter.reloadData(pays);

                switchVisibility(false, new View[]{layProgressPaylist, layProgressSaldo}, new View[]{listView, laySaldo});
            }
        }

        @Override
        public void onLoaderReset(Loader<Map<Integer, Object>> listLoader) {
        }
    };

    BackgroundTask backgroundTaskLoad = new BackgroundTask<Map<Integer, Object>>() {
        @Override
        public Map<Integer, Object> load(Bundle args) {
            Map<Integer, Object> res = new HashMap<Integer, Object>();
            Account acc = (Account) (args.getSerializable(Account.getBundleKey()));
            Payord.Type payType = (Payord.Type) args.getSerializable(Payord.Type.getBundleKey());
            DatePeriod per = (DatePeriod) args.getSerializable(DatePeriod.getBundleKey());
            List<Payord> pays = new PayordDAO(context).getByDateTransfer(acc, payType, per);
            res.put(RESULT_PARAM_ID_PAYLIST, pays);

            Map<TypeCalcSaldo, String> saldos = new HashMap<TypeCalcSaldo, String>();
            Report rep = new Report(context);
            Calendar cal = Calendar.getInstance();
            cal.setTime(per.dateFrom);
            cal.add(Calendar.DATE, -1);
            saldos.put(TypeCalcSaldo.IN, Utils.intToMoney(rep.getSaldoOnDate(acc.getId(), cal.getTime())));
            saldos.put(TypeCalcSaldo.DEBIT, Utils.intToMoney(rep.getAmountOnPeriod(acc.getId(), Payord.Type.DEBIT, per)));
            saldos.put(TypeCalcSaldo.CREDIT, Utils.intToMoney(rep.getAmountOnPeriod(acc.getId(), Payord.Type.CREDIT, per)));
            saldos.put(TypeCalcSaldo.OUT, Utils.intToMoney(rep.getSaldoOnDate(acc.getId(), per.dateTo)));
            res.put(RESULT_PARAM_ID_SALDO, saldos);

            return res;
        }
    };

    //====================================================================================

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // super.onCreateContextMenu(menu, v, menuInfo);
        // getActivity().getMenuInflater().inflate(idContextMenu, menu);
        IconContextMenu cm = new IconContextMenu(getActivity(), R.menu.context_paylist);
        cm.setOnIconContextItemSelectedListener(iconMenuiListener);
        cm.setInfo(((AdapterView.AdapterContextMenuInfo) menuInfo).position);
        cm.show();

    }


    IconContextMenu.IconContextItemSelectedListener iconMenuiListener = new IconContextMenu.IconContextItemSelectedListener() {
        @Override
        public void onIconContextItemSelected(MenuItem item, Object info) {
            Payord clickedPay = mAdapter.getPayord((Integer) info);
            switch (item.getItemId()) {
                case R.id.ctxNew:
                    callbackFragment.onFragmentEvent(R.string.title_acc_fragment, filterParams.getAccount());
                    MainSlidingMenu.getInstance().switchContent(new PayordFragment());
                    break;
                case R.id.ctxClone:
                    //clone pay by virtual template
                    Template template = new Template();
                    template.setIdPayord(clickedPay.getId());
                    callbackFragment.onFragmentEvent(R.string.title_template_fragment, template);
                    MainSlidingMenu.getInstance().switchContent(new PayordFragment());
                    break;
                case R.id.ctxEdit:
                    if (clickedPay.getId_cat() != Category.getTransferCategory(context).getId()) {
                        AbstractFragment fragment = new PayordFragment();
                        Bundle args = new Bundle();
                        args.putInt(Cnt.BUNDLE_KEY_TYPE_START, AbstractFragment.TYPE_START_EDIT);
                        MainSlidingMenu.getInstance().fragmentData.put(R.string.title_pay_faragment, clickedPay);
                        fragment.setArguments(args);
                        MainSlidingMenu.getInstance().switchContent(fragment);
                    } else {
                        Utils.showMessage(getActivity(),"", context.getString(R.string.mes_acc_acc_transfer_not_edit), null);
                    }
                    break;
                case R.id.ctxDelete:
                    String mes = context.getResources().getString(R.string.mes_confirm_delete);
                    confirmDeleteListener.onSetParam(clickedPay.getId());
                    ConfirmDialog dlg = new ConfirmDialog(context, confirmDeleteListener, String.format(mes, clickedPay.getName()));
                    dlg.show(getActivity().getSupportFragmentManager(), "");
                    break;
                case R.id.ctxViewRepeat:
                    MainSlidingMenu.getInstance().switchContent(new TransferFragment(clickedPay));
                    break;
                case R.id.ctxAddTemplate:
                    Template templ = new Template(clickedPay.getName(), clickedPay.getId());
                    templ = new TemplateDAO(context).add(templ);
                    if (templ != null) {
                        Utils.procMessage(context, R.string.mes_template_created_successfully);
                    }
                    break;
            }
        }
    };


    ListView.OnItemClickListener onItemClickListener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Payord clickedPay = mAdapter.getPayord(position);
            MainSlidingMenu.getInstance().switchContent(new TransferFragment(clickedPay));
        }
    };


    Spinner.OnItemSelectedListener spinTypePayOnItemClickListener = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            filterParams.setPayType(Payord.Type.fromInt(position));
            refreshViewData();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    DialogInterface.OnClickListener dateRangeDialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (Period.ReportType.fromInt(which) != Period.ReportType.CUSTOM) {
                filterParams.setDatePeriod(Period.calcSelectDateRange(context, Period.ReportType.fromInt(which)));
                refreshViewData();
            }
        }
    };


    ConfirmDialogListener confirmDeleteListener = new ConfirmDialogListener() {
        private int deleteId;

        @Override
        public void onSetParam(int param) {
            this.deleteId = param;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:

                    progressDialogInfin = ProgressDialog.show(getActivity(), "", "", true, false);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                handlerDafault.sendMessage(createHandlerMessageString(Cnt.HANDLER_MES_START, context.getResources().getString(R.string.mes_deleting)));
                                new PayordDAO(context).getById(deleteId).delete(context);
                                handlerDafault.sendEmptyMessage(Cnt.HANDLER_MES_FINISH);
                            } catch (Exception e) {
                                handlerDafault.sendMessage(createHandlerMessageString(Cnt.HANDLER_MES_ERROR, e.getMessage()));
                            }
                        }
                    }).start();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:  /*NOP*/
                    break;
            }
        }
    };


}
