package com.sanq.ui.fragment;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.*;
import android.widget.*;
import com.sanq.dao.TransferDAO;
import com.sanq.entity.Payord;
import com.sanq.entity.Transfer;
import com.sanq.exception.SavingException;
import com.sanq.loader.BackgroundLoader;
import com.sanq.loader.BackgroundTask;
import com.sanq.moneys.R;
import com.sanq.params.FilterParams;
import com.sanq.params.TransferFilterParams;
import com.sanq.ui.adapter.TransferAdapter;
import com.sanq.ui.dialog.ConfirmDialog;
import com.sanq.ui.dialog.ConfirmDialogListener;
import com.sanq.ui.dialog.ShowPicDialog;
import com.sanq.utils.Cnt;
import com.sanq.utils.Preferences;
import com.sanq.utils.SLog;
import com.sanq.utils.Utils;
import yuku.iconcontextmenu.IconContextMenu;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 14.06.13
 * Time: 14:05
 */
public class TransferFragment extends AbstractFragment implements View.OnClickListener {
    //  private Payord filterPay;
    TransferAdapter mAdapter;
    TransferFilterParams filterParams = new FilterParams();
    private final int LOADER_ID = 300;

    int idTransAlarm = -1;

    private Uri photoUri;
    private static final int REG_CAMERA_CODE = 100;
    private int lastCapturedPhotoId = Integer.MAX_VALUE;
    private String capturedImageMD5;

    TextView txtPayName;
    TextView txtPayCat;
    TextView txtAccName;
    TextView txtRemindTime;

    ImageView imgRemind;

    ViewGroup layProgress;
    ListView listView;


    public TransferFragment(Payord filterPay) {
        filterParams.initParams(filterPay, null);
    }


    @Override
    protected String getHeadCaption() {
        return context.getResources().getString(R.string.title_transfer_fragment);
    }

    @Override
    protected int getHeadIcon() {
        return R.drawable.transact;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new TransferAdapter(context, idTransAlarm);
    }


    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        idTransAlarm = args.getInt(Cnt.BUNDLE_KEY_ALARM_TRANSACTION, 0);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.transfer, null);

        listView = (ListView) v.findViewById(R.id.lvTransfer);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(onItemClickListener);

        layProgress = (ViewGroup) v.findViewById(R.id.layTransProgress);

        txtPayName = (TextView) v.findViewById(R.id.txtTransPayName);
        txtPayCat = (TextView) v.findViewById(R.id.txtTransPayCat);
        txtAccName = (TextView) v.findViewById(R.id.txtTransAccName);
        txtRemindTime = (TextView) v.findViewById(R.id.txtTransPayTimeRemind);
        imgRemind = (ImageView) v.findViewById(R.id.imgTransRemind);
        registerForContextMenu(listView);
        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        refreshViewData();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.cmdTransBack:
