package com.example.ehsueh.appygolucky;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;


public interface DrawingLocationActivity {

    public void drawRouteOnMap(List<LatLng> drawPoints, String distance);

}
