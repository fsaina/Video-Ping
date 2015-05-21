package com.example.filipsaina.videoping.provider;

import com.example.filipsaina.videoping.RecycleViewItemData;

import java.util.List;

/**
 * Interface that every provider class must fulfill.
 * Created by filipsaina on 15/05/15.
 */
public interface Provider {

    public String getProviderName();
    public List<RecycleViewItemData> fetchDataFromServer(String searchTerm);
    public String getFullVideoUrl(String videoId, String startTime);

}
