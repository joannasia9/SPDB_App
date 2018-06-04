package com.spdb.spdb_app.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.spdb.spdb_app.R;
import com.spdb.spdb_app.form.OnValueChangedListener;
import com.spdb.spdb_app.viewholders.CalendarViewHolder;
import com.spdb.spdb_app.viewholders.CategoryViewHolder;
import com.spdb.spdb_app.viewholders.RatingViewHolder;
import com.spdb.spdb_app.viewholders.StayTimeViewHolder;
import com.spdb.spdb_app.viewholders.TravelDistanceViewHolder;
import com.spdb.spdb_app.viewholders.TravelModeViewHolder;
import com.spdb.spdb_app.viewholders.TravelTimeViewHolder;

public class FormAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private String[] titles;
    private Context context;
    private Context mainContext;
    private SparseBooleanArray expandState = new SparseBooleanArray();
    private OnValueChangedListener onValueChangedListener;

    public void setOnValueChangedListener(OnValueChangedListener onValueChangedListener) {
        this.onValueChangedListener = onValueChangedListener;
    }

    public FormAdapter(Context c, String[] items){
        this.titles = items;
        this.mainContext = c;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);

        switch (viewType){
            case 0:
                View  view = inflater.inflate(R.layout.item_travel_mode,parent,false);
                return new TravelModeViewHolder(view, mainContext);
            case 1:
                View view1 = inflater.inflate(R.layout.item_travel_date,parent,false);
                return new CalendarViewHolder(view1);
            case 2:
                View view2 = inflater.inflate(R.layout.item_travel_lenght,parent,false);
                return new TravelTimeViewHolder(view2);
            case 3:
                View view3 = inflater.inflate(R.layout.temp_layout,parent,false);
                return new StayTimeViewHolder(view3);
            case 4:
                View view4 = inflater.inflate(R.layout.item_category_selection,parent,false);
                return new CategoryViewHolder(view4,context);
            case 5:
                View view5 = inflater.inflate(R.layout.item_distance_selection,parent,false);
                return new TravelDistanceViewHolder(view5);
            case 6:
                View view6 = inflater.inflate(R.layout.item_rating_selection,parent,false);
                return new RatingViewHolder(view6);
                default:
                    View view7 = inflater.inflate(R.layout.item_rating_selection,parent,false);
                    return new RatingViewHolder(view7);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        String item = titles[position];

        switch (holder.getItemViewType()){
            case 0:
            {
                final TravelModeViewHolder viewHolder = (TravelModeViewHolder) holder;
                viewHolder.expButton.setText(item);
                viewHolder.setIsRecyclable(false);
                viewHolder.layout.setInRecyclerView(true);
                viewHolder.layout.setExpanded(expandState.get(position));
                viewHolder.layout.setListener(new ExpandableLayoutListenerAdapter() {
                    @Override
                    public void onPreOpen() {
                        expandState.put(position,true);
                    }

                    @Override
                    public void onPreClose() {
                        expandState.put(position,false);
                    }

                });
                viewHolder.expButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewHolder.layout.toggle();
                    }
                });
                viewHolder.saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onValueChangedListener.onValueChanged(0,viewHolder.getTransportWayPosition());
                        viewHolder.layout.toggle();
                    }
                });

            }
                break;
            case 1:
            {
                final CalendarViewHolder calendarViewHolder = (CalendarViewHolder) holder;
                calendarViewHolder.expButton.setText(item);
                calendarViewHolder.setIsRecyclable(false);
                calendarViewHolder.calendarLayout.setInRecyclerView(true);
                calendarViewHolder.calendarLayout.initLayout();
                calendarViewHolder.calendarLayout.setExpanded(expandState.get(position));
                calendarViewHolder.calendarLayout.setListener(new ExpandableLayoutListenerAdapter() {
                    @Override
                    public void onPreOpen() {
                        expandState.put(position,true);
                    }

                    @Override
                    public void onPreClose() {
                        expandState.put(position,false);
                    }

                });

                calendarViewHolder.expButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        calendarViewHolder.calendarLayout.toggle();
                    }
                });

                calendarViewHolder.saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onValueChangedListener.onValueChanged(1, calendarViewHolder.getSelectedDate());
                        calendarViewHolder.calendarLayout.toggle();
                    }
                });

            }

                break;
            case 2:
            {
                final TravelTimeViewHolder travelTimeViewHolder = (TravelTimeViewHolder) holder;
                travelTimeViewHolder.expButton.setText(item);
                travelTimeViewHolder.setIsRecyclable(false);
                travelTimeViewHolder.layout.setInRecyclerView(true);
                travelTimeViewHolder.layout.initLayout();
                travelTimeViewHolder.layout.setExpanded(expandState.get(position));
                travelTimeViewHolder.layout.setListener(new ExpandableLayoutListenerAdapter() {
                    @Override
                    public void onPreOpen() {
                        expandState.put(position,true);
                    }

                    @Override
                    public void onPreClose() {
                        expandState.put(position,false);
                    }

                });
                travelTimeViewHolder.expButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        travelTimeViewHolder.layout.toggle();
                    }
                });
                travelTimeViewHolder.saveTravelTimeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onValueChangedListener.onValueChanged(2,travelTimeViewHolder.getChangedTravelTime());
                        onValueChangedListener.onValueChanged(7,travelTimeViewHolder.getArrivalTimeSec());
                        travelTimeViewHolder.layout.toggle();
                    }
                });
            }
                break;
            case 3:
            {
                final StayTimeViewHolder stayTimeViewHolder = (StayTimeViewHolder) holder;
                stayTimeViewHolder.expButton.setText(item);
                stayTimeViewHolder.setIsRecyclable(false);
                stayTimeViewHolder.layout.setInRecyclerView(true);
                stayTimeViewHolder.layout.initLayout();
                stayTimeViewHolder.layout.setExpanded(expandState.get(position));
                stayTimeViewHolder.layout.setListener(new ExpandableLayoutListenerAdapter() {
                    @Override
                    public void onPreOpen() {
                        expandState.put(position,true);
                    }

                    @Override
                    public void onPreClose() {
                        expandState.put(position,false);
                    }

                });
                stayTimeViewHolder.expButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stayTimeViewHolder.layout.toggle();
                    }
                });

                stayTimeViewHolder.saveTravelTimeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onValueChangedListener.onValueChanged(3, stayTimeViewHolder.getChangedTravelTime());
                        stayTimeViewHolder.layout.toggle();
                    }
                });
            }
                break;
            case 4:
            {
                final CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;
                categoryViewHolder.expButton.setText(item);
                categoryViewHolder.setIsRecyclable(false);
                categoryViewHolder.layout.setInRecyclerView(true);
                categoryViewHolder.layout.initLayout();
                categoryViewHolder.layout.setExpanded(expandState.get(position));
                categoryViewHolder.layout.setListener(new ExpandableLayoutListenerAdapter() {
                    @Override
                    public void onPreOpen() {
                        expandState.put(position,true);
                    }

                    @Override
                    public void onPreClose() {
                        expandState.put(position,false);
                    }

                });
                categoryViewHolder.saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onValueChangedListener.onValueChanged(4,categoryViewHolder.getChangedCategory());
                        categoryViewHolder.layout.toggle();
                    }
                });
                categoryViewHolder.expButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        categoryViewHolder.layout.toggle();
                    }
                });
            }
               break;
            case 5:
            {
                final TravelDistanceViewHolder distanceViewHolder = (TravelDistanceViewHolder) holder;
                distanceViewHolder.expButton.setText(item);
                distanceViewHolder.setIsRecyclable(false);
                distanceViewHolder.layout.setInRecyclerView(true);
                distanceViewHolder.layout.initLayout();
                distanceViewHolder.layout.setExpanded(expandState.get(position));
                distanceViewHolder.layout.setListener(new ExpandableLayoutListenerAdapter() {
                    @Override
                    public void onPreOpen() {
                        expandState.put(position,true);
                    }

                    @Override
                    public void onPreClose() {
                        expandState.put(position,false);
                    }

                });
                distanceViewHolder.expButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        distanceViewHolder.layout.toggle();
                    }
                });
                distanceViewHolder.saveDistanceButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onValueChangedListener.onValueChanged(5,distanceViewHolder.getChangedDistance());
                        distanceViewHolder.layout.toggle();
                    }
                });
            }
                break;
            case 6:
            {
                final RatingViewHolder ratingViewHolder = (RatingViewHolder) holder;
                ratingViewHolder.expButton.setText(item);
                ratingViewHolder.setIsRecyclable(false);
                ratingViewHolder.layout.setInRecyclerView(true);
                ratingViewHolder.layout.initLayout();
                ratingViewHolder.layout.setExpanded(expandState.get(position));
                ratingViewHolder.layout.setListener(new ExpandableLayoutListenerAdapter() {
                    @Override
                    public void onPreOpen() {
                        expandState.put(position,true);
                    }

                    @Override
                    public void onPreClose() {
                        expandState.put(position,false);
                    }

                });
                ratingViewHolder.expButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ratingViewHolder.layout.toggle();
                    }
                });
                ratingViewHolder.saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onValueChangedListener.onValueChanged(6,ratingViewHolder.getRatingValue());
                        ratingViewHolder.layout.toggle();
                    }
                });
            }
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

}
