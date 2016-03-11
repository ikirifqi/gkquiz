package com.nextinnovation.gkquiz.entities;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by rifqi on Feb 15, 2016.
 */
public class BaseEntity implements Serializable {
    @DatabaseField(columnName = "id", generatedId = true)
    protected int id;

    protected BaseEntity() {
        // prevent creating instance
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
