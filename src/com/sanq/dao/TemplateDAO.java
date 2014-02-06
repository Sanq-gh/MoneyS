package com.sanq.dao;

import android.content.Context;
import com.sanq.entity.Template;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 29.05.13
 * Time: 11:34
 */
public class TemplateDAO extends AbstractDAO<Template>{
    private final  String tableName = "template";

    public TemplateDAO(Context context) {
        super(context);
    }

    @Override
    protected String getTableName() {
        return tableName;
    }

    @Override
    protected Extractor<Template> getExtractor() {
        return  new TemplateExtractor();
    }

}
