package com.example.filipsaina.videoping;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.filipsaina.videoping.provider.Provider;
import com.example.filipsaina.videoping.provider.YoutubeProvider;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;


/**
Initaial class Activity
 */

public class HomeActivity extends ActionBarActivity implements ThreadCompleteListener {

    //adding new providers is done here, simple as including them in this array
    public Provider[] listOfAllProviders = {
            new YoutubeProvider()
    };

    //drawer reference(on drawerSetup)
    private Drawer.Result drawer = null;
    private long noThread =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            try {
                getSupportActionBar().setTitle(null);
            }catch (Exception e){}      //TODO bad soution(find better one)
        }

        //recycleView setup
        recyclerViewSetup();

        //drawer setup
        drawerSetup();

        //seatch button setup
        Button searchButton = (Button) findViewById(R.id.button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchButtonPressed(null);
            }
        });


    }

    //this method is called everythime a dispached thread completed its task
    //in this implementation there is just one Thread
    @Override
    public void notifyOfThreadComplete(final List<RecycleViewItemData> data) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //update the RecyclerView
                    recyclerUpdateView(data);

                    //end animation
                    SmoothProgressBar progressBar = (SmoothProgressBar) findViewById(R.id.progressBar);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });

    }

    //event when the user presses the button
    //new threads are dispached and the UI thread awaits for results
    public void onSearchButtonPressed(View v){
        AutoCompleteTextView edv = (AutoCompleteTextView) findViewById(R.id.searchField);
        String searchTerm = edv.getText().toString();

        ThreadManager tm = new ThreadManager(searchTerm);
        tm.addListener(this);

        //TODO should i use all the providers at once or just one? fix this accordingly
        //list all the providers as the job the thread will need to do
        for(Provider provider : listOfAllProviders){
            tm.addProvider(provider);
        }

        //start loading animation
        SmoothProgressBar progressBar = (SmoothProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        tm.start();
    }

    /*
    Initial widget setup that is called once, onCreate
    */
    private void recyclerViewSetup() {
        //grab a reference to the recyclerView
        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        RecycleViewItemData[] array = new RecycleViewItemData[0];
        RecycleViewAdapter rva = new RecycleViewAdapter(array, rv, this);
        rv.setAdapter(rva);
        rv.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * Method used to update the recycleView with all its elements.
     Method is invoked with the 'data' parameter that consists of RecycleViewItemData objects
     which will populate the widget
     * @param data RecycleViewItemData object will all the data
     */

    private void recyclerUpdateView(List<RecycleViewItemData> data){

        RecyclerView rv = (RecyclerView) findViewById(R.id.recyclerView);
        RecycleViewItemData[] array = data.toArray(new RecycleViewItemData[data.size()]);
        RecycleViewAdapter rva = new RecycleViewAdapter(array, rv, this);

        rv.setAdapter(rva);

        //clean the data(necessary)
        data.clear();
    }

    /*
    Set up the drawer troughout the application and all its elements
     */
    private void drawerSetup() {
        //TODO add to the drawer a title saying something like "Select video service provider:"
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

        for(Provider provider: listOfAllProviders){
            result.addItem(new PrimaryDrawerItem().withName(provider.getProviderName()));
        }
        this.drawer = result;
    }

    //method used to change the open/close state of the drawer
    //used as an onClick call
    //should be used in case an menu button is added to the toolbar widget
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
