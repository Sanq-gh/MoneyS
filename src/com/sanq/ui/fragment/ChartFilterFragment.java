package com.sanq.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sanq.charts.AbstractChart;
import com.sanq.charts.IChart;
import com.sanq.dao.AccountDAO;
import com.sanq.entity.Account;
import com.sanq.entity.Payord;
import com.sanq.entity.Period;
import com.sanq.loader.BackgroundLoader;
import com.sanq.loader.BackgroundTask;
import com.sanq.moneys.MainSlidingMenu;
import com.sanq.moneys.R;
import com.sanq.params.ChartFilterParams;
import com.sanq.params.FilterParams;
import com.sanq.ui.adapter.AccountAdapter;
import com.sanq.ui.dialog.DateRangeDialog;
import com.sanq.utils.Preferences;
import com.sanq.utils.Utils;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 15.07.13
 * Time: 14:22
 */
public class ChartFilterFragment extends AbstractFragment implements View.OnClickListener {

    private final int LOADER_ID = 500;

    private final int RESULT_PARAM_ID_ACCOUNTS = 0;
    private final int RESULT_PARAM_ID_CURRENCY = 1;

    AccountAdapter adapterAcc;
    ArrayAdapter<AbstractChart.TypeChart> adapterTypeChart;
    ArrayAdapter<String> adapterCurrency;
    ArrayAdapter<Period.Type> adapterTypePeriod;
    ArrayAdapter<Payord.Type> adapterPayType;

    ViewGroup layProgressAcc;
    ViewGroup layProgressCurrensy;

    Button cmdDtFrom;
    Button cmdDtTo;
    Button cmdPeriod;

    ViewGroup layAcc;
    ViewGroup layCurrency;
    ViewGroup layPayType;
    ViewGroup layPeriodType;


    Spinner spinTypeChart;
    Spinner spinAcc;
    Spinner spinCurrency;
    Spinner spinTypePeriod;
    Spinner spinPayType;

    TextView txtDescChart;

    ImageView imgAcc;
    ImageView imgCurrency;
    ImageView imgPayType;
    ImageView imgPeriodType;

    Button cmdView;
    Button cmdBack;

    AbstractChart.TypeChart filterTypeChart;
    ChartFilterParams filterParams;


    private final int CNT_SPINNER = 4;
    private int cntSpinnerInit = 0;


    @Override
    protected String getHeadCaption() {
        return context.getResources().getString(R.string.title_chart_filter_fragment);
    }

