package com.sanq.ui.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import com.sanq.entity.AbstractEntity;
import com.sanq.moneys.MainSlidingMenu;
import com.sanq.moneys.R;
import com.sanq.ui.dialog.DateDialog;
import com.sanq.utils.Cnt;
import com.sanq.utils.Utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: sanq
 * Date: 16.06.13
 * Time: 21:55
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractFragment extends Fragment {
    protected static final int TYPE_START_UNDEFINED = 1000;
    protected static final int TYPE_START_SELECT = 1001;
    protected static final int TYPE_START_EDIT = 1002;
    protected static final int TYPE_START_NEW = 1003;
    Context context;
    LayoutInflater layInflater;

    protected int typeStart = TYPE_START_UNDEFINED;

    protected ProgressDialog progressDialogInfin;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layInflater = getActivity().getLayoutInflater();
        context = getActivity().getApplicationContext();
        Bundle args = getArguments();
        if (args != null) {
            typeStart = args.getInt(Cnt.BUNDLE_KEY_TYPE_START);
        }
    }

    protected <T> T parseInputData(Integer idFragmentCaption, Class clazz) {
        T res = null;
        if (getActivity() instanceof MainSlidingMenu) {
            MainSlidingMenu mainMenu = (MainSlidingMenu) getActivity();
            AbstractEntity ent = mainMenu.fragmentData.get(idFragmentCaption);
            if (ent != null && clazz.isInstance(ent)) {
                res = (T) ent;
                mainMenu.fragmentData.remove(idFragmentCaption);
            }
        }
        return res;
    }


    /**
     * Must be unique for all fragment
     */
    protected abstract String getHeadCaption();

    protected abstract int getHeadIcon();

    @Override
    public void onResume() {
        super.onResume();
        MainSlidingMenu.getInstance().setTitleActionBar(getHeadCaption());
        MainSlidingMenu.getInstance().setTitleImageActionBar(getHeadIcon());
    }


    protected void goBack() {
        getActivity().getSupportFragmentManager().popBackStack();
    }


    protected android.support.v4.app.LoaderManager getSLoaderManager() {
        return MainSlidingMenu.getInstance().getSupportLoaderManager();
    }

    protected void refreshLoader(int id, Bundle args, android.support.v4.app.LoaderManager.LoaderCallbacks callback) {
        if (getSLoaderManager().getLoader(id) == null) {
            getSLoaderManager().initLoader(id, args, callback);
        } else {
            getSLoaderManager().restartLoader(id, args, callback);
        }
    }


    protected void setDateByDialog(final Button cmd, final Date dt) {
        DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(year, monthOfYear, dayOfMonth);
                dt.setTime(cal.getTime().getTime());
                cmd.setText(Utils.dateToString(context, dt));
                refreshViewData();
            }
        };
        DateDialog.showDatePicker(getActivity(), datePickerListener, dt);
    }


    protected abstract void refreshViewData();


    protected void switchVisibility(boolean progressViewsVisibility, View progressView, View dataView) {
        switchVisibility(progressViewsVisibility, new View[]{progressView}, new View[]{dataView});
    }

    protected void switchVisibility(boolean progressViewsVisibility, View[] progressViews, View[] dataViews) {
        if (progressViewsVisibility) {
            for (View progressView : progressViews) {
                progressView.setVisibility(View.VISIBLE);
            }
            for (View dataView : dataViews) {
                dataView.setVisibility(View.GONE);
            }
        } else {
            for (View progressView : progressViews) {
                progressView.setVisibility(View.GONE);
            }
            for (View dataView : dataViews) {
                dataView.setVisibility(View.VISIBLE);
            }
        }
    }


    protected Message createHandlerMessageString(int code, String stringMessage) {
        Message res = new Message();
        Bundle bundle = new Bundle();
        bundle.putString(Cnt.BUNDLE_KEY_HANDLER_MES, stringMessage);
        res.setData(bundle);
        res.what = code;
        return res;
    }

    Handler handlerDafault = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (progressDialogInfin != null) {
                switch (msg.what) {
                    case Cnt.HANDLER_MES_START:
                        String mes = msg.getData().getString(Cnt.BUNDLE_KEY_HANDLER_MES);
                        progressDialogInfin.setMessage(mes != null ? mes : "");
                        break;
                    case Cnt.HANDLER_MES_FINISH:
                        refreshViewData();
                        if (progressDialogInfin.isShowing()) {
                            progressDialogInfin.dismiss();
                        }
                        break;
                    case Cnt.HANDLER_MES_ERROR:
                        refreshViewData();
                        if (progressDialogInfin.isShowing()) {
                            progressDialogInfin.dismiss();
                        }
                        Utils.showMessage(getActivity(), "", context.getString(R.string.mes_error_occurs) + " " + msg.getData().getString(Cnt.BUNDLE_KEY_HANDLER_MES), null);
                        break;
                }
            }
        }
    };


}
