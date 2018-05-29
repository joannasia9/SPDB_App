package com.spdb.spdb_app.helpfulComponents;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.spdb.spdb_app.R;

public class FormValidator {
    private Context context;

    public FormValidator(Context c){
        this.context = c;
    }

    public boolean isValidForm(Place startPlace){
        if(startPlace==null){
            showAlert();
            return false;
        } else return true;
    }

    public void setLimit(final EditText editText, final int value){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String sequence = s.toString();
                int sequenceValue = 0;

                if(sequence.equals("")) sequenceValue = 0; else
                    if(isInteger(sequence)) sequenceValue = Integer.parseInt(sequence); else {
                        sequenceValue=0;
                        editText.setError(context.getString(R.string.wrong_v));
                        editText.setText("");
                }

                if(sequenceValue>value){
                    editText.setError(context.getString(R.string.wrong_v));
                    editText.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException | NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }


    public void showAlert(){
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(R.string.warning)
                .setMessage(R.string.select_start_place)
                .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();
        dialog.show();
    }
}
