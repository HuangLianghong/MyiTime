package com.example.myself.myitime;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.myself.myitime.data.AddSetting;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddActivity extends AppCompatActivity {
    private final String IMAGE_TYPE = "image/*";
    private final static int IMAGE_CODE=0;
    private EditText editTextTitle,editTextRemark;
    private ImageView imageView;

    private AddSettingArrayAdapter1  Adapter1=null;
    private ArrayList<AddSetting> ArrayAddSetting;

    private FloatingActionButton fab_canccel,fab_done;
    public byte[] pic;
    public String date;
    private int years,months,days,hours,minutes,period;
    private ArrayList<Integer> RGB = MainActivity.RGB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_event);


        editTextTitle = (EditText)findViewById(R.id.edit_text_title);
        editTextRemark= (EditText)findViewById(R.id.edit_text_remark);
        imageView = (ImageView)findViewById(R.id.image_view_background);

        String title_in=getIntent().getStringExtra("title");
        String remark_in=getIntent().getStringExtra("remark");
        String date_in = getIntent().getStringExtra("date");
        years = getIntent().getIntExtra("years",0);
        months = getIntent().getIntExtra("months",0);
        days = getIntent().getIntExtra("days",0);
        hours = getIntent().getIntExtra("hours",0);
        minutes = getIntent().getIntExtra("minutes",0);
        period = getIntent().getIntExtra("period",0);

        String type=getIntent().getStringExtra("type");

        ImageView imageView = (ImageView)findViewById(R.id.image_view_add_title_background);
        if(!RGB.isEmpty()) {
            getWindow().setStatusBarColor(Color.rgb(RGB.get(0),RGB.get(1),RGB.get(2)));
            imageView.setColorFilter(Color.rgb(RGB.get(0), RGB.get(1), RGB.get(2)));
        }
        Calendar curDate = Calendar.getInstance();
        int year = curDate.get(Calendar.YEAR);
        int month = curDate.get(Calendar.MONTH)+1;
        int day = curDate.get(Calendar.DAY_OF_MONTH);

        ArrayAddSetting = new ArrayList<AddSetting>();
        if(type.equals("add") ) {
            date = String.valueOf(year + "年" + month + "月" + day + "日");
            ArrayAddSetting.add(new AddSetting(R.drawable.date,"日期","长按使用计算器"));
            ArrayAddSetting.add(new AddSetting(R.drawable.repeat,"重复设置","无"));
        }
        else{
            String P = null;
            if(period == 0){
                P = "无";
            }
            else if (period == 7){
                P = "每周";
            }
            else if (period == 30){
                P = "每月";
            }
            else {
                P = "每年";
            }
            date = String.valueOf(years + "年" + (months+1) + "月" + days + "日");
            editTextTitle.setText(title_in);
            editTextRemark.setText(remark_in);
            ArrayAddSetting.add(new AddSetting(R.drawable.date,"日期",date));
            ArrayAddSetting.add(new AddSetting(R.drawable.repeat,"重复设置",P));
            pic = getIntent().getByteArrayExtra("picture");
        }




        ArrayAddSetting.add(new AddSetting(R.drawable.pic_icon,"图片",""));
        ArrayAddSetting.add(new AddSetting(R.drawable.note,"添加标签",""));

        Adapter1 = new AddSettingArrayAdapter1(this,R.layout.add_setting, ArrayAddSetting);
        final ListView listviewSet=(ListView)findViewById(R.id.list_view_add_settings);
        listviewSet.setAdapter(Adapter1);
        listviewSet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AddSetting As = ArrayAddSetting.get(position);
                switch (position){
                    case 0:
                        setTime(position);
                        setDate(position);
                        break;
                    case 1:
                        //选择周期
                        PeriodDialog();
                        break;
                    case 2:
                        //选择照片,对图片大小有要求，目前只能用KB大小的图片
                        setImage();

                        break;
                    case 3:
                        //添加标签（先不做）
                        break;
                }
            }
        });



        fab_canccel= (FloatingActionButton) findViewById(R.id.fab_back);
        fab_done= (FloatingActionButton) findViewById(R.id.fab_done);

        fab_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "设置完成", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("title",editTextTitle.getText().toString().trim());
                intent.putExtra("remark",editTextRemark.getText().toString().trim());
                intent.putExtra("picture",pic);
                intent.putExtra("years",years);
                intent.putExtra("months",months);
                intent.putExtra("days",days);
                intent.putExtra("hours",hours);
                intent.putExtra("minutes",minutes);
                intent.putExtra("period",period);


                if(editTextTitle.getText().length() == 0){
                    Toast.makeText(getApplicationContext(), "标题不能为空", Toast.LENGTH_SHORT).show();
                }
                else if(null== pic || pic.length==0){
                    Toast.makeText(getApplicationContext(), "请选择图片", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "点击了OK", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK, intent);
                    AddActivity.this.finish();
                }
            }
        });


        fab_canccel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                AddActivity.this.finish();
            }
        });


    }

    private void PeriodDialog() {
        final String[] items = { "每周","每月","每年","无" };
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(AddActivity.this);
        listDialog.setTitle("周期");
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int n = 3;
               if(which == 0){
                   period=7;
                   n = 0;
               }
               else if(which ==1){
                   period = 30;
                   n = 1;
               }
               else if(which == 2){
                   period = 365;
                   n = 2;
               }
               else{
                   period = 0;
                   n = 3;
               }
               ArrayAddSetting.remove(1);
               ArrayAddSetting.add(1,new AddSetting(R.drawable.repeat,"重复设置",items[n]));
               Adapter1.notifyDataSetChanged();
            }
        });
        listDialog.show();
    }
    public void setDate(int position) {
        //选择日期
        final Calendar calendar = Calendar.getInstance();
        //当前位置的item
        final AddSetting addSetting= ArrayAddSetting.get(position);
        DatePickerDialog dialog = new DatePickerDialog(AddActivity.this, R.style.MyPickerDialogTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                        years=calendar.get(Calendar.YEAR);
                        months=calendar.get(Calendar.MONTH);
                        days=calendar.get(Calendar.DAY_OF_MONTH);
                        date = String.valueOf(years + "年" + (months+1) + "月" + days + "日");
                        addSetting.setRemark(date);
                        Adapter1.notifyDataSetChanged();
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }
    public void setTime(int position) {
        final AddSetting addSetting= ArrayAddSetting.get(position);
        new TimePickerDialog(this, R.style.MyPickerDialogTheme,new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hours = hourOfDay;
                minutes = minute;
                date= String.valueOf(years+"年"+(months+1)+"月"+days+"日"+hours+"小时"+minutes+"分钟");
                addSetting.setRemark(date);
            }
        }, 0, 0, true).show();

    }

    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bm = null;

        ContentResolver resolver = getContentResolver();
        if (requestCode == IMAGE_CODE) {

            try {
                Uri originalUri = data.getData();
                bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                ByteArrayOutputStream output = new ByteArrayOutputStream();//初始化一个流对象
                bm = Bitmap.createScaledBitmap(bm, 450, 450, true);//将图片尺寸变小
                bm.compress(Bitmap.CompressFormat.PNG, 100, output);//把bitmap100%高质量压缩 到 output对象里
                pic = output.toByteArray();//转换成功了  pic就是一个bit的资源数组
                if(pic.length!= 0)
                    Toast.makeText(getApplicationContext(), "转换完成", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    protected class AddSettingArrayAdapter1 extends ArrayAdapter<AddSetting>
    {
        private  int resourceId;
        public AddSettingArrayAdapter1 (@NonNull Context context, @LayoutRes int resource, @NonNull List<AddSetting> objects) {
            super(context, resource, objects);
            resourceId=resource;
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater mInflater= LayoutInflater.from(this.getContext());
            View item = mInflater.inflate(this.resourceId,null);

            ImageView img = (ImageView)item.findViewById(R.id.image_view_of_setting1);
            TextView title = (TextView)item.findViewById(R.id.text_view_addSetting_title);
            TextView remark= (TextView)item.findViewById(R.id.text_view_addSetting_remark);

            AddSetting good_item= this.getItem(position);
            img.setImageResource(good_item.getPictureId());
            title.setText(good_item.getTitle());
            remark.setText(good_item.getRemark());
            return item;
        }
    }

    private void setImage() {
        Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
        getAlbum.setType(IMAGE_TYPE);
        startActivityForResult(getAlbum, IMAGE_CODE);
    }



}
