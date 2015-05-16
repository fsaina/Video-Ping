package com.example.filipsaina.videoping.provider;

import android.os.Bundle;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

/**
 * Class used for easier instantiating new YoutubePlayerSupportFragmnet objects
 *
 * Created by filipsaina on 16/05/15.
 */
public class YoutubeVideoFragment extends YouTubePlayerSupportFragment {


    public static YoutubeVideoFragment newInstance(String url) {
        YoutubeVideoFragment videoFragment = new YoutubeVideoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        videoFragment.setArguments(bundle);
        videoFragment.init(0);
        return videoFragment;
    }

    private void init(final int time) {
        initialize(YoutubeProvider.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
            }

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
                String id = getArguments().getString("url");
                if (!wasRestored) {
                    player.loadVideo(id, time);
                }
            }
        });
    }
}