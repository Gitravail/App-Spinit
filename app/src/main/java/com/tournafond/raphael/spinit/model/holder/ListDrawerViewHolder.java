package com.tournafond.raphael.spinit.model.holder;

import android.content.ClipData;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.tournafond.raphael.spinit.R;
import com.tournafond.raphael.spinit.model.Liste;
import com.tournafond.raphael.spinit.model.adapter.ListDrawerAdapter;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListDrawerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.drawer_item_text)
    TextView textView;


    // FOR DATA
    private WeakReference<ListDrawerAdapter.Listener> callbackWeakRef;

    public ListDrawerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithItem(Liste liste, ListDrawerAdapter.Listener callback){
        this.callbackWeakRef = new WeakReference<>(callback);
        this.textView.setText(liste.getTitre());
    }

    @Override
    public void onClick(View view) {
        ListDrawerAdapter.Listener callback = callbackWeakRef.get();
        if (callback != null) callback.onClickDeleteButton(getAdapterPosition());
    }
}


