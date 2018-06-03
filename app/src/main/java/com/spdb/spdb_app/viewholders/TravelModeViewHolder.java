package com.spdb.spdb_app.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.spdb.spdb_app.R;

public class TravelModeViewHolder extends RecyclerView.ViewHolder{
    public Spinner travelModeSpinner;
    public Button saveButton;
    public Button expButton;
    public ExpandableLinearLayout layout;
    private String[] allTravelModes;

    private String transportWayPosition;

    public TravelModeViewHolder(View view, Context context){
        super(view);
        travelModeSpinner = view.findViewById(R.id.travelModeSpinner);
        saveButton = view.findViewById(R.id.saveTransportModeButton);
        expButton = itemView.findViewById(R.id.travModeB);
        layout = itemView.findViewById(R.id.expandableTMode);

        allTravelModes = context.getResources().getStringArray(R.array.mode);

        final ArrayAdapter<CharSequence> transportAdapter = ArrayAdapter.createFromResource(context,
                R.array.transport, android.R.layout.simple_spinner_item);
        transportAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        travelModeSpinner.setAdapter(transportAdapter);
        travelModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setTransportWayPosition(allTravelModes[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                setTransportWayPosition("0");
            }
        });
    }

    public String getTransportWayPosition() {
        return transportWayPosition;
    }

    public void setTransportWayPosition(String transportWayPosition) {
        this.transportWayPosition = transportWayPosition;
    }
}
