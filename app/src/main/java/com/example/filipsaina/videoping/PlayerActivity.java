package com.example.filipsaina.videoping;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.filipsaina.videoping.provider.Provider;
import com.example.filipsaina.videoping.provider.ProviderList;


/**
Activity that will be used to play video
 */

public class PlayerActivity extends AppCompatActivity {

    private boolean isPlaying = true;

    private RecycleViewItemData currentElement;
    private VideoWebViewPlayer videoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        //define on screen off behaviour (pause the reproduction)
        onScreenChangeStateSetup();

        //retrieve the data from the selected data element
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String description = extras.getString("videoDescription");
            String videoTitle = extras.getString("videoTitle");
            String imageUrl = extras.getString("imageUrl");
            String videoId = extras.getString("videoId");
            int providerIndex = extras.getInt("providerIndex");

            currentElement = new RecycleViewItemData(videoTitle,imageUrl, videoId ,description,providerIndex);
        }

        //set title
        TextView description = (TextView) findViewById(R.id.videoDescription);
        TextView title = (TextView) findViewById(R.id.title);

        //set description
        description.setText(currentElement.getVideoDescription());
        title.setText(currentElement.getVideoTitle());

        //get video provider from the element
        Provider provider = ProviderList.getProviderWithIndex(currentElement.getProviderIndex());

        //set the videoPlayer element
        videoPlayer = (VideoWebViewPlayer) findViewById(R.id.webPlayer);
        videoPlayer.playVideo(provider, currentElement.getVideoId());
    }

    private void onScreenChangeStateSetup() {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                    if(isPlaying) onPlayPauseButtonPressed(null);
                }
            }
        }, intentFilter);
    }

    /*
    Define the button press operation for the Play/Pause button
     */
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

    /*
    Define the button press operation for the Jump button 
     */
    public void onJumpButtonPressed(View v) {
        EditText seekField = (EditText) findViewById(R.id.seekField);
        videoPlayer.seekTo(seekField.getText().toString());
    }

    /*
    In order to stop the video execution, before killing the Activity we
    load a blank page onto the webView. To stop the reproduction.
     */
    @Override
    public void onBackPressed() {
        videoPlayer.loadUrl("");    //small hack to stop the video
        finish();
        overridePendingTransition(R.anim.left_to_right_enter_element, R.anim.left_to_right_exit_element);
    }
}