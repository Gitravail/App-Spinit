package com.tournafond.raphael.spinit.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tournafond.raphael.spinit.R;
import com.tournafond.raphael.spinit.model.Liste;
import com.tournafond.raphael.spinit.view.adapter.DrawerListeAdapter;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

// Debut du holder pour le drawer
public class ListeDrawerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.drawer_item_text)
    TextView textView;


    // FOR DATA
    private WeakReference<DrawerListeAdapter.Listener> callbackWeakRef;

    public ListeDrawerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithItem(Liste liste, DrawerListeAdapter.Listener callback){
        this.callbackWeakRef = new WeakReference<>(callback);
        this.textView.setText(liste.getTitre());
    }

    @Override
    public void onClick(View view) {
        DrawerListeAdapter.Listener callback = callbackWeakRef.get();
        if (callback != null) callback.onClickListe(getAdapterPosition());
    }
}


