package com.spdb.spdb_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.spdb.spdb_app.models.ApiClient;
import com.spdb.spdb_app.models.ApiInterface;
import com.spdb.spdb_app.models.LegModel;
import com.spdb.spdb_app.models.Points;
import com.spdb.spdb_app.models.Route;
import com.spdb.spdb_app.models.RouteModel;
import com.spdb.spdb_app.models.StepModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String travelMode;
    private String destination;
    private String origin;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        travelMode = intent.getStringExtra("mode");
        Log.e("TravelMode: ", travelMode);
        destination = intent.getStringExtra("destination");
        Log.e("Destination: ", destination);
        origin = intent.getStringExtra("origin");
        Log.e("Origin: ", origin);
        address = intent.getStringExtra("address");
        Log.e("Dest address: ", address);

        getRoutePolyline(origin, destination, travelMode);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void getRoutePolyline(final String origin, final String destination, String travelMode) {
        String API_KEY = getString(R.string.google_maps_key);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<RouteModel> requestRoute = apiInterface.getRoute(origin, destination, travelMode, API_KEY);
        requestRoute.enqueue(new Callback<RouteModel>() {
            @Override
            public void onResponse(@NonNull Call<RouteModel> call, @NonNull Response<RouteModel> response) {
                RouteModel model = response.body();
                if (model != null) {
                    List<List<LatLng>> routes = new ArrayList<>();
                    Route[] routesList = model.getRoutes();

                    //Traversing through all of routes
                    for (Route singleRoute : routesList) {
                        LegModel[] legs = singleRoute.getLegs();
                        for (LegModel legModel : legs) {
                            StepModel[] steps = legModel.getSteps();
                            for (StepModel stepModel : steps) {
                                Points polyline = stepModel.getPolyline();
                                routes.add(PolyUtil.decode(polyline.getPoints()));
                                Log.e("Decoded polyline", polyline.getPoints());
                            }
                        }
                    }

                    //Adding points to the polyline
                    PolylineOptions polylineOptions = null;
                    polylineOptions = new PolylineOptions();
                    for (List<LatLng> singleRoute : routes) {
                        polylineOptions.addAll(singleRoute);
                        polylineOptions.width(10.0f);
                        polylineOptions.color(Color.GREEN);
                        Log.d("onPostExecute", "onPostExecute lineoptions decoded");
                    }

                    // Drawing polyline in the Google Map for the i-th route
                    addMarkers(origin, destination);
                    mMap.addPolyline(polylineOptions);


                }
            }

            @Override
            public void onFailure(@NonNull Call<RouteModel> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    ArrayList<MarkerOptions> markers;

    private void addMarkers(String origin, String destination) {
        String[] destLatLng = destination.split(",");
        String[] originLatLng = origin.split(",");
        LatLng originLL = new LatLng(Double.parseDouble(originLatLng[0]), Double.parseDouble(originLatLng[1]));
        LatLng destLL = new LatLng(Double.parseDouble(destLatLng[0]), Double.parseDouble(destLatLng[1]));


        markers = new ArrayList<>();
        markers.add(new MarkerOptions().position(originLL).title("Punkt początkowy")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        markers.add(new MarkerOptions().position(destLL).title(address)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        // Add new marker to the Google Map Android API V2
        for (MarkerOptions options : markers) {
            mMap.addMarker(options);

        }

        // Move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(originLL,17.0f));

    }

}
