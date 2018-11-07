package com.tournafond.raphael.spinit.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tournafond.raphael.spinit.R;
import com.tournafond.raphael.spinit.view.holder.ListDualItem;

import java.util.ArrayList;

public class ResultListeAdapter extends ArrayAdapter<ListDualItem> {

    private int layoutResource;

    public ResultListeAdapter(Context context, int layoutResource, ArrayList<ListDualItem> ldi) {
        super(context, layoutResource, ldi);
        this.layoutResource = layoutResource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(layoutResource, null);
        }

        ListDualItem threeStrings = getItem(position);

        if (threeStrings != null) {
            TextView actionTextView = view.findViewById(R.id.textAction);
            TextView participantTextView = view.findViewById(R.id.textParticipant);

            if (actionTextView != null) {
                actionTextView.setText(threeStrings.getAction());
            }
            if (participantTextView != null) {
                participantTextView.setText(threeStrings.getParticipant());
            }
        }

        return view;
    }

}