//                goBack();
//                break;
        }
    }


    protected void refreshViewData() {
        refreshLoader(LOADER_ID, filterParams.getBundle(), loaderCallback);
        txtPayName.setText(filterParams.getPayord().getName());
        txtAccName.setText(filterParams.getPayord().getAccount(context).getNameAndCurrency());
        txtPayCat.setText(filterParams.getPayord().getCategory(context).getName());

        if (filterParams.getTransfer() != null) {
            mAdapter.setSelectedTransfer(filterParams.getTransfer());
        }

        if (filterParams.getPayord().isRemind()) {
            imgRemind.setVisibility(View.VISIBLE);
            txtRemindTime.setText(Utils.floatToTime(filterParams.getPayord().getTimeRemind()));
            if (mAdapter.getIdTransAlarm() != -1) {
                imgRemind.setImageDrawable(getResources().getDrawable(R.drawable.alert_red));
                Utils.anim(imgRemind);
            } else {
                imgRemind.setImageDrawable(getResources().getDrawable(R.drawable.alert_blue));
                imgRemind.clearAnimation();
            }
        } else {
            imgRemind.setVisibility(View.GONE);
            txtRemindTime.setText("");
        }
    }

    LoaderManager.LoaderCallbacks<List<Transfer>> loaderCallback = new LoaderManager.LoaderCallbacks<List<Transfer>>() {
        @Override
        public Loader<List<Transfer>> onCreateLoader(int id, Bundle args) {
            switchVisibility(true, layProgress, listView);
            return new BackgroundLoader(context, backgroundTaskLoad, args);
        }

        @Override
        public void onLoadFinished(Loader<List<Transfer>> listLoader, List<Transfer> transfers) {
            if (listLoader.getId() == LOADER_ID) {
                mAdapter.reloadData(transfers);
                switchVisibility(false, layProgress, listView);
                if (idTransAlarm != -1) {
                    listView.smoothScrollToPosition(mAdapter.getAlarmPos());
                } else {
                    mAdapter.setSelectedNearestCurrDate();
                    listView.smoothScrollToPosition(mAdapter.getSelectedPos());
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Transfer>> listLoader) {
        }
    };

    BackgroundTask backgroundTaskLoad = new BackgroundTask<List<Transfer>>() {
        @Override
        public List<Transfer> load(Bundle args) {
            Payord payord = (Payord) (args.getSerializable(Payord.getBundleKey()));
            return new TransferDAO(context).getListByPayordId(payord.getId());
        }
    };


    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        IconContextMenu cm = new IconContextMenu(getActivity(), R.menu.context_transfer);
        cm.setOnIconContextItemSelectedListener(iconMenuListener);
        cm.setInfo(((AdapterView.AdapterContextMenuInfo) menuInfo).position);
        cm.show();
    }

    IconContextMenu.IconContextItemSelectedListener iconMenuListener = new IconContextMenu.IconContextItemSelectedListener() {
        @Override
        public void onIconContextItemSelected(MenuItem item, Object info) {
            //    Transfer clickedTrans = mAdapter.getTransfer((Integer)info);
            mAdapter.setSelectedPos((Integer) info);
            filterParams.setTransfer(mAdapter.getSelectedTransfer());
            switch (item.getItemId()) {
                case R.id.ctxSet:
                    setPhoto();
                    break;
                case R.id.ctxView:
                    showPhoto(mAdapter.getSelectedTransfer());
                    break;
                case R.id.ctxClear:
                    clearPhoto(mAdapter.getSelectedTransfer());
                    break;
            }

        }
    };

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mAdapter.setSelectedPos(position);
            filterParams.setTransfer(mAdapter.getSelectedTransfer());
            if (mAdapter.getSelectedTransfer().getId() == idTransAlarm) {
                imgRemind.setImageDrawable(getResources().getDrawable(R.drawable.alert_blue));
                imgRemind.clearAnimation();
            }
        }
    };


    private void clearPhoto(final Transfer trans) {
        if ((trans != null) && (trans.getImageName() != null) && (!trans.getImageName().isEmpty())) {
            confirmClearListener.onSetParam(trans.getId());
            ConfirmDialog dlg = new ConfirmDialog(context, confirmClearListener, context.getResources().getString(R.string.mes_confirm_clear_photo));
            dlg.show(getActivity().getSupportFragmentManager(), "");
        } else {
            Toast.makeText(context, getResources().getString(R.string.mes_trans_photo_not_set), Toast.LENGTH_LONG).show();
        }

    }

    ConfirmDialogListener confirmClearListener = new ConfirmDialogListener() {
        Transfer transfer;

        @Override
        public void onSetParam(int param) {
            transfer = new TransferDAO(context).getById(param);
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                File imgFile = null;
                try {
                    imgFile = new File(Utils.getAppPath(context, Utils.TypePath.Images) + "//" + transfer.getImageName());
                    imgFile.delete();
                } catch (SavingException e) {
                    /*NOP*/
                }
                transfer.setImageName("");
                new TransferDAO(context).update(transfer);
                refreshViewData();
            }
        }
    };

    private void showPhoto(Transfer transf) {
        if ((transf.getImageName() != null) && (!transf.getImageName().isEmpty())) {
            File photo = null;
            try {
                photo = new File(Utils.getAppPath(context, Utils.TypePath.Images), String.valueOf(transf.getId()) + Cnt.PHOTO_EXT);
                if (photo.exists()) {
                    ShowPicDialog dlg = new ShowPicDialog(context, BitmapFactory.decodeFile(photo.getAbsolutePath()));
                    dlg.show(getActivity().getSupportFragmentManager(), "");
                } else {
                    //if file not found  - erasing the link
                    transf.setImageName("");
                    new TransferDAO(context).update(transf);
                    String mes = context.getResources().getString(R.string.mes_trans_err_view_photo);
                    Utils.showMessage(getActivity(), "", String.format(mes, photo.getAbsolutePath()), null);
                    refreshViewData();
                }
            } catch (SavingException e) {
                Utils.showMessage(getActivity(), "", e.getMessage(), null);
            }
        } else {
            Utils.showMessage(getActivity(), "", getResources().getString(R.string.mes_trans_photo_not_set), null);
        }
    }

    // CAMERA ==============================================================================================================
    //http://stackoverflow.com/questions/6390163/deleting-a-gallery-image-after-camera-intent-photo-taken


    public void setPhoto() {
        try {
            photoUri = Uri.fromFile(new File(Utils.getAppPath(context, Utils.TypePath.Temp), Cnt.PHOTO_TMP_NAME));
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            lastCapturedPhotoId = getLastImageId();
            startActivityForResult(intent, REG_CAMERA_CODE);
        } catch (SavingException e) {
            Utils.showMessage(getActivity(), "", e.getMessage(), null);
        }
    }

    /**
     * Gets the last image id from the media store
     *
     * @return
     */
    private int getLastImageId() {
        final String[] imageColumns = {MediaStore.Images.Media._ID};
        final String imageOrderBy = MediaStore.Images.Media._ID + " DESC";
        final String imageWhere = null;
        final String[] imageArguments = null;
        Cursor imageCursor = getActivity().managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageColumns, imageWhere, imageArguments, imageOrderBy);
        if (imageCursor.moveToFirst()) {
            int id = imageCursor.getInt(imageCursor.getColumnIndex(MediaStore.Images.Media._ID));
            imageCursor.close();
            return id;
        } else {
            return 0;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REG_CAMERA_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                ContentResolver contentResolver = getActivity().getContentResolver();
                try {
                    if (filterParams.getTransfer() == null) {
                        new SavingException(getResources().getString(R.string.mes_trans_not_selected));
                    }
                    capturedImageMD5 = Utils.fileToMD5(photoUri.getPath());
                    Bitmap bitmap = android.provider.MediaStore.Images.Media.getBitmap(contentResolver, photoUri);
                    ByteArrayOutputStream compresStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, new Preferences(context).getCompressValue(), compresStream);
                    FileOutputStream fos = new FileOutputStream(Utils.getAppPath(context, Utils.TypePath.Images) + "/" + String.valueOf(mAdapter.getSelectedTransfer().getId() + Cnt.PHOTO_EXT));
                    compresStream.writeTo(fos);
                    compresStream.flush();
                    fos.flush();
                    compresStream.close();
                    fos.close();
                    deleteOrigImage(lastCapturedPhotoId);
                    filterParams.getTransfer().setImageName(String.valueOf(mAdapter.getSelectedTransfer().getId()));
                    new TransferDAO(context).update(filterParams.getTransfer());
                    SLog.e("ok 0 ");
                } catch (IOException e) {
                    Utils.procMessage(context, getResources().getString(R.string.mes_trans_err_save_photo) + ": " + e.getMessage());
                } catch (SavingException e) {
                    Utils.procMessage(context, getResources().getString(R.string.mes_trans_err_save_photo) + ": " + e.getMessage());
                }
            } else {
                Utils.procMessage(context, getResources().getString(R.string.mes_trans_err_camera));
            }
        }
    }


