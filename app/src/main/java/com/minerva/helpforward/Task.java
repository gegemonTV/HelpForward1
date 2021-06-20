package com.minerva.helpforward;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;
import java.lang.String;

public class Task {
    private String name, description;
    private float lat, lon;
    private long gems;

    public Task(){}

    public Task(String name, String description, float lat, float lon, long gems) {
        this.name = name;
        this.description = description;
        this.lat = lat;
        this.lon = lon;
        this.gems = gems;
    }

    public long getGems() {
        return gems;
    }

    public void setGems(long gems) {
        this.gems = gems;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return "Task{" + lat + ", " + lon + "}";
    }
}
