package test.com.sanq.core;

import android.test.AndroidTestCase;
import com.sanq.core.InvoiceAccount;
import com.sanq.core.InvoicePayord;
import com.sanq.core.Report;
import com.sanq.dao.AccountDAO;
import com.sanq.dao.PeriodDAO;
import com.sanq.dao.TransferDAO;
import com.sanq.entity.Account;
import com.sanq.entity.Payord;
import com.sanq.entity.Period;
import com.sanq.entity.Transfer;
import com.sanq.utils.SLog;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 01.06.13
 * Time: 18:01
 */
//import org.junit.runner.RunWith;
//import org.junit.runners.JUnit4;
//@RunWith(JUnit4.class)

public class InvoiceTest extends AndroidTestCase {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void _testCreatePayord() throws Exception {
        InvoiceAccount invAcc = new InvoiceAccount(this.getContext());
        InvoicePayord  invPay = new InvoicePayord(this.getContext());
        Period per = new Period("Test period",Period.Type.MONTH , 12 );

        Account acc =invAcc.createAcc("Invoice_create_acc",25000,"usd");

        Payord pay = new Payord(acc.getId(),1,0,0,"test pay invoice",Calendar.getInstance().getTime() , Payord.Type.CREDIT, 9000,false, false, null,0) ;

        pay =  invPay.createPayord(pay, per,false);
        assertNotNull(pay);
        SLog.d(pay.toString());

    }

    @Test
    public void _testEditPayord() throws Exception {
        InvoiceAccount invAcc = new InvoiceAccount(this.getContext());
        InvoicePayord  invPay = new InvoicePayord(this.getContext());
        PeriodDAO perDao =  new PeriodDAO(this.getContext());

        Account acc =invAcc.createAcc("Invoice_create_acc",15000,"usd");

        Period per = new Period("Every month", Period.Type.MONTH, 12);
        per = perDao.add(per);
        assertNotNull(per);

        Payord pay =  invPay.createPayord(acc.getId(),1,per.getId(),0,"test pay invoice" ,Calendar.getInstance().getTime(), Payord.Type.CREDIT, 5000, false,0);

        assertNotNull(pay);

        per.setCount(6);
        perDao.update(per);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1 );
        pay.setDt(cal.getTime());
        pay.setType(Payord.Type.DEBIT);

        Payord newPay = invPay.editPayord(pay , per, false );

        SLog.d("BEFORE"  + pay.toString() );
        SLog.d("AFTER"  + newPay.toString() );
        assertNotNull(newPay);
     }

    @Test
    public void testTransferBetweenAcc() throws Exception {
        InvoiceAccount invAcc = new InvoiceAccount(this.getContext());
        Period per = new Period("Test period",Period.Type.MONTH,12);

        Account accFrom =invAcc.createAcc("Invoice_create_acc", 12000, "usd");
        Account accTo = invAcc.createAcc("Invoice_create_acc", 7000, "usd");

        invAcc.transferBetweenAcc(accFrom, accTo,"test transfer", 1000, new Date(),per, true,0);



        int from =  new Report(this.getContext()).getSaldoOnDate(accFrom.getId(),new Date());
        int to =  new Report(this.getContext()).getSaldoOnDate(accTo.getId(),new Date());

        assertTrue((from == 11000) & (to == 8000));

    }

    @Test
    public void _testCreatePayordNotPermanent() throws Exception {
        SLog.d("------------------- PayordNotPermanent -------------------");
        InvoicePayord inv = new InvoicePayord(this.getContext());
        Payord pay =  inv.createPayord(1,1,0,0,"test pay invoice",Calendar.getInstance().getTime(), Payord.Type.CREDIT, 5000, false,0);
        List<Transfer> transf  = new TransferDAO(this.getContext()).getListByPayordId(pay.getId());
        assertTrue(transf.size() > 0);
        SLog.d(transf.toString());
    }

    @Test
    public void _testCreatePayordDay() throws Exception {
        addTransfer(Period.Type.DAY, 15);
    }

    @Test
    public void _testCreatePayordWeek() throws Exception {
        addTransfer(Period.Type.WEEK , 2);
    }
    @Test
    public void _testCreatePayordMonth() throws Exception {
        addTransfer(Period.Type.MONTH , 6);
    }
    @Test
    public void _testCreatePayordQuart() throws Exception {
        addTransfer(Period.Type.QUART , 2);
    }

    @Test
    public void _testCreatePayordYear() throws Exception {
        addTransfer(Period.Type.YEAR , 1);
    }

    private void addTransfer(Period.Type typePeriod , int count){

    SLog.d("------------------- " + typePeriod.toString() + " -------------------");

    Period per = new PeriodDAO(this.getContext()).add(new Period("Test period",typePeriod, count  ));
    InvoicePayord inv = new InvoicePayord(this.getContext());

    Payord pay  = inv.createPayord(1,1,per.getId(),0,"test pay invoice",Calendar.getInstance().getTime(), Payord.Type.CREDIT, 5000, false,0);
    assertNotNull(pay);

    List<Transfer> transf  = new TransferDAO(this.getContext()).getListByPayordId(pay.getId());
    assertTrue(transf.size() > 0);

    Account acc  = new AccountDAO(this.getContext()).getById(pay.getId_acc());
    assertNotNull(acc);

    SLog.d(acc.toString());
    SLog.d(pay.toString());
    SLog.d(per.toString());
    SLog.d(transf.toString());
    }
}
