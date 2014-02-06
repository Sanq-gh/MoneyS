package com.sanq.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sanq.dao.CategoryDAO;
import com.sanq.entity.Category;
import com.sanq.exception.SavingException;
import com.sanq.moneys.R;
import com.sanq.utils.Utils;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 14.06.13
 * Time: 14:05
 */
public class CategoryAdd extends AbstractFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    List<Category> parentCats;
    ArrayAdapter<String> adapter;
    Category updatedCat = null;

    EditText txtCatName;
    Spinner spinCatParent;
    Button cmdSave;
    Button cmdCancel;


    public CategoryAdd() {
    }

    public CategoryAdd(Category updatedCat) {
        this.updatedCat = updatedCat;

    }


    @Override
    protected String getHeadCaption() {
        return context.getResources().getString(R.string.title_cat_activity_add);
    }
    @Override
    protected int getHeadIcon() {
        return R.drawable.category;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.category_add, null);

        txtCatName = (EditText) v.findViewById(R.id.txtCatName);

        cmdSave = (Button) v.findViewById(R.id.cmdCatSave);
        cmdSave.setOnClickListener(this);
        cmdCancel = (Button) v.findViewById(R.id.cmdCatCancel);
        cmdCancel.setOnClickListener(this);

        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item);
        refreshData();
        adapter.setDropDownViewResource(R.layout.my_simple_spinner_dropdown_item);

        spinCatParent = (Spinner) v.findViewById(R.id.spinCatNameParent);
        spinCatParent.setAdapter(adapter);
        spinCatParent.setOnItemSelectedListener(this);

        // fill controls if get cats for update...
        refreshViewData();

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cmdCatSave:
                saveData();
                break;
            case R.id.cmdCatCancel:
               goBack();
               break;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


    protected void refreshViewData() {
        if (updatedCat != null) {
            txtCatName.setText(updatedCat.getName());
            int parentPos = Utils.getObjectPos(updatedCat.getParent(), parentCats);
            if (parentPos != -1) {
                spinCatParent.setSelection(parentPos);
            }
        }
    }

    private void refreshData(){
        parentCats  = Category.getAllParents(context);
        parentCats.add(0, new Category("", 0, true));
        if (parentCats != null && adapter != null) {
            adapter.clear();
            for (Category cat : parentCats) {
                adapter.add(cat.getName());
            }
        }
    }


    private void saveData() {
        String catName = txtCatName.getText().toString().trim();
        try {
            if (catName.isEmpty()) {
                throw new SavingException(context.getResources().getString(R.string.mes_cat_empty_name));
            }
            int idParent = this.parentCats.get(spinCatParent.getSelectedItemPosition()).getId();

            if (updatedCat == null) {
                Category cat = new Category(catName, idParent, idParent == 0);
                new CategoryDAO(context).add(cat);
            } else {
                updatedCat.setName(catName);
                updatedCat.setId_parent(idParent);
                updatedCat.setIsgroup(idParent == 0);
                new CategoryDAO(context).update(updatedCat);
            }
            goBack();

        } catch (Exception ex) {
            Utils.showMessage(getActivity(),"", ex.getMessage(), null);
        }
    }

}




