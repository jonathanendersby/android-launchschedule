package com.example.jonathan.launchschedule;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ListDataAdapter extends ArrayAdapter<List>  {

    private static final String TAG = "ArrayAdapter";
    private final Context context;
    private final List values;

    public ListDataAdapter(Context context, List values) {
        super(context, R.layout.simplerow, values);
        this.context = context;
        this.values = values;
        Log.v(TAG, "Setting up adapter");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.simplerow, parent, false);


        // Get the various elements in the simplerow.
        TextView first = (TextView) rowView.findViewById(R.id.firstLine);
        TextView second = (TextView) rowView.findViewById(R.id.secondLine);
        TextView gmt_date = (TextView) rowView.findViewById(R.id.dateLine);
        TextView dateLocal = (TextView) rowView.findViewById(R.id.dateLocal);

        // Load up the launch instance.
        Launch launch = (Launch) values.get(position);

        // Set the various fields.
        first.setText(launch.mission);
        second.setText(launch.description);
        gmt_date.setText(launch.launchSite);

        if (launch.gmtDateString == null){
            // If we don't have a GMT date, collapse the view.
            rowView.findViewById(R.id.dateRow).setVisibility(View.GONE);
        }else{
            // If we do have a GMT date, calculate the delta and prettify the date string.
            Date now = new Date();
            long diff = launch.date.getTime() - now.getTime();
            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            String pretty_date = new SimpleDateFormat("dd MMM hh:mm").format(launch.date);
            if (days < 7){
                long actual_hours = hours - (days*24);
                dateLocal.setText(pretty_date + " (" + days +" days, " + actual_hours + " hours)");
            }else{
                dateLocal.setText(pretty_date);
            }
        }

        return rowView;
    }



}