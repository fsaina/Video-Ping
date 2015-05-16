package com.example.filipsaina.videoping.provider;

import android.support.v4.app.Fragment;

import com.example.filipsaina.videoping.RecycleViewItemData;

import java.util.List;

/**
 * Interface that every provider class must fulfill.
 * Created by filipsaina on 15/05/15.
 */
public interface Provider {

    public List<RecycleViewItemData> fetchDataFromServer(String searchTerm);
    public String getProviderName();
    public Fragment getPlayerFragment(final String videoId);

}
