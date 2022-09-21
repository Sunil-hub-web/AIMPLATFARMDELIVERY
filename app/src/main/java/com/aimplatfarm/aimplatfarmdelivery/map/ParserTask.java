package com.aimplatfarm.aimplatfarmdelivery.map;



import static com.aimplatfarm.aimplatfarmdelivery.Fragments.MapsFragment.mGoogleMap;
import static com.aimplatfarm.aimplatfarmdelivery.Fragments.MapsFragment.polyline;
import static com.aimplatfarm.aimplatfarmdelivery.Fragments.MapsFragment.polylines;
import static com.google.android.gms.maps.model.JointType.ROUND;

import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

    // Parsing the data in non-ui thread
    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

        JSONObject jObject;
        JSONObject j_Start_Address;
        JSONObject j_Desti_Address;
        List<List<HashMap<String, String>>> routes = null;

        try {
            jObject = new JSONObject(jsonData[0]);

            DirectionsJSONParser parser = new DirectionsJSONParser();
            // Starts parsing data
            routes = parser.parse(jObject);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return routes;
    }

    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
        ArrayList<LatLng> points = null;
        PolylineOptions polyLineOptions = null;
        String distance = "";
        String duration = "";

        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        // traversing through routes
        try {
            if (!routes.isEmpty()) {
                if (routes.size() > 0) {
                    for (int i = 0; i < routes.size(); i++) {

                        points = new ArrayList<LatLng>();
                        polyLineOptions = new PolylineOptions();
                        List<HashMap<String, String>> path = routes.get(i);

                        // Fetching all the points in i-th route
                        for (int j = 0; j < path.size(); j++) {
                            HashMap<String, String> point = path.get(j);

                            if (j == 0) {    // Get distance from the list
                                distance = (String) point.get("distance");
                                continue;
                            } else if (j == 1) { // Get duration from the list
                                duration = (String) point.get("duration");
                                continue;
                            }

                            if (point.get("lat") != null || point.get("lng") != null) {
                                double lat = Double.parseDouble(point.get("lat"));
                                double lng = Double.parseDouble(point.get("lng"));
                                LatLng position = new LatLng(lat, lng);
                                points.add(position);

                            }
                        }
                      //  ((HomeFragment) homeFragment).showTime(distance, duration);

                        polyLineOptions.addAll(points);

                    }
                    polyLineOptions = new PolylineOptions();
                    polyLineOptions.color(Color.GREEN);
                    polyLineOptions.width(20);
                    polyLineOptions.startCap(new SquareCap());
                    polyLineOptions.endCap(new SquareCap());
                    polyLineOptions.jointType(ROUND);
                    polyLineOptions.addAll(points);
                }

            }
            if (polyLineOptions != null) {
                polyline = mGoogleMap.addPolyline(polyLineOptions);
                polylines.add(polyline);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

