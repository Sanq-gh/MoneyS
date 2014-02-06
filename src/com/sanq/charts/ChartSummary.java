package com.sanq.charts;


import com.sanq.moneys.R;
import com.sanq.utils.Cnt;
import com.sanq.utils.DatePeriod;
import com.sanq.utils.Utils;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: sanq
 * Date: 14.10.13
 * Time: 22:58
 * To change this template use File | Settings | File Templates.
 */
public class ChartSummary extends AbstractChart {


    @Override
    public String getName() {
        return context.getResources().getString(R.string.title_chart_summary);
    }

    @Override
    public String getDesc() {
        return context.getResources().getString(R.string.title_chart_summary_desc);
    }

    @Override
    public TypeChart getTypeChart() {
        return TypeChart.Summary;
    }


    public GraphicalView getView(Map<Integer, Object> resultParams ) {

        Map<DatePeriod, Double> serDataDebit = (Map<DatePeriod, Double>)resultParams.get(RESULT_PARAM_ID_SUMMARY_DEBIT);
        Map<DatePeriod, Double> serDataCredit = (Map<DatePeriod, Double>)resultParams.get(RESULT_PARAM_ID_SUMMARY_CREDIT);

        XYMultipleSeriesDataset chrDataSet = new XYMultipleSeriesDataset();
        XYMultipleSeriesRenderer multiRender = buildMultiRender();
        XYSeries seriesCredit = new XYSeries(context.getResources().getString(R.string.title_chart_credit));
        XYSeries seriesDebit = new XYSeries(context.getResources().getString(R.string.title_chart_debit));
        XYSeriesRenderer renderCredit = new XYSeriesRenderer();
        XYSeriesRenderer renderDebit = new XYSeriesRenderer();

        double maxMoney = Math.max(Collections.max(serDataDebit.values()), Collections.max(serDataCredit.values()));
        maxMoney += maxMoney * 0.25;
        multiRender.setYAxisMax(maxMoney);

        renderCredit.setColor(context.getResources().getColor(R.color.dark_green));
        renderCredit.setFillPoints(true);
        renderCredit.setLineWidth(2);
        //renderCredit.setDisplayChartValues(true);

        renderDebit.setColor(context.getResources().getColor(R.color.red));
        renderDebit.setFillPoints(true);
        renderDebit.setLineWidth(2);
        //debitRenderer.setDisplayChartValues(true);

        int i = 0;
        for (double el : serDataCredit.values()) {
            seriesCredit.add(i++, el);
        }

        i = 0;
        for (double el : serDataDebit.values()) {
            seriesDebit.add(i++, el);
        }

        List<Integer> indexes = Utils.averIndexes(serDataCredit.size(), Cnt.CHART_MAX_X);
        i = 0;
        for (Map.Entry<DatePeriod, Double> el : serDataCredit.entrySet()) {
            if (indexes.contains(i)) {
                multiRender.addXTextLabel(i, Utils.dateToString(context, el.getKey().dateTo));
            }
            ;
            i++;
        }


        List<Date> dates = new ArrayList<Date>();
        for (DatePeriod per : serDataDebit.keySet()) {
            dates.add(per.dateTo);
        }
        addXLabels(multiRender, Cnt.CHART_MAX_X, dates);


        multiRender.addSeriesRenderer(renderCredit);
        multiRender.addSeriesRenderer(renderDebit);
        chrDataSet.addSeries(seriesCredit);
        chrDataSet.addSeries(seriesDebit);
        return ChartFactory.getBarChartView(context, chrDataSet, multiRender, BarChart.Type.DEFAULT);

    }
}
