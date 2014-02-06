package com.sanq.core;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import com.sanq.dao.PayordDAO;
import com.sanq.dao.PeriodDAO;
import com.sanq.dao.TemplateDAO;
import com.sanq.dao.TransferDAO;
import com.sanq.entity.Payord;
import com.sanq.entity.Period;
import com.sanq.entity.Template;
import com.sanq.entity.Transfer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 01.06.13
 * Time: 13:37
 */
public class InvoicePayord extends InvoiceAbstract{


    public InvoicePayord(Context context) {
        super(context);
    }


    public Payord editPayord(Payord payEdited) {
        return editPayord(false,payEdited,null, false ) ;
    }

    public Payord editPayord(Payord payEdited,  Period perEdited ,boolean  saveAsTemplate) {
        return editPayord(false,payEdited,perEdited, saveAsTemplate) ;
        }

    /**
     * @param inSuperTrans
     * @param payEdited  must exists in DB.
     *                   if change dashboard payord params:  deleting old payord (active = 0) and adding new Payord;
     * @param perEdited  if null - period still the same.
     * @return
     */
    public Payord editPayord(boolean inSuperTrans, Payord payEdited, Period perEdited , boolean  saveAsTemplate) {
        Payord res = null;

        Payord payDb  = new PayordDAO(context).getById(payEdited.getId());
        if (payDb==null){
            throw new IllegalArgumentException("Payord id='" + payEdited.getId() + "' not found.");
        }

        //если поменялись параметры, влияющие на рсчет  или  передали период - старую грохаем, новую добавялем
        //(если период пустой - значит не менялся)
        if ((!payEdited.getDt().equals(payDb.getDt()))
                | (payEdited.getType() != payDb.getType())
                | (perEdited != null)) {
            try {
                if (!inSuperTrans) {
                    if (!getDb().inTransaction()){
                        getDb().beginTransaction();
                    }
                }
                new PayordDAO(context).delete(payEdited);

//                if (payEdited!=null){
//                     payEdited.setId_period(perEdited.getId());
//                }

                res = this.createPayord(true, payEdited, perEdited ,saveAsTemplate);

                if (!inSuperTrans) {
                    if (getDb().inTransaction()){
                        getDb().setTransactionSuccessful();
                    }
                }
            } catch (SQLiteException e) {
                cachSqlException(e);
            } finally {
                if (!inSuperTrans) {
                    if (getDb().inTransaction()){
                        getDb().endTransaction();
                    }
                }
            }
        } else {
            PayordDAO pDao = new PayordDAO(context);
            pDao.update(payEdited);
            res = pDao.getById(payEdited.getId());

            if (saveAsTemplate){
                Template templ = new Template(res.getName(),res.getId());
                new TemplateDAO(context).add(templ);
            }

        }
        return res;
    }


    public Payord copyFrom(Payord pay){
        return createPayord(pay.getId_acc(), pay.getId_cat(), pay.getId_period(),pay.getId_link() ,pay.getName(), pay.getDt(), pay.getType(), pay.getAmount(), pay.isRemind(),pay.getTimeRemind()) ;
    }

    public Payord copyFrom(int id_payord){
        return copyFrom(new PayordDAO(context).getById(id_payord));
    }

    public Payord createPayord(Payord pay, Period per, boolean saveAsTemplate) {
       return createPayord(false, pay, per, saveAsTemplate);
    }

    public Payord createPayord(boolean  inSuperTrans, Payord pay, Period per, boolean saveAsTemplate) {
        Payord res = null;
        try {
            if (!inSuperTrans) {
                if (!getDb().inTransaction()) {
                    getDb().beginTransaction();
                }
            }
            if (per != null) {
                per = new PeriodDAO(context).add(per);
                if (per == null) {
                    throw new SQLiteException("Error create 'Period' entity.");
                }
            } else {
                per = new Period();
                per.setId(0);
            }
            res = createPayordInner(true , pay.getId_acc(), pay.getId_cat(), per.getId(),pay.getId_link() ,pay.getName(), pay.getDt(),pay.getType(), pay.getAmount(),pay.isRemind(), saveAsTemplate,pay.getTimeRemind()) ;
            if (!inSuperTrans) {
                if (getDb().inTransaction()){
                    getDb().setTransactionSuccessful();
                }
            }
        } catch (SQLiteException e) {
            cachSqlException(e);
        } finally {
            if (!inSuperTrans) {
                if (getDb().inTransaction()){
                    getDb().endTransaction();
                }
            }
        }
        return res;
    }




