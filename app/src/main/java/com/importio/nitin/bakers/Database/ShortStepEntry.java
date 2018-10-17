package com.importio.nitin.bakers.Database;

public class ShortStepEntry {
    private int id;
    private String shortDesc;

    ShortStepEntry(int id, String shortDesc) {
        this.id = id;
        this.shortDesc = shortDesc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }
}
