package com.example.ehsueh.appygolucky;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;
import java.util.List;

import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by Corey on 2016-11-07.
 *
 */

/*NOTE: this class may need to be split into User and Ride controller classes later.
    However, it may be simpler to keep it as one class.  We will see how big it gets. */

public class ElasticSearchUserController {
    private static JestDroidClient client;
    private static String teamName = "cmput301f16t03";
    private static String userType = "user";

    /** Adds a user to the server.  Note that we could end up with duplicates.
     *  The caller should check whether username is unique. */
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
     * By input a username string, this outputs the user obj if it exists return null otherwise.
     */
    public static class CheckUsernameTask extends AsyncTask<String, Void, List<User>> {
        @Override
        protected List<User> doInBackground(String... params) {
            verifySettings();

            if (params[0]=="" | params[0]==null) {
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

                    List<User> users = result.getSourceAsObjectList(User.class);

                    if(users.size() == 0) {
                        return null;
                    } else {
                        return users;
                    }

                } else {
                    return null;
                }
            } catch (IOException e) {
                Log.i("Error", "Failed to communicate with elasticsearch server");
                e.printStackTrace();
                return null;
            }
        }
    }
    //Called after request object has been returned by search
    //By input a user obj, this outputs the rest of user info
    //return null otherwise
    //TODO: use the username instead of the id for this query
    // (because we don't know how to save the user id)

    /**
     * Called after request object has been returned by search; input is a user obj, this outputs
     * the rest of user info, return null otherwise.
     */
    public static class retrieveUserInfo extends AsyncTask<User, Void, User> {
        @Override
        protected User doInBackground(User... users) {

            verifySettings();

            String search_string = "{\"query\": {\"match\": {\"id\": \"" + users[0].getId() + "\"}}}";

            Search search = new Search.Builder(search_string)
                    .addIndex(teamName)
                    .addType(userType)
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {


                    User user = result.getSourceAsObject(User.class);

                    if (user.getId().equals(users[0].getId())) {
                        return user;
                    }
                    else {
                        return null;
                    }
                } else {
                    return null;
                }
            } catch (IOException e) {
                Log.i("Error", "Failed to communicate with elasticsearch server");
                e.printStackTrace();
                return null;
            }
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

    /**
     * This allows us to delete a user.
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



}
