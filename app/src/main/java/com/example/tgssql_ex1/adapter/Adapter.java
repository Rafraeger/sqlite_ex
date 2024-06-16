package com.example.tgssql_ex1.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.tgssql_ex1.model.Data;
import com.example.tgssql_ex1.R;
import com.example.tgssql_ex1.MainActivity;

import java.util.List;

public class Adapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<Data> lists;

    public Adapter(Activity activity, List<Data> lists) {
        this.activity = activity;
        this.lists = lists;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int i) {
        return lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (view == null && inflater != null) {
            view = inflater.inflate(R.layout.list_users, null);
        }
        if (view != null) {
            TextView name = view.findViewById(R.id.text_name);
            TextView reg = view.findViewById(R.id.text_reg);
            TextView email = view.findViewById(R.id.text_email);
            TextView phone = view.findViewById(R.id.text_phone);
            Button btnEdit = view.findViewById(R.id.btn_edit);
            Button btnDelete = view.findViewById(R.id.btn_delete);

            Data data = lists.get(i);
            name.setText(data.getName());
            reg.setText(data.getReg());
            email.setText(data.getEmail());
            phone.setText(data.getPhone());

            btnEdit.setOnClickListener(v -> {
                if (activity instanceof MainActivity) {
                    ((MainActivity) activity).showEditDialog(data);
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (activity instanceof MainActivity) {
                    ((MainActivity) activity).deleteData(data.getId());
                }
            });
        }
        return view;
    }
}
