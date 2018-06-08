package com.spdb.spdb_app;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.spdb.spdb_app.adapters.FormAdapter;
import com.spdb.spdb_app.adapters.PlacesListAdapter;
import com.spdb.spdb_app.form.OnValueChangedListener;
import com.spdb.spdb_app.helpers.FormValidator;
import com.spdb.spdb_app.helpers.PlacesSelectionHelper;
import com.spdb.spdb_app.models.ApiClient;
import com.spdb.spdb_app.models.ApiInterface;
import com.spdb.spdb_app.models.Availability;
import com.spdb.spdb_app.models.ElementModel;
import com.spdb.spdb_app.models.PlaceDetailsModel;
import com.spdb.spdb_app.models.PlaceModel;
import com.spdb.spdb_app.models.ResultsModels;
import com.spdb.spdb_app.models.RowsModel;
import com.spdb.spdb_app.models.TravelTimeModel;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormActivity extends MyBaseActivity implements OnValueChangedListener {
    //Constants
    public static final int LOCATION_PERMISSIONS_REQUEST = 123;
    //Widgets
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    Button searchPlacesButton;
    private PlaceAutocompleteFragment autocompleteFragment;

    //Class fields
    //Place startPlace;
    Context mainContext;
    FormAdapter formAdapter;

    PlacesSelectionHelper placesSelectionHelper;
    PlacesSelectionHelper.OnReceivedPlaceCallback onReceivedPlaceCallback = new PlacesSelectionHelper.OnReceivedPlaceCallback() {
        @Override
        public void onPlaceReceived(CharSequence receivedPlaceName, Place place) {
            autocompleteFragment.setText(receivedPlaceName);
            if (place != null) {
                currentLocationString = place.getLatLng().latitude + "," + place.getLatLng().longitude;
                locationSetup = true;
            } else {
                currentLocationString = "";
                locationSetup = false;
            }

        }

        @Override
        public void onPlaceFailure(Exception e) {
            autocompleteFragment.setText("");
            e.printStackTrace();
        }
    };

    //ApiClient instance
    private ApiInterface apiInterface;
    private String API_KEY;

    //Results sets
    private ResultsModels resultsModels;
    private ArrayList<PlaceModel> allPlacesFound;
    private List<PlaceModel> allFilteredPlaces;
    private RowsModel[] rowsModels;
    private ArrayList<String> travelLength;

    //Algorithm fields
    private String currentLocationString;
    private String travelDay;
    private float rating;
    long distance;
    long travelTimeSecs;
    private String placeType;
    private String travelMode;


    //Helpful variables
    private boolean isMorePagesAvailable;
    private PlacesListAdapter placesListAdapter;
    private long visitMaxLength;
    private long arrivalTimeSec;
    private boolean locationSetup = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        mainContext = this;

        //ImageView
        ImageView myLocationImageView = findViewById(R.id.myLocationImageView);

        //AutocompleteFragment settings
        placesSelectionHelper = new PlacesSelectionHelper(this, this);
        placesSelectionHelper.askForPermissions();

        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.autoCompletePlace);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                String coordinates = place.getLatLng().toString();
                currentLocationString = place.getLatLng().latitude + "," + place.getLatLng().longitude;
                locationSetup = true;
                Log.e("COORDINATES", "onPlaceSelected: " + coordinates);
            }

            @Override
            public void onError(Status status) {

            }
        });

        autocompleteFragment.getView().findViewById(R.id.place_autocomplete_clear_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        currentLocationString = "";
                        autocompleteFragment.setText("");
                        view.setVisibility(View.GONE);
                    }
                });


        //Search Button settings
        searchPlacesButton = findViewById(R.id.searchPlacesB);

        //RecyclerView settings
        recyclerView = findViewById(R.id.list_parent);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        String[] titles = getResources().getStringArray(R.array.form_items);
        formAdapter = new FormAdapter(this, titles);
        formAdapter.setOnValueChangedListener(this);

        recyclerView.setAdapter(formAdapter);

        initiateVariables();
    }

    @Override
    public void onValueChanged(int whichValueChanged, String newValue) {
        Log.e("onClick: ", "Value " + whichValueChanged + " changed to: " + newValue);

        switch (whichValueChanged) {
            case 0:
                travelMode = newValue;
                //travel_mode
                break;
            case 1:
                //travel_date
                travelDay = newValue;
                break;
            case 2:
                //travel_time
                travelTimeSecs = Long.parseLong(newValue);
                break;
            case 3:
                //stay_lenght
                visitMaxLength = Long.parseLong(newValue);
                break;
            case 4:
                //category
                placeType = newValue;
                break;
            case 5:
                //distance
                distance = Long.parseLong(newValue);
                break;
            case 6:
                //rating
                rating = Float.parseFloat(newValue);
                break;
            case 7:
                //arrival time [s]
                arrivalTimeSec = Long.parseLong(newValue);
                break;
        }
    }

    public void getCurrentLocation(View view) {
        placesSelectionHelper.getCurrentLocation(onReceivedPlaceCallback);
    }

    private void initiateVariables() {
        isMorePagesAvailable = false;
        currentLocationString = "";
        locationSetup = false;
        distance = 2000; //1km
        placeType = "bar";
        travelTimeSecs = 3600; // 1h
        visitMaxLength = 600; // 10min
        travelMode = "driving";
        rating = 0;
        API_KEY = getString(R.string.google_maps_key);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        DateFormatSymbols symbols = new DateFormatSymbols(new Locale("pl"));
        travelDay = symbols.getWeekdays()[Calendar.getInstance().get(Calendar.DAY_OF_WEEK)];

        int h = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int m = Calendar.getInstance().get(Calendar.MINUTE);
        arrivalTimeSec = h * 3600 + m * 60;
    }

    public void searchPlaces(View view) {
        FormValidator validator = new FormValidator(this);

        if (!currentLocationString.equals("") && locationSetup) {
            //Search places with specified placeType and distance from selected location - currentLocationString
            Call<ResultsModels> placesRequest = apiInterface.getPlaces(currentLocationString, distance, placeType, rating, API_KEY);
            placesRequest.enqueue(new Callback<ResultsModels>() {
                @Override
                public void onResponse(@NonNull Call<ResultsModels> call, @NonNull Response<ResultsModels> response) {
                    if (response.body() != null) {
                        //List of places found by radius and place_type
                        resultsModels = response.body();
                        assert resultsModels != null;

                        //Filtering by rating
                        allPlacesFound = new ArrayList<>();
                        for (PlaceModel placeModel : resultsModels.getPlaceModels()) {
                            if (placeModel.getRating() > rating) {
                                allPlacesFound.add(placeModel);
                                Log.e("Filtered 1", placeModel.getPlaceName());
                            }
                        }

                        isMorePagesAvailable = resultsModels.getNextPageToken() != null && !resultsModels.getNextPageToken().equals("");
                        Log.e("IS_MORE_PAGES: ", String.valueOf(isMorePagesAvailable));

                        if (isMorePagesAvailable)
                            getMorePlaces(rating, resultsModels.getNextPageToken(), API_KEY);

                        //Filter places by travel time and distance
                        if (allPlacesFound.size() > 0) {
                            filterPlacesByTravelTimeAndDistance(apiInterface, travelTimeSecs, distance, allPlacesFound);
                        } else {
                            //Clear all data
                            autocompleteFragment.setText("");
                            formAdapter.notifyDataSetChanged();
                            initiateVariables();
                            showToastOnUI();
                        }

                    }

                }

                @Override
                public void onFailure(@NonNull Call<ResultsModels> call, @NonNull Throwable t) {
                    t.printStackTrace();
                }
            });

        } else validator.showAlert();

    }

    private void getMorePlaces(final float rating, String nextPageToken, final String API_KEY) {
        Call<ResultsModels> morePlacesRequest = apiInterface.getMorePlaces(nextPageToken, API_KEY);
        morePlacesRequest.enqueue(new Callback<ResultsModels>() {
            @Override
            public void onResponse(@NonNull Call<ResultsModels> call, @NonNull Response<ResultsModels> response) {
                resultsModels = response.body();
                if (resultsModels != null) {
                    isMorePagesAvailable = resultsModels.getNextPageToken() != null && !resultsModels.getNextPageToken().equals("");
                    Log.e("IS_MORE_PAGES: ", String.valueOf(isMorePagesAvailable));
                    for (PlaceModel placeModel : resultsModels.getPlaceModels()) {
                        if (placeModel.getRating() > rating) {
                            allPlacesFound.add(placeModel);
                            Log.e("Filtered more 1", placeModel.getPlaceName());
                        }
                    }
                    if (isMorePagesAvailable)
                        getMorePlaces(rating, resultsModels.getNextPageToken(), API_KEY);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResultsModels> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void filterPlacesByTravelTimeAndDistance(ApiInterface apiInterface,
                                                     final long selectedTime,
                                                     final long selectedDistance,
                                                     final ArrayList<PlaceModel> placesToFilter) {

        //Build destinations
        final StringBuilder builder = new StringBuilder();
        if (placesToFilter != null)
            if (placesToFilter.size() != 0) {
                for (int i = 0; i < placesToFilter.size(); i++) {
                    if (i < (placesToFilter.size() - 1)) {
                        builder.append("place_id:").append(placesToFilter.get(i).getPlaceId()).append("|");
                    } else builder.append("place_id:").append(placesToFilter.get(i).getPlaceId());
                }
                String destinations = builder.toString();

                //Travel times and distances request
                Call<TravelTimeModel> distancesRequest = apiInterface.getTravelTimeDatas("pl", currentLocationString, destinations, travelMode, API_KEY);
                distancesRequest.enqueue(new Callback<TravelTimeModel>() {
                    @Override
                    public void onResponse(@NonNull Call<TravelTimeModel> call, @NonNull Response<TravelTimeModel> response) {
                        TravelTimeModel travelTimeModel = response.body();

                        ArrayList<ElementModel> filteredElementModels = new ArrayList<>();
                        if (travelTimeModel != null) {
                            //get all rows
                            rowsModels = travelTimeModel.getElements();
                            //initiate filtered places list
                            allFilteredPlaces = new ArrayList<>();

                            if (rowsModels.length > 0) {
                                for (RowsModel rowModel : rowsModels) {
                                    ElementModel[] elementModels = rowModel.getElements();

                                    if (elementModels != null) if (elementModels.length > 0) {
                                        for (int i = 0; i < elementModels.length; i++) {
                                            if (elementModels[i].getDistance().getValueValue() < selectedDistance
                                                    && elementModels[i].getDuration().getValueValue() < selectedTime) {
                                                allFilteredPlaces.add(placesToFilter.get(i));
                                                Log.e("Filtered 2: ", placesToFilter.get(i).getPlaceName());
                                                filteredElementModels.add(elementModels[i]);
                                            }
                                        }
                                    }

                                }
                            }
                        }

                        if (allFilteredPlaces.size() > 0) {
                            filterPlacesByVisitLegthAndArrivalTime(filteredElementModels, allFilteredPlaces);
                        } else {
                            autocompleteFragment.setText("");
                            formAdapter.notifyDataSetChanged();
                            initiateVariables();
                            showToastOnUI();
                        }

                    }

                    @Override
                    public void onFailure(@NonNull Call<TravelTimeModel> call, @NonNull Throwable t) {
                        t.printStackTrace();
                    }
                });

            }

    }

    private void filterPlacesByVisitLegthAndArrivalTime(ArrayList<ElementModel> placesToFilter, List<PlaceModel> places) {
        ArrayList<PlaceModel> filtered = new ArrayList<>();
        Availability availability = new Availability();

        //Set random availability
        switch (placeType) {
            case "subway_station":
                setAvailability(availability, 3, 5, 23, 27, travelDay);
                break;
            case "airport":
                setAvailability(availability, 1, 2, 22, 24, travelDay);
                break;
            case "night_club":
                setAvailability(availability, 18, 20, 23, 27, travelDay);
                break;
            case "taxi_stand":
                setAvailability(availability, 1, 2, 22, 24, travelDay);
                break;
            default:
                setAvailability(availability, 8, 10, 17, 19, travelDay);
                break;
        }


        long availabilityStart = availability.getFromHour() * 3600;
        long availabilityEnd = availability.getToHour() * 3600;

        int i = 0;
        ArrayList<String> visitTimeValues = new ArrayList<>();
        travelLength = new ArrayList<>();
        for (ElementModel model : placesToFilter) {
            long departureTime = arrivalTimeSec + model.getDuration().getValueValue();
            long timeVisitEnd = departureTime + visitMaxLength;

            if (availabilityStart <= departureTime) {
                if (departureTime < availabilityEnd) {
                    if (timeVisitEnd <= availabilityEnd) {
                        filtered.add(places.get(i));
                        Log.e("Filtered 3: ", places.get(i).getPlaceName());
                        travelLength.add(placesToFilter.get(i).getDuration().getTextValue());
                        visitTimeValues.add(getTimeValue(visitMaxLength));
                    } else {
                        filtered.add(places.get(i));
                        Log.e("Filtered 3a: ", places.get(i).getPlaceName());
                        travelLength.add(placesToFilter.get(i).getDuration().getTextValue());
                        visitTimeValues.add(getTimeValue(availabilityEnd - departureTime));
                    }
                }

            } else {
                filtered.add(places.get(i));
                long beforeOpenMins = Math.round((availabilityStart - departureTime) / 60);
                String text;
                if (beforeOpenMins > 60) {
                    double j = Math.floor(beforeOpenMins / 60);
                    text = placesToFilter.get(i).getDuration().getTextValue() + "\n" + j + "h " + (beforeOpenMins - 60 * j) + "min przed otwarciem.";
                } else {
                    text = placesToFilter.get(i).getDuration().getTextValue() + "\n" + beforeOpenMins + "min przed otwarciem.";
                }
                travelLength.add(text);
                visitTimeValues.add(getTimeValue(visitMaxLength));
            }

            i++;
        }

        // Show results
        if (filtered.size() > 0) {
            showFilteredPlacesListed(mainContext, filtered, travelLength, visitTimeValues);
        } else {
            autocompleteFragment.setText("");
            formAdapter.notifyDataSetChanged();
            initiateVariables();
            showToastOnUI();
        }
    }

    private String getTimeValue(long seconds) {
        StringBuilder builder = new StringBuilder();
        int mins = (int) Math.floor(seconds / 60);
        if (mins > 60) {
            int h = (int) Math.floor(mins / 60);
            int m = mins - (h * 60);
            builder.append(h).append("h").append(m).append("min");
        } else {
            builder.append(mins).append("min");
        }

        return builder.toString();
    }

    private void setAvailability(Availability availability, int fromFrom, int toFrom, int fromTo, int toTo, String day) {
        Random random = new Random();
        switch (day) {
            case "sobota":
                fromFrom += 1;
                toTo -= 1;

                break;
            case "niedziela":
                fromFrom += 1;
                toTo -= 1;
                break;
            default:
                break;
        }
        availability.setToHour(random.nextInt(toTo) + fromTo);
        availability.setFromHour(random.nextInt(toFrom) + fromFrom);
    }

    private void showFilteredPlacesListed(Context c,
                                          final List<PlaceModel> filteredPlaces,
                                          ArrayList<String> travelLenght,
                                          ArrayList<String> maxVisitValues) {
        ListView listView;
        Button okButton;
        placesListAdapter = new PlacesListAdapter(c, filteredPlaces, travelLenght, maxVisitValues);


        final Dialog dialog = new Dialog(c);
        dialog.setContentView(R.layout.activity_places_list);

        okButton = dialog.findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                autocompleteFragment.setText("");
                formAdapter.notifyDataSetChanged();
                initiateVariables();
            }
        });

        listView = dialog.findViewById(R.id.placesListView);

        listView.setAdapter(placesListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlaceModel model = filteredPlaces.get(position);
                showOptionsDialog(model.getPlaceId());
                Log.e("onItemClick: ", "CLICKED");
            }
        });

        dialog.create();
        dialog.show();
    }

    private void showOptionsDialog(final String placeId) {
        final Dialog dialog = new Dialog(mainContext);
        dialog.setContentView(R.layout.dialog_question);
        Button showDetails = dialog.findViewById(R.id.showDetailsBtn);
        Button showAtMap = dialog.findViewById(R.id.checkAtMapBtn);
        Button back = dialog.findViewById(R.id.backBtn);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        showDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSinglePlaceDetails(placeId);
            }
        });

        showAtMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMapsActivity(placeId);
            }
        });

        dialog.create();
        dialog.show();
    }

    private void startMapsActivity(final String placeId) {
        Call<PlaceDetailsModel> placeDetailsModelCall = apiInterface.getPlaceDetails(placeId, API_KEY);

        placeDetailsModelCall.enqueue(new Callback<PlaceDetailsModel>() {
            @Override
            public void onResponse(Call<PlaceDetailsModel> call, Response<PlaceDetailsModel> response) {
                PlaceDetailsModel model = response.body();
                if (model != null) {
                    Intent intent = new Intent(mainContext, MapsActivity.class);

                    String latLng = model.getResult().getGeometry().getLocationModel().getLat()
                            + "," + model.getResult().getGeometry().getLocationModel().getLng();

                    intent.putExtra("mode", travelMode);
                    intent.putExtra("destination", latLng);
                    intent.putExtra("address", model.getResult().getAddress());
                    intent.putExtra("origin", currentLocationString);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<PlaceDetailsModel> call, Throwable t) {

            }
        });
    }

    private void showSinglePlaceDetails(String placeId) {
        Call<PlaceDetailsModel> placeDetailsRequest = apiInterface.getPlaceDetails(placeId, API_KEY);

        placeDetailsRequest.enqueue(new Callback<PlaceDetailsModel>() {
            @Override
            public void onResponse(@NonNull Call<PlaceDetailsModel> call, @NonNull Response<PlaceDetailsModel> response) {
                PlaceDetailsModel model = response.body();
                if (model != null) {
                    final Dialog d = new Dialog(mainContext);
                    d.setContentView(R.layout.dialog_place_details);

                    TextView name = d.findViewById(R.id.placeName);
                    if (model.getResult() != null) {
                        if (model.getResult().getPlaceName() != null || !model.getResult().getPlaceName().equals("")) {
                            name.setText(model.getResult().getPlaceName());
                        } else name.setText("Brak");

                        TextView lngLat = d.findViewById(R.id.lngLat);

                        if (model.getResult().getGeometry().getLocationModel() != null) {
                            String lnglat = "Szerokość geograficzna: " + model.getResult().getGeometry().getLocationModel().getLat()
                                    + "\n" + "Długość geograficzna: " + model.getResult().getGeometry().getLocationModel().getLng();
                            lngLat.setText(lnglat);
                        } else lngLat.setText("Wpółrzędne: Brak");


                        TextView address = d.findViewById(R.id.address);

                        if (model.getResult().getAddress() != null || !model.getResult().getAddress().equals("")) {
                            String adress = "Adres: " + model.getResult().getAddress();
                            address.setText(adress);
                        } else address.setText("Adres: Brak");


                        TextView rating = d.findViewById(R.id.rating);
                        String r = "Rating: " + model.getResult().getRating();
                        rating.setText(r);

                        TextView phone = d.findViewById(R.id.phoneNo);

                        if (model.getResult().getPhoneNumber() != null && !model.getResult().getPhoneNumber().equals("")) {
                            String p = "Tel. " + model.getResult().getPhoneNumber();
                            phone.setText(p);
                        } else phone.setText("Telefon: Brak");

                    }

                    Button okBtn = d.findViewById(R.id.okBtn);
                    okBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            d.cancel();
                        }
                    });
                    d.create();
                    d.show();

                }
            }

            @Override
            public void onFailure(@NonNull Call<PlaceDetailsModel> call, @NonNull Throwable t) {

            }
        });
    }

    private void showToastOnUI() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "No results found.", Toast.LENGTH_LONG).show();
            }
        });
    }
}
