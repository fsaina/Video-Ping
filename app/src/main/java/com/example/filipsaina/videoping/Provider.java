package com.example.filipsaina.videoping;

import android.view.View;

import java.util.List;

/**
 * Created by filipsaina on 15/05/15.
 */
public interface Provider {

    public List<RecycleViewItemData> fetchDataFromServer(String searchTerm);
    public String getProviderName();
    public View getPlayerView(final String videoId);

}
