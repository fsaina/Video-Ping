package com.example.filipsaina.videoping.provider.dailymotion;

import com.example.filipsaina.videoping.RecycleViewItemData;
import com.example.filipsaina.videoping.provider.Provider;
import com.example.filipsaina.videoping.provider.ProviderList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    private static final int LIMIT_RESULTS = 50;       //maximum number of elements per search

    @Override
    public List<RecycleViewItemData> fetchDataFromServer(String searchTerm) {

        List<RecycleViewItemData> returnElements = new ArrayList<>();

        //preset json request
        //more information on >> https://developer.dailymotion.com/tools#/video
        String searchFullUrl ="https://api.dailymotion.com/videos?fields=description,id,thumbnail_480_url,title,&flags=no_live,no_premium&search=<SEARCHTERM>&sort=relevance&limit=<LIMIT>";
        searchTerm = searchTerm.trim().replaceAll(" ", "+");
        searchFullUrl = searchFullUrl.replace("<SEARCHTERM>", searchTerm);
        searchFullUrl = searchFullUrl.replace("<LIMIT>", LIMIT_RESULTS+"");

        String response = performHttpConnection(searchFullUrl);

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

                returnElements.add(new RecycleViewItemData(title, thumbnailUrl, videoId, description, ProviderList.getCurrentProviderIndex()));
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
                + PROVIDER_EMBED_URL_PREFIX + videoId + PROVIDER_EMBED_URL_SUFFIX + "<START_TIME>" //TODO fix this
                + " frameborder=0 webkitAllowFullScreen mozallowfullscreen allowFullScreen></iframe></body></html>";
        url = url.replaceAll("<START_TIME>", START_TIME_SUFFIX + startTime);
        return url;
    }

    private String performHttpConnection(String url) {
        String resString = "";
        try {
            HttpClient httpclient = new DefaultHttpClient(); // Create HTTP Client
            HttpGet httpget = new HttpGet(url); // Set the action you want to do
            HttpResponse response = httpclient.execute(httpget); // Executeit
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent(); // Create an InputStream with the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);

            resString = sb.toString();

            is.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return  resString;
    }
}
