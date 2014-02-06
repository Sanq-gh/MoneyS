package com.sanq.dao;

import android.content.Context;
import com.sanq.entity.Period;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 29.05.13
 * Time: 11:34
 */
public class PeriodDAO extends AbstractDAO<Period>{
    private final String tableName = "period";

    public PeriodDAO(Context context) {
        super(context);
    }


    @Override
    protected String getTableName() {
        return tableName;
    }

    @Override
    protected Extractor<Period> getExtractor() {
        return  new PeriodExtractor();
    }


}
