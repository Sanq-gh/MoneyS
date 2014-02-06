package test.com.sanq.dao;


import android.test.AndroidTestCase;
import com.sanq.dao.PayordDAO;
import com.sanq.entity.Payord;
import com.sanq.utils.SLog;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sanq
 * Date: 30.05.13
 * Time: 23:44
 * To change this template use File | Settings | File Templates.
 */
public class PayordDAOTest extends AndroidTestCase {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testAdd() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 100);
        Payord pay = new Payord(1, 1,1,0, "Test pay" ,Calendar.getInstance().getTime(), Payord.Type.CREDIT, 100, true,false, cal.getTime(),0);
        pay = new PayordDAO(this.getContext()).add(pay);
        assertNotNull(pay);
    }

    @Test
    public void testGetById(){
        int id = 0 ;
        Payord Payord =null;
        while(Payord == null & id<10){
            id++;
            Payord = new PayordDAO(this.getContext()).getById(id);
        }
        SLog.d(Payord.toString());
        assertTrue(Payord.getId()==id);
    }

    @Test
    public void testGetAll() throws Exception {
        List<Payord> entities = new PayordDAO(this.getContext()).getAll();
        assertTrue(entities.size()>0);
        SLog.d(entities.toString());
    }

    @Test
    public void testUpdate() throws Exception {
        Payord pay = new Payord(1, 1, 0, 0,"Test pay", Calendar.getInstance().getTime(), Payord.Type.CREDIT, 100, true,false, new Date(),0);
        assertNotNull(pay);
        Payord entity = new PayordDAO(this.getContext()).add(pay);
        assertNotNull(entity);
        entity.setAmount(999);
        entity.setDt(Calendar.getInstance().getTime());
        new PayordDAO(this.getContext()).update(entity);
        Payord udpatedPay = new PayordDAO(this.getContext()).getById(entity.getId());
        assertTrue( udpatedPay.equals(entity));
    }


    @Test
    public void testDeleteById() throws Exception {
        int id = 0 ;
        Payord Payord =null;
        while(Payord == null & id<10){
            id++;
            Payord = new PayordDAO(this.getContext()).getById(id);
        }
        assertTrue(Payord.getId()==id);
        new PayordDAO(this.getContext()).deleteById(id);
    }


}
