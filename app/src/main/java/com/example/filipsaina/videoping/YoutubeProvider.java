package com.example.filipsaina.videoping;

import android.view.View;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * YouTube prvoider implementation.
 * (Every provider implementation must be added to the > Provider[] listOfAllProviders < variable
 * within the HomeActivity.java calss)
 * Created by filipsaina on 15/05/15.
 */
public class YoutubeProvider  implements Provider {

    //predefined youtube necessary parameters
    private static final String PROVIDER_NAME = "Youtube";
    private static final String YOUTUBE_API_KEY = "AIzaSyD0dkCKWmkzLIJJ0ALFokXlnq7e9n9epyo";
    private static final String APPLICATION_NAME = "Video ping";
    private static final long LIMIT_RESULTS = 20;

    private List<RecycleViewItemData> providerResult = new ArrayList<>();


    /*
    Method requred by the Provider interface.
    All data fetching, internal client object initialization and manipulation sould be
    done here, as for data grabing and formating.
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
                dataFormat(searchResultList.iterator());
            }

        } catch (Exception e) {
            //TODO check documentation for thrown exceptions for better exception handling
            e.printStackTrace();
        }
        return providerResult;
    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }


    /*
    This is a necessary method that should provide the caller with a View element that
    is capable of playing the video the service provides.
    Later this View is going to be replaced with anoteher view at runtime

     */
    @Override
    public View getPlayerView(final String videoId) {


        return null;
    }

    /*
    This method is used to transfer receved data provided by the YouTube class
    from type <SearchResult> into <RecycleViewItemData> that is required for the
    fetchDataFromServer method
    */
    private void dataFormat(Iterator<SearchResult> iteratorSearchResults) {
        while (iteratorSearchResults.hasNext()) {

            SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();

            // Confirm that the result represents a video. Otherwise, the
            // item will not contain a video ID.
            if (rId.getKind().equals("youtube#video")) {
                Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();
                providerResult.add(new RecycleViewItemData(
                        singleVideo.getSnippet().getTitle(),    // video title
                        thumbnail.getUrl(),                     // thumbnail URL
                        rId.getVideoId(),                       // video ID
                        singleVideo.getSnippet().getDescription()));        //description of the video
            }
        }
    }

}
