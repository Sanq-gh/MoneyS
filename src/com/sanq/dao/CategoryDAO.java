package com.sanq.dao;

import android.content.Context;
import com.sanq.entity.Category;


/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 29.05.13
 * Time: 11:34
 */
public class CategoryDAO extends AbstractDAO<Category>{
    private final String tableName = "category";


    public CategoryDAO(Context context) {
        super(context);
    }


    @Override
    protected String getTableName() {
        return tableName;
    }

    @Override
    protected Extractor<Category> getExtractor() {
        return  new CategoryExtractor();
    }


}