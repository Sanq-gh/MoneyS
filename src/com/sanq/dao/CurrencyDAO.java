package com.sanq.dao;

import android.content.Context;
import com.sanq.entity.Currency;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 29.05.13
 * Time: 11:34
 */
@Deprecated
public class CurrencyDAO extends AbstractDAO<Currency>{
    private final  String tableName = "currency";
    public CurrencyDAO(Context context) {
        super(context);
    }

    @Override
    protected String getTableName() {
        return tableName;
    }

    @Override
    protected Extractor<Currency> getExtractor() {
        return  new CurrencyExtractor();
    }

}
