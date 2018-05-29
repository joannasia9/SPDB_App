package com.spdb.spdb_app.helpfulComponents;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.spdb.spdb_app.R;

public class SinglePlaceViewHolder {
    public TextView placeName, arrivalTimeValue, visitLengthValue;
    public RatingBar rating;


    public SinglePlaceViewHolder(View v){
       placeName = v.findViewById(R.id.placeName);
       arrivalTimeValue = v.findViewById(R.id.arrivalTimeValue);
       visitLengthValue = v.findViewById(R.id.visitLengthValue);
       rating = v.findViewById(R.id.rating);
    }
}
