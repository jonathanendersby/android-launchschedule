package com.example.jonathan.launchschedule;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;

/**
 * Created by jonathan on 15/07/05.
 */
public class Util {
    private static final String TAG = "Launcher";

    public static Date convertStringToDate(String dateString) {
        //2015-07-03 04:55:00+00:00
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss+00:00");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            Log.v(TAG, "Can not parse date string");
            return null;
        }

        return convertedDate;
    }


}


