package com.sanq.ui;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 14.06.13
 * Time: 11:52
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.sanq.moneys.R;
import com.sanq.utils.Preferences;

public class BaseActivity extends SlidingFragmentActivity {
    private static Context context;
    private int mTitleRes;
    // private int orientation;
    protected ListFragment mFrag;
    private boolean rightButtonVisible = false;
    private TextView txtTitle;
    private ImageView imgTitle;

    public BaseActivity(int titleRes) {
        mTitleRes = titleRes;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        context = getApplicationContext();
        super.onCreate(savedInstanceState);
        // set the Behind View
        setBehindContentView(R.layout.navigate);

        // customize the SlidingMenu
        SlidingMenu sm = getSlidingMenu();
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //custom settings
        setOrientation(new Preferences(context).getMenuOrient().getInt());
        getSupportActionBar().setIcon(R.drawable.ic_menu_white);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1D1B37")));

        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.action_bar, null);

        imgTitle = (ImageView) v.findViewById(R.id.imgTitle);
        txtTitle = (TextView) v.findViewById(R.id.txtTitle);

        setTitleActionBar("");
        setTitleImageActionBar(R.drawable._app_icon);
        getSupportActionBar().setCustomView(v);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                toggle();
                return true;
            case R.id.rightButtonOnActionBar:
                toggle();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.rightButtonOnActionBar);
        menuItem.setVisible(rightButtonVisible);
        return true;
    }


    private void setOrientation(int orientation) {
        if (orientation == SlidingMenu.RIGHT) {
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSlidingMenu().setMode(SlidingMenu.RIGHT);
            getSlidingMenu().setShadowDrawable(R.drawable.shadowright);
            rightButtonVisible = true;
        } else {
            rightButtonVisible = false;
            getSlidingMenu().setMode(SlidingMenu.LEFT);
            getSlidingMenu().setShadowDrawable(R.drawable.shadow);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }


    @Override
    public boolean onKeyDown(int keycode, KeyEvent e) {
        switch(keycode) {
            case KeyEvent.KEYCODE_MENU:
                 toggle();
                return true;
        }
        return super.onKeyDown(keycode, e);
    }


   public void setTitleImageActionBar(int idImage) {
        if (imgTitle != null) {
            imgTitle.setImageResource(idImage);
        }
    }


    public void setTitleActionBar(String title) {
        if (txtTitle != null) {
            txtTitle.setText(title);
        }
    }


    public static Context getAppContext() {
        return context;
    }
}
