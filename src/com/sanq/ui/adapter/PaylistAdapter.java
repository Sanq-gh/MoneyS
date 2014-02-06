package com.sanq.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.sanq.entity.Category;
import com.sanq.entity.Payord;
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
public class PaylistAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layInflater;
    private List<Payord> pays;


    public PaylistAdapter(Context context) {
        this.context = context;
        layInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pays = new ArrayList<Payord>();

    }

    public void reloadData(List<Payord> pays) {
        this.pays = pays;
        Collections.sort(this.pays, DATE_COMPARATOR);
        notifyDataSetChanged();
    }


    public static final Comparator<Payord> DATE_COMPARATOR = new Comparator<Payord>() {
        //  private final Collator sCollator = Collator.getInstance();
        @Override
        public int compare(Payord object1, Payord object2) {
            // return sCollator.compare(Long.valueOf(object1.lastModified()),Long.valueOf(object2.lastModified()));
            if (object1.getDt().getTime() < object2.getDt().getTime()) {
                return   -1;
            }else if (object1.getDt().getTime() > object2.getDt().getTime()){
                return   1;
            }else{
                return  0;
            }
        }
    };


    public Payord getPayord(int pos) {
        if (pos >= 0 && pos < pays.size()) {
            return pays.get(pos);
        } else {
            return null;
        }
    }


    @Override
    public int getCount() {
        return pays.size();
    }

    @Override
    public Object getItem(int position) {
        return pays.get(position).getName();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = this.layInflater.inflate(R.layout.template_row_paylist, null);
        }


        TextView txtPayName = (TextView) v.findViewById(R.id.rowPayListPayName);
        TextView txtSaldo = (TextView) v.findViewById(R.id.rowPayListSaldo);

        TextView txtPayCat = (TextView) v.findViewById(R.id.rowPayListPayCat);
        TextView txtPayDt = (TextView) v.findViewById(R.id.rowPayListDt);

        ImageView imgRepeat = (ImageView) v.findViewById(R.id.rowImgPayListRepeat);
        ImageView imgRemind = (ImageView) v.findViewById(R.id.rowImgPayListRemind);
        ImageView imgPlus = (ImageView) v.findViewById(R.id.rowImgPayListPlus);
        ImageView imgMinus = (ImageView) v.findViewById(R.id.rowImgPayListMinus);


        final Payord pay = pays.get(position);
        Category cat = pay.getCategory(context);

        txtPayName.setText(pay.getName());
        txtPayCat.setText(cat.getName());
        txtPayDt.setText(Utils.dateToString(context, pay.getDt()));

        imgRepeat.setVisibility(View.INVISIBLE);
        imgRemind.setVisibility(View.INVISIBLE);

        imgPlus.setVisibility(View.GONE);
        imgMinus.setVisibility(View.GONE);

        if (pay.isPermanent()) {
            imgRepeat.setVisibility(View.VISIBLE);
        }
        if (pay.isRemind()) {
            imgRemind.setVisibility(View.VISIBLE);
        }
        if (pay.getType() == Payord.Type.CREDIT) {
            imgPlus.setVisibility(View.VISIBLE);
        }
        if (pay.getType() == Payord.Type.DEBIT) {
            imgMinus.setVisibility(View.VISIBLE);
        }
        txtSaldo.setTextColor(pay.getType() == Payord.Type.DEBIT ? context.getResources().getColor(R.color.red) : context.getResources().getColor(R.color.xdark_green));
        txtSaldo.setText(Utils.intToMoney(pay.getAmount()));

        return v;
    }


}

