package com.sanq.ui.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import com.sanq.dao.TemplateDAO;
import com.sanq.entity.Template;
import com.sanq.loader.BackgroundLoader;
import com.sanq.loader.BackgroundTask;
import com.sanq.moneys.MainSlidingMenu;
import com.sanq.moneys.R;
import com.sanq.ui.adapter.TemplateAdapter;
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
public class TemplateFragment extends AbstractFragment implements View.OnClickListener {
    private final int LOADER_ID = 900;

    TemplateAdapter mAdapter;
    ViewGroup layProgress;
    ListView listView;
    //  Button cmdBack;

    CallbackFragment callbackFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new TemplateAdapter(context);
    }

    @Override
    protected String getHeadCaption() {
        return context.getResources().getString(R.string.title_template_fragment);
    }

    @Override
    protected int getHeadIcon() {
        return R.drawable.puzzle;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.template, null);

        layProgress = (ViewGroup) v.findViewById(R.id.layTemplProgress);

        listView = (ListView) v.findViewById(R.id.lvTemplate);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(onItemClickListener);

//        cmdBack = (Button) v.findViewById(R.id.cmdTemplBack);
//        cmdBack.setOnClickListener(this);

        registerForContextMenu(listView);

        return v;
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
    public void onResume() {
        super.onResume();
        refreshViewData();
    }

    protected void refreshViewData() {
        refreshLoader(LOADER_ID, null, loaderCallback);
    }


    LoaderManager.LoaderCallbacks<List<Template>> loaderCallback = new LoaderManager.LoaderCallbacks<List<Template>>() {
        @Override
        public Loader<List<Template>> onCreateLoader(int id, Bundle args) {
            switchVisibility(true, layProgress, listView);
            return new BackgroundLoader(context, backgroundTaskLoad, args);
        }

        @Override
        public void onLoadFinished(Loader<List<Template>> listLoader, List<Template> templates) {
            if (listLoader.getId() == LOADER_ID) {
                mAdapter.reloadData(templates);
                switchVisibility(false, layProgress, listView);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Template>> listLoader) {
        }
    };

    BackgroundTask backgroundTaskLoad = new BackgroundTask<List<Template>>() {
        @Override
        public List<Template> load(Bundle args) {
            return new TemplateDAO(context).getAll();
        }
    };


    @Override
    public void onClick(View v) {
        //       switch (v.getId()) {
//            case R.id.cmdTemplBack:
//                getActivity().getSupportFragmentManager().popBackStack();
//                break;
//        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //super.onCreateContextMenu(menu, v, menuInfo);
        // getActivity().getMenuInflater().inflate(R.menu.context_template, menu);
        IconContextMenu cm = new IconContextMenu(getActivity(), R.menu.context_template);
        cm.setOnIconContextItemSelectedListener(iconMenuiListener);
        cm.setInfo(((AdapterView.AdapterContextMenuInfo) menuInfo).position);
        cm.show();
    }


    IconContextMenu.IconContextItemSelectedListener iconMenuiListener = new IconContextMenu.IconContextItemSelectedListener() {
        @Override
        public void onIconContextItemSelected(MenuItem item, Object info) {
            Template clickedTempl = mAdapter.getTemplate((Integer) info);

            switch (item.getItemId()) {
                case R.id.ctxCreatePay:
                    callbackFragment.onFragmentEvent(R.string.title_template_fragment, clickedTempl);
                    MainSlidingMenu.getInstance().switchContent(new PayordFragment());
                    break;
                case R.id.ctxDelete:
                    String mes = context.getResources().getString(R.string.mes_confirm_delete);
                    confirmDeleteListener.onSetParam(clickedTempl.getId());
                    ConfirmDialog dlg = new ConfirmDialog(context, confirmDeleteListener, String.format(mes, clickedTempl.getName()));
                    dlg.show(getActivity().getSupportFragmentManager(), "");
                    break;
                case R.id.ctxCancel:
                /*NOP*/
                    break;
            }

        }
    };

    ListView.OnItemClickListener onItemClickListener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (typeStart == TYPE_START_SELECT) {
                Template clickedTempl = mAdapter.getTemplate(position);
                selectAndPostBack(clickedTempl);
            }
        }
    };


    private void selectAndPostBack(Template clickedTempl) {
        callbackFragment.onFragmentEvent(R.string.title_template_fragment, clickedTempl);
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
                case android.content.DialogInterface.BUTTON_POSITIVE:
                    try {
                        new TemplateDAO(context).deleteById(deleteId);
                        refreshViewData();
                    } catch (SQLiteException e) {
                        Utils.showMessage(getActivity(), "" , e.getMessage(), null);
                    }
                    break;
                case android.content.DialogInterface.BUTTON_NEGATIVE:  /*NOP*/
                    break;
            }
        }
    };

}
