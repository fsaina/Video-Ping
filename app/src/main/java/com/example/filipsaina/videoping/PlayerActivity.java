package com.example.filipsaina.videoping;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.filipsaina.videoping.provider.Provider;
import com.example.filipsaina.videoping.provider.ProviderList;


/**
Activity that will be used to play video
 */

public class PlayerActivity extends ActionBarActivity  {

    private static float scale =0;
    private boolean isPlaying = false;  //TODO fix this when autoplay is implemneted

    private RecycleViewItemData currentElement;
    VideoWebViewPlayer videoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        scale = getResources().getDisplayMetrics().density;

        //retrieve the data from the selected data element
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String description = extras.getString("videoDescription");
            String videoTitle = extras.getString("videoTitle");
            String imageUrl = extras.getString("imageUrl");
            String videoId = extras.getString("videoId");
            String duration = extras.getString("duration");
            int providerIndex = extras.getInt("providerIndex");

            currentElement = new RecycleViewItemData(videoTitle,imageUrl, videoId ,description, duration, providerIndex);
        }


        //set title
        TextView description = (TextView) findViewById(R.id.videoDescription);
        TextView title = (TextView) findViewById(R.id.title);

        //set description
        description.setText(currentElement.getVideoDescription());
        title.setText(currentElement.getVideoTitle());

        //TODO add video duration information
        //get video provider from the element
        Provider provider = ProviderList.getProviderWithIndex(currentElement.getProviderIndex());

        //set the videoPlayer element
        videoPlayer = (VideoWebViewPlayer) findViewById(R.id.webPlayer);
        videoPlayer.playVideo(provider, currentElement.getVideoId());
    }

    public void onPlayPauseButtonPressed(View v){
        Button playPause = (Button) findViewById(R.id.playPause);
        if(isPlaying) {
            playPause.setBackgroundResource(R.drawable.play);
        }else{
            playPause.setBackgroundResource(R.drawable.pause);
        }

        isPlaying = !isPlaying;
        VideoWebViewPlayer.emulateClick(videoPlayer, 0);
    }

    public void onJumpButtonPressed(View v) {
        EditText seekField = (EditText) findViewById(R.id.seekField);
        videoPlayer.seekTo(seekField.getText().toString());
    }

    @Override
    public void onBackPressed() {
        videoPlayer.loadUrl("");    //small hack to stop the video
        finish();
        overridePendingTransition(R.anim.left_to_right_enter_element, R.anim.left_to_right_exit_element);
    }

}
