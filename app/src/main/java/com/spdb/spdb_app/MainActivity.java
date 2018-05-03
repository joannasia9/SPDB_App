package com.spdb.spdb_app;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;

import com.spdb.spdb_app.models.Category;

import io.realm.Realm;
import io.realm.RealmResults;


public class MainActivity extends AppCompatActivity {
    EditText fromPlace;
    AutoCompleteTextView category;
    EditText daysValue, hoursValue, minsValue;
    Spinner transport;
    TextView hours, minutes;
    SeekBar seekBar, radiusSeekBar;
    TextView radiusValue;
    public static final int LOCATION_PERMISSIONS_REQUEST = 111;
    Activity activity;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] categories_eng = getResources().getStringArray(R.array.categories_eng);
        String[] categories_pl = getResources().getStringArray(R.array.categories_pl);

        //save categories to the Realm local database
        realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        for(int i=0; i<categories_eng.length;i++){
            Category category = new Category(categories_pl[i], categories_eng[i]);
            realm.copyToRealm(category);
        }
        realm.commitTransaction();

        ////////////////////////////////////////////////////////////////////////

        activity = this;

        fromPlace = findViewById(R.id.from);
        transport = findViewById(R.id.spinner);

        hours = findViewById(R.id.hours);
        minutes = findViewById(R.id.mins);
        seekBar = findViewById(R.id.seekBar);
        category = findViewById(R.id.category);
        daysValue = findViewById(R.id.daysValue);
        hoursValue = findViewById(R.id.hoursValue);
        minsValue = findViewById(R.id.minsValue);

        radiusSeekBar = findViewById(R.id.radiusSeekBar);
        radiusValue = findViewById(R.id.radiusValue);

        seekBar.setMax(720);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress>60){
                    int hValue = Math.round(progress/60);
                    int mValue = progress - (hValue*60);
                    hours.setText(String.valueOf(hValue));
                    minutes.setText(String.valueOf(mValue));
                } else {
                    hours.setText(String.valueOf(0));
                    minutes.setText(String.valueOf(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        radiusSeekBar.setMax(100);
        radiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    radiusValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1 ,categories_pl);
        category.setAdapter(categoriesAdapter);

/*
        realm.beginTransaction();
        RealmResults<Category> cat = realm.where(Category.class)
                .equalTo("polishItemName","Lotnisko")
                .findAll();
        if(cat.size()>0)  category.setText(cat.get(0).getRealCategoryName());*/


    }

    public void searchPlaces(View view) {
    }

    public void getCurrentLocation(View view) {
        askForPermissions();
        fromPlace.setText(getCurrentLocation());
    }

    private void askForPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                showExplainDialog();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        LOCATION_PERMISSIONS_REQUEST);
            }
        }

    }

    private void showExplainDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.warning)
                .setMessage(getString(R.string.reason) + "\n" + getString(R.string.change_mind))
                .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                LOCATION_PERMISSIONS_REQUEST);
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

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSIONS_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                }
                break;
            }

        }
    }

    private String getCurrentLocation() {
        String location = "";
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.location_disabled);
            builder.setMessage(R.string.turn_loc_on);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });
            builder.setNegativeButton(R.string.no, null);
            builder.create().show();
        } else {
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, false);

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        LOCATION_PERMISSIONS_REQUEST);
            } else {

            Location loc = locationManager.getLastKnownLocation(provider);
            double longitude = loc.getLongitude();
            double latitude = loc.getLatitude();
                longitude *= 1000;
                longitude = Math.round(longitude);
                longitude /= 1000;

                latitude *= 1000;
                latitude = Math.round(latitude);
                latitude /= 1000;

                location = getString(R.string.coordinates) +" "+longitude+", "+latitude;
        }}
        return location;
    }


}
