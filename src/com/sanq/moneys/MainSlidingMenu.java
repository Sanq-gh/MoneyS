package com.sanq.moneys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import com.google.ads.AdView;
import com.sanq.dao.TransferDAO;
import com.sanq.ds.DS;
import com.sanq.entity.AbstractEntity;
import com.sanq.entity.Payord;
import com.sanq.entity.Transfer;
import com.sanq.ui.BaseActivity;
import com.sanq.ui.dialog.ConfirmDialog;
import com.sanq.ui.dialog.ConfirmDialogListener;
import com.sanq.ui.fragment.*;
import com.sanq.utils.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 14.06.13
 * Time: 11:51
 */
public class MainSlidingMenu extends BaseActivity implements CallbackFragment {
    private static MainSlidingMenu instance;
    private AbstractFragment mContent;
    //for sending data between fragments
    public Map<Integer, AbstractEntity> fragmentData = new HashMap<Integer, AbstractEntity>();

    private AdView adView;

    public MainSlidingMenu() {
        super(R.string.title_acc_fragment);
        this.instance = this;
    }

    public static MainSlidingMenu getInstance() {
        return instance;
    }

    @Override
    public void onResume() {
        super.onResume();
//        int transCode=0;
//        Bundle extras =  getView().getExtras();
//        if (extras!=null){
//                transCode = extras.getInt(Cnt.BUNDLE_KEY_ALARM_TRANSACTION,0);
//            Log.d(Cnt.TAG," transCode " + String.valueOf(transCode));
//        }else {
//            Log.d(Cnt.TAG," Bundle is null");
//        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        SLog.d("onNewIntent transCode " + String.valueOf(intent.getIntExtra(Cnt.BUNDLE_KEY_ALARM_TRANSACTION, 0)));
        AbstractFragment fragment = Utils.nvl(fragmentFromNotify(intent), new AccountFragment());
        switchContent(fragment);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // creating default account by first time
        new Preferences(getAppContext()).getDefaultAcc();

        if (savedInstanceState != null) {
            mContent = (AbstractFragment) getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        }
        if (mContent == null) {
            //first-show fragment
            mContent = new AccountFragment();
        }
        setContentView(R.layout.content_frame);

        setBehindContentView(R.layout.navigate);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_frame, new NavigateFragment())
                .commit();

        switchContent(mContent);


        //recreate alarms
        RemindManager.setAlarmTransfAll(getAppContext(), new Date());
        RemindManager.scheduleNextBackup(getAppContext(), new Preferences(getAppContext()));

        //if start from notify -  switch to transfer remind
        AbstractFragment fragTransRemind = fragmentFromNotify(getIntent());
        if (fragTransRemind != null) {
            switchContent(fragTransRemind);
        }

        // switchContent(Utils.nvl(fragTransRemind, mContent) );

        //todo:  реклама
        adView = (AdView) findViewById(R.id.adView);
         /*
        //todo: clear in prodaction
	  AdRequest reqTest = new AdRequest();
        reqTest.addTestDevice(AdRequest.TEST_EMULATOR);
        reqTest.addTestDevice("A5026754C941E895F9FACE07C999C602"); //http://webhole.net/2011/12/02/android-sdk-tutorial-get-admob-test-device-id/
        adView.loadAd(reqTest);
       */
    }

    private AbstractFragment fragmentFromNotify(Intent intent) {
        AbstractFragment res = null;
        int transCode = intent.getIntExtra(Cnt.BUNDLE_KEY_ALARM_TRANSACTION, 0);
        if (transCode != 0) {
            Transfer trans = new TransferDAO(getAppContext()).getById(transCode);
            Payord pay;
            if ((trans != null) && ((pay = trans.getPayord(getAppContext())) != null)) {
                res = new TransferFragment(pay);
                Bundle bundle = new Bundle();
                bundle.putInt(Cnt.BUNDLE_KEY_ALARM_TRANSACTION, transCode);
                res.setArguments(bundle);
            } else {
                Utils.procMessage(getAppContext(), getResources().getString(R.string.mes_notify_pay_not_found));
            }
        }



        return res;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
      //  getSupportFragmentManager().putFragment(outState, "mContent", mContent);
        getSupportFragmentManager()
                .beginTransaction()
                .add(mContent,"mContent");
    }

    public void switchContent(AbstractFragment fragment) {
        mContent = fragment;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null)
                .commit();
        getSlidingMenu().showContent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // this.exit();
    }



    public void exit() {
        ConfirmDialog dlg = new ConfirmDialog(getAppContext(), confirmExitListener, R.string.mes_confirm_exit);
        dlg.show(getSupportFragmentManager(), "");
    }

    ConfirmDialogListener confirmExitListener = new ConfirmDialogListener() {
        @Override
        public void onSetParam(int param) {
        }
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                //MainSlidingMenu.super.onBackPressed();
                //super.onDestroy();
                closeAll();
                MainSlidingMenu.getInstance().finish();
                //android.os.Process.killProcess(android.os.Process.myPid());
            }
        }
    };

    private void closeAll() {
        DS.getInstance().closeDb();
        if (adView != null) {
            adView.destroy();
        }
    }
    ///////    Fragments calback
    @Override
    public void onFragmentEvent(Integer idFragmentCaption, AbstractEntity ent) {
        fragmentData.put(idFragmentCaption, ent);
    }
}
