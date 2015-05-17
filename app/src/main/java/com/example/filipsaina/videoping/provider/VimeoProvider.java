package com.example.filipsaina.videoping.provider;

import android.support.v4.app.Fragment;

import com.example.filipsaina.videoping.JSONParser;
import com.example.filipsaina.videoping.RecycleViewItemData;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by filipsaina on 17/05/15.
 */
public class VimeoProvider implements Provider {
    private static String PROVIDER_NAME = "Vimeo";
    private static String PROVIDER_PREFIX = "http://vimeo.com/api/v2/video/";

    @Override
    public List<RecycleViewItemData> fetchDataFromServer(String searchTerm) {

        // Creating new JSON Parser
        JSONParser jParser = new JSONParser();

        // Getting JSON from URL
        JSONObject json = jParser.getJSONFromUrl("https://vimeo.com/api/v2/video/126580704.json");      //TODO remove hardcode, use PROVIDER_PREFIX
        return null;
    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }

    @Override
    public Fragment getPlayerFragment(String videoId) {
        return null;
    }
}
