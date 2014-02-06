package com.sanq.moneys;

import android.app.Activity;
import android.os.Bundle;
import com.sanq.ui.HorizontalPager;
import com.sanq.utils.SLog;

public class MainHorizontalPager extends Activity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.horisontalpager);

        //HorizontalPager horizontalPager = new HorizontalPager(getApplicationContext());

        // Add some views to it
//        final int[] backgroundColors =  { Color.RED, Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW };
//        for (int i = 0; i < 5; i++) {
//            TextView textView = new TextView(getApplicationContext());
//            textView.setText(Integer.toString(i + 1));
//            textView.setTextSize(100);
//            textView.setTextColor(Color.BLACK);
//            textView.setGravity(Gravity.CENTER);
//            textView.setBackgroundColor(backgroundColors[i]);
//            realViewSwitcher.addView(textView);
//        }


//        DashBoard dash = new DashBoard(this.getApplicationContext());
//        View v   = (View)findViewById(R.layout.main);
//        Log.d(Cnt.TAG, "" + (v==null));
//        horizontalPager.addView(dash);

        // set as content view
        //setContentView(horizontalPager);

        /*
         * Note that you can also define your own views directly in a resource XML, too by using:
         * <com.github.ysamlan.horizontalpager.RealViewSwitcher
         *     android:layout_width="fill_parent"
         *     android:layout_height="fill_parent"
         *     android:id="@+id/real_view_switcher">
         *     <!-- your views here -->
         * </com.github.ysamlan.horizontalpager.RealViewSwitcher>
         */

        // OPTIONAL: listen for screen changes
      //  horizontalPager.setOnScreenSwitchListener(onScreenSwitchListener);

    }


    private final HorizontalPager.OnScreenSwitchListener onScreenSwitchListener =
            new HorizontalPager.OnScreenSwitchListener() {
                @Override
                public void onScreenSwitched(final int screen) {
                    /*
                     * this method is executed if a screen has been activated, i.e. the screen is
                     * completely visible and the animation has stopped (might be useful for
                     * removing / adding new views)
                     */
                    SLog.d("switched to screen: " + screen);
                }
            };
}
