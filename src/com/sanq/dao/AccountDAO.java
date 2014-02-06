package com.sanq.dao;

import android.content.Context;
import com.sanq.entity.Account;
import com.sanq.moneys.R;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 29.05.13
 * Time: 11:34
 */
public class AccountDAO extends AbstractDAO<Account>{
    private final  String tableName = "accounts";


    public AccountDAO(Context context) {
        super(context);
    }


    @Override
    protected String getTableName() {
        return tableName;
    }

    @Override
    protected Extractor<Account> getExtractor() {
        return  new AccountExtractor();
    }

    /**
     * @return unlike other, always returned -1
     */
    @Override
    public int physicsDeleteNotActive() {
        // return getDb().delete(this.getTableName(), "active = 0 and not _id in (select id_payord from template where active = 1)", new String[]{});
        getDb().execSQL(context.getString(R.string.SQL_DeleteAccountsNA));
        return -1;
    }

}
