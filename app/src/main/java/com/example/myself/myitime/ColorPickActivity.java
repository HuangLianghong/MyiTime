package com.example.myself.myitime;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myself.myitime.data.ColorPickerView;

import java.util.ArrayList;

public class ColorPickActivity extends Activity {

    private LinearLayout ll;
    private TextView tv;
    private ColorPickerView colorPickerView;
    private Button buttonOk,buttonCancel;
    private ArrayList<Integer> RGB = new ArrayList<Integer>();

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_color_pick);

        ll = (LinearLayout) findViewById(R.id.ColorPickerLayout);
        tv = (TextView)findViewById(R.id.color_text);
        buttonOk = (Button)findViewById(R.id.ColorPickOk);
        buttonCancel=(Button)findViewById(R.id.ColorPickCancel);

        if(!MainActivity.RGB.isEmpty()){
            RGB = MainActivity.RGB;
            getWindow().setStatusBarColor(Color.rgb(RGB.get(0),RGB.get(1),RGB.get(2)));
        }

        colorPickerView = new ColorPickerView(this);
        ll.addView(colorPickerView);
        colorPickerView.setOnColorBackListener(new ColorPickerView.OnColorBackListener() {
            @Override
            public void onColorBack(int a, int r, int g, int b) {
                tv.setText("R：" + r + "\nG：" + g + "\nB：" + b + "\n" + colorPickerView.getStrColor());
                tv.setTextColor(Color.argb(a, r, g, b));
                if(RGB.isEmpty()){}
                else {
                    RGB.clear();
                }RGB.add(r);
                RGB.add(g);
                RGB.add(b);
                getWindow().setStatusBarColor(Color.rgb(RGB.get(0),RGB.get(1),RGB.get(2)));

            }
        });
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.putExtra("RGB",RGB);
                setResult(RESULT_OK,intent);
                ColorPickActivity.this.finish();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                ColorPickActivity.this.finish();
            }
        });
    }
}
