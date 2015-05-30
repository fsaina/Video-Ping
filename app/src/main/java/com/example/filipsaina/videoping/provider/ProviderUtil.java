package com.example.filipsaina.videoping.provider;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Class providing with various methods that Provider classes need to implement.
 * Removes some of the redundant code.
 *
 * Created by filipsaina on 30/05/15.
 */
public class ProviderUtil {

    public static String performHttpConnection(String url) {
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
