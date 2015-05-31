package com.example.filipsaina.videoping.provider;
import com.example.filipsaina.videoping.provider.dailymotion.DailymotionProvider;
import com.example.filipsaina.videoping.provider.youtube.YoutubeProvider;


/**
 * Static class that holds the references to Objects that implemented the Provider interface.
 *      Important: Every Object that implements the Provider interface needs to be
 *      included in this object(via listOfAllProvides array)
 *
 * Created by filipsaina on 18/05/15.
 */
/*
    Because 'Provider' objects are too complex, they are not transferable between Activities.
    Therefore they are introduced in this Object containing static data and methods so they
    can be available trough-out the application.
    This is a workaround to the problem which keeps the code simple and efficient although it
    is not recommendable (may introduce, in some rare cases, unexpected application crashes)
 */
public class ProviderList {

    //Every new provider needs to be included in this list
    private static Provider[] listOfAllProviders = {
            new YoutubeProvider(),
            new DailymotionProvider()
    };


    public static String getProviderNameWithIndex(int index){
        String name = "";
        try{
            name = listOfAllProviders[index].getProviderName();
        }catch (ArrayIndexOutOfBoundsException e){}
        return name;
    }

    public static Provider[] getListOfAllProviders(){
        return listOfAllProviders;
    }

    public static Provider getProviderWithName(String name){
        int index = getProviderIndexWithName(name);
        return getProviderWithIndex(index);
    }

    public static int getProviderIndexWithName(String keyName){
        int i =0;
        for(Provider p: listOfAllProviders){
            if(p.getProviderName().equalsIgnoreCase(keyName)) break;
            i++;
        }
        return i;
    }


    public static Provider getProviderWithIndex(int i) throws ArrayIndexOutOfBoundsException{
        return listOfAllProviders[i];
    }

}
