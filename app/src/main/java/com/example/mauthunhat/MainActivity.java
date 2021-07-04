package com.example.mauthunhat;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
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
    public An_Sqlite db = new An_Sqlite(MainActivity.this);
    public ListView listView;
    public EditText editTextFilter;
    public FloatingActionButton actionButtonAdd;
    public An_Adapter adapter;
    public ArrayList<Product_181203458> arrayListObject = new ArrayList<>();
    public ArrayList<Product_181203458> arrayListObjectDelete = new ArrayList<>();
    public long backPressTime;
    public Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Trang chủ");
        // Ánh xạ
        listView = findViewById(R.id.listView);
        editTextFilter = findViewById(R.id.editTextSearch);
        actionButtonAdd = findViewById(R.id.floatingActionButtonAdd);
        // Insert data
        if (db.GetData("SELECT * FROM " + db.TABLENAME).getCount() == 0) {
            db.Insert(new Product_181203458(0, "Bóng đèn", "220V", "a", false));
            db.Insert(new Product_181203458(1, "Máy bơm", "220V", "a", false));
            db.Insert(new Product_181203458(2, "Quạt điện", "220V", "a", false));
            db.Insert(new Product_181203458(3, "Tủ lạnh", "220V", "a", false));
            db.Insert(new Product_181203458(4, "Điều hòa", "Nguyễn Văn An", "a", false));
            db.Insert(new Product_181203458(5, "Tivi", "220V", "a", false));
        }

        // End Insert data

        arrayListObject = selectAll(db.TABLENAME);
        // Sort arrayList theo Giá (đảo vị trí (t1,t2) để đảo chiều sort)
        Collections.sort(arrayListObject, (t1, t2) -> t1.getName().compareTo(t2.getName()));
        // set Adapter
        adapter = new An_Adapter(MainActivity.this, arrayListObject, new An_Adapter.onClick() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClickItem(Product_181203458 obj, Boolean isChecked) {
                if (isChecked) {
                    arrayListObjectDelete.add(obj);
//                    Toast.makeText(MainActivity.this, "true" + obj.getName(), Toast.LENGTH_SHORT).show();
                } else {
                    arrayListObjectDelete.removeIf(ob -> (obj.getId().equals(ob.getId())));
//                    Toast.makeText(MainActivity.this, "false" + obj.getName(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onClickEditItem(Product_181203458 obj) {
                intentView(obj.getId(), AddObjectActivity.class, "katoit", 1);
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
                // chọn item 'Xóa'
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Cảnh báo xóa");
                builder.setMessage("Nguyen Van An wants to delete?");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Sự kiện click nút 'Có'
                        for (Product_181203458 sp : arrayListObjectDelete) {
                            db.Delete(sp);
                            arrayListObject.remove(sp);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // sự kiện nút 'Không'
                    }
                });
                builder.create();// tạo dialog
                builder.show(); // show dialog
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public ArrayList<Product_181203458> selectAll(String tableName) {
        // Select data SQLite
        ArrayList<Product_181203458> arrayList = new ArrayList<>();
        Cursor cursor = db.GetData("SELECT * FROM " + tableName + "");
        while (cursor.moveToNext()) {
            int col0 = Integer.parseInt(cursor.getString(0));
            String col1 = cursor.getString(1);
            String col2 = cursor.getString(2);
            String col3 = cursor.getString(3);
            boolean col4 = Boolean.valueOf(cursor.getString(4));
            arrayList.add(new Product_181203458(col0, col1, col2, col3, col4));
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