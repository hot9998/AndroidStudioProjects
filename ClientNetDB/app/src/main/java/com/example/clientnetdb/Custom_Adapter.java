package com.example.clientnetdb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Custom_Adapter extends BaseAdapter {
    private Activity mAct;
    LayoutInflater mInflater;
    ArrayList<UserInfo> mUserInfoObjArr;
    int mListLayout;

    public Custom_Adapter(Activity mAct, int mListLayout, ArrayList<UserInfo> mUserInfoObjArr) {
        this.mAct = mAct;
        this.mListLayout = mListLayout;
        this.mUserInfoObjArr = mUserInfoObjArr;
        mInflater = (LayoutInflater) mAct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setDatas(ArrayList<UserInfo> arrayList) {
        mUserInfoObjArr = arrayList;
    }

    @Override
    public int getCount() {
        return mUserInfoObjArr.size();
    }

    @Override
    public Object getItem(int position) {
        return mUserInfoObjArr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(mListLayout, parent, false);
        }
        final TextView tvID = (TextView) convertView.findViewById(R.id.tv_id);
        tvID.setText(mUserInfoObjArr.get(position).id);

        final TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
        tvName.setText(mUserInfoObjArr.get(position).name);

        final TextView tvPhone = (TextView) convertView.findViewById(R.id.tv_phone);
        tvPhone.setText(mUserInfoObjArr.get(position).phone);

        final TextView tvGrade = (TextView) convertView.findViewById(R.id.tv_grade);
        tvGrade.setText(mUserInfoObjArr.get(position).grade);

        final TextView tvWriteTime = (TextView) convertView.findViewById(R.id.tv_write_time);
        tvWriteTime.setText(mUserInfoObjArr.get(position).writeTime);

        Button updateButton = (Button) convertView.findViewById(R.id.btnUpdate);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final View view = mAct.getLayoutInflater().inflate(R.layout.dialog_update,null);

                EditText edtId = (EditText)  view.findViewById(R.id.edtID);
                EditText edtName = (EditText) view.findViewById(R.id.edtName);
                EditText edtPhone = (EditText) view.findViewById(R.id.edtPhone);
                EditText edtgrade = (EditText) view.findViewById(R.id.edtGrade);

                edtId.setText(tvID.getText().toString());
                edtName.setText(tvName.getText().toString());
                edtPhone.setText(tvPhone.getText().toString());
                edtgrade.setText(tvGrade.getText().toString());

                edtId.setEnabled(false);

                new AlertDialog.Builder(mAct)
                        .setTitle("멤버 수정")
                        .setView(view)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String id = tvID.getText().toString();
                                String name = ((EditText) view.findViewById(R.id.edtName)).getText().toString();
                                String phone = ((EditText) view.findViewById(R.id.edtPhone)).getText().toString();
                                String grade = ((EditText) view.findViewById(R.id.edtGrade)).getText().toString();

                                new NetworkUpdate(Custom_Adapter.this).execute(id, name, phone, grade);
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

        Button deleteButton = (Button) convertView.findViewById(R.id.btnDelete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = tvID.getText().toString();
                AlertDialog.Builder dlg = new AlertDialog.Builder(mAct);
                dlg.setTitle("삭제하기");
                dlg.setMessage("사용자 ID: " + userID + "를(을) 정말 삭제하시겠습니까?");

                dlg.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new NetworkDelete(Custom_Adapter.this).execute(tvID.getText().toString());
                        new NetworkGet(Custom_Adapter.this).execute("");
                    }
                });
                dlg.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mAct, "취소하였습니다", Toast.LENGTH_SHORT).show();
                    }
                });
                dlg.show();
            }
        });
        return convertView;
    }

}
