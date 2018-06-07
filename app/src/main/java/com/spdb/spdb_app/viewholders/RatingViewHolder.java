package com.spdb.spdb_app.viewholders;

import android.graphics.Matrix;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.spdb.spdb_app.R;

public class RatingViewHolder extends RecyclerView.ViewHolder{
    public RatingBar ratingBar;
    public Button saveButton;
    public Button expButton;
    public ExpandableLinearLayout layout;
    private float ratingValue;

    public RatingViewHolder(View view){
        super(view);
        ratingBar = view.findViewById(R.id.ratingSelection);
        saveButton = view.findViewById(R.id.saveRatingButton);

        expButton = itemView.findViewById(R.id.ratItemB);
        layout = itemView.findViewById(R.id.expandableRating);

        ratingBar.setMax(5);
        ratingBar.setStepSize(0.2f);
        ratingBar.setRating(0.0f);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingValue = rating;
            }
        });

    }

    public String getRatingValue(){
        return String.valueOf(ratingValue);
    }
}
