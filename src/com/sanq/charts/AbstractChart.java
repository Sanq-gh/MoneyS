package com.sanq.charts;

import android.content.Context;
import android.graphics.Paint;
import com.sanq.moneys.MainSlidingMenu;
import com.sanq.moneys.R;
import com.sanq.params.ChartFilterParams;
import com.sanq.utils.Utils;
import org.achartengine.model.*;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 17.10.13
 * Time: 12:21
 */
public abstract class AbstractChart implements IChart  {
    ///static .................................

    public static final int  RESULT_PARAM_ID_CATEGORY = 0;
    public static final int  RESULT_PARAM_ID_BALANCE = 1;
    public static final int  RESULT_PARAM_ID_SUMMARY_DEBIT = 2;
    public static final int  RESULT_PARAM_ID_SUMMARY_CREDIT = 3;


    public static String getBundleKey() {
        return "CHART";
    }

    public enum TypeChart {
        Balance(new ChartBalance()), Summary(new ChartSummary()),  byCategory(new ChartCategory());

        private IChart ent;

        TypeChart(IChart chart) {
            this.ent = chart;
        }

        public int toInt() {
            switch (ent.getTypeChart()) {
                case Balance : return 0;
                case Summary: return 1;
                case byCategory: return 2;
                default: return -1;
            }
        }

        public IChart getChart() {
            return ent;
        }

        @Override
        public String toString() {
            return ent.getName();
        }

        public static IChart chartByIndex(int x) {
            switch (x) {
                case 0:
                    return new ChartBalance();
                case 1:
                    return new ChartSummary();
                case 2:
                    return new ChartCategory();

                default:
                    return null;
            }
        }

        public static TypeChart byIndex(int x) {
            switch (x) {
                case 0:
                    return Balance;
                case 1:
                    return Summary;
                case 2:
                    return byCategory;
                default:
                    return null;
            }
        }
    }

    ///........................................


    Context context;
    ChartFilterParams filterParams;

    protected AbstractChart() {
        this.context = MainSlidingMenu.getAppContext();
    }

    public ChartFilterParams getFilterParams() {
        return filterParams;
    }

    public void setFilterParams(ChartFilterParams filterParams) {
        this.filterParams = filterParams;
    }


    protected String getXCaption() {
        return context.getResources().getString(R.string.title_chart_x_caption);
    }

    protected String getYCaption() {
        return context.getResources().getString(R.string.title_chart_y_caption);
    }




    protected XYMultipleSeriesRenderer buildMultiRender() {
        XYMultipleSeriesRenderer multiRender = new XYMultipleSeriesRenderer();
        multiRender.setMarginsColor(context.getResources().getColor(R.color.xxlight_blue));
        multiRender.setXLabelsColor(context.getResources().getColor(R.color.black));
        multiRender.setYLabelsColor(0, context.getResources().getColor(R.color.black));
        multiRender.setAxesColor(context.getResources().getColor(R.color.black));
        multiRender.setLabelsColor(context.getResources().getColor(R.color.black));
        multiRender.setChartTitle(getDesc());
        multiRender.setLabelsColor(context.getResources().getColor(R.color.dark_blue));
        multiRender.setZoomButtonsVisible(true);
        multiRender.setXLabelsAlign(Paint.Align.LEFT);
        multiRender.setYLabelsAlign(Paint.Align.CENTER);
        multiRender.setAxisTitleTextSize(10);
        multiRender.setChartTitleTextSize(20);
        multiRender.setLabelsTextSize(10);
        multiRender.setLegendTextSize(10);
        multiRender.setBarSpacing(0.5f);
        multiRender.setXLabelsAngle(45);
        multiRender.setShowGridY(true);
        multiRender.setShowGridX(true);
        multiRender.setMargins(new int[] {30, 30, 20, 0});

        multiRender.setXLabels(0);
        return multiRender;
    }




    protected void  addXLabels (XYMultipleSeriesRenderer mRender, int maxCountIndex,List<Date> dateXValues){

        List<Integer> indexes = Utils.averIndexes(dateXValues.size(), maxCountIndex);
        int i=0;
        for (Date el : dateXValues) {
            if (indexes.contains(i)){
                mRender.addXTextLabel(i, Utils.dateToString(context, dateXValues.get(i)) );
            };
            i++;
        }
    }






    //.................................. from example


    /**
     * Builds an XY multiple dataset using the provided values.
     *
     * @param titles  the series titles
     * @param xValues the values for the X axis
     * @param yValues the values for the Y axis
     * @return the XY multiple dataset
     */

    protected XYMultipleSeriesDataset buildDataset(String[] titles, List<double[]> xValues, List<double[]> yValues) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        addXYSeries(dataset, titles, xValues, yValues, 0);
        return dataset;
    }

    public void addXYSeries(XYMultipleSeriesDataset dataset, String[] titles, List<double[]> xValues,
                            List<double[]> yValues, int scale) {
        int length = titles.length;
        for (int i = 0; i < length; i++) {
            XYSeries series = new XYSeries(titles[i], scale);
            double[] xV = xValues.get(i);
            double[] yV = yValues.get(i);
            int seriesLength = xV.length;
            for (int k = 0; k < seriesLength; k++) {
                series.add(xV[k], yV[k]);
            }
            dataset.addSeries(series);
        }
    }

    /**
     * Builds an XY multiple series renderer.
     *
     * @param colors the series rendering colors
     * @param styles the series point styles
     * @return the XY multiple series renderers
     */
