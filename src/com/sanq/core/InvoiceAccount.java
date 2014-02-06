package com.sanq.core;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import com.sanq.dao.AccountDAO;
import com.sanq.dao.PayordDAO;
import com.sanq.entity.Account;
import com.sanq.entity.Category;
import com.sanq.entity.Payord;
import com.sanq.entity.Period;
import com.sanq.moneys.R;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 01.06.13
 * Time: 13:37
 */
public class InvoiceAccount  extends InvoiceAbstract {
   private int tmp = 0;

    public InvoiceAccount(Context context) {
       super(context);
    }


    public Account createAcc(String name, int startSaldo , String currency) throws IllegalArgumentException , SQLiteException {
        tmp++;
        return new AccountDAO(context).add(new Account(name, startSaldo, currency));
    }


    public void transferBetweenAcc(Account fromAcc , Account toAcc ,String transferName, int amount,Date dt, Period per , boolean remind,float timeRemind ) {
        if (!fromAcc.getCurrency().equals(toAcc.getCurrency())){
            String mes = context.getResources().getString(R.string.mes_illegal_trans_acc);
            throw new IllegalArgumentException(mes);
        }

        try {
          if (!getDb().inTransaction()){
              getDb().beginTransaction();
          }
            int idCat = Category.getTransferCategory(context).getId();


            Payord payFrom = new Payord(fromAcc.getId(),idCat,0,0,transferName,dt, Payord.Type.DEBIT,amount, true,remind,null,timeRemind);

            Payord payTo = new Payord(toAcc.getId(),idCat,0,0,transferName,dt, Payord.Type.CREDIT,amount, true,false,null,0);

            InvoicePayord invPay = new InvoicePayord(context);

            //creating
            payFrom = invPay.createPayord(true, payFrom, per, false);
            payTo = invPay.createPayord(true, payTo, per, false);
            //linking
            payFrom.setId_link( payTo.getId());
            payTo.setId_link( payFrom.getId());
            PayordDAO payDAO = new PayordDAO(context);
            payDAO.update(payFrom);
            payDAO.update(payTo);

            //alarm setting only for fromAccount
            payFrom.setAlarm(context);

            getDb().setTransactionSuccessful();
        } catch (SQLiteException e) {
           cachSqlException(e);
        } finally {
            if (getDb().inTransaction()){
                getDb().endTransaction();
            }
        }
    }
}
















