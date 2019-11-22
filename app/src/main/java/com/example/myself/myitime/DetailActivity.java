package com.example.myself.myitime;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private static final int RESULT_DELETE=901;
    private static final int REQUEST_CODE_EDIT=100;
    Intent intent_DetailToMain=intent_DetailToMain = new Intent();;

    byte[] picture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final String title=getIntent().getStringExtra("title");
        final String remark=getIntent().getStringExtra("remark");
        byte[] pic=getIntent().getByteArrayExtra("picture");
        final int years = getIntent().getIntExtra("years",0);
        final int months = getIntent().getIntExtra("months",0);
        final int days = getIntent().getIntExtra("days",0);
        final int hours = getIntent().getIntExtra("hours",0);
        final int minutes = getIntent().getIntExtra("minutes",0);
        picture=pic;

        String date= String.valueOf(years+"年"+(months+1)+"月"+days+"日"+hours+"时"+minutes+"分");
        intent_DetailToMain.putExtra("state","notChanged");
        Bitmap bitmap = Bytes2Bimap(pic);
        ImageView imageView = (ImageView)findViewById(R.id.image_view_detail);
        imageView.setImageBitmap(bitmap);
        TextView textView_title = (TextView)findViewById(R.id.text_view_detail_title);
        textView_title.setText(title);
        TextView textView_targetTime = (TextView)findViewById(R.id.text_view_detail_targetTime);
        textView_targetTime.setText(date);

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

                Intent intent = new Intent(DetailActivity.this, AddActivity.class);
                intent.putExtra("title",title);

                intent.putExtra("remark",remark);
                intent.putExtra("type","edit");
                intent.putExtra("picture",picture);
                intent.putExtra("years",years);
                intent.putExtra("months",months);
                intent.putExtra("days",days);
                intent.putExtra("hours",hours);
                intent.putExtra("minutes",minutes);

                startActivityForResult(intent, REQUEST_CODE_EDIT);

            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_EDIT){
            if(resultCode == RESULT_CANCELED){

                intent_DetailToMain.putExtra("state","notChanged");
            }
            else if(resultCode==RESULT_OK){
                String title=data.getStringExtra("title");
                String remark=data.getStringExtra("remark");
                byte[] pic=data.getByteArrayExtra("picture");
                int years = getIntent().getIntExtra("years",0);
                int months = getIntent().getIntExtra("months",0);
                int days = getIntent().getIntExtra("days",0);
                int hours = getIntent().getIntExtra("hours",0);
                int minutes = getIntent().getIntExtra("minutes",0);


                intent_DetailToMain.putExtra("title",title);
                intent_DetailToMain.putExtra("remark",remark);
                intent_DetailToMain.putExtra("picture",pic);
                intent_DetailToMain.putExtra("state","Changed");
                intent_DetailToMain.putExtra("years",years);
                intent_DetailToMain.putExtra("months",months);
                intent_DetailToMain.putExtra("days",days);
                intent_DetailToMain.putExtra("hours",hours);
                intent_DetailToMain.putExtra("minutes",minutes);
            }
        }

    }
    public Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

}
