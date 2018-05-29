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
    private ArrayList<String> travelLenght;

 public PlacesListAdapter(Context c, ArrayList<PlaceModel> places, String maxLenghtValue, ArrayList<String> travelLenght){
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
        String l = context.getString(R.string.v_len)+" "+ maxVisitValue;
        singlePlaceViewHolder.visitLengthValue.setText(l);

        String at;
        if(travelLenght.get(position)!=null){
             at= context.getString(R.string.arr_time) + " " + travelLenght.get(position);
        } else {
            at = context.getString(R.string.no_spec);
        }
        singlePlaceViewHolder.arrivalTimeValue.setText(at);
        singlePlaceViewHolder.rating.setMax(5);
        singlePlaceViewHolder.rating.setRating(model.getRating());

        return placeItem;
    }


}
