package test.com.sanq.dao;


import android.content.Context;
import android.test.AndroidTestCase;
import com.sanq.dao.AccountDAO;
import com.sanq.entity.Account;
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
public class AccountDAOTest extends AndroidTestCase {
    Context context;

    @Before
    public void setUp()   throws Exception {
        //run before each(!) test ..
        context  = this.getContext();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetAll() throws Exception {
        List<Account> ents = new AccountDAO(context).getAll();
        assertTrue(ents.size()>0);
        SLog.d(ents.toString());
    }

    @Test
    public void testDeleteById() throws Exception {
        int id = 0 ;
        Account ent =null;
        while(ent == null & id <10){
            id++;
            ent = new AccountDAO(context).getById(id);
        }
        assertTrue(ent.getId()==id);
         new AccountDAO(context).deleteById(id);
    }


    @Test
    public void testUpdate() throws Exception {
        int id = 0 ;
        Account ent =null;
        while(ent == null & id < 10){
            id++;
            ent = new AccountDAO(context).getById(id);
        }

        ent = new AccountDAO(context).getById(id);
        ent.setName("test_update");
        ent.setSaldo(777);
        new AccountDAO(context).update(ent) ;
    }















}
