package com.spdb.spdb_app.viewholders;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.spdb.spdb_app.R;

public class StayTimeViewHolder extends RecyclerView.ViewHolder{
        public SeekBar hours;
        public SeekBar minutes;
        public Button saveTravelTimeButton;
        public Button expButton;
        public ExpandableLinearLayout layout;

        private int hoursInt, minutesInt;
        private TextView h, min;

        public StayTimeViewHolder(View itemView) {
            super(itemView);
            hours = itemView.findViewById(R.id.hoursSeekBar);
            minutes = itemView.findViewById(R.id.minutesSeekBar);
            saveTravelTimeButton = itemView.findViewById(R.id.saveStayinTimeButton);
            expButton = itemView.findViewById(R.id.stayTimeB);
            layout = itemView.findViewById(R.id.expandableTStay);

            h = itemView.findViewById(R.id.textView3);
            min = itemView.findViewById(R.id.textView4);

            hours.setMax(12);
            hours.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    switch (progress){
                        case 12:
                            minutes.setMax(0);
                            hoursInt = progress;
                            h.setText("Godziny: "+ hoursInt);
                            break;
                        default:
                            minutes.setMax(59);
                            hoursInt = progress;
                            h.setText("Godziny: "+ hoursInt);
                            break;
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            minutes.setMax(59);
            minutes.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    minutesInt = progress;
                    min.setText("Minuty: "+minutesInt);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }

    public String getChangedTravelTime(){
        long travelTimeSecs = hoursInt*3600+minutesInt*60;
        return String.valueOf(travelTimeSecs);
    }

    }

