package com.example.myself.myitime;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myself.myitime.data.FileDataSource;
import com.example.myself.myitime.data.ImageFilter;
import com.example.myself.myitime.data.Item;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_CODE_ADD = 1;
    private static final int REQUEST_CODE_DETAIL=2;
    private static final int RESULT_DELETE=901;
    private static final int COLOR_PICK_REQUEST=333;

    private ArrayList<Item> theItems;
    private FileDataSource fileDataSource;
    private EventsArrayAdapter listviewAdapter = null;
    public static ArrayList<Integer> RGB = new ArrayList<Integer>();

    Item it;
    int changed_position=0;
    Bitmap mImageIds ;  //转换后的bitmap


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        InitData();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //悬浮按钮事件监听

                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                intent.putExtra("title","");
                intent.putExtra("remark","");
                intent.putExtra("pic","");
                intent.putExtra("type","add");
                intent.putExtra("years",0);
                intent.putExtra("months",0);
                intent.putExtra("days",0);
                intent.putExtra("hours",0);
                intent.putExtra("minutes",0);
                intent.putExtra("period",0);

                startActivityForResult(intent, REQUEST_CODE_ADD);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        //set adapter for ListView
        listviewAdapter = new EventsArrayAdapter(this, R.layout.event, theItems);
        final ListView listView = (ListView) findViewById(R.id.list_view_items);
        listView.setAdapter(listviewAdapter);

        //listview中item的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                changed_position=position;
                it =theItems.get(position);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("title",it.getTitle());
                intent.putExtra("remark",it.getRemark());
                intent.putExtra("picture",it.getPic());
                intent.putExtra("years",it.getYears());
                intent.putExtra("months",it.getMonths());
                intent.putExtra("days",it.getDays());
                intent.putExtra("hours",it.getHours());
                intent.putExtra("minutes",it.getMinutes());
                intent.putExtra("period",it.getPeriod());

                startActivityForResult(intent, REQUEST_CODE_DETAIL);
            }

        });
        if(theItems.size() != 0){
            showTimes();
        }




    }

    private void InitData() {
        fileDataSource=new FileDataSource(this);
        theItems=fileDataSource.loadItems();


    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        fileDataSource.save();
    }
    public void showTimes() {

        int runTime = 10*365*24*60*60;
        CountDownTimer showTime = new CountDownTimer(runTime,1000) {
            @Override
            public void onTick(long l) {
                //倒计时时，在没有完成倒计时时会多次调用，直到结束倒计时


                for(Item item:theItems){
                    //获取当前的时间
                    Calendar curDate = Calendar.getInstance();
                    int year = curDate.get(Calendar.YEAR);
                    int month = curDate.get(Calendar.MONTH)+1;
                    int day = curDate.get(Calendar.DAY_OF_MONTH);

                    int years = item.getYears();
                    int months = item.getMonths();
                    int days = item.getDays();

                    int year_left = years-year;
                    int month_left = months-month+1;
                    int day_left = days-day;

                    if(month_left < 0&&year_left>0){
                        year_left--;
                        month_left+=11;
                    }
                    if(day_left < 0&&month_left>0){
                        month_left--;
                        day_left+=29;
                    }
                    int left_time = year_left*12*30+month_left*30+day_left;
                    String time = null;
                    Log.d("time_Check", "onTick: "+left_time);
                    if(left_time >=0) {
                        time = String.valueOf("剩余" + left_time + "天");
                    }
                    else if(item.getPeriod() != 0){
                        while(left_time < 0){
                            left_time += item.getPeriod();
                        }
                        time = String.valueOf("剩余" + left_time + "天");
                    }
                    else{
                        time = String.valueOf("已经"+(0-left_time)+"天");
                    }
                    item.setLeftTime(time);
                    listviewAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFinish() {
                //倒计时完成时调用
            }
        };
        showTime.start();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_color) {
            Intent intent = new Intent(this, ColorPickActivity.class);
            intent.putExtra("RGB",RGB);
            startActivityForResult(intent,COLOR_PICK_REQUEST);
        }
        /*else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    protected class EventsArrayAdapter extends ArrayAdapter<Item> {
        private int resourceId;

        public EventsArrayAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Item> objects) {
            super(context, resource, objects);
            resourceId = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater mInflater = LayoutInflater.from(this.getContext());
            View item = mInflater.inflate(this.resourceId, null);

            ImageView img = (ImageView) item.findViewById(R.id.image_view_background);
            TextView title = (TextView) item.findViewById(R.id.text_view_title);
            TextView date = (TextView) item.findViewById(R.id.text_view_date);
            TextView remark = (TextView) item.findViewById(R.id.text_view_remark);
            TextView leftTime = (TextView)item.findViewById(R.id.text_view_event_leftTIme);

            Item good_item = this.getItem(position);
            byte[] picMatrix = good_item.getPic();
            Bitmap bitmap = Bytes2Bimap(picMatrix);
            bitmap = ImageFilter.blurBitmap(getApplicationContext() , bitmap, 10);
            String date_text= String.valueOf(good_item.getYears()+"年"+(good_item.getMonths()+1)+"月"+good_item.getDays()+"日");

            img.setImageBitmap(bitmap);
            title.setText(good_item.getTitle());
            remark.setText(good_item.getRemark());
            date.setText(date_text);
            leftTime.setText(good_item.getLeftTime());

            return item;
        }
    }
    public Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
             } else {
            return null;
             }
        }



    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_ADD){
           if(resultCode==RESULT_OK){


               String title=data.getStringExtra("title");
               String remark=data.getStringExtra("remark");
               byte[] res = data.getByteArrayExtra("picture");
               int year = data.getIntExtra("years",0);
               int month= data.getIntExtra("months",0);
               int day = data.getIntExtra("days",0);
               int hour = data.getIntExtra("hours",0);
               int minute = data.getIntExtra("minutes",0);
               int period = data.getIntExtra("period",0);
               Item item =new Item(title, remark, res,year,month,day,hour,minute,period);

               theItems.add(item);

               listviewAdapter.notifyDataSetChanged();
           }
        }
        else if(requestCode == REQUEST_CODE_DETAIL){
            if(resultCode==RESULT_CANCELED){
                String state = data.getStringExtra("state");

                if(state.equals("Changed")) {
                    String title = data.getStringExtra("title");
                    String remark = data.getStringExtra("remark");
                    byte[] res = data.getByteArrayExtra("picture");
                    int year = data.getIntExtra("years",0);
                    int month= data.getIntExtra("months",0);
                    int day = data.getIntExtra("days",0);
                    int hour = data.getIntExtra("hours",0);
                    int minute = data.getIntExtra("minutes",0);
                    int period = data.getIntExtra("period",0);

                    String date= String.valueOf(year+"年"+month+"月"+day+"日");
                    it=theItems.get(changed_position);
                    it.setDate(date);
                    it.setPic(res);
                    it.setTitle(title);
                    it.setRemark(remark);
                    it.setYears(year);
                    it.setMonths(month);
                    it.setDays(day);
                    it.setHours(hour);
                    it.setMinutes(minute);
                    it.setPeriod(period);
                    listviewAdapter.notifyDataSetChanged();
                }
                else{

                }
            }
            else if(resultCode == RESULT_DELETE){
                int pos = data.getIntExtra("position",0);
                final int itemPosition=pos;
                theItems.remove(itemPosition);
                listviewAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
            }


            else{}
        }
        else if(requestCode == COLOR_PICK_REQUEST){
            if(resultCode ==RESULT_OK){
                RGB = data.getIntegerArrayListExtra("RGB");
                Toast.makeText(MainActivity.this, "R：" + RGB.get(0) + "\nG：" + RGB.get(1) + "\nB：" + RGB.get(2) + "\n", Toast.LENGTH_SHORT).show();
                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

                toolbar.setBackgroundColor(Color.rgb(RGB.get(0),RGB.get(1),RGB.get(2)));


                int[][] states = new int[2][];
                states[0] = new int[]{android.R.attr.state_pressed};
                states[1] = new int[]{android.R.attr.state_enabled};
                int[] colors;
                colors = new int[]{0x00e43d2b, Color.rgb(RGB.get(0),RGB.get(1),RGB.get(2))};

                getWindow().setStatusBarColor(Color.rgb(RGB.get(0),RGB.get(1),RGB.get(2)));

                fab.setBackgroundTintList(new ColorStateList(states, colors));
            }
            else{

            }
        }

    }



}
