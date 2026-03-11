package com.example.quanlysv;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.stream.Collectors;
import androidx.appcompat.app.AlertDialog;


public class SubjectActivity extends AppCompatActivity {

    private EditText etMaMon, etTenMon;
    private ListView lvSubjects;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> subjectDisplayList;
    private Subject selectedSubject = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        etMaMon = findViewById(R.id.etMaMon);
        etTenMon = findViewById(R.id.etTenMon);
        lvSubjects = findViewById(R.id.lvSubjects);
        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnUpdate = findViewById(R.id.btnUpdate);
        Button btnBack = findViewById(R.id.btnBack);

        loadSubjects();

        btnAdd.setOnClickListener(v -> addSubject());
        btnUpdate.setOnClickListener(v -> updateSubject());
        btnBack.setOnClickListener(v -> finish());

        lvSubjects.setOnItemClickListener((parent, view, position, id) -> {
            selectedSubject = Database.subjects.get(position);
            etMaMon.setText(selectedSubject.getMaMon());
            etTenMon.setText(selectedSubject.getTenMon());
            etMaMon.setEnabled(false); // Không cho sửa mã môn
        });

        lvSubjects.setOnItemLongClickListener((parent, view, position, id) -> {
            confirmDelete(position); // Gọi hàm xác nhận xóa
            return true; // Đánh dấu sự kiện đã xử lý
        });
    }



    private void loadSubjects() {
        subjectDisplayList = (ArrayList<String>) Database.subjects.stream()
                .map(Subject::toString)
                .collect(Collectors.toList());
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, subjectDisplayList);
        lvSubjects.setAdapter(adapter);
    }

    private void addSubject() {
        String maMon = etMaMon.getText().toString().trim();
        String tenMon = etTenMon.getText().toString().trim();

        if (maMon.isEmpty() || tenMon.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isDuplicate = Database.subjects.stream().anyMatch(s -> s.getMaMon().equalsIgnoreCase(maMon));
        if (isDuplicate) {
            Toast.makeText(this, "Mã môn đã tồn tại!", Toast.LENGTH_SHORT).show();
            return;
        }

        Database.subjects.add(new Subject(maMon, tenMon));

        loadSubjects(); // ✅ Cập nhật ListView ngay sau khi thêm
        clearFields();  // ✅ Reset form nhập
        Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
    }


    private void updateSubject() {
        if (selectedSubject == null) {
            Toast.makeText(this, "Vui lòng chọn môn để sửa", Toast.LENGTH_SHORT).show();
            return;
        }

        String tenMon = etTenMon.getText().toString().trim();
        if (tenMon.isEmpty()) {
            Toast.makeText(this, "Tên môn không được để trống", Toast.LENGTH_SHORT).show();
            return;
        }

        selectedSubject.setTenMon(tenMon);

        loadSubjects(); // ✅ Cập nhật ListView ngay sau khi sửa
        clearFields();  // ✅ Reset form nhập
        Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
    }

    private void confirmDelete(final int position) {
        new AlertDialog.Builder(this)
                .setMessage("Bạn muốn xóa môn học này?")
                .setPositiveButton("Có", (dialog, which) -> {
                    new AlertDialog.Builder(this)
                            .setTitle("Xác nhận lần cuối")
                            .setIcon(android.R.drawable.ic_delete)
                            .setPositiveButton("Có", (d, w) -> {
                                String subjectInfoToDelete = Database.subjects.get(position).toString();

                                // ✅ Xóa môn học
                                Database.subjects.remove(position);

                                // ✅ Xóa điểm liên quan đến môn học này
                                Database.grades.removeIf(g -> g.getSubjectInfo().equals(subjectInfoToDelete));

                                loadSubjects();
                                clearFields();
                                Toast.makeText(SubjectActivity.this, "Đã xóa môn học", Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton("Không", (d, w) -> {
                                Toast.makeText(SubjectActivity.this, "Đã giữ lại môn học", Toast.LENGTH_SHORT).show();
                            })
                            .show();
                })
                .setNegativeButton("Không", null)
                .show();
    }


    private void clearFields() {
        etMaMon.setText("");
        etTenMon.setText("");
        etMaMon.setEnabled(true);
        selectedSubject = null;
    }
}