package com.example.filipsaina.videoping;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
            int duration = extras.getInt("duration");
            int providerIndex = extras.getInt("providerIndex");

            currentElement = new RecycleViewItemData(videoTitle,imageUrl, videoId ,description,duration,providerIndex);
        }

        //set title
        TextView description = (TextView) findViewById(R.id.videoDescription);
        TextView title = (TextView) findViewById(R.id.title);

        //set description
        description.setText(currentElement.getVideoDescription());
        title.setText(currentElement.getVideoTitle());

        //set duration
        TextView duration = (TextView) findViewById(R.id.videoDuration);
        duration.setText(RecycleViewAdapter.getFromatedTime(currentElement.getDurationInSeconds()));

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
                    if (isPlaying) onPlayPauseButtonPressed(null);
                }
            }
        }, intentFilter);
    }

    /*
    Define the button press operation for the Play/Pause button
     */
    public void onPlayPauseButtonPressed(View v){
        changePlayPauseIcon();
        VideoWebViewPlayer.emulateClick(videoPlayer, 0);
    }

    private void changePlayPauseIcon() {
        Button playPause = (Button) findViewById(R.id.playPause);
        if(isPlaying) {
            playPause.setBackgroundResource(R.drawable.play);
        }else{
            playPause.setBackgroundResource(R.drawable.pause);
        }
        isPlaying = !isPlaying;
    }

    public void onStopButtonPressed(View v){
        if(isPlaying) {
            changePlayPauseIcon();
        }
        videoPlayer.stop();
    }

    /*
    Show dialog and ask the user for input
     */
    public void onJumpButtonPressed(View v) {

        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setGravity(Gravity.CENTER);
        editText.setTextColor(Color.WHITE);
        editText.setMaxLines(1);
        editText.setText("0");
        editText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(6)});
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.jump_dialog_message_text))
                .setMessage(null)       //no need for a description message
                .setView(editText)
                .setPositiveButton(getString(R.string.jump_dialog_confirm_button), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = editText.getText().toString();
                        int numericalValue = Integer.parseInt(value);
                        if(numericalValue > currentElement.getDurationInSeconds()){
                            Toast.makeText(getApplicationContext(), getString(R.string.jump_dialog_onTooBigValueProvided),Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(isPlaying == false) changePlayPauseIcon();
                        videoPlayer.seekTo(value);
                    }
                }).setNegativeButton(getString(R.string.jump_dialog_cancle_button), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Do nothing
            }
        }).show();
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