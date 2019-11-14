package com.example.myself.myitime;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myself.myitime.data.AddSetting;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        editTextTitle = (EditText)findViewById(R.id.edit_text_title);
        editTextRemark= (EditText)findViewById(R.id.edit_text_remark);
        imageView = (ImageView)findViewById(R.id.image_view_background);

        ArrayAddSetting = new ArrayList<AddSetting>();

        ArrayAddSetting.add(new AddSetting(R.drawable.date,"日期","长按使用计算器"));
        ArrayAddSetting.add(new AddSetting(R.drawable.repeat,"重复设置","无"));
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
                       //选择日期
                        break;
                    case 1:
                        //选择周期
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
                if(editTextTitle.getText().length() != 0){
                    Toast.makeText(getApplicationContext(), "点击了OK", Toast.LENGTH_SHORT).show();
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

    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bm = null;

        ContentResolver resolver = getContentResolver();
        if (requestCode == IMAGE_CODE) {

            try {
                Uri originalUri = data.getData();
                bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                ByteArrayOutputStream output = new ByteArrayOutputStream();//初始化一个流对象
                bm = Bitmap.createScaledBitmap(bm, 85, 85, true);//将图片尺寸变小
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
