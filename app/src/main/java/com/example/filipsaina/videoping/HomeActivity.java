package com.example.filipsaina.videoping;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends ActionBarActivity implements ThreadCompleteListener {

    //drawer reference(on drawerSetup)
    private Drawer.Result drawer = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }

        //dummy data
        List<RecycleViewItemData> list = new ArrayList<RecycleViewItemData>();
        list.add(new RecycleViewItemData("Title 1", "url", "asd"));
        list.add(new RecycleViewItemData("Title 2", "url", "asd"));
        list.add(new RecycleViewItemData("Title 3", "url", "asd"));

        //recyle view setup with all its elements and settings
        recylerViewSetup(list);

        //drawer setup
        drawerSetup();

    }


    @Override
    public void notifyOfThreadComplete(final List<RecycleViewItemData> data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recylerViewSetup(data);
            }
        });

    }

    public void onSearchButtonPressed(View v){
        EditText edv = (EditText) findViewById(R.id.editText);
        String searchTerm = edv.getText().toString();


        //TODO better thread implementation, big memory hog (more implements Runnable, less extends Thread)
        //release the hounds!
        ThreadManager tm = new ThreadManager();
        tm.addListener(this);
        //add all the providers here
        Provider youTube = new YoutubeProvider(searchTerm);
        tm.addProvider(youTube);
        tm.start();

    }

    /*
        Method used to set up the recycleView with all its elements.
        Method is invoked with the 'data' parameter that consists of RecycleViewItemData objects
         */
    private void recylerViewSetup(List<RecycleViewItemData> data) {
        //grab a reference to the recyclerView
        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerView);

        rv.setLayoutManager(new LinearLayoutManager(this));

        RecycleViewItemData[] array = data.toArray(new RecycleViewItemData[data.size()]);
        RecycleViewAdapter rva = new RecycleViewAdapter(array, rv);

        rv.setAdapter(rva);
        rv.setItemAnimator(new DefaultItemAnimator());
    }

    /*
    Set up the drawer troughout the application and all its elements
     */
    private void drawerSetup() {
        //TODO add to the drawer a title saying something like "Selected services:"
        //for more details
        //https://github.com/mikepenz/MaterialDrawer
        Drawer.Result result = new Drawer()
                .withActivity(this)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        //TODO do something after the click on the item (probablly a checkbox with the name and icon of the service)
                        //that are going to be included
                    }
                })
                .build();


        //TODO add a number of services
        result.addItem(new DividerDrawerItem());
        result.addItem(new PrimaryDrawerItem().withName("Youtube"));
        result.addItem(new PrimaryDrawerItem().withName("Vimeo"));
        this.drawer = result;
    }

    //method used to change the open/close state of the drawer
    //used as an onClick call
    public void drawerChangeState(View v){
        if(drawer.isDrawerOpen() == true){
            drawer.closeDrawer();
        } else {
            drawer.openDrawer();
        }
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
