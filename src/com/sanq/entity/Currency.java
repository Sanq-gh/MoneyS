package com.sanq.entity;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Sanq
 * Date: 29.05.13
 * Time: 11:04
 */

@Deprecated
public class Currency implements Entity {
    private int id ;
    private String name;
    private boolean active = true;
    private Date dt_create;
    private Date dt_update;

    public Currency() {
    }

    public Currency(String name) {
        this.name = name;
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

        Currency currency = (Currency) o;

        if (active != currency.active) return false;
        if (name != null ? !name.equals(currency.name) : currency.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "\nCurrency{" +
                "\nid=" + id +
                "\n, name='" + name + '\'' +
                "\n, active=" + active +
                "\n, dt_create=" + dt_create +
                "\n, dt_update=" + dt_update +
                '}';
    }
}
