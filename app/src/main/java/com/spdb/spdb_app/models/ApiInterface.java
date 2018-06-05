package com.spdb.spdb_app.models;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("place/nearbysearch/json?")
    Call<ResultsModels> getPlaces(@Query("location") String location, @Query("radius") long radius, @Query("type") String type, @Query("rating") float rating, @Query("key") String apikey);

    @GET("place/nearbysearch/json?")
    Call<ResultsModels> getMorePlaces( @Query("pagetoken") String nextPageToken, @Query("key") String apikey);


    //Distances
    @GET("distancematrix/json?")
    Call<TravelTimeModel> getTravelTimeDatas(@Query("language") String language, @Query("origins") String origins, @Query("destinations") String destinations, @Query("mode") String travelMode, @Query("key") String apikey);

    @GET("place/details/json?")
    Call<PlaceDetailsModel> getPlaceDetails(@Query("placeid") String placeId, @Query("key") String apikey);

    @GET("directions/json?")
    Call<RouteModel> getRoute(@Query("origin") String origin, @Query("destination") String destination,@Query("mode") String mode, @Query("key") String apiKey);

}