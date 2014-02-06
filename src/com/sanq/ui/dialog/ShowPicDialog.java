package com.sanq.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import com.sanq.moneys.R;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 17.06.13
 * Time: 15:42
 */

public class ShowPicDialog extends DialogFragment {
    Context context;
    Bitmap bitmap;
    LayoutInflater inflater;

    public ShowPicDialog(Context context, Bitmap bitmap) {
        this.bitmap = bitmap;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View v = inflater.inflate(R.layout.dialog_show_pic, null);
        ImageView imgPhoto = (ImageView) v.findViewById(R.id.imgDialogShowPic);
        imgPhoto.setImageBitmap(bitmap);

        builder.setView(v)
                .setPositiveButton(R.string.menu_capt_close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        return builder.create();
    }
}
