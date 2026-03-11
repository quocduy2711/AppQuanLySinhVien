package com.example.quanlysv;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnManageStudents = findViewById(R.id.btnManageStudents);
        Button btnManageSubjects = findViewById(R.id.btnManageSubjects);
        Button btnManageGrades = findViewById(R.id.btnManageGrades);
        Button btnExit = findViewById(R.id.btnExit);

        btnManageStudents.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, StudentActivity.class));
        });

        btnManageSubjects.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SubjectActivity.class));
        });

        btnManageGrades.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, GradeActivity.class));
        });

        btnExit.setOnClickListener(v -> {
            finishAffinity(); // Đóng toàn bộ ứng dụng
        });
    }
}