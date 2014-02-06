package com.sanq.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import com.sanq.moneys.MainSlidingMenu;
import com.sanq.moneys.R;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 14.06.13
 * Time: 14:05
 */
public class NavigateFragment extends AbstractFragment implements View.OnClickListener {

    TableRow rowAccounts;
    TableRow rowPay;
    TableRow rowPayords;
    TableRow rowTransfer ;


    TableRow rowReport ;
    TableRow rowCategory ;
    TableRow rowTemplate;

    TableRow rowPrefs ;
    TableRow rowInfo;

    TableRow rowTest;
    TableRow rowExit ;

    /*
    * AdView adView = (AdView)findViewById(R.id.adView);
2	AdRequest re = new AdRequest();
3	re.setTesting(true);
4	adView.loadAd(re);
*/

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.navigate, null);

        rowAccounts = (TableRow)v.findViewById(R.id.row_accounts);
        rowAccounts.setOnClickListener(this);
        rowPay = (TableRow)v.findViewById(R.id.row_pay);
        rowPay.setOnClickListener(this);
        rowPayords = (TableRow)v.findViewById(R.id.row_payords);
        rowPayords.setOnClickListener(this);
        rowTransfer = (TableRow)v.findViewById(R.id.row_transfer);
        rowTransfer.setOnClickListener(this);

        rowReport = (TableRow)v.findViewById(R.id.row_charts);
        rowReport.setOnClickListener(this);
        rowCategory = (TableRow)v.findViewById(R.id.row_category);
        rowCategory.setOnClickListener(this);
        rowTemplate = (TableRow)v.findViewById(R.id.row_templates);
        rowTemplate.setOnClickListener(this);

        rowPrefs = (TableRow)v.findViewById(R.id.row_prefs);
        rowPrefs.setOnClickListener(this);
        rowInfo = (TableRow)v.findViewById(R.id.row_info);
        rowInfo.setOnClickListener(this);

        rowTest = (TableRow)v.findViewById(R.id.row_test);
        rowTest.setOnClickListener(this);

        rowExit = (TableRow)v.findViewById(R.id.row_exit);
        rowExit.setOnClickListener(this);

        return v;
    }

    @Override
    public void onDestroy() {
         super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        AbstractFragment newContent = null;
        switch (v.getId()){

            case R.id.row_accounts: newContent = new AccountFragment(); break;
            case R.id.row_pay: newContent = new PayordFragment(); break;

            case R.id.row_payords: newContent = new PaylistFragment(); break;

            case R.id.row_transfer: newContent = new AccountTransferFragment(); break;

            case R.id.row_charts: newContent = new ChartFilterFragment();  break;
            case R.id.row_category: newContent = new CategoryFragment();  break;
            case R.id.row_templates: newContent = new TemplateFragment(); break;

            case R.id.row_prefs: newContent = new PrefsFragment(); break;
            case R.id.row_info: newContent = new InfoFragment() ; break;

            case R.id.row_test: newContent = new TestFragment(); break;

            case R.id.row_exit: MainSlidingMenu.getInstance().exit(); break;

        }
        if (newContent != null) {
            MainSlidingMenu.getInstance().switchContent(newContent);
        }
    }



    @Override
    protected String getHeadCaption() {
        return context.getResources().getString(R.string.title_nav_faragment);
    }
    @Override
    protected int getHeadIcon() {
        return R.drawable._app_icon;
    }


    @Override
    protected void refreshViewData() {
    }
}
