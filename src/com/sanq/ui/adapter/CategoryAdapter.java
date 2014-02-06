package com.sanq.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.sanq.entity.Category;
import com.sanq.moneys.R;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



/**
 * Created with IntelliJ IDEA.
 * User: sanq
 * Date: 19.06.13
 * Time: 21:57
 * To change this template use File | Settings | File Templates.
 */
public class CategoryAdapter extends BaseExpandableListAdapter{
    Context context;
    LayoutInflater layInflater ;
    List<Category> catsLinked ;


        public CategoryAdapter(Context context) {
          this.context = context;

          layInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
             catsLinked = new ArrayList<Category>();
        }

        public void reloadData(List<Category> catsLinked ){
            this.catsLinked = catsLinked;
            Collections.sort(this.catsLinked, ALPHA_COMPARATOR);
            notifyDataSetChanged();
        }


    public static final Comparator<Category> ALPHA_COMPARATOR = new Comparator<Category>() {
        private final Collator sCollator = Collator.getInstance();
        @Override
        public int compare(Category object1, Category object2) {
            return sCollator.compare(object1.getName(),object2.getName());
        }
    };



    public Category  getCategory( int groupPos, int childPos) {
            return childPos == -1 ?  catsLinked.get(groupPos) : catsLinked.get(groupPos).getChilds().get(childPos) ;
        }

        public Object getChild(int groupPosition, int childPosition) {
            return catsLinked.get(groupPosition).getChilds().get(childPosition).getName();
        }

        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        public int getChildrenCount(int groupPosition) {
            return catsLinked.get(groupPosition).getChilds().size();
            //return children[groupPosition].length;
        }

        public TextView getGenericView(int id_layout) {
            TextView textView = (TextView) layInflater.inflate(id_layout, null);
            return textView;
        }

        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            TextView textView = getGenericView(R.layout.template_text_view_child);
            textView.setText(getChild(groupPosition, childPosition).toString());
            return textView;
        }

        public Object getGroup(int groupPosition) {
            return catsLinked.get(groupPosition).getName();
        }

        public int getGroupCount() {
            return catsLinked.size();
        }

        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            TextView textView = getGenericView(R.layout.template_text_view_group);
            textView.setText(getGroup(groupPosition).toString());
            return textView;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        public boolean hasStableIds() {
            return true;
        }

    }

