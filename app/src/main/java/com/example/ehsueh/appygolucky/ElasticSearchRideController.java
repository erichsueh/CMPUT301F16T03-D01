package com.example.ehsueh.appygolucky;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.searchbox.client.JestResult;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Get;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;


public class ElasticSearchRideController {
    private static JestDroidClient client;
    private static String teamName = "cmput301f16t03";
    private static String rideType = "ride";

    /**
     * Adds a ride to the server.
     * Passes the RideId to an ESQueryListener, as we will want to add this ID to the user's list
     */
    public static class AddRideTask extends AsyncTask<Ride, Void, List<String>> {
        ESQueryListener queryListener;

        public AddRideTask(ESQueryListener queryListener) {
            this.queryListener = queryListener;
        }

        @Override
        protected List<String> doInBackground(Ride... rides) {
            //We only want to process one ride at each call
            Ride ride = rides[0];

            List<String> rideIds = new ArrayList<String>();

            verifySettings();

            Index index = new Index.Builder(ride).index(teamName).type(rideType).build();

            try {
                DocumentResult result = client.execute(index);
                if (result.isSucceeded()) {
                    ride.setId(result.getId());
                    rideIds.add(result.getId());
                }
                else {
                    Log.e("ESRide", "Elastic search was not able to add the ride.");
                }
            }
            catch (Exception e) {
                Log.e("ESRide", "We failed to add a ride to elastic search!");
                e.printStackTrace();
            }

            return rideIds;
        }

        @Override
        protected void onPostExecute(List<String> rideIds) {
            queryListener.onQueryCompletion(rideIds);
        }
    }

    public static class GetRidesByIdTask extends AsyncTask<String, Void, List<Ride>> {
        private ESQueryListener queryListener;

        public GetRidesByIdTask(ESQueryListener queryListener) {
            this.queryListener = queryListener;
        }

        @Override
        public List<Ride> doInBackground(String... params) {
            verifySettings();

            if (params[0] == null || params[0].equals("")) {
                return null;
            }

            List<Ride> results = new ArrayList<Ride>();

            for(String id: params) {
                Get get = new Get.Builder(teamName, id).type(rideType).build();
                try {
                    JestResult result = client.execute(get);
                    Ride ride = result.getSourceAsObject(Ride.class);
                    results.add(ride);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return results;
        }

        /**
         * Called at the completion of the task.  Feeds the retrieved data to the ESQueryListener
         * to do any further computation/action.
         */
        @Override
        public void onPostExecute(List<Ride> retrievedRides) {
            queryListener.onQueryCompletion(retrievedRides);
        }
    }

    public static class GetRidesByKeywordTask extends AsyncTask<String, Void, List<Ride>> {
        private ESQueryListener queryListener;


        public GetRidesByKeywordTask(ESQueryListener queryListener) {
            this.queryListener = queryListener;
        }

        @Override
        protected List<Ride> doInBackground(String... params) {
            verifySettings();

            if (params[0] == null || params[0].equals("")) {
                return null;
            }

            String search_string =
                    "{\"query\":" +
                    "   {" +
                    "       \"query_string\" : " +
                    "           {" +
                    "               \"query\" : \"" + params[0] +"\""+
                    "           }" +
                    "   }" +
                    "}";

            Search search = new Search.Builder(search_string)
                    .addIndex(teamName)
                    .addType(rideType)
                    .build();

            try {
                SearchResult result = client.execute(search);
                if (result.isSucceeded()) {

                    return result.getSourceAsObjectList(Ride.class);

                } else {
                    return (List<Ride>) new ArrayList<Ride>();
                }
            } catch (IOException e) {
                Log.i("Error", "Failed to communicate with elasticsearch server");
                e.printStackTrace();
                return null;
            }
        }

        /**
         * Called at the completion of the task.  Feeds the retrieved data to the ESQueryListener
         * to do any further computation/action.
         */
        @Override
        protected void onPostExecute(List<Ride> retrievedRides) {
            queryListener.onQueryCompletion(retrievedRides);
        }
    }





    /**
     * This allows us to delete a ride.
     */
    public static class DeleteRideTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... IDs) {

            verifySettings();


            try {
                client.execute(new Delete.Builder(IDs[0])
                        .index(teamName)
                        .type(rideType)
                        .build());
            }
            catch (Exception e) {
                Log.e("ESRide", "We failed to delete a ride from elastic search!");
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
