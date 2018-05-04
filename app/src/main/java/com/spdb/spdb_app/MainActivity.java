package com.spdb.spdb_app;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.spdb.spdb_app.helpfulComponents.DataInterpreter;
import com.spdb.spdb_app.helpfulComponents.FormValidator;
import com.spdb.spdb_app.helpfulComponents.PlacesSelectionHelper;

public class MainActivity extends MyBaseActivity {
    PlaceAutocompleteFragment autocompleteFragment;
    AutoCompleteTextView category;
    EditText hoursValue, minsValue;
    Spinner transport;
    TextView hours, minutes;
    SeekBar seekBar, radiusSeekBar;
    TextView radiusValue;
    CheckBox checkBox;

    Place startPlace = null;

    FormValidator validator = new FormValidator(this);
    PlacesSelectionHelper placesSelectionHelper;
    PlacesSelectionHelper.OnReceivedPlaceCallback onReceivedPlaceCallback = new PlacesSelectionHelper.OnReceivedPlaceCallback() {
        @Override
        public void onPlaceReceived(CharSequence receivedPlaceName, Place place) {
            autocompleteFragment.setText(receivedPlaceName);
            startPlace = place;

        }

        @Override
        public void onPlaceFailure(Exception e) {
            autocompleteFragment.setText("");
            e.printStackTrace();
        }
    };
    public static final int LOCATION_PERMISSIONS_REQUEST = 111;

    String[] categories_pl;
    String[] categories_eng;
    //items necessary to search places:
    int transportWayPosition = 0;
    int travelTimeMins = 0;
    int selectedCatPosition;
    String selectedCategory;
    int visitLenghtMins=0;
    int radius = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        placesSelectionHelper = new PlacesSelectionHelper(this, this);

        categories_pl = getResources().getStringArray(R.array.categories_pl);
        categories_eng = getResources().getStringArray(R.array.categories_eng);

        transport = findViewById(R.id.spinner);
        final ArrayAdapter<CharSequence> transportAdapter = ArrayAdapter.createFromResource(this,
                R.array.transport, android.R.layout.simple_spinner_item);
        transportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transport.setAdapter(transportAdapter);
        transport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                transportWayPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                transportWayPosition = 0;
            }
        });


        hours = findViewById(R.id.hours);
        minutes = findViewById(R.id.mins);
        seekBar = findViewById(R.id.seekBar);
        category = findViewById(R.id.category);


        hoursValue = findViewById(R.id.hoursValue);
        minsValue = findViewById(R.id.minsValue);
        validator.setLimit(hoursValue, 23);
        validator.setLimit(minsValue, 59);

        checkBox = findViewById(R.id.checkBox);
        radiusSeekBar = findViewById(R.id.radiusSeekBar);
        radiusValue = findViewById(R.id.radiusValue);

        seekBar.setMax(720);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                travelTimeMins = progress;

                if (progress > 60) {
                    int hValue = Math.round(progress / 60);
                    int mValue = progress - (hValue * 60);
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
                radius = progress;

                radiusValue.setText(String.valueOf(progress));

                if (progress != 0) {
                    checkBox.setChecked(false);
                } else checkBox.setChecked(true);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories_pl);
        category.setAdapter(categoriesAdapter);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCatPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                startPlace = place;
                String coordinates = place.getLatLng().toString();
                Log.e("COORDINATES", "onPlaceSelected: " + coordinates);
            }

            @Override
            public void onError(Status status) {

            }
        });
    }

    public void searchPlaces(View view) {
        if (validator.isValidForm(startPlace)) {
            //search specified places using:

            //transportPosition
            //travelTimeMins
            //selectedCategory = categories_eng[selectedCatPosition];
            // visitLenghtMins = getVisitLenghtMinsValue(hoursValue,minsValue);
            //if(!checkBox.isChecked()) { use radius [km] }else //dowolna odl.

            DataInterpreter interpreter = new DataInterpreter(this);
            interpreter.searchSpecifiedPlaces();
        }
    }

    public void getCurrentLocation(View view) {
        placesSelectionHelper.getCurrentLocation(onReceivedPlaceCallback);
    }

    private int getVisitLenghtMinsValue(EditText hoursValue, EditText minsValue){
        String h = hoursValue.getText().toString().trim();
        String m = minsValue.getText().toString().trim();

        int hMins = 0;
        if(!h.equals("")){
            hMins = Integer.parseInt(h);
        }

        int mins = 0;
        if(!m.equals("")){
            mins = Integer.parseInt(m);
        }
        return (hMins+mins);
    }
}
