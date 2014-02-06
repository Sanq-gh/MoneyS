package com.sanq.entity;

import android.content.Context;
import com.sanq.dao.*;
import com.sanq.moneys.MainSlidingMenu;
import com.sanq.moneys.R;
import com.sanq.utils.RemindManager;
import com.sanq.utils.Utils;

import java.io.Serializable;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 29.05.13
 * Time: 11:10
 * <P>
 * Payord(int id_acc, int id_cat, int id_period, Date dt, int type, int amount, boolean permanent,boolean remind, Date dt_end)
 */
public class Payord extends AbstractEntity  implements Entity , Serializable{

    public static String getBundleKey() {
        return "PAYORD";
    }


    private int id;
    private int  id_acc;
    private int id_cat;
    private int id_link = 0;
    private String name;
    private Date dt;
    private Type type;
    private int amount;
    private boolean permanent;
    private boolean remind;
    private Date dt_end;
    private int id_period;
    private boolean active = true;
    private Date dt_create;
    private Date dt_update;
    private float timeRemind;


    public Account getAccount(Context context){
        Account acc = new AccountDAO(context).getById(this.getId_acc(),true);
        return acc;
    }

    public Category getCategory(Context context){
        Category cat = new CategoryDAO(context).getById(this.getId_cat(),true);
        return cat;
    }

    public Period getPeriod(Context context){
        Period per = new PeriodDAO(context).getById(this.getId_period(),true);
        return per;
    }



    /**
     *  - expense DEBIT(0)    ;
     *  + income  CREDIT(1)   ;
     */
    public static enum Type implements  Serializable{
        //endSaldo = startSalo - DEBIT + CREDIT ;

        DEBIT(0),   //-
        CREDIT(1),  //+
        UNDEFINED(2);

        private final int ent;

        private Type(int ent) {
            this.ent = ent;
        }
        public int getInt(){
            return ent;
        }
        public static Type fromInt(int x) {
            switch(x) {
                case 0: return DEBIT;
                case 1: return CREDIT;
                case 2: return UNDEFINED;
                default:return null;
            }
        }

        @Override
        public String toString() {
            Context context  = MainSlidingMenu.getAppContext();
            switch(ent) {
                case 0: return context.getResources().getString(R.string.name_pay_type_debit);
                case 1: return context.getResources().getString(R.string.name_pay_type_credit);
                case 2: return context.getResources().getString(R.string.name_pay_type_undefined);
                default:return "";
            }
        }
        public static String getBundleKey(){
            return "PAYORD_TYPE";
        }
    }

    public Payord() {
    }

    /**
     *
     * @param id_acc
     * @param id_cat
     * @param id_period
     * @param dt
     * @param type
     * @param amount
     * @param permanent
     * @param remind
     * @param dt_end  -   calculated, only 4 read.
     */
    public Payord(int id_acc, int id_cat, int id_period,int id_link, String name, Date dt, Type type, int amount, boolean permanent,boolean remind, Date dt_end, float timeRemind) {
        this.id_acc = id_acc;
        this.id_cat = id_cat;
        this.id_link = id_link;
        this.name = name;
        this.dt = dt;
        this.type = type;
        this.amount = amount;
        this.permanent = permanent;
        this.remind = remind;
        this.dt_end = dt_end;
        this.id_period = id_period;
        this.timeRemind = timeRemind;
    }

    public void setAlarm(Context context) {
        if (this.isRemind()) {
            Map<Integer, Date> alarms = new HashMap<Integer, Date>();
            List<Transfer> transfers = new TransferDAO(context).getListByPayordId(this.getId());
            for (Transfer el : transfers) {
                 // alarm sets only for future
               Date dateAlarm =  Utils.concatTimeDate(el.getDt(), this.getTimeRemind());
                if (dateAlarm.getTime() > new Date().getTime())
                alarms.put(el.getId(),dateAlarm );
            }
            RemindManager.setAlarmTransf(context, alarms);
        }
    }

    public void clearAlarms(Context context) {
        List<Transfer> transfers = new TransferDAO(context).getListByPayordId(this.getId());
        List<Integer> alarms = new ArrayList<Integer>();
        for (Transfer el : transfers) {
            alarms.add(el.getId());
        }
        RemindManager.cancelAlarmTransf(context, alarms);
    }

    public  void delete(Context context){
        clearAlarms(context);
        Payord payLink=new PayordDAO(context).getById(this.getId_link());
        if (payLink  != null){
          payLink.clearAlarms(context);
        }
        new PayordDAO(context).deleteById(getId());
    }


    public float getTimeRemind() {
        return timeRemind;
    }

    public void setTimeRemind(float timeRemind) {
        this.timeRemind = timeRemind;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setDt_create(Date dt_create) {
        this.dt_create = dt_create;
    }

    public void setDt_update(Date dt_update) {
        this.dt_update = dt_update;
    }



    public void setId(int id) {
        this.id = id;
    }

    public boolean isRemind() {
        return remind;
    }

    public void setRemind(boolean remind) {
        this.remind = remind;
    }


    public int getId() {
        return id;
    }

    public int getId_acc() {
        return id_acc;
    }

    public int getId_cat() {
        return id_cat;
    }
    public int getId_link() {
        return id_link;
    }

    public Date getDt() {
        return dt;
    }

    public Type getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public Date getDt_end() {
        return dt_end;
    }

    public int getId_period() {
        return id_period;
    }

    public Date getDt_create() {
        return dt_create;
    }

    public Date getDt_update() {
        return dt_update;
    }

    public void setId_acc(int id_acc) {
        this.id_acc = id_acc;
    }

    public void setId_cat(int id_cat) {
        this.id_cat = id_cat;
    }
    public void setId_link(int id_link) {
        this.id_link = id_link;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    public void setDt_end(Date dt_end) {
        this.dt_end = dt_end;
    }

    public void setId_period(int id_period) {
        this.id_period = id_period;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Payord payord = (Payord) o;

        if (active != payord.active) return false;
        if (amount != payord.amount) return false;
        if (id_acc != payord.id_acc) return false;
        if (id_cat != payord.id_cat) return false;
        if (id_link != payord.id_link) return false;
        if (id_period != payord.id_period) return false;
        if (permanent != payord.permanent) return false;
        if (remind != payord.remind) return false;
        if (!dt.equals(payord.dt)) return false;
        if (!dt_end.equals(payord.dt_end)) return false;
        if (type != payord.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + id_acc;
        result = 31 * result + id_cat;
        return result;
    }

    @Override
    public String toString() {
        return "\nPayord{" +
                "\nid=" + id +
                "\n, id_acc=" + id_acc +
                "\n, id_cat=" + id_cat +
                "\n, id_link=" + id_link +
                "\n, dt=" + dt +
                "\n, type=" + type +
                "\n, amount=" + amount +
                "\n, permanent=" + permanent +
                "\n, remind=" + remind +
                "\n, dt_end=" + dt_end +
                "\n, id_period=" + id_period +
                "\n, dt_create=" + dt_create +
                "\n, dt_update=" + dt_update +
                '}';
    }
}
