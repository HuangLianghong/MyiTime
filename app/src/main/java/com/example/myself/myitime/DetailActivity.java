package com.example.myself.myitime;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DetailActivity extends AppCompatActivity {

    private static final int RESULT_DELETE=901;
    private static final int REQUEST_CODE_EDIT=100;
    Intent intent_DetailToMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        FloatingActionButton fab_delete = (FloatingActionButton) findViewById(R.id.fab_detail_delete);
        fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new android.app.AlertDialog.Builder(DetailActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("询问")
                        .setMessage("你确定要删除这条吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int pos = getIntent().getIntExtra("position",0);

                                Intent intent = new Intent();
                                intent.putExtra("position",pos);
                                setResult(RESULT_DELETE,intent);
                                DetailActivity.this.finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create().show();


            }
        });

        FloatingActionButton fab_cancel = (FloatingActionButton) findViewById(R.id.fab_detail_back);
        fab_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setResult(RESULT_CANCELED,intent_DetailToMain);
                DetailActivity.this.finish();
            }
        });

        FloatingActionButton fab_edit = (FloatingActionButton) findViewById(R.id.fab_detail_edit);
        fab_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title=getIntent().getStringExtra("title");
                String remark=getIntent().getStringExtra("remark");
                String date=getIntent().getStringExtra("date");
                byte[] pic=getIntent().getByteArrayExtra("picture");

                Intent intent = new Intent(DetailActivity.this, AddActivity.class);
                intent.putExtra("title",title);
                intent.putExtra("date",date);
                intent.putExtra("remark",remark);
                intent.putExtra("type","edit");
                intent.putExtra("picture",pic);

                startActivityForResult(intent, REQUEST_CODE_EDIT);

            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_EDIT){
            if(resultCode == RESULT_CANCELED){
                intent_DetailToMain = new Intent();
                intent_DetailToMain.putExtra("state","notChanged");
            }
            else if(resultCode==RESULT_OK){
                String title=data.getStringExtra("title");
                String remark=data.getStringExtra("remark");
                String date=data.getStringExtra("date");
                byte[] pic=data.getByteArrayExtra("picture");

                intent_DetailToMain = new Intent();
                intent_DetailToMain.putExtra("title",title);
                intent_DetailToMain.putExtra("remark",remark);
                intent_DetailToMain.putExtra("date",date);
                intent_DetailToMain.putExtra("picture",pic);
                intent_DetailToMain.putExtra("state","Changed");
            }
        }

    }
}
