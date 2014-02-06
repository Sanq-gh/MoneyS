package test.com.sanq.dao;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.test.AndroidTestCase;
import com.sanq.dao.AccountDAO;
import com.sanq.dao.PayordDAO;
import com.sanq.ds.DbHelper;
import com.sanq.entity.Account;
import com.sanq.entity.Payord;
import com.sanq.utils.SLog;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 31.05.13
 * Time: 10:46
 */

public class TransactionDAOTest extends AndroidTestCase{
    private Context context;

    @Before
    public void setUp() throws Exception {
        context  = this.getContext();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testAdd() throws Exception {
        SQLiteDatabase db = null;
        try {
            db = new DbHelper(context).getWritableDatabase() ;
            db.beginTransaction();

            AccountDAO acc_dao = new AccountDAO(context);
            Account acc = new Account("З/П карта", 1500 , "RUB");
            acc = acc_dao.add(acc);
            assertNotNull(acc);


            PayordDAO pay_dao = new PayordDAO(context);

//todo: insert here exception ...
//if ( true) {throw new SQLiteException("test exception ...");}

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 100);
            Payord pay = new Payord(acc.getId(), 1 ,1,0,"test Transaction Pay" ,Calendar.getInstance().getTime(), Payord.Type.CREDIT, 100, true,false, cal.getTime(),0);
            pay = pay_dao.add(pay);
            assertNotNull(pay);

            SLog.d(pay.toString());

            db.setTransactionSuccessful();

        } catch (SQLiteException ex) {
            SLog.d(ex.getMessage());
        } finally {
            if (db != null && db.inTransaction() && db.isOpen()) {
                db.endTransaction();
                db.close();
            }
        }
    }
}
