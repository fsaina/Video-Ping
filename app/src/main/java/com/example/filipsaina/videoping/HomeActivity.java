package com.example.filipsaina.videoping;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.filipsaina.videoping.provider.Provider;
import com.example.filipsaina.videoping.provider.ProviderList;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;


/**
 * This project('Video ping') and all its code was created for the 'Croapps workshop', 2015.
 */

public class HomeActivity extends AppCompatActivity implements ThreadCompleteListener {

    //drawer reference(on drawerSetup)
    private Drawer.Result drawer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //give the new appCompat v21 toolbar widget properties of a actionBar
        actionBarSetup();

        //define the looks and properties
        recyclerViewSetup();

        //populate with elements
        drawerSetup();

        //initial search for avoiding a blank screen
        onSearchPerform("");
    }

    private void actionBarSetup() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.menu_icon));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    //new threads are dispatched and the UI thread awaits for results
    public void onSearchPerform(String searchTerm){
        //in case there is no internet connection - show a message
        if(!haveNetworkConnection()){
            Toast.makeText(this,getString(R.string.NoInternetConnectionMessage), Toast.LENGTH_SHORT).show();
            return;
        }
        ThreadManager tm = new ThreadManager(searchTerm);
        tm.addListener(this);

        //apply the current provider for data grabbing from the Internet
        //>>on the selected Provider class will be done the >fetchDataFromServer< method
        Provider selectedProvider = ProviderList.getCurrentProvider();
        tm.addProvider(selectedProvider);

        //start loading animation
        SmoothProgressBar progressBar = (SmoothProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        tm.start();
    }

    //this method is called every time a dispatched thread completed its task
    //so it is notifying the caller thread of its completion
    @Override
    public void notifyOfThreadComplete(final List<RecycleViewItemData> data) {
        final Context contextReference = this.getApplicationContext();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (data.size() > 0) {
                    //update the RecyclerView
                    recyclerUpdateView(data);
                } else {
                    Toast.makeText(contextReference, getString(R.string.NoMatchFoundMessage), Toast.LENGTH_SHORT).show();
                }

                //end loading progress animation
                SmoothProgressBar progressBar = (SmoothProgressBar) findViewById(R.id.progressBar);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
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
     * @param data RecycleViewItemData object with all the data
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
    Set up the drawer trough out the application and all its elements
     */
    private void drawerSetup() {
        //for more details
        //https://github.com/mikepenz/MaterialDrawer
        Drawer.Result result = new Drawer()
                .withActivity(this)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        ProviderList.setCurrentProviderIndex(position);
                    }
                })
                .build();

        //add all the providers to the drawer
        for(Provider provider: ProviderList.getListOfAllProviders()){
            result.addItem(new PrimaryDrawerItem().withName(provider.getProviderName()));
        }
        //add the divider at the end(with a description)
        result.addItem(new SectionDrawerItem().withName(getString(R.string.DrawerTitleMessage)));
        this.drawer = result;
    }

    //method used to change the open/close state of the drawer
    //used as an onClick call
    private void drawerChangeState(){
        if(drawer.isDrawerOpen()){
            drawer.closeDrawer();
        } else {
            drawer.openDrawer();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen()) {
            //id the drawer is open - close if first(but don't exit the app)
            drawerChangeState();
        } else {
            super.onBackPressed();
        }
    }

    /*
    Populate the options menu with items defined in <menu_home.xml>
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_home, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        //set the text functions of the search_view component
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onSearchPerform(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //do nothing
                //optimally here should be some 'showSuggestions' method call implemented
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    /*
    Give functionality to the menu item(home button)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            drawerChangeState();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*
        Method used for checking if there is an active Internet connection on the
        mobile device (wifi or mobile)
         */
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

}