package com.spdb.spdb_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.spdb.spdb_app.R;
import com.spdb.spdb_app.models.PlaceModel;
import com.spdb.spdb_app.viewholders.SinglePlaceViewHolder;

import java.util.ArrayList;
import java.util.List;

public class PlacesListAdapter extends BaseAdapter {
    private Context context;
    private List<PlaceModel> filteredPlaces;
    private ArrayList<String> travelLenght, maxVisitValues;

    public PlacesListAdapter(Context c, List<PlaceModel> places, ArrayList<String> travelLength, ArrayList<String> maxVisitValues){
    this.context = c;
    this.filteredPlaces = places;
    this.travelLenght = travelLength;
    this.maxVisitValues = maxVisitValues;
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

        final PlaceModel model = filteredPlaces.get(position);

        singlePlaceViewHolder.placeName.setText(model.getPlaceName());
        String l = context.getString(R.string.v_len)+" "+ maxVisitValues.get(position);
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

    public void updateAdapter(List<PlaceModel> places, ArrayList<String> travelLength, ArrayList<String> maxVisitValues){
        this.filteredPlaces = places;
        this.travelLenght = travelLength;
        this.maxVisitValues = maxVisitValues;
        this.notifyDataSetChanged();
    }


}
