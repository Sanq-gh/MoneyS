package com.sanq.dao;

import android.content.Context;
import android.database.Cursor;
import com.sanq.entity.Account;
import com.sanq.entity.Payord;
import com.sanq.moneys.R;
import com.sanq.utils.DatePeriod;
import com.sanq.utils.Utils;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 29.05.13
 * Time: 11:34
 */
public class PayordDAO extends AbstractDAO<Payord> {
    private final String tableName = "payords";

    public PayordDAO(Context context) {
        super(context);
    }

    @Override
    protected String getTableName() {
        return tableName;
    }

    @Override
    protected Extractor<Payord> getExtractor() {
        return new PayordExtractor();
    }


    /**
     * @return unlike other, always returned -1
     */
    @Override
    public int physicsDeleteNotActive() {
      // return getDb().delete(this.getTableName(), "active = 0 and not _id in (select id_payord from template where active = 1)", new String[]{});
        getDb().execSQL(context.getString(R.string.SQL_DeletePayordsNA));
        return -1;
    }


    @Deprecated
    public List<Payord> getAll(Payord.Type payType,  DatePeriod dtPer ) {
        dtPer = Utils.zeroTime(dtPer );
        dtPer.dateTo = Utils.addDays(dtPer.dateTo , 1);

        if (payType == Payord.Type.UNDEFINED) {
            return getByCondition("dt >= ? and dt < ?", new String[]{String.valueOf(dtPer.dateFrom.getTime()), String.valueOf(dtPer.dateTo.getTime())});
        } else {
            return getByCondition("type = ? and (dt >= ? and dt < ?)", new String[]{String.valueOf(payType.getInt()), String.valueOf(dtPer.dateFrom.getTime()), String.valueOf(dtPer.dateTo.getTime())});

        }
    }

    public List<Payord> getByDateTransfer(Account acc, Payord.Type payType, DatePeriod dtPer ) {
        dtPer = Utils.zeroTime(dtPer );
        dtPer.dateTo = Utils.addDays(dtPer.dateTo , 1);
        List<Payord> res;
        String strPayType = payType == Payord.Type.UNDEFINED ? "%" : String.valueOf(payType.getInt());
        String sql = context.getString(R.string.SQL_GetPayordByDateInTransfer);
        String[] params = Utils.getArrayString( String.valueOf(acc.getId()),strPayType, String.valueOf(dtPer.dateFrom.getTime()), String.valueOf(dtPer.dateTo.getTime()) );
        Cursor cur = getRawQuery(sql, params);
        res  =  getExtractor().extractAll(cur);
        cur.close();
        return res;
    }



}
