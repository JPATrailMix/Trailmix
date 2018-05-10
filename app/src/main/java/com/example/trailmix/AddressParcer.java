package com.example.trailmix;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class AddressParcer extends AsyncTask<Void,Void,Void> {
    MainActivity mainActivity;
    public AddressParcer(MainActivity mainActivity){
        this.mainActivity= mainActivity;
    }

    public static String readURL(String URL) throws IOException { // I found this code on stackoverflow.com
        BufferedReader reader = null;
        try {
            URL url = new URL(URL);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        }
        finally {
            if (reader != null)
                reader.close();
        }
    }

    public static double getTripDuration(String lon, String lat, String destination) {
        try {
            String json;
            Log.d("Address", "In the method");
            json = readURL("https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + lat + "," + lon + "&destinations="+ plusify(destination) +"&key=AIzaSyASVU_Ws92GaMXBNhtREbNYXR3WBsbqDP0");
            Log.d("Address Parcer", "I got the URL!");
            System.out.println(json);
            System.out.println(json.lastIndexOf("value"));
            System.out.println(json.substring(json.lastIndexOf("value")));
            String durationString = json.substring(json.lastIndexOf("value") + 9,json.lastIndexOf("value") + 9 + 7);
            System.out.println(durationString);
            durationString = durationString.trim();
            int duration = Integer.parseInt(durationString);
            System.out.println(duration);
            return duration/60;

        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }

    public static String plusify(String s) {
        for(int i = 0; i < s.length(); i++)
            if(s.charAt(i)==' ')
                s = s.substring(0,i) + "+" + s.substring(i+1);

        return s;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        mainActivity.setTripDuration(AddressParcer.getTripDuration(mainActivity.getLon(),mainActivity.getLat(),mainActivity.getAddress()));
        return null;
    }
}

