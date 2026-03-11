package com.example.quanlysv;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StudentActivity extends AppCompatActivity {

    private EditText etMSSV, etHoTen, etSoDienThoai, etGmail;
    private Spinner spGioiTinh;
    private TextView tvSelectedPosition;
    private ListView lvStudents;
    private ArrayAdapter<String> adapter; // Adapter hiển thị danh sách sinh viên
    private ArrayAdapter<String> genderAdapter; // Adapter cho Spinner giới tính
    private Student selectedStudent = null; // Lưu sinh viên đang chọn
    private int selectedPosition = -1; // Vị trí sinh viên đang chọn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        // ✅ Ánh xạ View
        etMSSV = findViewById(R.id.etMSSV);
        etHoTen = findViewById(R.id.etHoTen);
        spGioiTinh = findViewById(R.id.spGioiTinh);
        etSoDienThoai = findViewById(R.id.etSoDienThoai);
        etGmail = findViewById(R.id.etGmail);
        tvSelectedPosition = findViewById(R.id.tvSelectedPosition);
        lvStudents = findViewById(R.id.lvStudents);
        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnUpdate = findViewById(R.id.btnUpdate);
        Button btnBack = findViewById(R.id.btnBack);

        // ✅ Spinner giới tính
        genderAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_item_gender,
                new String[]{"Nam", "Nữ"});
        genderAdapter.setDropDownViewResource(R.layout.spinner_item_gender);
        spGioiTinh.setAdapter(genderAdapter);

        // ✅ Load danh sách sinh viên ban đầu
        loadStudents();

        // ✅ Sự kiện nút
        btnAdd.setOnClickListener(v -> addStudent());
        btnUpdate.setOnClickListener(v -> updateStudent());
        btnBack.setOnClickListener(v -> finish());

        // ✅ Sự kiện click chọn 1 sinh viên trong danh sách
        lvStudents.setOnItemClickListener((parent, view, position, id) -> {
            selectedStudent = Database.students.get(position);
            selectedPosition = position;
            tvSelectedPosition.setText("Vị trí đang chọn: " + (position + 1));
            etMSSV.setText(selectedStudent.getMssv());
            etHoTen.setText(selectedStudent.getHoTen());
            int pos = genderAdapter.getPosition(selectedStudent.getGioiTinh());
            if (pos >= 0) spGioiTinh.setSelection(pos);
            etSoDienThoai.setText(selectedStudent.getSoDienThoai());
            etGmail.setText(selectedStudent.getGmail());
            etMSSV.setEnabled(false); // Không cho sửa MSSV
        });

        // ✅ Sự kiện giữ lâu để xóa
        lvStudents.setOnItemLongClickListener((parent, view, position, id) -> {
            showDeleteConfirmationDialog(position);
            return true;
        });
    }

    // ✅ Hiển thị danh sách sinh viên - mỗi thông tin 1 dòng
    private void loadStudents() {
        ArrayList<String> studentDisplayList = (ArrayList<String>) Database.students.stream()
                .map(s ->
                        "MSSV: " + s.getMssv() + "\n" +
                                "Họ tên: " + s.getHoTen() + "\n" +
                                "Giới tính: " + s.getGioiTinh() + "\n" +
                                "SĐT: " + s.getSoDienThoai() + "\n" +
                                "Gmail: " + s.getGmail()
                )
                .collect(Collectors.toList());
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, studentDisplayList);
        lvStudents.setAdapter(adapter);
    }

    // ✅ Hàm kiểm tra dữ liệu nhập
    private boolean validateInput(String mssv, String hoTen, String gioiTinh, String sdt, String gmail, boolean checkDuplicate) {
        if (!Pattern.matches("\\d{8}", mssv)) {
            Toast.makeText(this, "MSSV phải đúng 8 chữ số", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (checkDuplicate && Database.students.stream().anyMatch(s -> s.getMssv().equalsIgnoreCase(mssv))) {
            Toast.makeText(this, "MSSV đã tồn tại!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (hoTen.length() < 5) {
            Toast.makeText(this, "Họ và tên phải từ 10 ký tự trở lên", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!sdt.isEmpty() && !Pattern.matches("0\\d{9}", sdt)) {
            Toast.makeText(this, "Số điện thoại phải có 10 số và bắt đầu bằng 0", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!gmail.isEmpty() && !gmail.endsWith("@gmail.com")) {
            Toast.makeText(this, "Gmail phải có đuôi @gmail.com", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // ✅ Thêm sinh viên
    private void addStudent() {
        String mssv = etMSSV.getText().toString().trim();
        String hoTen = etHoTen.getText().toString().trim();
        String gioiTinh = spGioiTinh.getSelectedItem().toString();
        String sdt = etSoDienThoai.getText().toString().trim();
        String gmail = etGmail.getText().toString().trim();

        if (!validateInput(mssv, hoTen, gioiTinh, sdt, gmail, true)) return;

        Student newStudent = new Student(mssv, hoTen, gioiTinh, sdt, gmail);
        Database.students.add(newStudent);

        // Format xuống dòng khi thêm mới
        String displayText =
                "MSSV: " + mssv + "\n" +
                        "Họ tên: " + hoTen + "\n" +
                        "Giới tính: " + gioiTinh + "\n" +
                        "SĐT: " + sdt + "\n" +
                        "Gmail: " + gmail;

        adapter.add(displayText);
        adapter.notifyDataSetChanged();

        clearFields();
        Toast.makeText(this, "Thêm sinh viên thành công", Toast.LENGTH_SHORT).show();
    }

    // ✅ Cập nhật sinh viên
    private void updateStudent() {
        if (selectedStudent == null) {
            Toast.makeText(this, "Vui lòng chọn sinh viên để cập nhật", Toast.LENGTH_SHORT).show();
            return;
        }

        String hoTen = etHoTen.getText().toString().trim();
        String gioiTinh = spGioiTinh.getSelectedItem().toString();
        String sdt = etSoDienThoai.getText().toString().trim();
        String gmail = etGmail.getText().toString().trim();

        if (!validateInput(selectedStudent.getMssv(), hoTen, gioiTinh, sdt, gmail, false)) return;

        selectedStudent.setHoTen(hoTen);
        selectedStudent.setGioiTinh(gioiTinh);
        selectedStudent.setSoDienThoai(sdt);
        selectedStudent.setGmail(gmail);

        loadStudents(); // Reload lại danh sách sau khi update
        clearFields();
        Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
    }

    // ✅ Hộp thoại xác nhận xóa
    private void showDeleteConfirmationDialog(final int position) {
        new AlertDialog.Builder(this)
                .setMessage("Bạn có muốn xóa sinh viên")
                .setPositiveButton("Có", (dialog, which) -> {
                    new AlertDialog.Builder(this)
                            .setTitle("Xác nhận")
                            .setIcon(android.R.drawable.ic_delete)
                            .setPositiveButton("Có", (d, w) -> {
                                String mssvToDelete = Database.students.get(position).getMssv();

                                // ✅ Xóa sinh viên
                                Database.students.remove(position);

                                // ✅ Xóa điểm liên quan đến sinh viên này
                                Database.grades.removeIf(g -> g.getMssv().equals(mssvToDelete));

                                loadStudents();
                                clearFields();
                                Toast.makeText(StudentActivity.this, "Đã xóa thành công", Toast.LENGTH_SHORT).show();
                            })
                            .setNegativeButton("Không", (d, w) -> {
                                Toast.makeText(StudentActivity.this, "Không thay đổi", Toast.LENGTH_SHORT).show();
                            })
                            .show();
                })
                .setNegativeButton("Không", null)
                .show();
    }


    // ✅ Xóa dữ liệu trong các ô nhập
    private void clearFields() {
        etMSSV.setText("");
        etHoTen.setText("");
        spGioiTinh.setSelection(0);
        etSoDienThoai.setText("");
        etGmail.setText("");
        etMSSV.setEnabled(true);
        tvSelectedPosition.setText("Vị trí đang chọn: 0");
        selectedStudent = null;
        selectedPosition = -1;
        lvStudents.clearChoices();
        adapter.notifyDataSetChanged();
    }
}
