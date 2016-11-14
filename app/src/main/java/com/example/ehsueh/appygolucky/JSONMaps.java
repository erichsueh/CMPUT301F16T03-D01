package com.example.ehsueh.appygolucky;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used in create the JSON request from a list of lats and long and then parsing through
 * the results returned from hte google maps.
 * Reference:http://stackoverflow.com/questions/14702621/answer-draw-path-between-two-points-using-google-maps-android-api-v2
 */
class JSONMaps {
    private  DrawingLocationActivity act;

    public JSONMaps(DrawingLocationActivity act){
        this.act = act;
    }

    /**
     * This sets up the async task and calls it. This async task does alot of communication with google maps
     * but then simply draws the correct route from these two locations on the map inside the activity.
     * @param startLocation
     * @param endLocation
     */
    public void drawPathCoordinates(Marker startLocation, Marker endLocation){

        String url = createURl(startLocation, endLocation);
        urlParser impl  = new urlParser();
        impl.execute(url);
    }

    /**
     * This functions creates a URL JSON request from two google map markers
     * @param startLocation
     * @param endLocation
     * @return A valid String url request for calculated route
     */
    private String createURl(Marker startLocation, Marker endLocation){
        double latStart = startLocation.getPosition().latitude;
        double lonStart = startLocation.getPosition().longitude;
        double latEnd   = endLocation.getPosition().latitude;
        double lonEnd   = endLocation.getPosition().longitude;

        StringBuilder url = new StringBuilder();
        url.append("https://maps.googleapis.com/maps/api/directions/json");
        url.append("?origin=");// from
        url.append(Double.toString(latStart));
        url.append(",");
        url.append(Double.toString(lonStart));
        url.append("&destination=");// to
        url.append(Double.toString(latEnd));
        url.append(",");
        url.append(Double.toString(lonEnd));
        url.append("&sensor=false&mode=driving&alternatives=true");
        url.append("&key=AIzaSyCsF_t4VNATAc0QtC3jKPnpSglUWhpSwCM");

        return url.toString();
    }

    /**
     * This function connects with google server and get a JSON object able to draw the route
     * from a proper URL request.
     * @param url
     * @return JSON string object
     */
    public String getJsonFromUrlRequest(String url){

        //Make the url request alot of things can go wrong here
        HttpURLConnection urlConnection = null;
        String results = "";
        try{
            URL validUrl = new URL(url);
            urlConnection = (HttpURLConnection) validUrl.openConnection();
            urlConnection.connect();

            BufferedReader is = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            results = readInStream(is);
        }
        catch(MalformedURLException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        finally{
            if(urlConnection != null){
                urlConnection.disconnect();
            }
        }
        return results;
    }

    /**
     * Read in the http request from the website
     * @param is Buffered Reader of input stream
     * @return return the results from the reader
     */
    private String readInStream(BufferedReader is){

        try {
            StringBuilder builder = new StringBuilder();

            //Loop through the reader and get all the lines
            String currentLine;
            while ((currentLine = is.readLine()) != null) {
                builder.append(currentLine + "\n");
            }

            is.close();
            return builder.toString();
        }
        catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }

        return "";
    }

    /**
     * Get all the draw results from JSON string
     * @param result result from google server request
     * @return latlng list of all te points to draw lines to
     */
    private List<LatLng> getDrawPath(String result) {
        List<LatLng> list;
        try {
            //Tranform the string into a json object
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            list = decodePoly(encodedString);
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private String getDistance(String result){
        try{
            JSONObject jsonObject = new JSONObject(result);
            JSONArray array = jsonObject.getJSONArray("routes");
            JSONObject routes = array.getJSONObject(0);
            JSONArray legs = routes.getJSONArray("legs");
            JSONObject steps = legs.getJSONObject(0);
            JSONObject distance = steps.getJSONObject("distance");
            String dist = distance.getString("text");
            return dist;
        }
        catch(JSONException e){
            e.printStackTrace();
            return "";
        }

    }

    /**
     * This is just copied and pasted from :http://stackoverflow.com/questions/14702621/answer-draw-path-between-two-points-using-google-maps-android-api-v2
     * Decoded poly line from jason object and turns them into a more understandable latLng list
     * @param encoded
     * @return
     */
    private List<LatLng> decodePoly(String encoded){
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }

    /**
     * private async task so the html requests are run in the background and not on ui thread
     */
    private class urlParser extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url){
            String Json = "";
            try{
                Json = getJsonFromUrlRequest(url[0]);
            }
            catch(Exception e){
                e.printStackTrace();
            }

            return Json;
        }

        @Override
        protected void onPostExecute(String Result) {
            super.onPostExecute(Result);

            List<LatLng> paths = getDrawPath(Result);
            String distance = getDistance(Result);
            act.drawRouteOnMap(paths, distance);
        }


    }
}
