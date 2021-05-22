package com.example.mauthunhat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    public Database db = new Database(MainActivity.this);
    public ListView listView;
    public EditText editTextFilter;
    public FloatingActionButton actionButtonAdd;
    public BaseAdapterK adapter;
    public ArrayList<SanPham> arrayListObject = new ArrayList<>();
    public ArrayList<SanPham> arrayListObjectDelete = new ArrayList<>();
    public long backPressTime;
    public Toast toast;
    private VarFinal mVarFinal = new VarFinal();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Ánh xạ
        listView = findViewById(R.id.listView);
        editTextFilter = findViewById(R.id.editTextSearch);
        actionButtonAdd = findViewById(R.id.floatingActionButtonAdd);
        // Insert data
        if (db.GetData("SELECT * FROM " + mVarFinal.TABLENAME).getCount() == 0) {
            db.Insert(new SanPham(0, "Bóng đèn", "220V", "a", false));
            db.Insert(new SanPham(0, "Máy bơm", "220V", "a", false));
            db.Insert(new SanPham(0, "Quạt điện", "220V", "a", false));
            db.Insert(new SanPham(0, "Tủ lạnh", "220V", "a", false));
            db.Insert(new SanPham(0, "Điều hòa", "220V", "a", false));
            db.Insert(new SanPham(0, "Tivi", "220V", "a", false));
            db.Insert(new SanPham(0, "Nồi cơm điện", "220V", "a", false));
            db.Insert(new SanPham(0, "Máy giặt", "220V", "a", false));
            db.Insert(new SanPham(0, "Máy tính", "220V", "a", false));
        }

        // End Insert data

        arrayListObject = selectAll(mVarFinal.TABLENAME);
        // Sort arrayList theo Giá (đảo vị trí (t1,t2) để đảo chiều sort)
        Collections.sort(arrayListObject, (t1, t2) -> t1.getName().compareTo(t2.getName()));
        // set Adapter
        adapter = new BaseAdapterK(MainActivity.this, arrayListObject, new BaseAdapterK.onClick() {
            @Override
            public void onClickItem(SanPham sp, Boolean isChecked) {
                if (isChecked) {
                    arrayListObjectDelete.add(sp);
                } else {
                    arrayListObjectDelete.remove(sp);
                }
            }

            @Override
            public void onClickEditItem(SanPham sp) {
                intentView(sp.getId(), AddObjectActivity.class, "katoit", 1);
            }
        });
        listView.setAdapter(adapter);
        // Đăng ký contextMenu
        registerForContextMenu(listView);
        // Filter
        editTextFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        actionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentView(0, AddObjectActivity.class, "katoit", 0);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_xoa: {
                for (SanPham sp : arrayListObjectDelete) {
                    db.Delete(sp);
                    arrayListObject.remove(sp);
                    adapter.notifyDataSetChanged();
                }
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public ArrayList<SanPham> selectAll(String tableName) {
        // Select data SQLite
        ArrayList<SanPham> arrayList = new ArrayList<>();
        Cursor cursor = db.GetData("SELECT * FROM " + tableName + "");
        while (cursor.moveToNext()) {
            int col0 = Integer.parseInt(cursor.getString(0));
            String col1 = cursor.getString(1);
            String col2 = cursor.getString(2);
            String col3 = cursor.getString(3);
            boolean col4 = Boolean.valueOf(cursor.getString(4));
            arrayList.add(new SanPham(col0, col1, col2, col3, col4));
        }
        return arrayList;
    }

    private void intentView(int val, Class cls, String str, int isEdit) {
        // truyền dữ liệu giữa các activity
        Intent intent = new Intent(MainActivity.this, cls);
        Bundle bundle = new Bundle();
        bundle.putInt("id", val);
        bundle.putInt("isEdit", isEdit);
        intent.putExtra(str, bundle);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (backPressTime + 2000 > System.currentTimeMillis()) {
            toast.cancel();
            this.finishAffinity();
            return;
        } else {
            toast = Toast.makeText(MainActivity.this, "Nhấp Back 1 lần nữa để thoát", Toast.LENGTH_SHORT);
            toast.show();
        }
        backPressTime = System.currentTimeMillis();
    }
}