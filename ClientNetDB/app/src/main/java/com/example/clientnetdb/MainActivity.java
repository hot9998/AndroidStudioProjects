package com.example.clientnetdb;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button refreshBtn, addBtn;
    private ListView listView;
    private Custom_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        adapter = new Custom_Adapter(MainActivity.this, R.layout.adapter_userinfo, new ArrayList<UserInfo>());
        listView.setAdapter(adapter);

        refreshBtn = (Button) findViewById(R.id.btnRefresh);
        new NetworkGet((Custom_Adapter) listView.getAdapter()).execute("");
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NetworkGet((Custom_Adapter) listView.getAdapter()).execute("");
            }
        });

        addBtn = (Button) findViewById(R.id.btn_add);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = getLayoutInflater().inflate(R.layout.dialog_add, null);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("멤버 추가")
                        .setView(view)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String id = ((EditText) view.findViewById(R.id.edtID)).getText().toString();
                                String name = ((EditText) view.findViewById(R.id.edtName)).getText().toString();
                                String phone = ((EditText) view.findViewById(R.id.edtPhone)).getText().toString();
                                String grade = ((EditText) view.findViewById(R.id.edtGrade)).getText().toString();
                                if (id.equals("")) {
                                    Toast.makeText(MainActivity.this, "id를 입력하세요", Toast.LENGTH_SHORT).show();
                                } else {
                                    new NetworkInsert(adapter).execute(id, name, phone, grade);
                                    new NetworkGet((Custom_Adapter) listView.getAdapter()).execute("");
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        });


    }
}
