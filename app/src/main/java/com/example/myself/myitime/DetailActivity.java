package com.example.myself.myitime;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

public class DetailActivity extends AppCompatActivity {

    private static final int RESULT_DELETE=901;
    private static final int REQUEST_CODE_EDIT=100;
    Intent intent_DetailToMain=intent_DetailToMain = new Intent();;

    byte[] picture;
    private int years,months,days,hours,minutes,period;
    private String title = null,remark = null;
    private byte[] pic = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        years = getIntent().getIntExtra("years",0);
        months = getIntent().getIntExtra("months",0);
        days = getIntent().getIntExtra("days",0);
        hours = getIntent().getIntExtra("hours",0);
        minutes = getIntent().getIntExtra("minutes",0);
        title=getIntent().getStringExtra("title");
        remark=getIntent().getStringExtra("remark");
        pic=getIntent().getByteArrayExtra("picture");
        period = getIntent().getIntExtra("period",0);

        picture=pic;

        String date= String.valueOf(years+"年"+(months+1)+"月"+days+"日");
        intent_DetailToMain.putExtra("state","notChanged");
        Bitmap bitmap = Bytes2Bimap(pic);
        ImageView imageView = (ImageView)findViewById(R.id.image_view_detail);
        imageView.setImageBitmap(bitmap);
        TextView textView_title = (TextView)findViewById(R.id.text_view_detail_title);
        textView_title.setText(title);
        TextView textView_targetTime = (TextView)findViewById(R.id.text_view_detail_targetTime);
        textView_targetTime.setText(date);

        showTimes();

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
                intent.putExtra("period",period);

                startActivityForResult(intent, REQUEST_CODE_EDIT);

            }
        });

    }

    public void showTimes() {

        int runTime = 10*365*24*60*60;
        CountDownTimer showTime = new CountDownTimer(runTime * 1000, 1000) {
            @Override
            public void onTick(long l) {
                //倒计时时，在没有完成倒计时时会多次调用，直到结束倒计时

                //获取当前的时间
                Calendar curDate = Calendar.getInstance();
                int year = curDate.get(Calendar.YEAR);
                int month = curDate.get(Calendar.MONTH)+1;
                int day = curDate.get(Calendar.DAY_OF_MONTH);
                int hour = curDate.get(Calendar.HOUR_OF_DAY);
                int minute = curDate.get(Calendar.MINUTE);
                int second = curDate.get(Calendar.SECOND);

                years = getIntent().getIntExtra("years",0);
                months = getIntent().getIntExtra("months",0);
                days = getIntent().getIntExtra("days",0);
                hours = getIntent().getIntExtra("hours",0);
                minutes = getIntent().getIntExtra("minutes",0);


                int seconds=60;

                TextView textView_leftTime = (TextView)findViewById(R.id.text_view_detail_leftTime);


                int year_left = years-year;
                int month_left = months-month+1;
                int day_left = days-day;
                int hour_left=hours-hour;
                int minute_left = minutes-minute;
                int second_left = seconds-second;
                String time = null;

                if(year_left>0 || month_left>0 || day_left>0 || hour_left>0 || minute_left >=0) {
                    if (month_left < 0 && year_left > 0) {
                        year_left--;
                        month_left += 11;
                    }
                    if (day_left < 0 && month_left > 0) {
                        month_left--;
                        day_left += 29;

                    }
                    if (hour_left < 0 && day_left > 0) {
                        day_left--;
                        hour_left += 23;

                    }
                    if (minute_left < 0 && hour_left > 0) {
                        hour_left--;
                        minute_left += 59;

                    }
                    if (second_left < 0 && minute_left > 0) {
                        minute_left--;
                        second_left += 59;
                    }
                    time = String.valueOf("剩余" + year_left + "年" + month_left + "月" + day_left + "日" + hour_left + "时" + minute_left + "分" + second_left + "秒");
                }
                else{
                    year_left = 0-year_left;
                    month_left = 0-month_left;
                    day_left = 0-day_left;
                    hour_left = 0-hour_left;
                    minute_left = 0-minute_left;
                    second_left = 60-second_left;
                    time = String.valueOf("已经" + year_left + "年" + month_left + "月" + day_left + "日" + hour_left + "时" + minute_left + "分" + second_left + "秒");
                }

                textView_leftTime.setText(time);
            }

            @Override
            public void onFinish() {
                //倒计时完成时调用
            }
        };
        showTime.start();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_EDIT){
            if(resultCode == RESULT_CANCELED){

                intent_DetailToMain.putExtra("state","notChanged");
            }
            else if(resultCode==RESULT_OK){
                title=data.getStringExtra("title");
                remark=data.getStringExtra("remark");
                pic=data.getByteArrayExtra("picture");
                years = data.getIntExtra("years",0);
                months = data.getIntExtra("months",0);
                days = data.getIntExtra("days",0);
                hours = data.getIntExtra("hours",0);
                minutes = data.getIntExtra("minutes",0);
                period = data.getIntExtra("period",0);

                intent_DetailToMain.putExtra("title",title);
                intent_DetailToMain.putExtra("remark",remark);
                intent_DetailToMain.putExtra("picture",pic);
                intent_DetailToMain.putExtra("state","Changed");
                intent_DetailToMain.putExtra("years",years);
                intent_DetailToMain.putExtra("months",months);
                intent_DetailToMain.putExtra("days",days);
                intent_DetailToMain.putExtra("hours",hours);
                intent_DetailToMain.putExtra("minutes",minutes);
                intent_DetailToMain.putExtra("period",period);
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
