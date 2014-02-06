package com.sanq.charts;

import com.sanq.params.ChartFilterParams;
import org.achartengine.GraphicalView;

import java.io.Serializable;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 17.10.13
 * Time: 12:22
 */
public interface IChart extends Serializable {

     /**
     * @return the chart_view name
     */
    String getName();

    /**
     * @return the chart_view description
     */
    String getDesc();

    /**
     * @return the built view
     */
    GraphicalView getView(Map<Integer, Object> resultParams );

    void setFilterParams(ChartFilterParams filterParams);

    ChartFilterParams getFilterParams();


    AbstractChart.TypeChart  getTypeChart();

}
