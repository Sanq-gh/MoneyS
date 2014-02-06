package com.sanq.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.sanq.entity.Account;
import com.sanq.entity.Category;
import com.sanq.entity.Payord;
import com.sanq.entity.Template;
import com.sanq.moneys.R;
import com.sanq.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sanq
 * Date: 19.06.13
 * Time: 21:57
 * To change this template use File | Settings | File Templates.
 */
public class TemplateAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layInflater ;
    List<Template> templs;

        public TemplateAdapter(Context context) {
        this.context = context;
        layInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        templs = new ArrayList<Template>();
        }

        public void reloadData(List<Template> templates){
            templs = templates;
            Collections.sort(templs, DATE_COMPARATOR);
            notifyDataSetChanged();
        }

    public static final Comparator<Template> DATE_COMPARATOR = new Comparator<Template>() {
        @Override
        public int compare(Template object1, Template object2) {
            if (object2.getDt_create().getTime() < object1.getDt_create().getTime()) {
                return   -1;
            }else if (object2.getDt_create().getTime() > object1.getDt_create().getTime()){
                return   1;
            }else{
                return  0;
            }
        }
    };

    public Template getTemplate (int pos ){
        if (  pos >= 0 && pos < templs.size()){
            return templs.get(pos);
        }else{
            return  null;
        }
    }


    @Override
    public int getCount() {
        return templs.size();
    }

    @Override
    public Object getItem(int position) {
        return templs.get(position).getName();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            v = this.layInflater.inflate(R.layout.template_row_template, null);
        }

        TextView txtAccName = (TextView)v.findViewById(R.id.rowTemplAccName);
        TextView txtSaldo = (TextView)v.findViewById(R.id.rowTemplSaldo);
        TextView txtPayName = (TextView)v.findViewById(R.id.rowTemplPayName);
        TextView txtPayCat = (TextView)v.findViewById(R.id.rowTemplPayCat);

        ImageView imgRepeat =   (ImageView)v.findViewById(R.id.rowImgTemplRepeat);
        ImageView imgRemind =   (ImageView)v.findViewById(R.id.rowImgTemplRemind);
        ImageView imgPlus =   (ImageView)v.findViewById(R.id.rowImgTemplPlus);
        ImageView imgMinus =   (ImageView)v.findViewById(R.id.rowImgTemplMinus);

        Template templ  = templs.get(position);
        Payord   pay = templ.getPayord(context);
        Account  acc = pay.getAccount(context);
        Category cat =  pay.getCategory(context);

        txtAccName.setText(acc.getNameAndCurrency());
        txtPayName.setText(pay.getName());
        txtPayCat.setText(cat.getName());

        imgRepeat.setVisibility(View.GONE);
        imgRemind.setVisibility(View.GONE);
        imgPlus.setVisibility(View.GONE);
        imgMinus.setVisibility(View.GONE);

        if (pay.isPermanent()){
         imgRepeat.setVisibility(View.VISIBLE);
        }
        if (pay.isRemind()){
         imgRemind.setVisibility(View.VISIBLE);
        }
        if (pay.getType()==Payord.Type.CREDIT ){
         imgPlus.setVisibility(View.VISIBLE);
        }
        if (pay.getType()==Payord.Type.DEBIT ){
         imgMinus.setVisibility(View.VISIBLE);
        }
        txtSaldo.setTextColor( pay.getType() == Payord.Type.DEBIT ? context.getResources().getColor(R.color.red) : context.getResources().getColor(R.color.xdark_green) );
        txtSaldo.setText(Utils.intToMoney(pay.getAmount()));
        return v;
    }
}

