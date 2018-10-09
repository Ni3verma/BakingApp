package com.importio.nitin.bakers.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "Step")
public class StepEntry {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int receipeId;
    private String shortDesc;
    private String desc;
    private String videoURL;
    private String thumbnailURL;

    @Ignore
    public StepEntry(int receipeId, String shortDesc, String desc, String videoURL, String thumbnailURL) {
        this.receipeId = receipeId;
        this.shortDesc = shortDesc;
        this.desc = desc;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    public StepEntry(int id, int receipeId, String shortDesc, String desc, String videoURL, String thumbnailURL) {
        this.id = id;
        this.receipeId = receipeId;
        this.shortDesc = shortDesc;
        this.desc = desc;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReceipeId() {
        return receipeId;
    }

    public void setReceipeId(int receipeId) {
        this.receipeId = receipeId;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    @Override
    public String toString() {
        return id + " - " + receipeId + " - " + shortDesc;
    }
}
