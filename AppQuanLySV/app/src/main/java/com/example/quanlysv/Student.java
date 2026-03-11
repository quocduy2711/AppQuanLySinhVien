package com.example.quanlysv;

import androidx.annotation.NonNull;

public class Student {
    private String mssv;
    private String hoTen;
    private String gioiTinh;
    private String soDienThoai;
    private String gmail;

    public Student(String mssv, String hoTen, String gioiTinh, String soDienThoai, String gmail) {
        this.mssv = mssv;
        this.hoTen = hoTen;
        this.gioiTinh = gioiTinh;
        this.soDienThoai = soDienThoai;
        this.gmail = gmail;
    }

    // Getters
    public String getMssv() { return mssv; }
    public String getHoTen() { return hoTen; }
    public String getGioiTinh() { return gioiTinh; }
    public String getSoDienThoai() { return soDienThoai; }
    public String getGmail() { return gmail; }

    // Setters
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public void setGioiTinh(String gioiTinh) { this.gioiTinh = gioiTinh; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }
    public void setGmail(String gmail) { this.gmail = gmail; }

    @NonNull
    @Override
    public String toString() {
        return mssv + " " + hoTen + " " + gioiTinh + " " + soDienThoai + " " + gmail;
    }
}