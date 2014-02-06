package com.sanq.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.sanq.charts.AbstractChart;
import com.sanq.charts.DataChart;
import com.sanq.charts.IChart;
import com.sanq.entity.Category;
import com.sanq.entity.Payord;
import com.sanq.loader.BackgroundLoader;
import com.sanq.loader.BackgroundTask;
import com.sanq.moneys.R;
import com.sanq.params.ChartFilterParams;
import com.sanq.params.FilterParams;
import com.sanq.utils.DatePeriod;
import org.achartengine.GraphicalView;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 15.07.13
 * Time: 14:22
 */
public class ChartViewFragment extends AbstractFragment implements View.OnClickListener {
    private final int LOADER_ID = 600;

//  Button cmdBack;
    IChart chart;
    LinearLayout layChart;
    LinearLayout layProgress;

    public ChartViewFragment(IChart chart) {
        this.chart = chart;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chart_view, container, false);
        layChart = (LinearLayout) v.findViewById(R.id.layChartView);
        layProgress = (LinearLayout) v.findViewById(R.id.layChartViewProgress);
//        cmdBack = (Button) v.findViewById(R.id.cmdChartViewBack);
//        cmdBack.setOnClickListener(this);
        return v;
    }

    @Override
    protected String getHeadCaption() {
        return context.getResources().getString(R.string.title_chart_view_fragment);
    }
    @Override
    protected int getHeadIcon() {
        return R.drawable.chart;
    }

    @Override
    public void onResume() {
        super.onResume();
        drawChart();
    }

    @Override
    protected void refreshViewData() {
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.cmdChartViewBack:
//                getActivity().getSupportFragmentManager().popBackStack();
//                break;
//        }
    }

    private void drawChart() {
        Bundle args = new Bundle();
        args.putInt(AbstractChart.getBundleKey(), chart.getTypeChart().toInt());
        args.putSerializable(FilterParams.getBundleKey(), chart.getFilterParams());
        refreshLoader(LOADER_ID, args, loaderCallback);
    }


    LoaderManager.LoaderCallbacks<Map<Integer, Object>> loaderCallback = new LoaderManager.LoaderCallbacks<Map<Integer, Object>>() {
        @Override
        public Loader<Map<Integer, Object>> onCreateLoader(int id, Bundle args) {
            switchVisibility(true, layProgress, layChart);
            return new BackgroundLoader(context, backgroundTaskLoad, args);
        }

        @Override
        public void onLoadFinished(Loader<Map<Integer, Object>> listLoader, Map<Integer, Object> resultParams) {
            if (listLoader.getId() == LOADER_ID) {
                //layChart.removeAllViews();
                GraphicalView  graphicalView = chart.getView(resultParams);
                layChart.addView(graphicalView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                switchVisibility(false, layProgress, layChart);
            }
        }

        @Override
        public void onLoaderReset(Loader<Map<Integer, Object>> listLoader) {
        }
    };

    BackgroundTask backgroundTaskLoad = new BackgroundTask<Map<Integer, Object>>() {
        @Override
        public Map<Integer, Object> load(Bundle args) {
            AbstractChart.TypeChart typeChart = AbstractChart.TypeChart.byIndex(args.getInt(AbstractChart.getBundleKey()));
            ChartFilterParams filterParams = (ChartFilterParams) args.getSerializable(FilterParams.getBundleKey());
            DataChart chrData = new DataChart(context);
            Map<Integer, Object> resultParams = new HashMap<Integer, Object>();
            switch (typeChart) {
                case Balance:
                    Map<Date, Double> serDataBalance;
                    if (filterParams.isByAccount()) {
                        serDataBalance = chrData.getBalance(filterParams.getAccount(), filterParams.getDatePeriod(), filterParams.getTypePeriod());
                    } else {
                        serDataBalance = chrData.getBalance(filterParams.getCurrency(), filterParams.getDatePeriod(), filterParams.getTypePeriod());
                    }
                    resultParams.put(AbstractChart.RESULT_PARAM_ID_BALANCE, serDataBalance);
                    break;
                case byCategory:
                    Map<Category, Double> serDataCategory;
                    if (filterParams.isByAccount()) {
                        serDataCategory = chrData.getAmountByGroupCategory(filterParams.getAccount(), filterParams.getDatePeriod(), filterParams.getPayType());
                    } else {
                        serDataCategory = chrData.getAmountByGroupCategory(filterParams.getCurrency(), filterParams.getDatePeriod(), filterParams.getPayType());
                    }
                    resultParams.put(AbstractChart.RESULT_PARAM_ID_CATEGORY, serDataCategory);
                    break;
                case Summary:
                    Map<DatePeriod, Double> serDataSummaryDebit;
                    Map<DatePeriod, Double> serDataSummaryCredit;

                    if (filterParams.isByAccount()) {
                        serDataSummaryDebit = chrData.getSummary(filterParams.getAccount(), filterParams.getDatePeriod(), filterParams.getTypePeriod(), Payord.Type.DEBIT);
                        serDataSummaryCredit = chrData.getSummary(filterParams.getAccount(), filterParams.getDatePeriod(), filterParams.getTypePeriod(), Payord.Type.CREDIT);
                    } else {
                        serDataSummaryDebit = chrData.getSummary(filterParams.getCurrency(), filterParams.getDatePeriod(), filterParams.getTypePeriod(), Payord.Type.DEBIT);
                        serDataSummaryCredit = chrData.getSummary(filterParams.getCurrency(), filterParams.getDatePeriod(), filterParams.getTypePeriod(), Payord.Type.CREDIT);
                    }
                    resultParams.put(AbstractChart.RESULT_PARAM_ID_SUMMARY_DEBIT, serDataSummaryDebit);
                    resultParams.put(AbstractChart.RESULT_PARAM_ID_SUMMARY_CREDIT, serDataSummaryCredit);
                    break;
            }
            return resultParams;
        }
    };
}