    public Payord createPayord( int id_acc, int id_cat, int id_period, int id_link, String name , Date dt, Payord.Type payType, int amount, boolean remind, float timeRemind) {
        return createPayordInner(false, id_acc, id_cat, id_period, id_link , name , dt, payType, amount, remind,false, timeRemind) ;
        }

    public Payord createPayord(Payord pay, boolean saveAsTemplate) {
        return createPayordInner(false, pay.getId_acc(), pay.getId_cat(), pay.getId_period(),pay.getId_link() ,pay.getName(), pay.getDt(), pay.getType(), pay.getAmount(), pay.isRemind(),saveAsTemplate,pay.getTimeRemind());
    }

    public Payord createPayord(boolean inSuperTrans, Payord pay,boolean saveAsTemplate) {
        return createPayordInner(inSuperTrans, pay.getId_acc(), pay.getId_cat(), pay.getId_period(), pay.getId_link() , pay.getName(), pay.getDt(), pay.getType(), pay.getAmount(), pay.isRemind(), saveAsTemplate,pay.getTimeRemind());
    }

    public void createPayord(boolean inSuperTrans , int id_acc, int id_cat, int id_period, int id_link ,String name ,Date dt, Payord.Type payType, int amount, boolean remind, float timeRemind) {
        createPayordInner(inSuperTrans, id_acc, id_cat, id_period, id_link , name ,dt, payType, amount, remind, false,timeRemind ) ;
    }

    /**
     * @param inSuperTrans        if null then DB in finally closes;
     * @param id_acc    int
     * @param id_cat    int
     * @param id_period int
     * @param dt        Date
     * @param payType   Payord.Type
     * @param amount    int
     * @param remind    boolean
     * @return          'null' or created payord
     */
        private Payord createPayordInner(boolean inSuperTrans, int id_acc, int id_cat, int id_period,int id_link, String name , Date dt, Payord.Type payType, int amount, boolean remind ,boolean saveAsTemplate, float timeRemind) {
            Payord res = null;

            Payord pay = new Payord(id_acc, id_cat, id_period, id_link, name , dt, payType, amount, false, remind, dt, timeRemind);

        //calc period
        Period per = null;
        if (id_period < 1) {
            //if not permanent simply adding payord
            pay.setDt_end(pay.getDt());
            pay.setPermanent(false);
            per = new Period("Fake period", Period.Type.DAY, 1);

        } else {
            // adding payord & depending on  period type - create list of transfers
            per = new PeriodDAO(context ).getById(id_period);
            if (per == null) {
                throw new IllegalArgumentException("Period with id='" + id_period + "' d't exists.");
            }
            //calc & set end_date
            pay.setPermanent(true);
            pay.setDt_end(Period.calcNextDate(pay.getDt(), per.getType(), per.getCount()));
        }

        // create list transfers by period
        List<Transfer> transfers = new ArrayList<Transfer>();
        if (per.getCount() > 0) {
            transfers.add(new Transfer(0, pay.getDt()));
            Date prevDate = pay.getDt();
            for (int i = 0; i < per.getCount() - 1; i++) {
                Transfer trans = new Transfer();
                trans.setDt(Period.calcNextDate(prevDate, per.getType()));
                prevDate = trans.getDt();
                transfers.add(trans);
            }
        }




            try {
                if (!inSuperTrans) {
                    if (!getDb().inTransaction()){
                        getDb().beginTransaction();
                    }
                }

                pay = new PayordDAO(context).add(pay);


                if (saveAsTemplate){
                    Template templ = new Template(pay.getName(),pay.getId());
                   new TemplateDAO(context).add(templ);
                }


                for (Transfer transfer : transfers) {
                    transfer.setIdPayord(pay.getId());
                }

               new TransferDAO(context).addAll(transfers);
                if (!inSuperTrans) {
                    if (getDb().inTransaction()){
                        getDb().setTransactionSuccessful();
                    }
                }
                res = pay;
            } catch (SQLiteException ex) {
                cachSqlException(ex);
            } finally {
                if (!inSuperTrans) {
                    if (getDb().inTransaction()){
                        getDb().endTransaction();
                    }
                }
            }
            return res;
        }

}
















