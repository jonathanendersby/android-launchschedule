package com.example.jonathan.launchschedule;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver implements AsyncResponse {
    private static final String TAG = "Alarm";

    @Override
    public void onReceive(Context context, Intent intent)  {

        // For our recurring task, we'll just display a message
        Toast.makeText(context, "LaunchSchedule Update Running", Toast.LENGTH_SHORT).show();
        AsyncTaskFetchJSON lo = new AsyncTaskFetchJSON();
        lo.delegate = this;
        lo.execute("");
    }

    @Override
    public void processFinish(String output) {

    }
}