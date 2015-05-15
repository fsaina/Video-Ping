package com.example.filipsaina.videoping;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;


public class HomeActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        //start of code

        //grab a reference to the recyclerView
        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerView);

        //dummy data
        RecycleViewItemData[] dummyData = {
                new RecycleViewItemData("prvi text", "bokobk"),
                new RecycleViewItemData("drugi text", "bokobk"),
                new RecycleViewItemData("treci text", "bokobk"),
                new RecycleViewItemData("cetvrti text", "bokobk"),
                new RecycleViewItemData("peti text", "bokobk"),
                new RecycleViewItemData("sesti text", "bokobk"),
                new RecycleViewItemData("prvi text", "bokobk"),
                new RecycleViewItemData("drugi text", "bokobk"),
                new RecycleViewItemData("treci text", "bokobk"),
                new RecycleViewItemData("cetvrti text", "bokobk"),
                new RecycleViewItemData("peti text", "bokobk"),
                new RecycleViewItemData("sesti text", "bokobk"),
        };

        rv.setLayoutManager(new LinearLayoutManager(this));
        RecycleViewAdapter rva = new RecycleViewAdapter(dummyData, rv);
        rv.setAdapter(rva);
        rv.setItemAnimator(new DefaultItemAnimator());



    }


    //TODO define settings parameters and basic settings functinality
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_home, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
