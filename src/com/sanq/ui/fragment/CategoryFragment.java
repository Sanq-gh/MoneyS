package com.sanq.ui.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.Button;
import android.widget.ExpandableListView;
import com.sanq.dao.CategoryDAO;
import com.sanq.entity.Category;
import com.sanq.loader.BackgroundLoader;
import com.sanq.loader.BackgroundTask;
import com.sanq.moneys.MainSlidingMenu;
import com.sanq.moneys.R;
import com.sanq.ui.adapter.CategoryAdapter;
import com.sanq.ui.dialog.ConfirmDialog;
import com.sanq.ui.dialog.ConfirmDialogListener;
import com.sanq.utils.Utils;
import yuku.iconcontextmenu.IconContextMenu;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 14.06.13
 * Time: 14:05
 */
public class CategoryFragment extends AbstractFragment implements View.OnClickListener {

    private final int LOADER_ID = 900;


    View layButtonFew;
  //  View layButtonOne;
    ViewGroup layProgress;

    CategoryAdapter mAdapter;
    boolean isExpand = false;
    ExpandableListView listView;
    Button cmdNew;
    Button cmdExpCol;
    //Button cmdBack;

    int idContextMenu = R.menu.context_category;
    CallbackFragment callbackFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new CategoryAdapter(context);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callbackFragment = (CallbackFragment) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement CallbackFragment");
        }
    }


    @Override
    protected String getHeadCaption() {
        return context.getResources().getString(R.string.title_cat_fragment);
    }

    @Override
    protected int getHeadIcon() {
        return R.drawable.category;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshViewData();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.category, null);

        listView = (ExpandableListView) v.findViewById(R.id.lvCats);
        listView.setAdapter(mAdapter);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        listView.setIndicatorBounds(width - Utils.GetPixelFromDips(context, 50), width - Utils.GetPixelFromDips(context, 10));



        layButtonFew = (View) v.findViewById(R.id.layCatButttonFew);
        //layButtonOne = (View) v.findViewById(R.id.layCatButtonOne);

        layProgress = (ViewGroup) v.findViewById(R.id.layCatProgress);

        cmdNew = (Button) v.findViewById(R.id.cmdCatNew);
        cmdNew.setOnClickListener(this);
        cmdExpCol = (Button) v.findViewById(R.id.cmdCatExpColl);
        cmdExpCol.setOnClickListener(this);
       // cmdBack = (Button) v.findViewById(R.id.cmdCatBack);
       // cmdBack.setOnClickListener(this);

        registerForContextMenu(listView);

        switchTypeStart(typeStart);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cmdCatNew:
                MainSlidingMenu.getInstance().switchContent(new CategoryAdd());
                refreshViewData();
                break;
            case R.id.cmdCatExpColl:
                doExpColl();
                break;
//            case R.id.cmdCatBack:
//                getActivity().getSupportFragmentManager().popBackStack();
//                break;
        }
    }

    @Override
    protected void refreshViewData() {
        refreshLoader(LOADER_ID, null, loaderCallback);
        //mAdapter.reloadData();
    }


    LoaderManager.LoaderCallbacks<List<Category>> loaderCallback = new LoaderManager.LoaderCallbacks<List<Category>>() {
        @Override
        public Loader<List<Category>> onCreateLoader(int id, Bundle args) {
            switchVisibility(true, layProgress, listView);
            return new BackgroundLoader(context, backgroundTaskLoad, args);
        }

        @Override
        public void onLoadFinished(Loader<List<Category>> listLoader, List<Category> categories) {
            if (listLoader.getId() == LOADER_ID) {

                mAdapter.reloadData(categories);
                switchVisibility(false, layProgress, listView);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Category>> listLoader) {
        }
    };

    BackgroundTask backgroundTaskLoad = new BackgroundTask<List<Category>>() {
        @Override
        public List<Category> load(Bundle args) {
            return Category.getAllLinked(context);
        }
    };


    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        getActivity().getMenuInflater().inflate(idContextMenu, menu);
        IconContextMenu cm = new IconContextMenu(getActivity(), idContextMenu);
        cm.setOnIconContextItemSelectedListener(iconMenuiListener);

          cm.setInfo(((ExpandableListView.ExpandableListContextMenuInfo) menuInfo).packedPosition);
        cm.show();
    }


    IconContextMenu.IconContextItemSelectedListener iconMenuiListener = new IconContextMenu.IconContextItemSelectedListener() {
        @Override
        public void onIconContextItemSelected(MenuItem item, Object info) {
            //   ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) menuItem.getMenuInfo();
            int groupPos = 0, childPos = 0;
            groupPos = ExpandableListView.getPackedPositionGroup((Long) info);
            childPos = ExpandableListView.getPackedPositionChild((Long) info);

            Category clickedCat = mAdapter.getCategory(groupPos, childPos);

            switch (item.getItemId()) {
                case R.id.ctxEdit:
                    MainSlidingMenu.getInstance().switchContent(new CategoryAdd(clickedCat));
                    refreshViewData();
                    break;
                case R.id.ctxDelete:
                    String mes = context.getResources().getString(R.string.mes_confirm_delete);
                    confirmDeleteListener.onSetParam(clickedCat.getId());
                    ConfirmDialog dlg = new ConfirmDialog(context, confirmDeleteListener, String.format(mes, clickedCat.getName()));
                    dlg.show(getActivity().getSupportFragmentManager(), "");
                    break;
                case R.id.ctxSelect:
                    selectAndPostBack(clickedCat);
                    break;
            }
        }
    };

    private void doExpColl() {
        for (int position = 1; position <= mAdapter.getGroupCount(); position++) {
            if (isExpand) {
                listView.collapseGroup(position - 1);
            } else {
                listView.expandGroup(position - 1);
            }
        }
        if (isExpand) {
            cmdExpCol.setText(R.string.btn_capt_expand);
            cmdExpCol.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.expand), null, null, null);
        }  else{
            cmdExpCol.setText(R.string.btn_capt_collapse);
            cmdExpCol.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.collapse), null, null, null);
        }
         isExpand = !isExpand;
    }


    private void switchTypeStart(int typeStart) {
        switch (typeStart) {
            case AbstractFragment.TYPE_START_SELECT:
                layButtonFew.setVisibility(View.GONE);
              //  layButtonOne.setVisibility(View.VISIBLE);
                idContextMenu = R.menu.context_select;
                listView.setOnChildClickListener(listenerOnChildClickListener);
                break;
            default:
                layButtonFew.setVisibility(View.VISIBLE);
               // layButtonOne.setVisibility(View.GONE);
                idContextMenu = R.menu.context_category;
                break;
        }
    }


    ExpandableListView.OnChildClickListener listenerOnChildClickListener = new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            Category clickedCat = mAdapter.getCategory(groupPosition, childPosition);
            selectAndPostBack(clickedCat);
            return false;
        }
    };


    private void selectAndPostBack(Category clickedCat) {
        callbackFragment.onFragmentEvent(R.string.title_cat_fragment, clickedCat);
        getActivity().getSupportFragmentManager().popBackStack();
    }


    ConfirmDialogListener confirmDeleteListener = new ConfirmDialogListener() {
        private int deleteId;

        @Override
        public void onSetParam(int param) {
            this.deleteId = param;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    try {
                        new CategoryDAO(context).deleteById(deleteId);
                        refreshViewData();
                    } catch (SQLiteException e) {
                        Utils.showMessage(getActivity(), "" , e.getMessage(), null);
                    }
                    break;
                case DialogInterface.BUTTON_NEGATIVE:  /*NOP*/
                    break;
            }
        }
    };

}
