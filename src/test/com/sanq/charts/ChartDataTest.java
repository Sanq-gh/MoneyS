package test.com.sanq.charts;

import android.test.AndroidTestCase;
import com.sanq.charts.DataChart;
import com.sanq.entity.Account;
import com.sanq.entity.Payord;
import com.sanq.entity.Period;
import com.sanq.utils.DatePeriod;
import com.sanq.utils.Preferences;
import com.sanq.utils.SLog;
import org.junit.Test;

import java.util.Date;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sanq
 * Date: 15.10.13
 * Time: 22:00
 * To change this template use File | Settings | File Templates.
 */
public class ChartDataTest extends AndroidTestCase {
    @Test
    public void testGetBalance() throws Exception {
        DataChart chrData = new DataChart(this.getContext());
        Account acc = new Preferences(getContext()).getDefaultAcc();

       Map<Date, Double> serData  = chrData.getBalance(acc, new DatePeriod("01.10.2013", "16.10.2013", "dd.MM.yyyy"), Period.Type.DAY);
        for (Map.Entry el : serData.entrySet()) {
            SLog.d(el.getKey().toString() + "  " + el.getValue().toString());
        }
        SLog.d(String.valueOf(serData.size()));
    }

    @Test
    public void _testGetSummary() throws Exception {
        DataChart chrData = new DataChart(this.getContext());
        Account acc =new Preferences(getContext()).getDefaultAcc();
        Map<DatePeriod, Double> serData  = chrData.getSummary(acc, new DatePeriod("01.10.2013", "01.12.2013", "dd.MM.yyyy"), Period.Type.MONTH, Payord.Type.CREDIT);
        for (Map.Entry el : serData.entrySet()) {
            SLog.d(el.getKey().toString() + "  "  +  el.getValue()   );
        }
        SLog.d(String.valueOf(serData.size()));
    }

}