    @Override
    protected int getHeadIcon() {
        return R.drawable.chart;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapterAcc = new AccountAdapter(context, AccountAdapter.TypeControl.SPINNER);
        adapterCurrency = new ArrayAdapter<String>(context, R.layout.template_spin_filter_chart, new ArrayList<String>());
        filterTypeChart = AbstractChart.TypeChart.Balance;
        Account defAcc = new Preferences(context).getDefaultAcc();
        filterParams = new FilterParams();
        filterParams.initParams(defAcc, defAcc.getCurrency(), Period.calcSelectDateRange(context, new Preferences(context).getDefaultTypeReportPeriod()), Payord.Type.CREDIT, Period.Type.DAY, true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chart_filter, container, false);
        cntSpinnerInit = 0;
        layProgressAcc = (ViewGroup) v.findViewById(R.id.layChartProgressAcc);
        layProgressCurrensy = (ViewGroup) v.findViewById(R.id.layChartProgressCurrensy);

        cmdDtFrom = (Button) v.findViewById(R.id.cmdChartFilterDtFrom);
        cmdDtFrom.setOnClickListener(this);
        cmdDtFrom.setText(Utils.dateToString(context, filterParams.getDatePeriod().dateFrom));

        cmdDtTo = (Button) v.findViewById(R.id.cmdChartFilterDtTo);
        cmdDtTo.setOnClickListener(this);
        cmdDtTo.setText(Utils.dateToString(context, filterParams.getDatePeriod().dateTo));

        cmdPeriod = (Button) v.findViewById(R.id.cmdChartFilterPeriod);
        cmdPeriod.setOnClickListener(this);

        layAcc = (ViewGroup) v.findViewById(R.id.layChartFilterAcc);
        layCurrency = (ViewGroup) v.findViewById(R.id.layChartFilterCurrency);
        layPayType = (ViewGroup) v.findViewById(R.id.layChartFilterPayType);
        layPeriodType = (ViewGroup) v.findViewById(R.id.layChartFilterPeriodType);

        spinTypeChart = (Spinner) v.findViewById(R.id.spinChartFilterTypeChart);
        adapterTypeChart = new ArrayAdapter<AbstractChart.TypeChart>(context, R.layout.template_spin_filter_chart, AbstractChart.TypeChart.values());
        adapterTypeChart.setDropDownViewResource(R.layout.my_simple_spinner_dropdown_item);
        spinTypeChart.setAdapter(adapterTypeChart);
        spinTypeChart.setOnItemSelectedListener(spinOnItemSelectedListener);


        spinPayType = (Spinner) v.findViewById(R.id.spinChartFilterPayType);
        adapterPayType = new ArrayAdapter<Payord.Type>(context, R.layout.template_spin_filter_chart, Payord.Type.values());
        adapterPayType.setDropDownViewResource(R.layout.my_simple_spinner_dropdown_item);
        spinPayType.setAdapter(adapterPayType);
        spinPayType.setOnItemSelectedListener(spinOnItemSelectedListener);

        spinCurrency = (Spinner) v.findViewById(R.id.spinChartFilterCurrency);
        adapterCurrency.setDropDownViewResource(R.layout.my_simple_spinner_dropdown_item);
        spinCurrency.setAdapter(adapterCurrency);
        spinCurrency.setOnItemSelectedListener(spinOnItemSelectedListener);
        spinCurrency.setOnTouchListener(spinOnTouchListener);


        spinAcc = (Spinner) v.findViewById(R.id.spinChartFilterAcc);
        spinAcc.setAdapter(adapterAcc);
        spinAcc.setOnItemSelectedListener(spinOnItemSelectedListener);
        spinAcc.setOnTouchListener(spinOnTouchListener);


        spinTypePeriod = (Spinner) v.findViewById(R.id.spinChartFilterTypePeriod);
        adapterTypePeriod = new ArrayAdapter<Period.Type>(context, R.layout.template_spin_filter_chart, Period.Type.values());
        adapterTypePeriod.setDropDownViewResource(R.layout.my_simple_spinner_dropdown_item);
        spinTypePeriod.setAdapter(adapterTypePeriod);
        spinTypePeriod.setOnItemSelectedListener(spinOnItemSelectedListener);

        txtDescChart = (TextView) v.findViewById(R.id.txtChartFilterDescChart);

        imgAcc = (ImageView) v.findViewById(R.id.imgChartFilterAcc);
        imgCurrency = (ImageView) v.findViewById(R.id.imgChartFilterCurrency);
        imgPayType = (ImageView) v.findViewById(R.id.imgChartFilterPayType);
        imgPeriodType = (ImageView) v.findViewById(R.id.imgChartFilterPeriodType);

        cmdView = (Button) v.findViewById(R.id.cmdChartFilterView);
        cmdView.setOnClickListener(this);
        cmdBack = (Button) v.findViewById(R.id.cmdChartFilterBack);
        cmdBack.setOnClickListener(this);

        refreshLoader(LOADER_ID, filterParams.getBundle(), loaderCallback);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshViewData();
    }


    @Override
    protected void refreshViewData() {
        cmdDtFrom.setText(Utils.dateToString(context, filterParams.getDatePeriod().dateFrom));
        cmdDtTo.setText(Utils.dateToString(context, filterParams.getDatePeriod().dateTo));
        cmdPeriod.setText(Period.encodeSelectDateRange(context, filterParams.getDatePeriod()).toString());
        spinTypeChart.setSelection(adapterTypeChart.getPosition(filterTypeChart));
        spinTypePeriod.setSelection(adapterTypePeriod.getPosition(filterParams.getTypePeriod()));
        txtDescChart.setText(filterTypeChart.getChart().getDesc());
        switchTypePayOrInterval();
        if (filterParams.isByAccount()) {
            spinAcc.setSelection(adapterAcc.getPosition(filterParams.getAccount()));
        } else {
            spinCurrency.setSelection(adapterCurrency.getPosition(filterParams.getCurrency()));
        }
        switchAccOrCurr();
    }

    LoaderManager.LoaderCallbacks<Map<Integer, Object>> loaderCallback = new LoaderManager.LoaderCallbacks<Map<Integer, Object>>() {
        @Override
        public Loader<Map<Integer, Object>> onCreateLoader(int id, Bundle args) {
            switchVisibility(true, new View[]{layProgressAcc, layProgressCurrensy}, new View[]{spinAcc, spinCurrency});
            return new BackgroundLoader(context, backgroundTaskLoad, args);
        }

        @Override
        public void onLoadFinished(Loader<Map<Integer, Object>> listLoader, Map<Integer, Object> resultParams) {
            if (listLoader.getId() == LOADER_ID) {
                List<String> currencies = (List<String>) resultParams.get(RESULT_PARAM_ID_CURRENCY);
                adapterCurrency.clear();
                for (String el : currencies) {
                    adapterCurrency.add(el);
                }
                List<Account> accounts = (List<Account>) resultParams.get(RESULT_PARAM_ID_ACCOUNTS);
                adapterAcc.reloadData(accounts);
                switchVisibility(false, new View[]{layProgressAcc, layProgressCurrensy}, new View[]{spinAcc, spinCurrency});
                refreshViewData();
            }
        }

        @Override
        public void onLoaderReset(Loader<Map<Integer, Object>> listLoader) {
        }
    };

