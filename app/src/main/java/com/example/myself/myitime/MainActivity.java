package com.example.myself.myitime;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myself.myitime.data.ImageFilter;
import com.example.myself.myitime.data.Item;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_CODE_ADD = 1;
    private ArrayList<Item> theItems;
    private EventsArrayAdapter listviewAdapter = null;
    Bitmap mImageIds ;  //转换后的bitmap


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //悬浮按钮事件监听

                Intent intent = new Intent(MainActivity.this, AddActivity.class);
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

        //Init data
        theItems = new ArrayList<Item>();

        //set adapter for ListView
        listviewAdapter = new EventsArrayAdapter(this, R.layout.event, theItems);
        ListView listView = (ListView) findViewById(R.id.list_view_items);
        listView.setAdapter(listviewAdapter);
        this.registerForContextMenu(listView);
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
/*
        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

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

            Item good_item = this.getItem(position);
            byte[] picMatrix = good_item.getPic();
            Bitmap bitmap = Bytes2Bimap(picMatrix);
            bitmap = ImageFilter.blurBitmap(getApplicationContext() , bitmap, 2);

            img.setImageBitmap(bitmap);
            title.setText(good_item.getTitle());
            remark.setText(good_item.getRemark());
            date.setText(good_item.getDate());

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



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_ADD){
           if(resultCode==RESULT_OK){


               String title=data.getStringExtra("title");
               String remark=data.getStringExtra("remark");
               byte[] res = data.getByteArrayExtra("picture");
               Item item =new Item(title, remark, "2019.11.4", res);

               theItems.add(item);
               listviewAdapter.notifyDataSetChanged();
           }
        }
    }



}
