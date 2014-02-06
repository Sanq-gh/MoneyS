package com.sanq.ui.fragment;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.sanq.exception.SavingException;
import com.sanq.moneys.R;
import com.sanq.utils.Utils;


/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 14.06.13
 * Time: 15:00
 */
public class InfoFragment extends AbstractFragment {

    TextView txtVersion;
    TextView txtDbName;
    TextView txtBackupsPath;
    TextView txtImagesPath;
    TextView txtVersionHistory;


    @Override
    protected String getHeadCaption() {
        return context.getResources().getString(R.string.title_info_fragment);
    }

    @Override
    protected int getHeadIcon() {
        return R.drawable.info;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.info, null);

        txtVersion = (TextView) v.findViewById(R.id.txtInfoVersionName);
        txtDbName = (TextView) v.findViewById(R.id.txtInfoDbName);
        txtBackupsPath = (TextView) v.findViewById(R.id.txtInfoBackupsPath);
        txtImagesPath = (TextView) v.findViewById(R.id.txtInfoImagesPath);
        txtVersionHistory = (TextView) v.findViewById(R.id.txtInfoVersionHistory);

//        Button button = (Button) v.findViewById(R.id.button);
//        button.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                Log.d(LOG_TAG, "Button click in Fragment1");
//            }
//        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshViewData();
    }

    @Override
    protected void refreshViewData() {
        try {
            txtVersion.setText(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName);
            txtDbName.setText(Utils.getDBFileName(context));
            txtBackupsPath.setText(Utils.getAppPath(context, Utils.TypePath.BackUp) + "/");
            txtImagesPath.setText(Utils.getAppPath(context, Utils.TypePath.Images) + "/");
            //txtVersionHistory.setText(Utils.getTxtFile(context, R.raw.version) );
          //  txtVersionHistory.setText(Utils.getTxtFile(context, R.raw.version).replaceAll(Cnt.DB_DELIMITER_STATEMENT, "\\\n"));
            txtVersionHistory.setText(Html.fromHtml(Utils.getTxtFile(context, R.raw.version)));

//            txtVersionHistory.setText(Html.fromHtml("<b>" + "title" + "</b>" + "<br />" +
//                    "<small>" + "description" + "</small>" + "<br />" +
//                    "<small>" + "DateAdded" + "</small>"));


        } catch (SavingException e) {
             /*NOP*/
        } catch (PackageManager.NameNotFoundException e) {
            /*NOP*/
        }
    }
}



