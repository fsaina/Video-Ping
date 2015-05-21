package com.example.filipsaina.videoping;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;

import com.example.filipsaina.videoping.provider.Provider;
import com.example.filipsaina.videoping.provider.ProviderList;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;


/**
Initaial class Activity
 */

public class HomeActivity extends ActionBarActivity implements ThreadCompleteListener {

    //drawer reference(on drawerSetup)
    private Drawer.Result drawer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);

        //recycleView setup
        recyclerViewSetup();

        //drawer setup
        drawerSetup();

        //initial Search
        onSearchPerform("");
    }

    //TODO onFocusLost for the searchView component(ditch the keyboard when the used decides to scroll in the midddle of typingd)

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

    //new threads are dispatched and the UI thread awaits for results
    public void onSearchPerform(String searchTerm){
        ThreadManager tm = new ThreadManager(searchTerm);
        tm.addListener(this);

        //apply the current provider for data grabbing from the Internet
        //>>on the slected Provider class will be done the >fetchDataFromServer< method
        Provider selectedProvider = ProviderList.getCurrentProvider();
        tm.addProvider(selectedProvider);

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
                        ProviderList.setCurrentProviderIndex(position);
                        onSearchPerform("");
                    }
                })
                .build();

        for(Provider provider: ProviderList.getListOfAllProviders()){
            result.addItem(new PrimaryDrawerItem().withName(provider.getProviderName()));
        }
        this.drawer = result;
    }

    //method used to change the open/close state of the drawer
    //used as an onClick call
    //should be used in case an menu button is added to the toolbar widget
    private void drawerChangeState(){
        if(drawer.isDrawerOpen()){
            drawer.closeDrawer();
        } else {
            drawer.openDrawer();
        }
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen()){
            //id the drawer is open - close if first(but don't exit the app)
            drawerChangeState();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        SearchView search=(SearchView) findViewById(R.id.search_view);

        //set the text functions of the search_view component
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onSearchPerform(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //do nothing
                //optimally here shound be some 'showSuggestions' call implemented
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            drawerChangeState();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
