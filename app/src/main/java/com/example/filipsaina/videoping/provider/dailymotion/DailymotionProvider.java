package com.example.filipsaina.videoping.provider.dailymotion;

import com.example.filipsaina.videoping.RecycleViewItemData;
import com.example.filipsaina.videoping.provider.Provider;
import com.example.filipsaina.videoping.provider.ProviderList;
import com.example.filipsaina.videoping.provider.ProviderUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Dailymotion provider Class
 * Created by filipsaina on 18/05/15.
 */
public class DailymotionProvider implements Provider {

    private static final String PROVIDER_NAME = "Dailymotion";
    private static final String PROVIDER_EMBED_URL_PREFIX = "http://www.dailymotion.com/embed/video/";
    private static final String PROVIDER_EMBED_URL_SUFFIX = "&logo=0&info=0&seek=30&related=0";
    private static final String START_TIME_SUFFIX = "";
    private static final int LIMIT_RESULTS = 25;       //maximum number of elements per search

    @Override
    public List<RecycleViewItemData> fetchDataFromServer(String searchTerm) {

        List<RecycleViewItemData> returnElements = new ArrayList<>();

        //preset json request
        //more information on >> https://developer.dailymotion.com/tools#/video
        String searchFullUrl ="https://api.dailymotion.com/videos?fields=description,duration,id,thumbnail_480_url,title,&flags=no_live,no_premium&search=<SEARCHTERM>&sort=relevance&limit=<LIMIT>";
        searchTerm = searchTerm.trim().replaceAll(" ", "+");
        searchFullUrl = searchFullUrl.replace("<SEARCHTERM>", searchTerm);
        searchFullUrl = searchFullUrl.replace("<LIMIT>", LIMIT_RESULTS+"");

        String response = ProviderUtil.performHttpConnection(searchFullUrl);

        try {
            JSONObject json = new JSONObject(response);
            JSONArray resultArray =json.optJSONArray("list");


            for(int i=0; i<resultArray.length();i++){
                JSONObject jsonChildNode = resultArray.getJSONObject(i);

                //data elements
                String title = jsonChildNode.optString("title");
                String thumbnailUrl = jsonChildNode.optString("thumbnail_480_url");
                String videoId = jsonChildNode.optString("id");
                String description = jsonChildNode.optString("description");
                String duration = jsonChildNode.optString("duration");

                returnElements.add(new RecycleViewItemData(title, thumbnailUrl, videoId, description,Integer.parseInt(duration), ProviderList.getProviderIndexWithName(this.getProviderName())));
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return returnElements;
    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }

    @Override
    public String getFullVideoUrl(String videoId, String startTime) {
        startTime = "";
        String url = "<html> <head> <style type=text/css> iframe {height:100%;width:100%;margin:0;padding:0;overflow:scroll;} body {background-color:#000; margin:0;}</style> </head> <body> <iframe width=240px height=220px src="
                + PROVIDER_EMBED_URL_PREFIX + videoId + PROVIDER_EMBED_URL_SUFFIX + "<START_TIME>"
                + " frameborder=0 webkitAllowFullScreen mozallowfullscreen allowFullScreen></iframe></body></html>";
        url = url.replaceAll("<START_TIME>", START_TIME_SUFFIX + startTime);
        System.out.println(url);
        return url;
    }

}
