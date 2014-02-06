package com.sanq.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.sanq.entity.Payord;
import com.sanq.entity.Transfer;
import com.sanq.moneys.R;
import com.sanq.utils.Utils;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: sanq
 * Date: 19.06.13
 * Time: 21:57
 * To change this template use File | Settings | File Templates.
 */
public class TransferAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layInflater;
    List<Transfer> transf;
    int selectedPos = -1;
    int idTransAlarm = -1;

    public int getIdTransAlarm() {
        return idTransAlarm;
    }

    public void setIdTransAlarm(int idTransAlarm) {
        this.idTransAlarm = idTransAlarm;
    }

    public int getSelectedPos() {
        return selectedPos;
    }

    public int getAlarmPos() {
        int res = -1;
        for (int i = 0; i < transf.size(); i++) {
            if (transf.get(i).getId() == getIdTransAlarm()) {
                return i;
            }
        }
        return res;
    }


    public int getPos(Transfer transfer) {
        return transf.lastIndexOf(transfer);
    }

    public void setSelectedNearestCurrDate() {
        for (int i = 0; i < transf.size(); i++) {
            this.selectedPos = i;
            if( Utils.zeroTime(transf.get(i).getDt()).getTime() >= Utils.zeroTime(new Date()).getTime()) {
                break;
            }
        }
        notifyDataSetChanged();
    }


    public void setSelectedPos(int selectedPos) {
        this.selectedPos = selectedPos;
        if (getTransfer(selectedPos).getId() == idTransAlarm) {
            idTransAlarm = 0;
        }
        notifyDataSetChanged();
    }

    public void setSelectedTransfer(Transfer transfer) {
        selectedPos = transf.indexOf(transfer);
        notifyDataSetChanged();
    }

    public TransferAdapter(Context context, int codeAlarm) {
        this.context = context;
        this.idTransAlarm = codeAlarm;
        layInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        transf = new ArrayList<Transfer>();
    }


    public void reloadData(List<Transfer> transf) {
        this.transf = transf;
        Collections.sort(this.transf, DATE_COMPARATOR);
        notifyDataSetChanged();
    }


    public static final Comparator<Transfer> DATE_COMPARATOR = new Comparator<Transfer>() {
        //  private final Collator sCollator = Collator.getInstance();
        @Override
        public int compare(Transfer object1, Transfer object2) {
            // return sCollator.compare(Long.valueOf(object1.lastModified()),Long.valueOf(object2.lastModified()));
            if (object1.getDt().getTime() < object2.getDt().getTime()) {
                return -1;
            } else if (object1.getDt().getTime() > object2.getDt().getTime()) {
                return 1;
            } else {
                return 0;
            }
        }
    };


    public Transfer getTransfer(int pos) {
        if (pos >= 0 && pos < transf.size()) {
            return transf.get(pos);
        } else {
            return null;
        }
    }

    public Transfer getSelectedTransfer() {
        return getTransfer(selectedPos);
    }

    @Override
    public int getCount() {
        return transf.size();
    }

    @Override
    public Object getItem(int position) {
        return transf.get(position).getDt();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = this.layInflater.inflate(R.layout.template_row_transfer, null);
        }

        TextView txtPayDt = (TextView) v.findViewById(R.id.rowTransDate);
        TextView txtAmount = (TextView) v.findViewById(R.id.rowTransAmount);

        ImageView imgPlus = (ImageView) v.findViewById(R.id.rowImgTransPlus);
        ImageView imgMinus = (ImageView) v.findViewById(R.id.rowImgTransMinus);

        ImageView imgPhoto = (ImageView) v.findViewById(R.id.rowImgTransPhoto);

        ImageView imgRemind = (ImageView) v.findViewById(R.id.rowImgTransRemind);

        final Transfer trans = transf.get(position);
        final Payord pay = trans.getPayord(context);

        txtPayDt.setText(Utils.dateToString(context, trans.getDt()));
        txtAmount.setText(Utils.intToMoney(pay.getAmount()));

        imgPlus.setVisibility(View.GONE);
        imgMinus.setVisibility(View.GONE);


        if (pay.getType() == Payord.Type.CREDIT) {
            imgPlus.setVisibility(View.VISIBLE);
            txtAmount.setTextColor(context.getResources().getColor(R.color.xdark_green));
        }
        if (pay.getType() == Payord.Type.DEBIT) {
            imgMinus.setVisibility(View.VISIBLE);
            txtAmount.setTextColor(context.getResources().getColor(R.color.red));
        }

        if (position == selectedPos) {
            v.setBackgroundResource(R.color.light_blue);
        } else {
            v.setBackgroundResource(android.R.color.transparent);
        }

        if (trans.getImageName() != null && !trans.getImageName().isEmpty()) {
            imgPhoto.setVisibility(View.VISIBLE);
        } else {
            imgPhoto.setVisibility(View.INVISIBLE);
        }

        if (trans.getId() == idTransAlarm) {
            imgRemind.setVisibility(View.VISIBLE);
            v.setBackgroundResource(R.color.light_blue);
        } else {
            imgRemind.setVisibility(View.INVISIBLE);
        }
        return v;
    }


}

