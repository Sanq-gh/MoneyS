package test.com.sanq.core;

import android.test.AndroidTestCase;
import com.sanq.core.Report;
import com.sanq.entity.Payord;
import com.sanq.utils.DatePeriod;
import com.sanq.utils.SLog;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 03.06.13
 * Time: 17:46
 */
public class ReportTest extends AndroidTestCase{

    @Test
    public void _testGetAmountOnPeriod() throws Exception {
        int amount=-1;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -10 );
        amount = new Report(this.getContext()).getAmountOnPeriod (1, Payord.Type.CREDIT ,new DatePeriod(cal.getTime() , new Date()));
        SLog.d(String.valueOf(amount));
        assertTrue(amount > 0);

        amount = new Report(this.getContext()).getAmountOnPeriod (1, Payord.Type.DEBIT ,new DatePeriod(cal.getTime(),new Date()));
        SLog.d(String.valueOf(amount));
        assertTrue(amount > 0);

    }


    @Test
    public void _testGetSaldoOnDate() throws Exception {
        Report rep =  new Report(this.getContext());
        int res =  rep.getSaldoOnDate(1, new Date());
        SLog.d(String.valueOf(res));
        assertTrue( res > 0 );
    }


    @Test
    public void testGetAmountByCategory() throws Exception {
        Report rep =  new Report(this.getContext());
        int res =  rep.getAmountByCategory(3, 4, DatePeriod.getAllPeriod(getContext()) , Payord.Type.CREDIT) ;
        SLog.d(String.valueOf(res) );
        assertTrue( res > 0 );
    }







}
