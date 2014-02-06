package com.sanq.entity;

import android.content.Context;
import com.sanq.dao.PayordDAO;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 29.05.13
 * Time: 11:04
 */
public class Template extends AbstractEntity  implements Entity {
    private int id ;
    private String name;
    private int idPayord;
    private boolean active = true;
    private Date dt_create;
    private Date dt_update;

    public Payord getPayord(Context context){
        Payord pay = new PayordDAO(context).getById(this.getIdPayord(),true);
        return pay;
    }

    public Template() {
    }

    public Template(String name, int idPayord) {
        this.name = name;
        this.idPayord = idPayord;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdPayord() {
        return idPayord;
    }

    public void setIdPayord(int idPayord) {
        this.idPayord = idPayord;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getDt_create() {
        return dt_create;
    }

    public void setDt_create(Date dt_create) {
        this.dt_create = dt_create;
    }

    public Date getDt_update() {
        return dt_update;
    }

    public void setDt_update(Date dt_update) {
        this.dt_update = dt_update;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Template template = (Template) o;

        if (active != template.active) return false;
        if (idPayord != template.idPayord) return false;
        if (name != null ? !name.equals(template.name) : template.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Template{" +
                "\nid=" + id +
                "\n, name='" + name + '\'' +
                "\n, idPayord=" + idPayord +
                "\n, active=" + active +
                "\n, dt_create=" + dt_create +
                "\n, dt_update=" + dt_update +
                '}';
    }
}
