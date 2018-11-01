package com.tournafond.raphael.spinit.model.adapter;

import android.content.ClipData;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tournafond.raphael.spinit.R;
import com.tournafond.raphael.spinit.model.Liste;
import com.tournafond.raphael.spinit.model.holder.ListDrawerViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ListDrawerAdapter extends RecyclerView.Adapter<ListDrawerViewHolder> {

    // CALLBACK
    public interface Listener { void onClickDeleteButton(int position); }
    private final Listener callback;

    // FOR DATA
    private List<Liste> listes;

    // CONSTRUCTOR
    public ListDrawerAdapter(Listener callback) {
        this.listes = new ArrayList<>();
        this.callback = callback;
    }

    @Override
    public ListDrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.drawer_list_item, parent, false);

        return new ListDrawerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListDrawerViewHolder viewHolder, int position) {
        viewHolder.updateWithItem(this.listes.get(position), this.callback);
    }

    @Override
    public int getItemCount() {
        return this.listes.size();
    }

    public Liste getItem(int position){
        return this.listes.get(position);
    }

    public void updateData(List<Liste> items){
        this.listes = items;
        this.notifyDataSetChanged();
    }
}
