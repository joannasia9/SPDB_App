package com.spdb.spdb_app.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import com.spdb.spdb_app.R;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;


public class TravelDistanceViewHolder extends RecyclerView.ViewHolder{
    public SeekBar km;
    public SeekBar m;
    public Button saveDistanceButton;
    public Button expButton;
    public ExpandableLinearLayout layout;
    private TextView kmetres, met;
    private int kilometresInt, metresInt;


    public TravelDistanceViewHolder(View itemView) {
        super(itemView);
        km = itemView.findViewById(R.id.kmSeekBar);
        m= itemView.findViewById(R.id.mSeekBar);
        saveDistanceButton = itemView.findViewById(R.id.saveTravelDistButton);
        kmetres = itemView.findViewById(R.id.textView3);
        met = itemView.findViewById(R.id.textView4);
        expButton = itemView.findViewById(R.id.distItemB);
        layout = itemView.findViewById(R.id.expandableDistance);

        km.setMax(50);
        km.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (progress){
                    case 50:
                        m.setMax(0);
                        kilometresInt = progress;
                        break;
                    default:
                        m.setMax(999);
                        kilometresInt = progress;
                        break;
                }

                kmetres.setText("Kilometry: "+kilometresInt);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        m.setMax(999);
        m.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               metresInt= progress;
               met.setText("Metry: "+ metresInt);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public String getChangedDistance(){
        long travelTimeSecs = kilometresInt*1000+metresInt;
        return String.valueOf(travelTimeSecs);
    }

}
