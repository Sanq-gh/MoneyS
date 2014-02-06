package com.sanq.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import com.sanq.moneys.R;
import com.sanq.ui.adapter.FileListAdapter;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 17.06.13
 * Time: 15:42
 */

public class FileListDialog extends DialogFragment {
    FileListAdapter adapter;
    DialogInterface.OnClickListener onClickListener;

    public FileListDialog(FileListAdapter adapter, DialogInterface.OnClickListener onClickListener) {
        this.adapter = adapter;
        this.onClickListener = onClickListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(adapter.getDir());
        builder.setAdapter(adapter, onClickListener);

        builder.setNegativeButton(R.string.btn_capt_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        return builder.create();
    }
}
