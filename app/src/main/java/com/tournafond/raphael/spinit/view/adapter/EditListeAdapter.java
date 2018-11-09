package com.tournafond.raphael.spinit.view.adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.tournafond.raphael.spinit.R;
import com.tournafond.raphael.spinit.controller.EditActivity;
import com.tournafond.raphael.spinit.model.Liste;
import com.tournafond.raphael.spinit.view.holder.ListeEditViewHolder;

import java.util.ArrayList;
import java.util.List;

// Adapter pour la recycler view du menu des listes
public class EditListeAdapter extends RecyclerView.Adapter<ListeEditViewHolder> {

    Context context;

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
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_cell, parent, false);

        return new ListeEditViewHolder(view, context);
    }

    // UPDATE VIEWHOLDER WITH A LISTE
    @Override
    public void onBindViewHolder(ListeEditViewHolder viewHolder, int position) {
        viewHolder.updateWithListe(this.listes.get(position), this.callback);

        viewHolder.mMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, viewHolder.mMenuButton);
                //inflating menu from xml resource
                popup.inflate(R.menu.option_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Liste liste = getListe(position);
                        switch (item.getItemId()) {
                            case R.id.charger:
                                if(context instanceof EditActivity){
                                    ((EditActivity)context).chargeListe(liste);
                                }
                                break;
                            case R.id.modifier:
                                if(context instanceof EditActivity){
                                    ((EditActivity)context).modifierListe(liste);
                                }
                                break;
                            case R.id.supprimer:
                                if(context instanceof EditActivity){
                                    ((EditActivity)context).deleteListe(liste);
                                }
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }
        });
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

