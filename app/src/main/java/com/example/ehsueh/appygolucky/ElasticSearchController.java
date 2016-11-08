package com.example.ehsueh.appygolucky;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;

/**
 * Created by Corey on 2016-11-07.
 *
 */

/*NOTE: this class may need to be split into User and Ride controller classes later.
    However, it may be simpler to keep it as one class.  We will see how big it gets. */

public class ElasticSearchController {
    private static JestDroidClient client;
    private static String teamName = "cmput301f16t03";

    public static class AddUsersTask extends AsyncTask<User, Void, Void> {

        @Override
        protected Void doInBackground(User... users) {
            verifySettings();

            for (User user: users) {
                Index index = new Index.Builder(user).index(teamName).type("user").build();

                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
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
            }

            return null;
        }
    }


    private static void verifySettings() {
        // if the client hasn't been initialized then we should make it!
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }
}
