package com.sanq.charts;

import com.sanq.entity.Category;
import com.sanq.moneys.R;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import java.util.Map;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 30.10.13
 * Time: 11:44
 */
public class ChartCategory extends AbstractChart {


    @Override
    public String getName() {
        return context.getResources().getString(R.string.title_chart_category);
    }

    @Override
    public String getDesc() {
        return context.getResources().getString(R.string.title_chart_category_desc);
    }


    @Override
    public TypeChart getTypeChart() {
        return TypeChart.byCategory;
    }

    @Override
    public GraphicalView getView(Map<Integer, Object> resultParams ) {
        Map<Category, Double> serData = (Map<Category, Double>)resultParams.get(RESULT_PARAM_ID_CATEGORY);
        String nameChart = context.getResources().getString(R.string.title_chart_category) + " - " + filterParams.getPayType()+"\n" ;
        if (filterParams.isByAccount()) {
            nameChart +=  filterParams.getAccount().getNameAndCurrency();
        } else {
            nameChart += filterParams.getCurrency();
        }

        XYMultipleSeriesRenderer multiRender = buildMultiRender();
        multiRender.setChartTitle(nameChart);
        multiRender.setDisplayValues(true);

         //XYMultipleSeriesDataset chrDataSet = new XYMultipleSeriesDataset();
        CategorySeries series = new CategorySeries(nameChart);

        Random rnd = new Random();
        for (Category category : serData.keySet()) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(category.hashCodeInteger());
            multiRender.addSeriesRenderer(r);
        }

        // ??????????????????????
//        SimpleSeriesRenderer r = multiRender.getSeriesRendererAt(0);
//        r.setGradientEnabled(true);
//        r.setGradientStart(0, Color.BLUE);
//        r.setGradientStop(0, Color.GREEN);
//        r.setHighlighted(true);

        int i = 0;
        for (Map.Entry<Category, Double> el : serData.entrySet()) {
            series.add(el.getKey().getName(),el.getValue());
        }
        return ChartFactory.getPieChartView(context, series, multiRender);

    }


}
