package com.example.ehsueh.appygolucky.OfflineOnline;

/**
 * Created by Maxwell on 2016-11-22.
 */

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import android.util.Log;

import com.example.ehsueh.appygolucky.ElasticSearchUserController;
import com.example.ehsueh.appygolucky.RideList;
import com.example.ehsueh.appygolucky.User;
import com.google.gson.Gson;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

import io.searchbox.core.Delete;
import io.searchbox.core.Index;


/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 *
 * Portion of code was taken from:
 * https://developer.android.com/training/sync-adapters/creating-sync-adapter.html#CreateAccountTypeAccount
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    //...
    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;
    protected static OfflineJobsDeleteQueue deleteQueue;
    protected static OfflineJobsIndexQueue indexQueue;
    protected Context applicationContext;
    protected final String INDEXQUEUE = "index_queue.json";
    protected final String DELETEQUEUE = "delete_queue.json";
    private static JestDroidClient client;
    private static String teamName = "cmput301f16t03";
    private static String rideType = "ride";

    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }
    //...

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
        //...
    }


    public void loadFromFile() {
        try {
            //Load the current user
            FileInputStream fis = applicationContext.openFileInput(DELETEQUEUE);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();

            deleteQueue = gson.fromJson(in, OfflineJobsDeleteQueue.class);

            //Load the requested rides
            fis = applicationContext.openFileInput(INDEXQUEUE);
            in = new BufferedReader(new InputStreamReader(fis));

            indexQueue = gson.fromJson(in, OfflineJobsIndexQueue.class);


        } catch (FileNotFoundException e) {
            deleteQueue = new OfflineJobsDeleteQueue();
            indexQueue = new OfflineJobsIndexQueue();
        }

    }


    /*
     * Specify the code you want to run in the sync adapter. The entire
     * sync adapter runs in a background thread, so you don't have to set
     * up your own background processing.
     */
    @Override
    public void onPerformSync(
            Account account,
            Bundle extras,
            String authority,
            ContentProviderClient provider,
            SyncResult syncResult) {


    /*
     * Put the data transfer code here.
     */
        //This assumes that the network connection is back and we can execute tasks that had been
        // queued while offline
        // Load the two queues
        loadFromFile();
        verifySettings();

        while (indexQueue.hasItems()) {
            try {
                Index item = indexQueue.dequeue();
                client.execute(item);
            }
            catch (Exception e) {
                Log.e("ESRide", "We failed to add using elastic search!");
                e.printStackTrace();
            }
        }

        while (deleteQueue.hasItems()) {
            try {
                Delete item = deleteQueue.dequeue();
                client.execute(item);
            }
            catch (Exception e) {
                Log.e("ESRide", "We failed to delete using elastic search!");
                e.printStackTrace();
            }
        }

        // This code overwrites everything locally to ensure consistency
        //ElasticSearchUserController.GetUsersByUsernameTask getUsersByUsernameTask =
        //        new ElasticSearchUserController.GetUsersByUsernameTask();
        //getUsersByUsernameTask.execute(desiredUsername);

    }

    /**
     * If the client hasn't been initialized then we should make it!
     */
    private static void verifySettings() {
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }
}