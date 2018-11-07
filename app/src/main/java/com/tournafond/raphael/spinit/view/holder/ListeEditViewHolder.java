package com.tournafond.raphael.spinit.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tournafond.raphael.spinit.R;
import com.tournafond.raphael.spinit.model.Liste;
import com.tournafond.raphael.spinit.view.adapter.EditListeAdapter;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListeEditViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.fragment_main_item_title) TextView mTextView;
    @BindView(R.id.fragement_main_fav_button) ImageButton mFavButton;

    // FOR DATA
    private WeakReference<EditListeAdapter.Listener> callbackWeakRef;

    public ListeEditViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithListe(Liste liste, EditListeAdapter.Listener callback) {
        this.callbackWeakRef = new WeakReference<EditListeAdapter.Listener>(callback);
        this.mTextView.setText(liste.getTitre());
        this.mFavButton.setOnClickListener(this);
        int type = liste.getType();
        switch (type) {
            case Liste.FAVORI:
                mFavButton.setBackgroundResource(R.drawable.ic_favorite_fill);
                break;
            case Liste.BONUS:
                mFavButton.setBackgroundResource(R.drawable.ic_bonus);
                break;
            default:
                mFavButton.setBackgroundResource(R.drawable.ic_favorite_empty);
        }
    }

    @Override
    public void onClick(View view) {
        EditListeAdapter.Listener callback = callbackWeakRef.get();
        if (callback != null) callback.onClickFavButton(getAdapterPosition());
    }
}
