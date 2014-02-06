package com.sanq.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import com.sanq.moneys.R;

import java.text.Collator;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: sanq
 * Date: 19.06.13
 * Time: 21:57
 * To change this template use File | Settings | File Templates.
 */
public class CurrencyAdapter extends ArrayAdapter {
    Context context;
    LayoutInflater layInflater;
    private List<Currency> currs = new ArrayList<Currency>();

    public CurrencyAdapter(Context context) {
        super(context, R.layout.my_simple_spinner_dropdown_item);
        this.context = context;
        layInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        reloadData();
    }

    public void reloadData() {
        Locale[] locs = Locale.getAvailableLocales();
        for (Locale loc : locs) {
            try {
                Currency curr = Currency.getInstance(loc);
                if (!currs.contains(curr))
                    currs.add(curr);
            } catch (Exception exc) {
                // Log.d(Cnt.TAG, "Locale not found");  /*NOP*/
            }
        }
        Collections.sort(this.currs, ALPHA_COMPARATOR);
        notifyDataSetChanged();
    }

    public static final Comparator<Currency> ALPHA_COMPARATOR = new Comparator<Currency>() {
        private final Collator sCollator = Collator.getInstance();

        @Override
        public int compare(Currency object1, Currency object2) {
            return sCollator.compare(object1.getCurrencyCode(), object2.getCurrencyCode());
        }
    };

    public int getPos(String currency) {
        int res = 0;
        for (Currency curr : currs) {
            if (curr.getCurrencyCode().equals(currency)) {
                return res;
            }
            res++;
        }
        return 0;
    }

    public Currency getCurrency(int pos) {
        if (pos >= 0 && pos < currs.size()) {
            return currs.get(pos);
        } else {
            return null;
        }
    }

    public Currency getDefaultCurrency() {
        return Currency.getInstance(Locale.getDefault());
    }

    public int getDefaultCurrencyPos() {
        Currency defCurr = Currency.getInstance(Locale.getDefault());
        return getPos(defCurr.getCurrencyCode());
    }


    @Override
    public int getCount() {
        return currs.size();
    }

    @Override
    public Object getItem(int position) {
        return  currs.get(position).getCurrencyCode() + " - " + currs.get(position).getSymbol() ;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        TextView textView = (TextView) layInflater.inflate(R.layout.template_spin, null);
//        textView.setText(currs.get(position).getCurrencyCode() + " - " + currs.get(position).getSymbol());
//        return textView;
//    }
//
//    @Override
//    public View getDropDownView(int position, View convertView, ViewGroup parent) {
//        View view = convertView;
//
//        Log.d(Cnt.TAG ,"getDropDownView" );
//
//        if (view == null)
//        {
//            //convertView = vi.inflate(android.R.layout.simple_spinner_dropdown_item, null);
//            view = layInflater.inflate(R.layout.my_simple_spinner_dropdown_item, null);
//            Log.d(Cnt.TAG, "view == null " );
//        }
//        ((CheckedTextView)view).setText(currs.get(position).getCurrencyCode() + " - " + currs.get(position).getSymbol());
//        return view;
//
//    }

}

