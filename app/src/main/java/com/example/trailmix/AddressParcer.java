package com.example.trailmix;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


/**
 * @author Andy Goering
 * @date 05/18/2018
 * Class for getting trip durations using Google Maps API
 */
public class AddressParcer extends AsyncTask<Void,Void,Void> {
    MainActivity mainActivity;
    public AddressParcer(MainActivity mainActivity){
        this.mainActivity= mainActivity;
    }

    /**
     * Reads a file from a URL
     * @param URL
     * @return The text in the file
     * @throws IOException
     */
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

    /**
     * Gets a JSON file from Google Maps giving the driving time from one place to another.
     * @param lon (origin longitude)
     * @param lat (origin latitude)
     * @param destination (destination descriptor String. Could be an address or the name of a place.)
     * @return time form the origin to the destination
     */
    public static double getTripDuration(String lon, String lat, String destination) {
        try {
            String json;
            Log.d("Address", "In the method");
            json = readURL("https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + lat + "," + lon + "&destinations="+ plusify(destination) +"&key=AIzaSyASVU_Ws92GaMXBNhtREbNYXR3WBsbqDP0");
            Log.d("Address", "Got the URL!");
            System.out.println(json);
            System.out.println(json.lastIndexOf("value") + "hi");
            if(json.indexOf("value")>-1) {
                String durationString = json.substring(json.lastIndexOf("value") + 9, json.indexOf(" ", json.lastIndexOf("value") + 9));
                System.out.println(durationString);
                durationString = durationString.trim();
                int duration = Integer.parseInt(durationString);
                System.out.println(duration);
                return duration / 60;
            }
            else{
                return -1;
            }

        } catch (IOException e) {
            Log.d("Error", "-" + 1);
            return -1;
        }
    }

    /**
     * Takes a String where tokens are delineated by spaces and switches the paces to "+".
     * @param s
     * @return
     */
    public static String plusify(String s) {
        for(int i = 0; i < s.length(); i++)
            if(s.charAt(i)==' ')
                s = s.substring(0,i) + "+" + s.substring(i+1);

        return s;
    }

    /**
     * Android won't allow URL retrieval in an activity so this thing has to operate in a separate thread.
     * This method describes what an instance of this class will do in that other thread.
     * @param voids
     * @return null
     */
    @Override
    protected Void doInBackground(Void... voids) {
        mainActivity.setTripDuration(AddressParcer.getTripDuration(mainActivity.getLon(),mainActivity.getLat(),mainActivity.getAddress()));
        return null;
    }
}