    BackgroundTask backgroundTaskLoad = new BackgroundTask<Map<Integer, Object>>() {
        @Override
        public Map<Integer, Object> load(Bundle args) {
            Map<Integer, Object> resultParams = new HashMap<Integer, Object>();
            List<Account> accounts = new AccountDAO(context).getAll();
            for (Account el : accounts) {
                el.calcSaldo(context, new Date());
            }
            resultParams.put(RESULT_PARAM_ID_ACCOUNTS, accounts);
            List<String> currencies = Account.getUsedCurrencies(context);
            resultParams.put(RESULT_PARAM_ID_CURRENCY, currencies);
            return resultParams;
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cmdChartFilterPeriod:
                new DateRangeDialog(context, dateRangeDialogListener).show(getActivity().getSupportFragmentManager(), "");
                break;
            case R.id.cmdChartFilterDtFrom:
                setDateByDialog(cmdDtFrom, filterParams.getDatePeriod().dateFrom);
                break;
            case R.id.cmdChartFilterDtTo:
                setDateByDialog(cmdDtTo, filterParams.getDatePeriod().dateTo);
                break;
            case R.id.cmdChartFilterView:
                IChart chart = filterTypeChart.getChart();
                chart.setFilterParams(filterParams);
                MainSlidingMenu.getInstance().switchContent(new ChartViewFragment(chart));
                break;
            case R.id.cmdChartFilterBack:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }
    }

    Spinner.OnItemSelectedListener spinOnItemSelectedListener = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // эта хрень нужна т.к. при  инициализации каждого спиннера для него вызывается Spinner.OnItemSelectedListener
            if (cntSpinnerInit < CNT_SPINNER) {
                cntSpinnerInit++;
            } else {
                switch (parent.getId()) {
                    case R.id.spinChartFilterTypeChart:
                        filterTypeChart = AbstractChart.TypeChart.byIndex(position);
                        txtDescChart.setText(filterTypeChart.getChart().getDesc());
                        break;
                    case R.id.spinChartFilterPayType:
                        filterParams.setPayType(Payord.Type.fromInt(position));
                        break;
                    case R.id.spinChartFilterCurrency:
                        filterParams.setCurrency(spinCurrency.getSelectedItem().toString());
                        break;
                    case R.id.spinChartFilterAcc:
                        filterParams.setAccount(adapterAcc.getAccount(position));
                        break;
                    case R.id.spinChartFilterTypePeriod:
                        filterParams.setTypePeriod(Period.Type.fromInt(position));
                        break;
                }
                refreshViewData();
            }
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

    View.OnTouchListener spinOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                switch (v.getId()) {
                    case R.id.spinChartFilterAcc:
                        filterParams.setAccount(filterParams.getAccount());
                        break;
                    case R.id.spinChartFilterCurrency:
                        filterParams.setCurrency(filterParams.getCurrency());
                        break;
                }
                refreshViewData();
            }
            return false;
        }
    };

    private void switchAccOrCurr() {
        Utils.disableEnableControls(filterParams.isByAccount(), layAcc);
        Utils.disableEnableControls(!filterParams.isByAccount(), layCurrency);
        if (filterParams.isByAccount()) {
            imgAcc.setImageResource(R.drawable.visa);
            imgCurrency.setImageResource(R.drawable.currency_white);
        } else {
            imgAcc.setImageResource(R.drawable.visa_white);
            imgCurrency.setImageResource(R.drawable.currency);
        }

    }

    private void switchTypePayOrInterval() {
        boolean enable = (filterTypeChart == AbstractChart.TypeChart.byCategory);

        Utils.disableEnableControls(enable, layPayType);
        spinPayType.setClickable(enable);
        Utils.disableEnableControls(!enable, layPeriodType);
        spinTypePeriod.setClickable(!enable);

        if (enable) {
            imgPayType.setImageResource(R.drawable.pay_type);
            imgPeriodType.setImageResource(R.drawable.date_interval_white);
        } else {
            imgPayType.setImageResource(R.drawable.pay_type_white);
            imgPeriodType.setImageResource(R.drawable.date_interval);
        }
    }
}
