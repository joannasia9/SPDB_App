package com.spdb.spdb_app.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.spdb.spdb_app.R;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;

public class TravelTimeViewHolder extends RecyclerView.ViewHolder{
    public SeekBar hours;
    public SeekBar minutes;
    public SeekBar arrivalSeekBar;
    private TextView arrivalTime;
    public Button saveTravelTimeButton;
    public Button expButton;
    public ExpandableLinearLayout layout;
    private int hoursInt, minutesInt;
    private TextView h, min;
    private long arrivalTimeSec;

    public TravelTimeViewHolder(View itemView) {
        super(itemView);
        hours = itemView.findViewById(R.id.hSeekBar);
        minutes = itemView.findViewById(R.id.minsSeekBar);
        saveTravelTimeButton = itemView.findViewById(R.id.saveTravelTimeButton);
        expButton = itemView.findViewById(R.id.depTimeB);
        layout = itemView.findViewById(R.id.expandableTLength);
        h = itemView.findViewById(R.id.textView3);
        min = itemView.findViewById(R.id.textView4);
        arrivalSeekBar = itemView.findViewById(R.id.arrivalTimeSeekbar);
        arrivalTime = itemView.findViewById(R.id.textView6);

        hours.setMax(12);
        hours.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (progress){
                    case 12:
                        minutes.setMax(0);
                        hoursInt = progress;
                        break;
                    default:
                        minutes.setMax(59);
                        hoursInt = progress;
                        break;
                }
                h.setText("Godziny: "+ hoursInt);
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

        Calendar calendar = Calendar.getInstance();
        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int m = calendar.get(Calendar.MINUTE);
        arrivalTime.setText("Czas wyjazdu: " + h + ":" + m);
        arrivalTimeSec = h*3600+m*60;

        int progress = h*4+m/15;
        arrivalSeekBar.setProgress(progress);

        arrivalSeekBar.setMax(96);
        arrivalSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                arrivalTimeSec = progress*15*60; //milis from 00:00:00
                int h = (int) Math.floor(progress/4);
                int m = (progress - (h*4))*15;
                String min = "";
                if(m==0) min="00"; else min = String.valueOf(m);
                arrivalTime.setText("Czas wyjazdu: " + h + ":" + min);

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

    public String getArrivalTimeSec(){
        return String.valueOf(arrivalTimeSec);
    }

}
