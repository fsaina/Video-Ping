package com.example.filipsaina.videoping.provider.youtube;

import com.example.filipsaina.videoping.RecycleViewItemData;
import com.example.filipsaina.videoping.provider.Provider;
import com.example.filipsaina.videoping.provider.ProviderList;
import com.example.filipsaina.videoping.provider.ProviderUtil;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

import org.joda.time.Period;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * YouTube provider implementation.
 * (Every provider implementation must be added to the > Provider[] listOfAllProviders < variable
 * within the HomeActivity.java calls)
 * Created by filipsaina on 15/05/15.
 */
public class YoutubeProvider implements Provider {

    //predefined youtube necessary parameters
    private static final String PROVIDER_NAME = "Youtube";
    protected static final String YOUTUBE_API_KEY = "AIzaSyD0dkCKWmkzLIJJ0ALFokXlnq7e9n9epyo";
    private static final String APPLICATION_NAME = "Video ping";
    private static final long LIMIT_RESULTS = 15;       //define maximum number of elements per search
    private static final String PROVIDER_EMBED_URL_PREFIX = "http://www.youtube.com/embed/";
    private static final String PROVIDER_EMBED_URL_SUFFIX = "?rel=0&amp;autoplay=1&showinfo=0&controls=0";
    private static final String START_TIME_SUFFIX = "&start=";

    private List<RecycleViewItemData> providerResult = new ArrayList<>();

    /**
    Method required by the Provider interface.
    All data fetching, internal client object initialization and manipulation should be
    done here, as for data grabbing and formatting.
     @param searchTerm represents what request will be send to the provider
     */
    @Override
    public List<RecycleViewItemData> fetchDataFromServer(String searchTerm) {
        try {
            YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName(APPLICATION_NAME).build();
            YouTube.Search.List search = youtube.search().list("id,snippet");

            //set key
            search.setKey(YOUTUBE_API_KEY);
            search.setQ(searchTerm);       //serch term is defined here

            //restric to videos
            search.setType("video");

            //grab from the servers just the stuff we need
            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url,snippet/description)");
            search.setMaxResults(LIMIT_RESULTS);

            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();
            if (searchResultList != null) {
                dataFormat(searchResultList);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return providerResult;
    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }


    /**
     This is a necessary method that should provide the caller with the url link for the
     WebView widget.
     */
    @Override
    public String getFullVideoUrl(String videoId, String startTime) {

        String Url = "<html> <head> <style type=text/css> iframe {height:100%;width:100%;margin:0;padding:0;overflow:scroll;} body {background-color:#000; margin:0;}</style> </head> <body> <iframe width=240px height=220px src="
                + PROVIDER_EMBED_URL_PREFIX + videoId + PROVIDER_EMBED_URL_SUFFIX + START_TIME_SUFFIX + startTime
                + " frameborder=0 webkitAllowFullScreen mozallowfullscreen allowFullScreen></iframe></body></html>";
        return Url;
    }

    /*
    This method is used to transfer received data provided by the YouTube class
    from type <SearchResult> into <RecycleViewItemData> that is required for the
    fetchDataFromServer method
    */
    private void dataFormat(List<SearchResult> SearchResults) {

        for(int i=0; i<SearchResults.size();i++){
            SearchResult singleVideo = SearchResults.get(i);
            ResourceId rId = singleVideo.getId();

            // Confirm that the result represents a video. Otherwise, the
            // item will not contain a video ID.
            if (rId.getKind().equals("youtube#video")) {
                Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();

                //retrive the full videoData
                String response = "https://www.googleapis.com/youtube/v3/videos?id=VIDEOID&part=contentDetails&key=APIKEY";
                response = response.replaceAll("VIDEOID", rId.getVideoId());
                response = response.replaceAll("APIKEY", YOUTUBE_API_KEY);
                String videoJsonResponse = ProviderUtil.performHttpConnection(response);
                String duration="";
                int durationSeconds =0;

                //Google API v3 requires another call to get the video duration
                try {
                    JSONObject json = new JSONObject(videoJsonResponse);
                    JSONArray resultArray =json.optJSONArray("items");

                    JSONObject content = resultArray.getJSONObject(0);
                    JSONObject contentDetails = content.getJSONObject("contentDetails");
                    duration= contentDetails.get("duration").toString();

                    PeriodFormatter pf = ISOPeriodFormat.standard();
                    Period period = pf.parsePeriod(duration);
                    durationSeconds = period.toStandardSeconds().getSeconds();

                }catch (Exception e){
                    e.printStackTrace();
                }

                providerResult.add(new RecycleViewItemData(
                        singleVideo.getSnippet().getTitle(),    // video title
                        thumbnail.getUrl(),                     // thumbnail URL
                        rId.getVideoId(),                       // video ID
                        singleVideo.getSnippet().getDescription(), //description of the video
                        durationSeconds,                        //duration of the video in seconds
                        ProviderList.getProviderIndexWithName(getProviderName())
                        ));
            }
        }
    }

}
