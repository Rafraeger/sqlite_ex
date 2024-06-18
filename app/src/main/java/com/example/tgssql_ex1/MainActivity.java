package com.example.tgssql_ex1;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tgssql_ex1.adapter.Adapter;
import com.example.tgssql_ex1.helper.Helper;
import com.example.tgssql_ex1.model.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnDialogAdd;
    private ListView listView;
    private AlertDialog.Builder dialog;
    private TextView textIsEmpty;
    private List<Data> lists = new ArrayList<>();
    private Adapter adapter;
    private Helper db = new Helper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listView = findViewById(R.id.list_item);
        adapter = new Adapter(this, lists);
        textIsEmpty = findViewById(R.id.text_is_empty);
        listView.setAdapter(adapter);

        btnDialogAdd = findViewById(R.id.btn_dialog_add);
        btnDialogAdd.setOnClickListener(v -> showAddDialog());

        // Load data into list
        getData();
    }

    private void showAddDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.diolog_box);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        EditText editname = dialog.findViewById(R.id.edit_name);
        EditText editreg = dialog.findViewById(R.id.edit_reg);
        EditText editemail = dialog.findViewById(R.id.edit_email);
        EditText editphone = dialog.findViewById(R.id.edit_phone);

        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        Button btnCreate = dialog.findViewById(R.id.btn_create);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnCreate.setOnClickListener(v -> {
            if (TextUtils.isEmpty(editname.getText()) || TextUtils.isEmpty(editreg.getText()) || TextUtils.isEmpty(editemail.getText()) || TextUtils.isEmpty(editphone.getText())) {
                Toast.makeText(getApplicationContext(), "Silahkan isi semua data", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    String reg = editreg.getText().toString();
                    String phone = editphone.getText().toString();
                    db.insert(editname.getText().toString(), reg, editemail.getText().toString(), phone);
                    dialog.dismiss();
                    getData(); // Refresh the list
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Nomor registrasi dan telepon harus berupa angka", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    public void showEditDialog(Data data) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_box_edit);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        EditText editname = dialog.findViewById(R.id.edit_name);
        EditText editreg = dialog.findViewById(R.id.edit_reg);
        EditText editemail = dialog.findViewById(R.id.edit_email);
        EditText editphone = dialog.findViewById(R.id.edit_phone);

        editname.setText(data.getName());
        editreg.setText(data.getReg());
        editemail.setText(data.getEmail());
        editphone.setText(data.getPhone());

        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        Button btnUpdate = dialog.findViewById(R.id.btn_update);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnUpdate.setOnClickListener(v -> {
            if (TextUtils.isEmpty(editname.getText()) || TextUtils.isEmpty(editreg.getText()) || TextUtils.isEmpty(editemail.getText()) || TextUtils.isEmpty(editphone.getText())) {
                Toast.makeText(getApplicationContext(), "Silahkan isi semua data", Toast.LENGTH_SHORT).show();
            } else {
                try {
//                    int reg = Integer.parseInt(editreg.getText().toString());
//                    int phone = Integer.parseInt(editphone.getText().toString());
                    String reg = editreg.getText().toString();
                    String phone = editphone.getText().toString();
                    db.update(Integer.parseInt(data.getId()), editname.getText().toString(), reg, editemail.getText().toString(), phone);
                    dialog.dismiss();
                    getData(); // Refresh the list
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Nomor registrasi dan telepon harus berupa angka", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    public void deleteData(String id) {
        db.delete(Integer.parseInt(id));
        getData(); // Refresh the list
    }

    private void getData() {
        lists.clear();
        ArrayList<HashMap<String, String>> rows = db.getAll();
        for (HashMap<String, String> row : rows) {
            String id = row.get("id");
            String name = row.get("name");
            String reg = row.get("reg");
            String email = row.get("email");
            String phone = row.get("phone");

            Data data = new Data();
            data.setId(id);
            data.setName(name);
            data.setReg(reg);
            data.setEmail(email);
            data.setPhone(phone);
            lists.add(data);
        }
        if (lists.isEmpty()) {
            textIsEmpty.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            textIsEmpty.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
    }
}
