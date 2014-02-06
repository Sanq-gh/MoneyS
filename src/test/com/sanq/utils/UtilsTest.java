package test.com.sanq.utils;

import android.test.AndroidTestCase;
import com.sanq.dao.CategoryDAO;
import com.sanq.entity.Category;
import com.sanq.entity.Period;
import com.sanq.exception.DateCheckedException;
import com.sanq.moneys.R;
import com.sanq.utils.DatePeriod;
import com.sanq.utils.SLog;
import com.sanq.utils.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sanq
 * Date: 31.05.13
 * Time: 0:28
 * To change this template use File | Settings | File Templates.
 */
public class UtilsTest extends AndroidTestCase {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void _testNullString() {
        String s = null;

        SLog.d("result - " + s);
    }




    @Test
    public void _testEncodeSelectDateRange() throws DateCheckedException {
        DatePeriod dtPeriod = new DatePeriod("01.11.2013", "30.11.2013", "dd.mm.yyyy");
        Period.ReportType res = Period.encodeSelectDateRange(getContext(), dtPeriod);
        SLog.d("result - " + res);
    }


    @Test
    public void _testCatsHash() {
        List<Category> cats = new CategoryDAO(getContext()).getAll();
        for (Category cat : cats) {
            SLog.d( cat.getName() + ": " + cat.hashCode());
        }
    }


    @Test
    public void _testAverIndexes() {
        List<Integer> res = Utils.averIndexes(100, 10);
        for (Integer re : res) {
            SLog.d( re.toString() + " ");
        }
    }


    @Test
    public void _testMoneyToInt() {
        int res = Utils.moneyToInt(getContext(),"80.2");
        SLog.d(  String.valueOf(res));
    }

    @Test
    public void _testMoneyIntToDouble() {
        Double res = Utils.moneyIntToDouble(1000);
        SLog.d( res.toString());
    }


    @Test
    public void _testZeroTime() {

        SLog.d( (new Date()).toString());
        SLog.d( Utils.zeroTime(new Date()).toString());
    }


    @Test
    public void _testTimeToDate() throws DateCheckedException {
        SLog.d( Utils.timeToDate("05:01").toString());
    }

    @Test
    public void _testTimeToFloat() throws DateCheckedException {
        SLog.d( String.valueOf(Utils.timeToFloat("05:01")));
    }

    @Test
    public void _testFloatToTime() {
        SLog.d( Utils.floatToTime(12.15f));
    }

    @Test
    public void _testGetSystemDelimiter() {
        SLog.d( String.valueOf(Utils.getSystemDelimiter()));
    }


//    @Test
//    public void _testGetTxtFile() {
//        String sql = Utils.getTxtFile(this.getContext(), R.raw.data);
//        assertTrue(sql.length() > 100);
//        SLog.d( sql);
//    }

    @Test
    public void _testGetTxtFileAsList() {
        List<String> sql = Utils.getSqlAsList(this.getContext(), R.raw.meta);
        assertTrue(sql.size() > 0);
        for (String el : sql) {
            SLog.d( el);
        }
    }
}
