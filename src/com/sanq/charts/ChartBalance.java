package com.sanq.charts;


import com.sanq.moneys.R;
import com.sanq.utils.Cnt;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
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
public class ChartBalance extends AbstractChart {


    @Override
    public String getName() {
        return context.getResources().getString(R.string.title_chart_balance);
    }

    @Override
    public String getDesc() {
        return context.getResources().getString(R.string.title_chart_balance_desc);
    }
    @Override
    public TypeChart getTypeChart() {
        return TypeChart.Balance;
    }


    public GraphicalView getView(Map<Integer, Object> resultParams ) {
        Map<Date, Double> serData = (Map<Date, Double>)resultParams.get(RESULT_PARAM_ID_BALANCE);
        String nameChart = context.getResources().getString(R.string.title_chart_balanse) + " " ;

        if (filterParams.isByAccount()) {
            nameChart += filterParams.getAccount().getNameAndCurrency();
        } else {
            nameChart += filterParams.getCurrency();
        }

        XYMultipleSeriesRenderer multiRender = buildMultiRender();
        XYMultipleSeriesDataset chrDataSet = new XYMultipleSeriesDataset();
        XYSeries series = new XYSeries(nameChart);

        XYSeriesRenderer render = new XYSeriesRenderer();

        double maxMoney = Collections.max(serData.values());
        maxMoney += maxMoney * 0.25;
        multiRender.setYAxisMax(maxMoney);

        render.setColor(context.getResources().getColor(R.color.dark_blue));
        render.setFillPoints(true);
        render.setLineWidth(2);
        render.setPointStyle(PointStyle.CIRCLE);
        //render.setDisplayChartValues(true);

        int i = 0;
        for (double el : serData.values()) {
            series.add(i++, el);
        }

        List<Date> dates = new ArrayList<Date>();
        for (Date el : serData.keySet()) {
            dates.add(el);
        }
        addXLabels(multiRender, Cnt.CHART_MAX_X, dates);

        multiRender.addSeriesRenderer(render);
        chrDataSet.addSeries(series);
        return ChartFactory.getLineChartView(context, chrDataSet, multiRender);
    }



}
