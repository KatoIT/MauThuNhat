package com.example.mauthunhat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class AddObjectActivity extends AppCompatActivity {
    private EditText editTextA, editTextA2, editTextB;
    private Button buttonThem;
    private RadioButton radioButton1, radioButton2;
    private SanPham mSanPham;
    private Database database = new Database(AddObjectActivity.this);
    private VarFinal mVarFinal = new VarFinal();
    private int id;
    private Boolean col4;
    private String col1, col2, col3;
    private boolean isSuccessful = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_object);
        // Ánh xạ
        editTextA = findViewById(R.id.editTextA);
        editTextA2 = findViewById(R.id.editTextA2);
        editTextB = findViewById(R.id.editTextB);
        radioButton1 = findViewById(R.id.radioButton1);
        radioButton2 = findViewById(R.id.radioButton2);
        buttonThem = findViewById(R.id.buttonThem);
        // Khai báo
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("katoit");
        id = bundle.getInt("id");
        int isEdit = bundle.getInt("isEdit");
        // Xử lý
        if (isEdit == 1) {
            buttonThem.setText("Cập nhật");
            Cursor cursor = database.GetData("SELECT * FROM " + mVarFinal.TABLENAME + " WHERE Id = " + id + ";");
            cursor.moveToFirst();
            col1 = cursor.getString(1);
            col2 = cursor.getString(2);
            col3 = cursor.getString(3);
            col4 = Boolean.valueOf(cursor.getString(4));
            // set Text cho EditText
            editTextA.setText(col1);
            editTextA2.setText(col2);
            editTextB.setText(col3);
            radioButton1.setChecked(col4);
            radioButton2.setChecked(!col4);
        } else {
            buttonThem.setText("Thêm");
        }
        buttonThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValidate()) {
                    Toast.makeText(AddObjectActivity.this, "Không được để trống thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (isEdit == 1) {
                    database.Update(mSanPham);
                    isSuccessful = true;
                } else {
                    try {
                        database.Insert(mSanPham);
                        isSuccessful = true;
                    } catch (Exception e) {
                        Toast.makeText(AddObjectActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                if (isSuccessful) {
                    finish();
                    Intent intent1 = new Intent(AddObjectActivity.this, MainActivity.class);
                    startActivity(intent1);
                    isSuccessful = false;
                }
            }
        });


    }

    public Boolean isValidate() {
        col1 = editTextA.getText().toString();
        col2 = editTextA2.getText().toString();
        col3 = editTextB.getText().toString();
        col4 = radioButton1.isChecked();
        if (col1.isEmpty() || col2.isEmpty() || col3.isEmpty()) {
            return false;
        } else {
            mSanPham = new SanPham(id, col1, col2, col3, col4);
            return true;
        }
    }


}