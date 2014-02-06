package test.com.sanq.dao;


import android.content.Context;
import android.test.AndroidTestCase;
import com.sanq.dao.PeriodDAO;
import com.sanq.entity.Period;
import com.sanq.utils.SLog;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sanq
 * Date: 30.05.13
 * Time: 23:44
 * To change this template use File | Settings | File Templates.
 */
public class PeriodDAOTest extends AndroidTestCase {
    Context context;

    @Before
    public void setUp()   throws Exception {
        //run before each(!) test ..
        context  = this.getContext();

        //fill db
        for(int i=0;i<2;i++){
            Period ent = new Period("Период" + i , Period.Type.MONTH , i * 2 );
            new PeriodDAO(context).add(ent);
        }

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetById(){
        int id = 0 ;
        Period ent =null;
        while(ent == null & id < 10 ){
            id++;
            ent = new PeriodDAO(context).getById(id);
        }
        SLog.d(ent.toString());
        assertTrue(ent.getId()==id);
    }
    @Test
    public void testAdd() throws Exception {
        Period ent = new Period("Тестовый период" , Period.Type.DAY, 10);
        Period ent_added = new PeriodDAO(context).add(ent);
        assertEquals(ent.getName(), ent_added.getName());
    }

    @Test
    public void testGetAll() throws Exception {
        List<Period> ents = new PeriodDAO(context).getAll();
        assertTrue(ents.size()>0);
        SLog.d(ents.toString());
    }

    @Test
    public void testDeleteById() throws Exception {
        int id = 0 ;
        Period ent =null;
        while(ent == null & id <10){
            id++;
            ent = new PeriodDAO(context).getById(id);
        }
        assertTrue(ent.getId()==id);
        new PeriodDAO(context).deleteById(id);
    }

    @Test
    public void testUpdate() throws Exception {
        int id = 0 ;
        Period ent =null;
        while(ent == null & id < 10){
            id++;
            ent = new PeriodDAO(context).getById(id);
        }

        ent = new PeriodDAO(context).getById(id);
        ent.setName("test_update");
        ent.setType(Period.Type.DAY);
        ent.setCount(99);
        new PeriodDAO(context).update(ent) ;
    }















}
