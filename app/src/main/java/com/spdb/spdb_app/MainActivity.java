package com.spdb.spdb_app;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.spdb.spdb_app.helpfulComponents.FormValidator;
import com.spdb.spdb_app.helpfulComponents.PlacesListAdapter;
import com.spdb.spdb_app.helpfulComponents.PlacesSelectionHelper;
import com.spdb.spdb_app.models.ApiClient;
import com.spdb.spdb_app.models.ApiInterface;
import com.spdb.spdb_app.models.ElementModel;
import com.spdb.spdb_app.models.PlaceModel;
import com.spdb.spdb_app.models.ResultsModels;
import com.spdb.spdb_app.models.RowsModel;
import com.spdb.spdb_app.models.TravelTimeModel;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    long travelTimeSecs = 0;
    int selectedCatPosition;
    String selectedCategory;
    int visitLenghtMins=0;
    long radius = 0;
    private ResultsModels models;
    String placeType = "";
    String locationString="";

    Context mainContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainContext = this;
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
                travelTimeSecs = progress*60;

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

        radiusSeekBar.setMax(50);
        radiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radius = progress*1000;

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

        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                startPlace = place;
                String coordinates = place.getLatLng().toString();
                locationString = place.getLatLng().latitude+","+place.getLatLng().longitude;
                Log.e("COORDINATES", "onPlaceSelected: " + coordinates);
            }

            @Override
            public void onError(Status status) {

            }
        });
    }




    public void searchPlaces(View view) {

        if(radius<500) radius = 500;
        if(category.getText().toString().trim().equals("")) placeType = categories_eng[0];
        else {
            String tmp = category.getText().toString().trim();
            placeType ="";
            for (int i = 0; i<categories_pl.length;i++){
                if(!placeType.equals("")){
                    continue;
                }
                if(categories_pl[i].equals(tmp)) placeType = categories_eng[i];
            }
        }

        Log.e("PLACE_TYPE: ", placeType );

        if (validator.isValidForm(startPlace)) {
            final String API_KEY = getString(R.string.google_maps_key);
            final ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            final Call<ResultsModels> requestPlacesData;
            if(!locationString.equals("") ) {
                requestPlacesData = apiInterface.getSomePlacesWithNoKeyword(locationString,radius,placeType, API_KEY);
                requestPlacesData.enqueue(new Callback<ResultsModels>() {
                    @Override
                    public void onResponse(Call<ResultsModels> call, Response<ResultsModels> response) {
                        models = response.body();
                        if(models!=null){
                            for (PlaceModel model: models.getPlaceModels()) {
                                Log.e("NOT FILTERED PLACES: ", model.getPlaceName());
                            }

                            if(travelTimeSecs>0){
                                filterPlacesByTravelTimeAndDistance(API_KEY,apiInterface,travelTimeSecs,radius,models.getPlaceModels(),locationString);
                            } else {
                                allFilteredPlaces = new ArrayList<>();
                                Collections.addAll(allFilteredPlaces, models.getPlaceModels());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultsModels> call, Throwable t) {
                        t.printStackTrace();
                        Log.e( "onFailure: ", "MAYDAY" );
                    }
                });
            } else validator.showAlert();
        }


}

    RowsModel[] rowsModels;
    ElementModel[] elementModels;
    ArrayList<PlaceModel> allFilteredPlaces;
    String[] travelLenght;
    
private void filterPlacesByTravelTimeAndDistance(String apiKey, ApiInterface apiInterface, final long selectedTime, final long selectedDistance, final PlaceModel[] placesToFilter, String currentLocation){

        final StringBuilder builder = new StringBuilder();
        if(placesToFilter!=null)
            if (placesToFilter.length != 0) {
                for(int i=0; i<placesToFilter.length;i++) {
                    if(i<(placesToFilter.length-1)) {
                        builder.append("place_id:").append(placesToFilter[i].getPlaceId()).append("|");
                    } else  builder.append("place_id:").append(placesToFilter[i].getPlaceId()); }

                String destinations = builder.toString(); // from placesToFilter
                Call<TravelTimeModel> distancesRequest = apiInterface.getTravelTimeDatas(currentLocation,destinations,apiKey);

                distancesRequest.enqueue(new Callback<TravelTimeModel>() {
                    @Override
                    public void onResponse(@NonNull Call<TravelTimeModel> call, @NonNull Response<TravelTimeModel> response) {
                        TravelTimeModel travelTimeModel = response.body();
                        if(travelTimeModel != null){
                            rowsModels = travelTimeModel.getElements();
                            allFilteredPlaces = new ArrayList<>();
                            if(rowsModels.length>0){
                                for (RowsModel rowModel:rowsModels) {
                                    ElementModel[] elementModels = rowModel.getElements();

                                    if (elementModels!=null) if (elementModels.length>0){
                                        travelLenght = new String[elementModels.length];

                                        for(int i=0; i<elementModels.length;i++){
                                            if(elementModels[i].getDistance().getValueValue()<selectedDistance
                                                    && elementModels[i].getDuration().getValueValue()<selectedTime){
                                                allFilteredPlaces.add(placesToFilter[i]);
                                                travelLenght[i]=elementModels[i].getDuration().getTextValue();
                                            }
                                        }
                                    }

                                }
                            }

                         StringBuilder builder1 = new StringBuilder();
                            for (PlaceModel fPlace : allFilteredPlaces) {
                                builder1.append(fPlace.getPlaceName()).append("\n");
                            }

                            Log.e("FILTERED PLACES: ", builder1.toString());
                        }
                        if(allFilteredPlaces.size()!=0&&travelLenght.length!=0){
                            showFilteredPlacesListed(mainContext,allFilteredPlaces,travelLenght);
                        } else {
                            showToastOnUI("No results found.");
                        }
                    }

                    @Override
                    public void onFailure(Call<TravelTimeModel> call, Throwable t) {
                        t.printStackTrace();
                    }
                });

            }

}
    public void getCurrentLocation(View view) {
        placesSelectionHelper.getCurrentLocation(onReceivedPlaceCallback);
    }

    private void showFilteredPlacesListed(Context c,ArrayList<PlaceModel> filteredPlaces, String[] travelLenght){
    ListView listView;
    Button okButton;
    PlacesListAdapter adapter = new PlacesListAdapter(c,filteredPlaces,getVisitLenghtStringValue(hoursValue,minsValue),travelLenght);

        final Dialog dialog = new Dialog(c);
        dialog.setContentView(R.layout.activity_places_list);

        okButton = dialog.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        listView = dialog.findViewById(R.id.placesListView);

        listView.setAdapter(adapter);
        dialog.create();
        dialog.show();
    }

    private void showToastOnUI(final String message){
    this.runOnUiThread(new Runnable() {
        @Override
        public void run() {
            Toast.makeText(getApplicationContext(), message,Toast.LENGTH_LONG).show();
        }
    });
    }

    private String getVisitLenghtStringValue(EditText hoursValue, EditText minsValue) {
        String h = hoursValue.getText().toString().trim();
        String m = minsValue.getText().toString().trim();

        String result = "";
        if(!h.equals("")){
            if(!m.equals("")){
                result = h+"h "+m+"min";
            }else result = h+"h";
        }else{
            if (!m.equals("")) {
                result = "0h "+m+"min";
            } else result = "10min";
        }
        return result;
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
