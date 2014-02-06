package com.sanq.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.sanq.moneys.R;
import com.sanq.utils.Utils;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sanq
 * Date: 19.06.13
 * Time: 21:57
 * To change this template use File | Settings | File Templates.
 */
public class FileListAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layInflater ;
    List<File> files;
    String dir;

        public FileListAdapter(Context context, String dir) {
        this.context = context;
        layInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.dir = dir;
        reloadData();
        }

        public void reloadData(){
            files = Utils.GetFiles(dir);
            Collections.sort(files, DATE_COMPARATOR);
            notifyDataSetChanged();
        }

    public File getFile (int pos ){
        if (  pos >= 0 && pos < files.size()){
            return files.get(pos);
        }else{
            return  null;
        }
    }


    public static final Comparator<File> DATE_COMPARATOR = new Comparator<File>() {
      //  private final Collator sCollator = Collator.getInstance();
        @Override
        public int compare(File object1, File object2) {
            if (object2.lastModified() < object1.lastModified()) {
                return   -1;
            }else if (object2.lastModified() > object1.lastModified()){
                return   1;
            }else{
                return  0;
            }
        }
    };



    public String getDir() {
        return dir;
    }

    @Override
    public int getCount() {
        return files.size();
    }

    @Override
    public Object getItem(int position) {
        return files.get(position).getName();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            v = this.layInflater.inflate(R.layout.template_row_files, null);
        }

        TextView txtFileName = (TextView)v.findViewById(R.id.rowBackupFilesFileName);
        TextView txtFileDate = (TextView)v.findViewById(R.id.rowBackupFilesFileDate);
        TextView txtFileTime = (TextView)v.findViewById(R.id.rowBackupFilesFileTime);

        File file  = files.get(position);

        txtFileName.setText(file.getName());
        txtFileDate.setText(Utils.dateToString(context, new Date(file.lastModified())));
        txtFileTime.setText(Utils.getStringFromTime(new Date(file.lastModified())));
        return v;
    }
}

