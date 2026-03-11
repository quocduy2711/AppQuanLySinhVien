package com.example.quanlysv;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class GradeActivity extends AppCompatActivity {

    private Spinner spinnerSubjects, spinnerStudents;
    private EditText etGrade;
    private ListView lvSubjects;
    private ArrayAdapter<String> gradeAdapter; // hiển thị text
    private ArrayList<String> gradeDisplayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);

        //ánh xạ
        spinnerSubjects = findViewById(R.id.spinnerSubjects);
        spinnerStudents = findViewById(R.id.spinnerStudents);
        etGrade = findViewById(R.id.etGrade);
        lvSubjects = findViewById(R.id.lvSubjects);

        Button btnUpdateGrade = findViewById(R.id.btnUpdateGrade);
        Button btnBack = findViewById(R.id.btnBack);

        gradeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, gradeDisplayList);
        lvSubjects.setAdapter(gradeAdapter);

        loadSpinners();
        loadGrades();

        btnUpdateGrade.setOnClickListener(v -> updateGrade());
        btnBack.setOnClickListener(v -> finish());

        lvSubjects.setOnItemLongClickListener((parent, view, position, id) -> {
            confirmDelete(position);
            return true;
        });
    }

    private void loadSpinners() {
        ArrayList<String> subjectList = (ArrayList<String>) Database.subjects.stream()
                .map(Subject::toString)
                .collect(Collectors.toList());
        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjectList);
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSubjects.setAdapter(subjectAdapter);

        ArrayList<String> studentList = (ArrayList<String>) Database.students.stream()
                .map(Student::toString)
                .collect(Collectors.toList());
        ArrayAdapter<String> studentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, studentList);
        studentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStudents.setAdapter(studentAdapter);
    }

    private void loadGrades() {
        gradeDisplayList.clear();
        for (Grade g : Database.grades) {
            gradeDisplayList.add(g.toString());
        }
        gradeAdapter.notifyDataSetChanged();
    }

    private void updateGrade() {
        if (spinnerStudents.getSelectedItem() == null || spinnerSubjects.getSelectedItem() == null) {
            Toast.makeText(this, "Không có sinh viên hoặc môn học để chấm điểm", Toast.LENGTH_SHORT).show();
            return;
        }

        String gradeStr = etGrade.getText().toString().trim();
        if (gradeStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập điểm", Toast.LENGTH_SHORT).show();
            return;
        }

        float gradeValue;
        try {
            gradeValue = Float.parseFloat(gradeStr);
            if (gradeValue < 0 || gradeValue > 10) {
                Toast.makeText(this, "Điểm phải từ 0 đến 10", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Điểm không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        int studentIndex = spinnerStudents.getSelectedItemPosition();
        int subjectIndex = spinnerSubjects.getSelectedItemPosition();
        Student student = Database.students.get(studentIndex);
        Subject subject = Database.subjects.get(subjectIndex);

        Grade newGrade = new Grade(student.getMssv(), student.getHoTen(), subject.toString(), gradeValue);
        Database.grades.add(newGrade);

        loadGrades();
        etGrade.setText("");
        Toast.makeText(this, "Đã cập nhật điểm cho " + student.getHoTen(), Toast.LENGTH_SHORT).show();
    }

    private void confirmDelete(final int position) {
        new AlertDialog.Builder(this)
                .setMessage("Bạn muốn xóa điểm?")
                .setPositiveButton("Có", (dialog, which) -> {
                    new AlertDialog.Builder(this)
                            .setTitle("Xác nhận")
                            .setIcon(android.R.drawable.ic_delete)
                            .setPositiveButton("Có", (d, w) -> {
                                Database.grades.remove(position);
                                loadGrades();
                                Toast.makeText(GradeActivity.this, "Đã xóa điểm", Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton("Không", (d, w) -> {
                                Toast.makeText(GradeActivity.this, "Không thay đổi", Toast.LENGTH_SHORT).show();
                            })
                            .show();
                })
                .setNegativeButton("Không", null)
                .show();
    }
}
