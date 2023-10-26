package com.example.basketbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    static ListView listView;
    static ArrayList<String> items;
    static ListViewAdapter adapter;

    EditText input;
    ImageView enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView =findViewById(R.id.listview);
        input =findViewById(R.id.input);
        enter = findViewById(R.id.add);

        listView =findViewById(R.id.listview);
        items=new ArrayList<>();
        items.add("Apples");
        items.add("banana");
        items.add("peanuts");
        items.add("oranges");
        items.add("bread");
        items.add("Eggs");
        items.add("flowers");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = items.get(i);
                makeToast(name);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                makeToast("removed:"+ items.get(i));
                removeItem(i);
                return false;

            }
        });


        adapter =new ListViewAdapter(getApplicationContext(),items);
        listView.setAdapter(adapter);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text =input.getText().toString();
                if(text == null || text.length() ==0 ){
                    makeToast("Enter an item.");
                }else{
                    addItem(text);
                    input.setText("");
                    makeToast("Added:"+ text);
                }
            }
        });
        loadContent();
    }

    public void loadContent(){
        File path =getApplicationContext().getFilesDir();
        File readFrom =new File(path,"list.txt");
        byte[] content =new byte[(int) readFrom.length()];

        FileInputStream stream= null;
        try {
            stream = new FileInputStream(readFrom);
            stream.read(content);

            String s =new String(content);
            s =s.substring(1,s.length()-1);
            String split[] =s.split(", ");
            items = new ArrayList<>(Arrays.asList(split));
            adapter=new ListViewAdapter(this,items);
            listView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy(){
        File path =getApplicationContext().getFilesDir();
        try {
            FileOutputStream writer =new FileOutputStream(new File(path,"list.txt"));
            writer.write(items.toString().getBytes());
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        super.onDestroy();
    }
    public static void removeItem(int remove){
        items.remove(remove);
        listView.setAdapter(adapter);
    }
    public static void addItem(String item){
        items.add(item);
        listView.setAdapter(adapter);
    }
    Toast t;
    private void makeToast(String s){
        if (t !=null) t.cancel();
        t= Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT);
        t.show();
    }
}