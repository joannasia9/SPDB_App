package com.spdb.spdb_app.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.spdb.spdb_app.R;

public class CategoryViewHolder extends RecyclerView.ViewHolder{
    public AutoCompleteTextView textView;
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

        textView = view.findViewById(R.id.categoryAutocomplete);
        saveButton = view.findViewById(R.id.saveCatButton);
        expButton = itemView.findViewById(R.id.catItemB);
        layout = itemView.findViewById(R.id.expandableCategory);

        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, categories_pl);
        textView.setAdapter(categoriesAdapter);


    }

    public String getChangedCategory(){
        String placeType = "";
        String tmp = textView.getText().toString().trim();
        for (int i = 0; i < categories_pl.length; i++){
            if(!placeType.equals("")){
                continue;
            }
            if(categories_pl[i].equals(tmp)) placeType = categories_eng[i];
        }

        return placeType;
    }
}
