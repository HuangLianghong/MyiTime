package com.example.myself.myitime.data;

import android.content.Context;

import com.example.myself.myitime.MainActivity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by Myself on 2019/11/18.
 */

public class FileDataSource {
    private Context context;

    public FileDataSource(Context context) {
        this.context = context;
    }

    public ArrayList<Item> getItems() {
        return items;
    }
    private ArrayList<Integer> RGB = MainActivity.RGB;
    private ArrayList<Item> items=new ArrayList<Item>();

    public ArrayList<Integer> getRGB() {
        return RGB;
    }

    public void save()
    {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    context.openFileOutput("Serializable.txt",Context.MODE_PRIVATE)
            );
            outputStream.writeObject(items);
            outputStream.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<Item> loadItems()
    {
        try{
            ObjectInputStream inputStream = new ObjectInputStream(
                    context.openFileInput("Serializable.txt")
            );
            items = (ArrayList<Item>) inputStream.readObject();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }


}
