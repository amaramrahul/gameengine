package com.example.gameengine.common;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by rahul on 22/2/16.
 */
public class Utils {
    public static String sendHttpPostRequest(URL url, String data) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type","application/json");
        conn.setRequestProperty("Accept","application/json");

        // Send post request
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(data);
        wr.flush();
        wr.close();

        StringBuffer response = new StringBuffer();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String inputLine;
        while((inputLine=in.readLine())!=null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    public static String sendHttpGetRequest(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept","application/json");

        StringBuffer response = new StringBuffer();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String inputLine;
        while((inputLine=in.readLine())!=null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }
}
