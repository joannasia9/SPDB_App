package com.spdb.spdb_app;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class FormValidator {
    Context context;

    public FormValidator(Context c){
        this.context = c;
    }

    public void validate(){

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
}
