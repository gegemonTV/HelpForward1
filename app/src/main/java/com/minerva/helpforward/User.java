package com.minerva.helpforward;

public class User {
    long completed_tasks, crystals, karma;
    public User() {
    }

    public User(long completed_tasks, long crystals, long karma) {
        this.completed_tasks = completed_tasks;
        this.crystals = crystals;
        this.karma = karma;
    }

    public long getCompleted_tasks() {
        return completed_tasks;
    }

    public long getCrystals() {
        return crystals;
    }

    public long getKarma() {
        return karma;
    }

    public void setCompleted_tasks(long completed_tasks) {
        this.completed_tasks = completed_tasks;
    }

    public void setCrystals(long crystals) {
        this.crystals = crystals;
    }

    public void setKarma(long karma) {
        this.karma = karma;
    }

    @Override
    public String toString() {
        return "User{" +
                "completed_tasks=" + completed_tasks +
                ", crystals=" + crystals +
                ", karma=" + karma +
                '}';
    }
}
