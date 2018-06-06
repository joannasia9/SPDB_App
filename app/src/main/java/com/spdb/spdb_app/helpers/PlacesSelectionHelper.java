package com.spdb.spdb_app.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.spdb.spdb_app.FormActivity;
import com.spdb.spdb_app.R;

public class PlacesSelectionHelper {
    private Activity activity;
    private Context context;
    private PlaceDetectionClient mPlaceDetectionClient;
    private CharSequence location;

   public interface OnReceivedPlaceCallback{
       void onPlaceReceived(CharSequence receivedPlaceName,Place place);
       void onPlaceFailure(Exception e);
   }

    public PlacesSelectionHelper(Activity activity, Context context){
        this.activity = activity;
        this.context = context;
    }

    public void askForPermissions() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                showExplainDialog();
            } else {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET},
                        FormActivity.LOCATION_PERMISSIONS_REQUEST);
            }
        }

    }

    private void showExplainDialog() {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(R.string.warning)
                .setMessage(context.getString(R.string.reason) + "\n" + context.getString(R.string.change_mind))
                .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                FormActivity.LOCATION_PERMISSIONS_REQUEST);
                    }
                })
                .setNegativeButton(R.string.quit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        activity.finish();
                    }
                }).create();

        dialog.show();
    }

    public void getCurrentLocation(final OnReceivedPlaceCallback callback) {
        location ="";

        final LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        assert locationManager != null;
        if (!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.location_disabled);
            builder.setMessage(R.string.turn_loc_on);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });
            builder.setNegativeButton(R.string.no, null);
            builder.create().show();
        } else {
          if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                  != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED||
                  ActivityCompat.checkSelfPermission(context, Manifest.permission.INTERNET)
                          != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET},
                        FormActivity.LOCATION_PERMISSIONS_REQUEST);
            } else {

              mPlaceDetectionClient = Places.getPlaceDetectionClient(context);

              Task<PlaceLikelihoodBufferResponse> placeResult = mPlaceDetectionClient.getCurrentPlace(null);
              placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                  @Override
                  public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                      PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
                      location = likelyPlaces.get(0).getPlace().getName();
                      if(location!=null) callback.onPlaceReceived(location,likelyPlaces.get(0).getPlace().freeze()); else callback.onPlaceFailure(task.getException());
                      likelyPlaces.release();
                      }
              });



            }
        }
    }
}
