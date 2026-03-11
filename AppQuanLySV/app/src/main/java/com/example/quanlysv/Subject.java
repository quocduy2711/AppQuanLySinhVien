package com.example.quanlysv;

import androidx.annotation.NonNull;

public class Subject {
    private String maMon;
    private String tenMon;

    public Subject(String maMon, String tenMon) {
        this.maMon = maMon;
        this.tenMon = tenMon;
    }

    // Getters
    public String getMaMon() { return maMon; }
    public String getTenMon() { return tenMon; }

    // Setters
    public void setTenMon(String tenMon) { this.tenMon = tenMon; }

    @NonNull
    @Override
    public String toString() {
        return maMon + " - " + tenMon;
    }
}