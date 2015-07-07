package com.example.jonathan.launchschedule;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener, AsyncResponse{
    ListView listView;
    private static final String TAG = "Launcher";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    final static Handler handler = new Handler();
    AsyncTaskFetchJSON lo = new AsyncTaskFetchJSON();
    ListDataAdapter adapter;


    @Override
    public void onRefresh() {
        AsyncTaskFetchJSON lo = new AsyncTaskFetchJSON();
        lo.delegate = this;
        Log.v(TAG, "pull refresh");
        lo.execute();
    }


    public void processFinish(String output){
        adapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        listView = (ListView) findViewById(R.id.listView);
        adapter = new ListDataAdapter(this, LaunchData.launches);
        listView.setAdapter(adapter);

        // Set up the Swipe Refresher
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);


        // Run the alarm receiver (fetches updated JSON) every 30 minutes.
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1800000, pi);
        Toast.makeText(this, "Alarm Set from Main Activity", Toast.LENGTH_SHORT).show();

        lo.delegate = this;
        lo.execute("");

        // Refresh the listview every minute.
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
                handler.postDelayed(this, 60 * 1000);
                Log.v(TAG, "Updating list...");
            }
        }, 60 * 1000);


        // Override the scroll to refresh listener so that it only triggers when you're at the tip
        // of the list and not everyt
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition =
                        (listView == null || listView.getChildCount() == 0) ?
                                0 : listView.getChildAt(0).getTop();
                mSwipeRefreshLayout.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_landing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

