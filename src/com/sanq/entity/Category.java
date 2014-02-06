package com.sanq.entity;

import android.content.Context;
import com.sanq.dao.CategoryDAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 29.05.13
 * Time: 11:17
 */
public class Category extends AbstractEntity implements Entity, Comparable<Category> , Serializable {

    // static sections........................................
    private static final int idTransferCat =  1;
    private static final int idWithoutCat  =  2;

    public static String getBundleKey() {
        return "CATEGORY";
    }
    public static Category getTransferCategory(Context context){
        return new CategoryDAO(context).getById(Category.idTransferCat, true);
    }
    public static Category getWithoutCategory(Context context){
        return new CategoryDAO(context).getById(Category.idWithoutCat, true);
    }

    public List<String> getAllParentNames(Context context) {
        List<String> res = new ArrayList<String>() ;
        List<Category> cats = getAllParents(context);
        for (Category cat : cats) {
            res.add(cat.getName());
        }
        return res;
    }

    public static List<Category> getAllParents(Context context){
        return  new CategoryDAO(context).getByCondition("isgroup = ?" , new String[]{"1"});
    }

    public static List<Category> getChilds(Context context, Category parentCat) {
        return  new CategoryDAO(context).getByCondition("id_parent = ?" , new String[]{String.valueOf(parentCat.getId())});
    }

    public static List<Category> getAllLinked(Context context) {
        List<Category> cats = getAllParents(context);
        for (Category group : cats) {
            List<Category> thisChilds = getChilds(context, group);
            for (Category child : thisChilds) {
                child.setParent(group);
                group.addChild(child);
            }
        }
        return cats;
    }

    //.......................................................



    private int id ;
    private String name;
    private int id_parent;
    private boolean isgroup;
    private boolean active = true;
    private Date dt_create;
    private Date dt_update;

    /**
     *************************************************************
     * follow works only if it get from CategoryDAO.getAllLinked ...
     */
    private Category parent;
    private List<Category> childs = new LinkedList<Category>();
    /************************************************************/

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public List<Category> getChilds() {
        return childs;
    }

    public void addChild(Category child) {
        childs.add(child);
    }
    public void addChild(List<Category> childs) {
        this.childs.addAll(childs);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDt_create(Date dt_create) {
        this.dt_create = dt_create;
    }

    public void setDt_update(Date dt_update) {
        this.dt_update = dt_update;
    }

    public Category() {
    }

    public Category(String name, int id_parent, boolean isgroup) {
        this.name = name;
        this.id_parent = id_parent;
        this.isgroup = isgroup;
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId_parent(int id_parent) {
        this.id_parent = id_parent;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setIsgroup(boolean isgroup) {
        this.isgroup = isgroup;
    }

    public int getId_parent() {
        return id_parent;
    }

    public boolean isGroup() {
        return isgroup;
    }

    public Date getDt_create() {
        return dt_create;
    }

    public Date getDt_update() {
        return dt_update;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        if (active != category.active) return false;
        if (id_parent != category.id_parent) return false;
        if (isgroup != category.isgroup) return false;
        if (!name.equals(category.name)) return false;
        return true;
    }



    public int hashCodeInteger() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + id_parent;
        return result;
    }


    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "\nCategory{" +
                "\nid=" + id +
                "\n, name='" + name + '\'' +
                "\n, id_parent=" + id_parent +
                "\n, isgroup=" + isgroup +
                "\n, dt_create=" + dt_create +
                "\n, dt_update=" + dt_update +
                '}';
    }

    @Override
    public int compareTo(Category another) {
        return this.getId() - another.getId();
    }

}
