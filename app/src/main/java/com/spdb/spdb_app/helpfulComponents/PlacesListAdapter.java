package com.spdb.spdb_app.helpfulComponents;

import android.content.Context;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.spdb.spdb_app.R;
import com.spdb.spdb_app.models.PlaceModel;

import java.util.ArrayList;

public class PlacesListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<PlaceModel> filteredPlaces;
    private  String maxVisitValue;
    private String[] travelLenght;

 public PlacesListAdapter(Context c, ArrayList<PlaceModel> places, String maxLenghtValue, String[] travelLenght){
    this.context = c;
    this.filteredPlaces = places;
    this.maxVisitValue= maxLenghtValue;
    this.travelLenght = travelLenght;
 }

    @Override
    public int getCount() {
        return filteredPlaces.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View placeItem = convertView;
        SinglePlaceViewHolder singlePlaceViewHolder;

        if (placeItem == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            placeItem = inflater.inflate(R.layout.single_place_item,parent,false);
            singlePlaceViewHolder = new SinglePlaceViewHolder(placeItem);
            placeItem.setTag(singlePlaceViewHolder);
        } else {
            singlePlaceViewHolder = (SinglePlaceViewHolder) placeItem.getTag();
        }

        PlaceModel model = filteredPlaces.get(position);

        singlePlaceViewHolder.placeName.setText(model.getPlaceName());
        singlePlaceViewHolder.visitLengthValue.setText("Czas trwania wizyty: "+maxVisitValue);

        singlePlaceViewHolder.arrivalTimeValue.setText("Dotrzesz za około: " + travelLenght[position]);
        //singlePlaceViewHolder.arrivalTimeValue.setText("Nie określono dokładnego czasu dotarcia");
        singlePlaceViewHolder.rating.setMax(5);
        singlePlaceViewHolder.rating.setRating(model.getRating());

        return placeItem;
    }


}
