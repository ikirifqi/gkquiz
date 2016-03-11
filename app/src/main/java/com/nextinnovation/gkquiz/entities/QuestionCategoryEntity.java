package com.nextinnovation.gkquiz.entities;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by rifqi on Feb 15, 2016.
 */
public class QuestionCategoryEntity extends BaseEntity {
    @DatabaseField(columnName = "name", canBeNull = false)
    private String name;

    // this to mark as answered question
    public boolean mark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
