package com.example.myself.myitime;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myself.myitime.data.AddSetting;

import java.util.ArrayList;
import java.util.List;

public class AddActivity extends AppCompatActivity {
    private EditText editTextTitle,editTextRemark;

    private AddSettingArrayAdapter1 Adapter1=null;
    private ArrayList<AddSetting> ArrayAddSetting;

    private FloatingActionButton fab_canccel,fab_done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        editTextTitle = (EditText)findViewById(R.id.edit_text_title);
        editTextRemark= (EditText)findViewById(R.id.edit_text_remark);

        ArrayAddSetting = new ArrayList<AddSetting>();

        ArrayAddSetting.add(new AddSetting(R.drawable.date,"日期","长按使用计算器"));
        ArrayAddSetting.add(new AddSetting(R.drawable.repeat,"重复设置","无"));
        ArrayAddSetting.add(new AddSetting(R.drawable.pic_icon,"图片",""));
        ArrayAddSetting.add(new AddSetting(R.drawable.note,"添加标签",""));

        Adapter1 = new AddSettingArrayAdapter1(this,R.layout.add_setting, ArrayAddSetting);
        ListView listview1=(ListView)findViewById(R.id.list_view_add_settings);
        listview1.setAdapter(Adapter1);





        fab_canccel= (FloatingActionButton) findViewById(R.id.fab_back);
        fab_done= (FloatingActionButton) findViewById(R.id.fab_done);

        fab_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("title",editTextTitle.getText().toString().trim());
                intent.putExtra("remark",editTextRemark.getText().toString().trim());

                if(editTextTitle.getText().length() != 0){
                    setResult(RESULT_OK, intent);
                    AddActivity.this.finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "标题不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });


        fab_canccel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddActivity.this.finish();
            }
        });


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



}
