package com.spdb.spdb_app;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.spdb.spdb_app.models.Category;

import io.realm.Realm;


public class MainActivity extends MyBaseActivity {
    PlaceAutocompleteFragment autocompleteFragment;
    AutoCompleteTextView category;
    EditText hoursValue, minsValue;
    Spinner transport;
    TextView hours, minutes;
    SeekBar seekBar, radiusSeekBar;
    TextView radiusValue;
    CheckBox checkBox;
    public static final int LOCATION_PERMISSIONS_REQUEST = 111;
    Activity activity;
    Realm realm;
    PlacesSelectionHelper placesSelectionHelper;
    FormValidator validator = new FormValidator(this);
    PlacesSelectionHelper.OnReceivedPlaceCallback onReceivedPlaceCallback = new PlacesSelectionHelper.OnReceivedPlaceCallback() {
        @Override
        public void onPlaceReceived(CharSequence receivedPlaceName) {
            autocompleteFragment.setText(receivedPlaceName);

        }

        @Override
        public void onPlaceFailure(Exception e) {
            autocompleteFragment.setText("");
            e.printStackTrace();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        placesSelectionHelper = new PlacesSelectionHelper(this, this);
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

        transport = findViewById(R.id.spinner);

        hours = findViewById(R.id.hours);
        minutes = findViewById(R.id.mins);
        seekBar = findViewById(R.id.seekBar);
        category = findViewById(R.id.category);


        hoursValue = findViewById(R.id.hoursValue);
        minsValue = findViewById(R.id.minsValue);
        validator.setLimit(hoursValue,23);
        validator.setLimit(minsValue,59);

        checkBox = findViewById(R.id.checkBox);
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


        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                String coordinates = place.getLatLng().toString();
                Log.e("COORDINATES", "onPlaceSelected: " + coordinates );
            }

            @Override
            public void onError(Status status) {

            }
        });
    }

    public void searchPlaces(View view) {
    }

    public void getCurrentLocation(View view) {
       placesSelectionHelper.getCurrentLocation(onReceivedPlaceCallback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSIONS_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    placesSelectionHelper.getCurrentLocation(onReceivedPlaceCallback);
                }
                break;
            }

        }
    }




}
