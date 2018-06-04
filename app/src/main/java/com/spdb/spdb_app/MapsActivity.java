package com.spdb.spdb_app;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.spdb.spdb_app.models.ApiClient;
import com.spdb.spdb_app.models.ApiInterface;
import com.spdb.spdb_app.models.Points;
import com.spdb.spdb_app.models.Route;
import com.spdb.spdb_app.models.RouteModel;

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
    private ArrayList<List<LatLng>> allRoutesFound;
    private List<LatLng> decoded;

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

        getRoutePolyline();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void getRoutePolyline(){
        String API_KEY = getString(R.string.google_maps_key);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<RouteModel> requestRoute = apiInterface.getRoute(origin,destination,travelMode,API_KEY);
        requestRoute.enqueue(new Callback<RouteModel>() {
            @Override
            public void onResponse(@NonNull Call<RouteModel> call, @NonNull Response<RouteModel> response) {
                RouteModel model = response.body();
                if(model!=null){
                   Route[] routes =  model.getRoutes();


                   allRoutesFound = new ArrayList<>();
                   for(Route route : routes){
                       String polyline = route.getPolyline().getPoints();
                       decoded = PolyUtil.decode(polyline);
                       allRoutesFound.add(decoded);

                   }

                    int i = 0;
                   for (List<LatLng> list : allRoutesFound){
                       int j = 0;
                       for(LatLng item : list){
                           if(decoded!=null) if(decoded.size()!=0){
                               mMap.addPolyline(new PolylineOptions().addAll(list));
                               mMap.addMarker(new MarkerOptions().position(item).title("Punkt startowy"));
                               mMap.moveCamera(CameraUpdateFactory.newLatLng(item));
                           }
                           Log.e( "LatLng ",  j+ " listy "+i + ": " + item.latitude + ", " + item.longitude );
                          j++;
                       }
                       i++;
                   }


                }
            }

            @Override
            public void onFailure(@NonNull Call<RouteModel> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
