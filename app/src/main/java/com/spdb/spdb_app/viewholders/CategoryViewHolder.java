package com.spdb.spdb_app.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.spdb.spdb_app.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder{
    public Spinner categorySpinner;
    public Button saveButton;
    public Button expButton;
    public ExpandableLinearLayout layout;
    private Context context;
    private String[] categories_pl;
    private String[] categories_eng;

    public CategoryViewHolder(View view, Context context){
        super(view);
        this.context = context;
        categories_pl = context.getResources().getStringArray(R.array.categories_pl);
        categories_eng = context.getResources().getStringArray(R.array.categories_eng);

        categorySpinner = view.findViewById(R.id.categorySpinner);
        saveButton = view.findViewById(R.id.saveCatButton);
        expButton = itemView.findViewById(R.id.catItemB);
        layout = itemView.findViewById(R.id.expandableCategory);
    }

    public String getChangedCategory(){
        Integer itemPosition = categorySpinner.getSelectedItemPosition();
        if (itemPosition == 0) {
            return "";
        }
        return categories_eng[itemPosition];
    }
}
