package com.example.filipsaina.videoping.provider;
import com.example.filipsaina.videoping.provider.dailymotion.DailymotionsProvider;
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
    This is a workaround to the problem which keeps the code simple and efficient although it
    is not recommendable (may introduce unexpected application crashes due to Java garbage collector)
 */
public class ProviderList {

    //Every new provider needs to be included in this list
    private static Provider[] listOfAllProviders = {
            new YoutubeProvider(),          //currentProviderIndex is currently set to Youtube(0)
            new DailymotionsProvider()
    };

    private static int currentProviderIndex =0;

    public static String getProviderName(int index){
        String name = "";
        try{
            name = listOfAllProviders[index].getProviderName();
        }catch (ArrayIndexOutOfBoundsException e){}
        return name;
    }

    public static Provider[] getListOfAllProviders(){
        return listOfAllProviders;
    }

    public static int getCurrentProviderIndex(){ return currentProviderIndex; }

    public static void setCurrentProviderIndex(int index){
        currentProviderIndex = index;
    }

    public static Provider getProviderWithIndex(int i) throws ArrayIndexOutOfBoundsException{
        return listOfAllProviders[i];
    }

    public static Provider getCurrentProvider(){
        return listOfAllProviders[currentProviderIndex];
    }
}
