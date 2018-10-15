package com.tournafond.raphael.spinit.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.tournafond.raphael.spinit.R;
import java.util.ArrayList;

public class ChoixAdapter extends ArrayAdapter<String> {

    private static final String PREFIXE = "Element";
    public ArrayList<String> liste;

    public ChoixAdapter(Context context, ArrayList<String> choix) {
        super(context,0, choix);
        liste = choix;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        final EditText editText = (EditText) convertView.findViewById(R.id.item);
        editText.setHint(PREFIXE + " " + (position+1));

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                liste.set(position,editText.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return convertView;
    }
}