/*
 * Delete duplicate images
 * This is necessary because some camera implementation not only save where you want them to save but also in their default location.
 * it call after saved captured image
 */

    private void deleteOrigImage(int capturedId) {
        final String[] imageColumns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_TAKEN, MediaStore.Images.Media.SIZE, MediaStore.Images.Media._ID};
        final String imageOrderBy = MediaStore.Images.Media._ID + " DESC";
        final String imageWhere = MediaStore.Images.Media._ID + ">?";
        final String[] imageArguments = {Integer.toString(capturedId)};
        Cursor imageCursor = getActivity().managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageColumns, imageWhere, imageArguments, imageOrderBy);
        if (imageCursor.moveToFirst()) {
            do {
                // Long takenTimeStamp = imageCursor.getLong(imageCursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN));
                //Long size = imageCursor.getLong(imageCursor.getColumnIndex(MediaStore.Images.Media.SIZE));
                int id = imageCursor.getInt(imageCursor.getColumnIndex(MediaStore.Images.Media._ID));
                String path = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                //  Log.d(Cnt.TAG, "find  path : " + id + "  " + path);
                if (capturedImageMD5 != null && capturedImageMD5.equals(Utils.fileToMD5(path))) {
                    ContentResolver cr = getActivity().getContentResolver();
                    cr.delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media._ID + "=?", new String[]{Long.toString(id)});
                }
            } while (imageCursor.moveToNext());

        }
       // imageCursor.close(); //it's because - http://stackoverflow.com/questions/9696868/unable-to-resume-activity-error
    }
}

//   for saved state
//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//        Log.d(Cnt.TAG, "onSaveInstanceState");
//        super.onSaveInstanceState(savedInstanceState);
//        savedInstanceState.putSerializable(Payord.getBundleKey(), filterParams.getPayord());
//        savedInstanceState.putSerializable(Transfer.getBundleKey(), filterParams.getTransfer());
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        Log.d(Cnt.TAG, "onActivityCreated");
//        super.onActivityCreated(savedInstanceState);
//        if (savedInstanceState != null) {
//            Payord payord = (Payord) (savedInstanceState.getSerializable(Payord.getBundleKey()));
//            Transfer transf = (Transfer) (savedInstanceState.getSerializable(Transfer.getBundleKey()));
//            if (transf != null & payord != null) {
//                filterParams.initParams(payord, transf);
//            }
//        }
//    }



