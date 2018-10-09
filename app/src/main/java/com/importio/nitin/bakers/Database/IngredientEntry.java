package com.importio.nitin.bakers.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "Ingredient")
public class IngredientEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int receipeId;
    private int qty;
    private String measure;
    private String name;

    @Ignore
    public IngredientEntry(int receipeId, int qty, String measure, String name) {
        this.receipeId = receipeId;
        this.qty = qty;
        this.measure = measure;
        this.name = name;
    }

    public IngredientEntry(int id, int receipeId, int qty, String measure, String name) {
        this.id = id;
        this.receipeId = receipeId;
        this.qty = qty;
        this.measure = measure;
        this.name = name;
    }

    @Override
    public String toString() {
        return id + " - " + receipeId + " - " + qty + " - " + measure + " - " + name;
    }

    public int getReceipeId() {
        return receipeId;
    }

    public void setReceipeId(int receipeId) {
        this.receipeId = receipeId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
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
