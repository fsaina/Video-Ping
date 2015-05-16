package com.example.filipsaina.videoping;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.filipsaina.videoping.provider.YoutubeProvider;


/**
Activity that will be used to play video
 */

public class PlayerActivity extends ActionBarActivity {

    private RecycleViewItemData currentElement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        //retrieve the data from the last activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String description = extras.getString("videoDescription");
            String videoTitle = extras.getString("videoTitle");
            String imageUrl = extras.getString("imageUrl");
            String videoId = extras.getString("videoId");

            currentElement = new RecycleViewItemData(videoTitle,imageUrl, videoId ,description);
        }


        //set visual elements
        TextView description = (TextView) findViewById(R.id.videoDescription);
        TextView title = (TextView) findViewById(R.id.title);

        description.setText(currentElement.getVideoDescription());
        title.setText(currentElement.getVideoTitle());


        //now populate key elements of our video player application
        //by replacing finished elements in out layout

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //TODO for simplicity Im just using the Youtube service, here should be added
        //(based on 'currentElement') selection of Services that will call getPlayerFragment
        YoutubeProvider yp = new YoutubeProvider();
        Fragment player = yp.getPlayerFragment(currentElement.getVideoId());

        RelativeLayout playerSpace = (RelativeLayout) findViewById(R.id.player);
        fragmentTransaction.add(playerSpace.getId(), player, "something").commit();     //TODO check documentation about this "something"

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_to_right_enter_element, R.anim.left_to_right_exit_element);
    }

    //TODO implement this stuff
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_player, menu);
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
