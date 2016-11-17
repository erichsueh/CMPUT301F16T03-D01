package com.example.ehsueh.appygolucky;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by Corey on 2016-11-07.
 */
//This code is based off of code we created in the labs

public class ElasticSearchUserController {
    private static JestDroidClient client;
    private static String teamName = "cmput301f16t03";
    private static String userType = "user";

    /**
     * Adds a user to the server.  Note that we could end up with duplicates.
     * The queryListener should check whether username is unique.
     */
    public static class AddUsersTask extends AsyncTask<User, Void, Void> {

        @Override
        protected Void doInBackground(User... users) {
            //We only want to process one user at each call
            User user = users[0];

            verifySettings();

            Index index = new Index.Builder(user).index(teamName).type(userType).build();

                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        //This doesn't seem to be adding the ID like I thought it would
                        user.setId(result.getId());

                    }
                    else {
                        Log.e("ESUser", "Elastic search was not able to add the user.");
                    }
                }
                catch (Exception e) {
                    Log.e("ESUser", "We failed to add a user to elastic search!");
                    e.printStackTrace();
                }

            return null;
        }
    }

    /**
     * Given a username, this will query the server for the corresponding user.  In order to avoid
     * freezing the UI thread, this uses a callback function to return the users.
     */
    //TODO: find a way to return the user without stalling the UI
    //Method is returning data without freezing UI taken from
    //http://stackoverflow.com/questions/7618614/return-data-from-asynctask-class
    //Nov 16 Adil Soomro
    public static class GetUserByUsernameTask extends AsyncTask<String, Void, List<User>> {
        private ESQueryListener queryListener;

        public GetUserByUsernameTask(ESQueryListener queryListener) {
            this.queryListener = queryListener;
        }

        @Override
        protected List<User> doInBackground(String... params) {
            verifySettings();

            if (params[0] == null || params[0].equals("")) {
                return null;
            }

            String search_string = "{\"query\": {\"match\": {\"username\": \"" + params[0] + "\"}}}";

            Search search = new Search.Builder(search_string)
                    .addIndex(teamName)
                    .addType(userType)
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {

                    return result.getSourceAsObjectList(User.class);

                } else {
                    return (List<User>) new ArrayList<User>();
                }
            } catch (IOException e) {
                Log.i("Error", "Failed to communicate with elasticsearch server");
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<User> retrievedUsers) {
            queryListener.onQueryCompletion(retrievedUsers);
        }
    }

    /**
     * Given an ID, deletes the corresponding user from the server.
     */
    public static class DeleteUserTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... IDs) {

            verifySettings();


            try {
                client.execute(new Delete.Builder(IDs[0])
                        .index(teamName)
                        .type(userType)
                        .build());
            }
            catch (Exception e) {
                Log.e("ESUser", "We failed to delete a user from elastic search!");
                e.printStackTrace();
            }

            return null;
        }
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
