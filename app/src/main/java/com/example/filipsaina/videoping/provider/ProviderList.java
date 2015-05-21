package com.example.filipsaina.videoping.provider;
import com.example.filipsaina.videoping.provider.dailymotion.DailymotionProvider;
import com.example.filipsaina.videoping.provider.youtube.YoutubeProvider;

/**
 * Static class that holds the references to Objects that implemented the Provider interface.
 *      Important: Every Object that implements the Provider interface needs to be
 *      included in this here object(via listOfAllProvides array)
 *
 * Created by filipsaina on 18/05/15.
 */
/*
    Because 'Provider' objects are too complex, they are not transferable between Activities.
    Therefore they are introduced in this Object containing static data and methods so they
    can be available trough-out the application.
    This is a workaround to the problem which keeps the code simple and efficient.
 */
public class ProviderList {

    //Every new provider needs to be included in this list(and just this one)
    private static Provider[] listOfAllProviders = {
            new YoutubeProvider(),
            new DailymotionProvider()
    };

    private static int currentProviderIndex =0;        //by default it is 0 (Youtube)

    public static Provider[] getListOfAllProviders(){
        return listOfAllProviders;
    }

    public static int getCurrentProviderIndex(){
        return currentProviderIndex;
    }

    public static void setCurrentProviderIndex(int index){
        currentProviderIndex = index;
    }

    public static Provider getProviderWithIndex(int i){
        return listOfAllProviders[i];
    }

    public static Provider getCurrentProvider(){
        return listOfAllProviders[currentProviderIndex];
    }
}
