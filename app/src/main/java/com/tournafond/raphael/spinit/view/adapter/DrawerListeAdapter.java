package com.tournafond.raphael.spinit.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tournafond.raphael.spinit.R;
import com.tournafond.raphael.spinit.model.Liste;
import com.tournafond.raphael.spinit.view.holder.ListeDrawerViewHolder;

import java.util.ArrayList;
import java.util.List;

// Debut d'implementation de l'adapter pour le drawer mais non termine
public class DrawerListeAdapter extends RecyclerView.Adapter<ListeDrawerViewHolder> {

    // CALLBACK
    public interface Listener { void onClickListe(int position); }
    private final Listener callback;

    // FOR DATA
    private List<Liste> listes;

    // CONSTRUCTOR
    public DrawerListeAdapter(Listener callback) {
        this.listes = new ArrayList<>();
        this.callback = callback;
    }

    @Override
    public ListeDrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.drawer_list_item, parent, false);

        return new ListeDrawerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListeDrawerViewHolder viewHolder, int position) {
        viewHolder.updateWithItem(this.listes.get(position), this.callback);
    }

    @Override
    public int getItemCount() {
        return this.listes.size();
    }

    public Liste getItem(int position){
        return this.listes.get(position);
    }

    public void updateData(List<Liste> listes){
        this.listes = listes;
        this.notifyDataSetChanged();
    }
}
