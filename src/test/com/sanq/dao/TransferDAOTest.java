package test.com.sanq.dao;

import android.content.Context;
import android.test.AndroidTestCase;
import com.sanq.dao.TransferDAO;
import com.sanq.entity.Transfer;
import com.sanq.utils.SLog;
import com.sanq.utils.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sanq
 * Date: 30.05.13
 * Time: 23:44
 * To change this template use File | Settings | File Templates.
 */
public class TransferDAOTest extends AndroidTestCase {

    Context context;

    @Before
    public void setUp() throws Exception {
        context = this.getContext();
        //fill db
//     for(int i=0;i<10;i++){
//         Calendar cal = Calendar.getInstance();
//         cal.add(Calendar.DATE, i*10);
//         Transfer ent = new Transfer(i,Calendar.getInstance().getTime());
//         new TransferDAO(context).add(ent);
//     }

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void _testGetAllScheduled() throws Exception {
        Map<Integer, Date> res = Transfer.getAllScheduled(getContext(), new Date());
        for (Map.Entry<Integer, Date> el : res.entrySet()) {
            SLog.d("id = " + el.getKey().toString() + " dt  = " + Utils.dateToString(getContext(), el.getValue()));
        }
        assertTrue(res.size() > 0);
    }

    @Test
    public void _testAdd() throws Exception {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 100);
        Transfer ent = new Transfer(1, Calendar.getInstance().getTime());
        ent = new TransferDAO(context).add(ent);
        assertNotNull(ent);
    }

    @Test
    public void _testGetById() {
        int id = 0;
        Transfer Transfer = null;
        while (Transfer == null & id < 10) {
            id++;
            Transfer = new TransferDAO(context).getById(id);
        }
        SLog.d( Transfer.toString());
        assertTrue(Transfer.getId() == id);
    }


    @Test
    public void _testGetAll() throws Exception {
        List<Transfer> ents = new TransferDAO(context).getAll();
        assertTrue(ents.size() > 0);
        SLog.d(ents.toString());
    }

    @Test
    public void _testUpdate() throws Exception {
        int id = 0;
        Transfer Transfer = null;
        while (Transfer == null & id < 10) {
            id++;
            Transfer = new TransferDAO(context).getById(id);
        }
        Transfer ent = new TransferDAO(context).getById(id);
        assertNotNull(ent);
        ent.setIdPayord(2);
        ent.setDt(Calendar.getInstance().getTime());
        new TransferDAO(context).update(ent);
    }


    @Test
    public void _testDeleteById() throws Exception {
        int id = 0;
        Transfer ent = null;
        while (ent == null & id < 10) {
            id++;
            ent = new TransferDAO(context).getById(id);
        }
        assertTrue(ent.getId() == id);
        new TransferDAO(context).deleteById(id);
    }


}