//    protected XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles) {
//        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
//        setRenderer(renderer, colors, styles);
//        return renderer;
//    }
//
//    protected void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors, PointStyle[] styles) {
//        renderer.setAxisTitleTextSize(16);
//        renderer.setChartTitleTextSize(20);
//        renderer.setLabelsTextSize(15);
//        renderer.setLegendTextSize(15);
//        renderer.setPointSize(5f);
//        renderer.setMargins(new int[]{5, 50, 5, 5});
//        int length = colors.length;
//        for (int i = 0; i < length; i++) {
//            XYSeriesRenderer r = new XYSeriesRenderer();
//            r.setColor(colors[i]);
//            r.setPointStyle(styles[i]);
//            renderer.addSeriesRenderer(r);
//        }
//    }

    /**
     * Sets a few of the series renderer settings.
     *
     * @param renderer    the renderer to set the properties to
     * @param title       the chart_view title
     * @param xTitle      the title for the X axis
     * @param yTitle      the title for the Y axis
     * @param xMin        the minimum value on the X axis
     * @param xMax        the maximum value on the X axis
     * @param yMin        the minimum value on the Y axis
     * @param yMax        the maximum value on the Y axis
     * @param axesColor   the axes color
     * @param labelsColor the labels color
     */
    protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
                                    String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor,
                                    int labelsColor) {
        renderer.setChartTitle(title);
        renderer.setXTitle(xTitle);
        renderer.setYTitle(yTitle);
        renderer.setXAxisMin(xMin);
        renderer.setXAxisMax(xMax);
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        renderer.setAxesColor(axesColor);
        renderer.setLabelsColor(labelsColor);
    }

    /**
     * Builds an XY multiple time dataset using the provided values.
     *
     * @param titles  the series titles
     * @param xValues the values for the X axis
     * @param yValues the values for the Y axis
     * @return the XY multiple time dataset
     */
    protected XYMultipleSeriesDataset buildDateDataset(String[] titles, List<Date[]> xValues,
                                                       List<double[]> yValues) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        int length = titles.length;
        for (int i = 0; i < length; i++) {
            TimeSeries series = new TimeSeries(titles[i]);
            Date[] xV = xValues.get(i);
            double[] yV = yValues.get(i);
            int seriesLength = xV.length;
            for (int k = 0; k < seriesLength; k++) {
                series.add(xV[k], yV[k]);
            }
            dataset.addSeries(series);
        }
        return dataset;
    }

    /**
     * Builds a category series using the provided values.
     *
     * @param title  the series titles
     * @param values the values
     * @return the category series
     */
    protected CategorySeries buildCategoryDataset(String title, double[] values) {
        CategorySeries series = new CategorySeries(title);
        int k = 0;
        for (double value : values) {
            series.add("Project " + ++k, value);
        }

        return series;
    }

    /**
     * Builds a multiple category series using the provided values.
     *
     * @param titles the series titles
     * @param values the values
     * @return the category series
     */
    protected MultipleCategorySeries buildMultipleCategoryDataset(String title,
                                                                  List<String[]> titles, List<double[]> values) {
        MultipleCategorySeries series = new MultipleCategorySeries(title);
        int k = 0;
        for (double[] value : values) {
            series.add(2007 + k + "", titles.get(k), value);
            k++;
        }
        return series;
    }

    /**
     * Builds a category renderer to use the provided colors.
     *
     * @param colors the colors
     * @return the category renderer
     */
    protected DefaultRenderer buildCategoryRenderer(int[] colors) {
        DefaultRenderer renderer = new DefaultRenderer();
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        renderer.setMargins(new int[]{20, 30, 15, 0});
        for (int color : colors) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(color);
            renderer.addSeriesRenderer(r);
        }
        return renderer;
    }

    /**
     * Builds a bar multiple series dataset using the provided values.
     *
     * @param titles the series titles
     * @param values the values
     * @return the XY multiple bar dataset
     */
    protected XYMultipleSeriesDataset buildBarDataset(String[] titles, List<double[]> values) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        int length = titles.length;
        for (int i = 0; i < length; i++) {
            CategorySeries series = new CategorySeries(titles[i]);
            double[] v = values.get(i);
            int seriesLength = v.length;
            for (int k = 0; k < seriesLength; k++) {
                series.add(v[k]);
            }
            dataset.addSeries(series.toXYSeries());
        }
        return dataset;
    }

    /**
     * Builds a bar multiple series renderer to use the provided colors.
     *
     * @param colors the series renderers colors
     * @return the bar multiple series renderer
     */
    protected XYMultipleSeriesRenderer buildBarRenderer(int[] colors) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setAxisTitleTextSize(16);
        renderer.setChartTitleTextSize(20);
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        int length = colors.length;
        for (int i = 0; i < length; i++) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(colors[i]);
            renderer.addSeriesRenderer(r);
        }
        return renderer;
    }


}
