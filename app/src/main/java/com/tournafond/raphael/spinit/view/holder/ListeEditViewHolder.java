package com.tournafond.raphael.spinit.view.holder;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tournafond.raphael.spinit.R;
import com.tournafond.raphael.spinit.model.Liste;
import com.tournafond.raphael.spinit.view.adapter.EditListeAdapter;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

// holder (lien vue) de la recyclerview des listes
public class ListeEditViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.fragment_main_item_title) TextView mTextView;
    public TextView mMenuButton;
    public ImageButton mFavButton;
    Context context;

    // FOR DATA
    private WeakReference<EditListeAdapter.Listener> callbackWeakRef;

    // recuperation des elements
    public ListeEditViewHolder(View itemView, Context context) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        mMenuButton = itemView.findViewById(R.id.fragement_main_menu_button);
        mFavButton = itemView.findViewById(R.id.fragement_main_fav_button);
        this.context = context;
    }

    // update de la vue
    public void updateWithListe(Liste liste, EditListeAdapter.Listener callback) {

        this.callbackWeakRef = new WeakReference<>(callback);
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

    // on click sur le bouton favoris
    @Override
    public void onClick(View view) {
        EditListeAdapter.Listener callback = callbackWeakRef.get();
        if (callback != null) {
            mFavButton.startAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom));
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    callback.onClickFavButton(getAdapterPosition());
                }
            }, 100);

        }
    }
}
