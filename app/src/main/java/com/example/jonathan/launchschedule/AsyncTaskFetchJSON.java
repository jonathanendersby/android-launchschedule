package com.example.jonathan.launchschedule;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class AsyncTaskFetchJSON extends AsyncTask<String, Void, String> {
    private static final String TAG = "LongOp";
    public AsyncResponse delegate=null;

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }

    @Override
    protected String doInBackground( String... params) {
        Log.v(TAG, "Fetching launch data...");

        // Fetch the JSON from the web server.
        String json = "";
        try {
            URL launch_json_source = new URL("http://underground.co.za/launchschedule.json");
            URLConnection yc = launch_json_source.openConnection();

            try (Scanner s = new Scanner(yc.getInputStream())) {
                while (s.hasNextLine()) {
                    json +=  s.nextLine();
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

            Log.v(TAG, json);

        } catch (Exception name){
            Log.v(TAG, "EXCEPTION" + name.getMessage());
        }

        final String json2 = json;

        try {
            // Parse the JSON
            JSONObject json_obj = new JSONObject(json2);
            JSONArray launches_obj = json_obj.getJSONArray("launches");

            // Empty the launches list.
            LaunchData.launches.clear();

            // Create new Launch objects and add them to the Launches List.
            for (int i = 0; i < launches_obj.length(); i++) {

                JSONObject launch_obj = launches_obj.getJSONObject(i);

                Launch launch = new Launch();
                launch.description = launch_obj.getString("description");

                if (launch_obj.has("gmt_date") && !launch_obj.isNull("gmt_date")) {
                    launch.gmtDateString = launch_obj.getString("gmt_date");
                    launch.date = Util.convertStringToDate(launch_obj.getString("gmt_date"));
                }

                if (launch_obj.has("mission") && !launch_obj.isNull("mission")) {
                    launch.mission = launch_obj.getString("mission");
                }

                if (launch_obj.has("launch_site") && !launch_obj.isNull("launch_site")) {
                    launch.launchSite = launch_obj.getString("launch_site");
                }

                // Add the launch to the launche list.
                LaunchData.launches.add(launch);

            }
        }catch(Exception e){
            Log.v(TAG, "Exception: " + e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }
}

