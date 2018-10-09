package com.importio.nitin.bakers.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "Receipe")
public class ReceipeEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private int servings;


    public ReceipeEntry(int id, String name, int servings) {
        this.id = id;
        this.name = name;
        this.servings = servings;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
