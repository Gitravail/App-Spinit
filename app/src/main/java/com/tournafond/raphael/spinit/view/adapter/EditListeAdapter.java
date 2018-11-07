package com.tournafond.raphael.spinit.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tournafond.raphael.spinit.R;
import com.tournafond.raphael.spinit.model.Liste;
import com.tournafond.raphael.spinit.view.holder.ListeEditViewHolder;

import java.util.ArrayList;
import java.util.List;

public class EditListeAdapter extends RecyclerView.Adapter<ListeEditViewHolder> {

    // CALLBACK
    public interface Listener { void onClickFavButton(int position); }
    private final Listener callback;

    // FOR DATA
    private List<Liste> listes;

    // CONSTRUCTOR
    public EditListeAdapter(Listener callback) {
        this.listes = new ArrayList<>();
        this.callback = callback;
    }

    @Override
    public ListeEditViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // CREATE VIEW HOLDER AND INFLATING ITS XML LAYOUT
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_cell, parent, false);

        return new ListeEditViewHolder(view);
    }

    // UPDATE VIEWHOLDER WITH A LISTE
    @Override
    public void onBindViewHolder(ListeEditViewHolder viewHolder, int position) {
        viewHolder.updateWithListe(this.listes.get(position), this.callback);
    }

    @Override
    public int getItemCount() {
        return this.listes.size();
    }

    public Liste getListe(int position){
        return this.listes.get(position);
    }

    public void updateData(List<Liste> listes){
        this.listes = listes;
        this.notifyDataSetChanged();
    }
}

