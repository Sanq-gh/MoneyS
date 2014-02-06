package test.com.sanq.utils;

import android.test.AndroidTestCase;
import com.sanq.entity.Transfer;
import com.sanq.utils.RemindManager;
import com.sanq.utils.SLog;
import com.sanq.utils.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sanq
 * Date: 23.11.13
 * Time: 13:19
 * To change this template use File | Settings | File Templates.
 */
public class AlarmTest extends AndroidTestCase {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }


    @Test
    public void _testAramTransfer() {
        Map<Integer, Date> alarms = Transfer.getAllScheduled(getContext(), new Date());

        for (Map.Entry<Integer, Date> el : alarms.entrySet()) {
            String mes = "";
            if (RemindManager.isAlarmTransfSet(getContext(), el.getKey())) {
                mes = "set:       " + el.getKey() + "  " + Utils.dateToString(getContext(), el.getValue());
            } else {
                mes = "not found: " + el.getKey();
            }
            SLog.d(mes);
        }
        //      assertTrue(true);
   }

}
