package com.sanq.dao;

import android.content.Context;
import com.sanq.entity.Transfer;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 29.05.13
 * Time: 11:34
 */
public class TransferDAO extends AbstractDAO<Transfer>{
    private final  String tableName = "transfer";

    public TransferDAO(Context context) {
        super(context);
    }

    @Override
    protected String getTableName() {
        return tableName;
    }

    @Override
    protected Extractor<Transfer> getExtractor() {
        return  new TransferExtractor();
    }

    public List<Transfer> getListByPayordId(long idPayord) {
        return getByCondition("id_payord = ?", new String[]{String.valueOf(idPayord)});
    }



}
