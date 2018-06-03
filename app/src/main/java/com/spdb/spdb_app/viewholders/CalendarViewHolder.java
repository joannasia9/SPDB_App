package com.spdb.spdb_app.viewholders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.spdb.spdb_app.R;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarViewHolder extends RecyclerView.ViewHolder{
    public CalendarView calendarView;
    public Button saveButton;
    public Button expButton;
    public ExpandableLinearLayout calendarLayout;
    private String selectedDate;

   public CalendarViewHolder(View view){
       super(view);
        calendarView = view.findViewById(R.id.calendarView);
        saveButton = view.findViewById(R.id.saveDate);
        expButton = itemView.findViewById(R.id.dateItemB);
        calendarLayout = itemView.findViewById(R.id.expandableTDate);


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year,month,dayOfMonth);

                DateFormatSymbols symbols = new DateFormatSymbols(new Locale("pl"));
                selectedDate = symbols.getWeekdays()[calendar.get(Calendar.DAY_OF_WEEK)];

            }
        });

    }

    public String getSelectedDate() {
        return selectedDate; //dayOfWeek
    }
}
