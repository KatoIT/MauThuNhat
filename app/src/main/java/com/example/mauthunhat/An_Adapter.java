package com.example.mauthunhat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class An_Adapter extends BaseAdapter implements Filterable {
    private Context mContext; //context
    private ArrayList<Product_181203458> arrayListObject; //data source of the list adapter

    private CustomFilter filter;
    private ArrayList<Product_181203458> filterList;
    private An_Sqlite db = new An_Sqlite(mContext);

    onClick mOnClick;

    public interface onClick {
        public void onClickItem(Product_181203458 obj, Boolean isChecked);

        public void onClickEditItem(Product_181203458 obj);
    }

    public An_Adapter(Context context, ArrayList<Product_181203458> arrayListObject, onClick mOnClick) {
        this.mContext = context;
        this.arrayListObject = arrayListObject;
        this.filterList = arrayListObject;
        this.mOnClick = mOnClick;
    }

    @Override
    public int getCount() {
        return arrayListObject.size();
    }

    @Override
    public Product_181203458 getItem(int position) {
        return arrayListObject.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void update(ArrayList<Product_181203458> lUser) {
        this.arrayListObject = lUser;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.item_list_view, null);
        }
        Product_181203458 object = (Product_181203458) getItem(position);
        if (object != null) {
            // Ánh xạ
            TextView textView1 = view.findViewById(R.id.textView1);
            TextView textView2 = view.findViewById(R.id.textView2);
            CheckBox checkBox = view.findViewById(R.id.checkBox);
            ImageView imageView = view.findViewById(R.id.imageView);
            ConstraintLayout constraintLayout = view.findViewById(R.id.constraintLayout);
            // Xử lý dữ liệu
            textView1.setText(object.getName());
            textView2.setText(object.getDescription());
            checkBox.setChecked(object.isStatus());
            //
            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkBox.setChecked(!checkBox.isChecked());
                    mOnClick.onClickItem(object, checkBox.isChecked());
                    object.setStatus(checkBox.isChecked());
//                    db.Update(object);
                }
            });
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mOnClick.onClickItem(object, isChecked);
                    object.setStatus(isChecked);
//                    db.Update(object);
                }
            });
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClick.onClickEditItem(object);
                }
            });

        }
        return view;
    }

    public String convertGia(int value) {
        String val = "";
        while (value >= 1000) {
            int var = (value % 1000);
            if (var == 0) {
                val = ".000" + val;
            } else {
                if (var < 10) {
                    val = "." + String.valueOf(100 * var) + val;
                } else {
                    if (var < 100) {
                        val = "." + String.valueOf(10 * var) + val;
                    } else {
                        val = "." + String.valueOf(var) + val;
                    }
                }
            }
            value = (int) value / 1000;
        }
        val = String.valueOf(value) + val;
        return val;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CustomFilter();
        }
        return filter;
    }


    private class CustomFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                constraint = constraint.toString().toUpperCase();
                ArrayList<Product_181203458> arrayList = new ArrayList<>();
                for (int i = 0; i < filterList.size(); i++) {
                    // search theo filterList.get(i).getTenSP() -------------------------------
                    if (filterList.get(i).getName().toUpperCase().contains(constraint)) {
                        // chú ý -----------------------------------------
                        Product_181203458 object = new Product_181203458(filterList.get(i).getId(), filterList.get(i).getName(), filterList.get(i).getDescription(), filterList.get(i).getImage(), filterList.get(i).isStatus());
                        //
                        arrayList.add(object);
                    }
                }
                results.count = arrayList.size();
                results.values = arrayList;
            } else {
                results.count = filterList.size();
                results.values = filterList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            arrayListObject = (ArrayList<Product_181203458>) results.values;
            notifyDataSetChanged();
        }


    }

    public void updateResults(ArrayList<Product_181203458> results) {
        arrayListObject = results;
        notifyDataSetChanged();
    }

}
