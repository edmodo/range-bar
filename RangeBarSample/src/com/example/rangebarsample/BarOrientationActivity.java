package com.example.rangebarsample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by jayeshthadani on 7/28/15.
 */
public class BarOrientationActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barorientation_layout);

        final ListView listview = (ListView) findViewById(R.id.listview);

        String[] values = new String[]{"Horizontal Bar", "Vertical Bar"};

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(BarOrientationActivity.this,
                android.R.layout.simple_list_item_1, values);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (position == 0){
                    startActivity(new Intent(BarOrientationActivity.this, HorizontalRatingBarActivity.class));
                }else if(position == 1){
                    startActivity(new Intent(BarOrientationActivity.this, VerticalRatingBarActivity.class));
                }
            }
        });
    }

}
