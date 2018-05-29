package com.spdb.spdb_app.models;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("place/nearbysearch/json?")
    Call<ResultsModels> getSomePlacesWithAll(@Query("location") String location, @Query("radius") int radius, @Query("type") String type, @Query("keyword") String keyword, @Query("key") String apikey);

    @GET("place/nearbysearch/json?")
    Call<ResultsModels> getSomePlacesWithNoType(@Query("location") String location, @Query("radius") int radius, @Query("keyword") String keyword, @Query("key") String apikey);

    @GET("place/nearbysearch/json?")
    Call<ResultsModels> getSomePlacesWithNoKeyword(@Query("location") String location, @Query("radius") long radius, @Query("type") String type, @Query("key") String apikey);

    @GET("place/nearbysearch/json?")
    Call<ResultsModels> getSomePlacesNoTypeNoKeyword(@Query("location") String location, @Query("radius") int radius, @Query("key") String apikey);

    //distances
    @GET("distancematrix/json?")
    Call<TravelTimeModel> getTravelTimeDatas(@Query("language") String language, @Query("origins") String origins, @Query("destinations") String destinations, @Query("key") String apikey);

//    @GET("/nearbysearch/json?location={location_latitude},{location_latitude}&radius={radius}&key={apikey}")
//    Call<PlaceModel[]> getSomePlaces(@Path("location_latitude") String latitude, @Path("location_longitude") String longitude, @Path("radius") String radius, @Path("apikey") String apikey);
//
//    @GET("/nearbysearch/json?location={location_latitude},{location_latitude}&radius={radius}&keyword={keyword}&key={apikey}")
//    Call<PlaceModel[]> getSomePlaces(@Path("location_latitude") String latitude, @Path("location_longitude") String longitude, @Path("radius") String radius,@Path("keyword") String keyword, @Path("apikey") String apikey);
//
//    @GET("/nearbysearch/json?location={location_latitude},{location_latitude}&radius={radius}&type={type}&key={apikey}")
//    Call<PlaceModel[]> getSPlaces(@Path("location_latitude") String latitude, @Path("location_longitude") String longitude, @Path("radius") String radius, @Path("type") String type, @Path("apikey") String apikey);
}