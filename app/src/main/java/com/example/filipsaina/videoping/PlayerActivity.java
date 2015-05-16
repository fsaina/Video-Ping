package com.example.filipsaina.videoping;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;


/*
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
