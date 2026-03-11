package com.example.quanlysv;

public class Grade {
    private String mssv;
    private String hoTen;
    private String subjectInfo;
    private float grade;

    public Grade(String mssv, String hoTen, String subjectInfo, float grade) {
        this.mssv = mssv;
        this.hoTen = hoTen;
        this.subjectInfo = subjectInfo;
        this.grade = grade;
    }

    public String getMssv() {
        return mssv;
    }

    public String getHoTen() {
        return hoTen;
    }

    public String getSubjectInfo() {
        return subjectInfo;
    }

    public float getGrade() {
        return grade;
    }

    @Override
    public String toString() {
        return "MSSV: " + mssv + "\n" +
                "Họ tên: " + hoTen + "\n" +
                "Môn học: " + subjectInfo + "\n" +
                "Điểm: " + grade;
    }
}
