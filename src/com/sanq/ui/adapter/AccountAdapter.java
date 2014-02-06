package com.sanq.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.sanq.entity.Account;
import com.sanq.moneys.R;
import com.sanq.utils.Utils;

import java.text.Collator;
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
public class AccountAdapter extends BaseAdapter {

   public enum TypeControl{
    LIST_VIEW,SPINNER
    }


    Context context;
    LayoutInflater layInflater ;
    List<Account> accounts;
    TypeControl typeControl =  TypeControl.LIST_VIEW;


    public AccountAdapter(Context context, TypeControl typeControl) {
        this(context);
        this.typeControl = typeControl;
    }

        public AccountAdapter(Context context) {
        this.context = context;
        layInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        accounts= new ArrayList<Account>();
        }

        public void reloadData(List<Account> accounts ){
            this.accounts = accounts;
            Collections.sort(this.accounts, ALPHA_COMPARATOR);
            notifyDataSetChanged();
        }



    public static final Comparator<Account> ALPHA_COMPARATOR = new Comparator<Account>() {
        private final Collator sCollator = Collator.getInstance();
        @Override
        public int compare(Account object1, Account object2) {
            return sCollator.compare(object1.getName(),object2.getName());
        }
    };



    public Account getAccount (int pos ){
        if (  pos >= 0 && pos < accounts.size()){
            return accounts.get(pos);
        }else{
            return  null;
        }
    }

    @Override
    public int getCount() {
        return accounts.size();
    }

    @Override
    public Object getItem(int position) {
        return accounts.get(position).getName();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getPosition(Account acc) {
        return    accounts.indexOf(acc);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      if (typeControl==TypeControl.LIST_VIEW) {
          return getItemView( position,  convertView,  parent) ;
      }  else return getCaptionView( position,  convertView,  parent) ;
      }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (typeControl==TypeControl.SPINNER) {
            return getItemView( position,  convertView,  parent) ;
        }     else {
            return null;
        }
    }



    View getItemView (int position, View convertView, ViewGroup parent){
        View v = convertView;
        if (v == null) {
            v = this.layInflater.inflate(R.layout.template_row_account, null);
        }
        TextView accName = (TextView)v.findViewById(R.id.rowAccName);
        TextView accDescr = (TextView)v.findViewById(R.id.rowAccDescript);
        TextView accSaldo = (TextView)v.findViewById(R.id.rowAccSaldo);
        TextView accCurrency = (TextView)v.findViewById(R.id.rowAccCurrency);

        Account acc  = accounts.get(position);

        accName.setText(acc.getName());
        accCurrency.setText(acc.getCurrency());
        String mes  = context.getResources().getString(R.string.row_acc_descript);
        accDescr.setText(mes + " " + Utils.intToMoney(acc.getSaldo()));

        accSaldo.setTextColor( acc.getLastCalcSaldo() < 0 ? context.getResources().getColor(R.color.red) : context.getResources().getColor(R.color.xdark_green) );
        accSaldo.setText(Utils.intToMoney(acc.getLastCalcSaldo()));
        return v;
    }

    View getCaptionView (int position, View convertView, ViewGroup parent){
        TextView textView = (TextView) layInflater.inflate(R.layout.template_spin, null);
        textView.setText(accounts.get(position).getNameAndCurrency());
        return textView;
    }






}

