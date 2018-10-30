package com.tournafond.raphael.spinit.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import com.tournafond.raphael.spinit.model.ListItem;
import com.tournafond.raphael.spinit.R;
import java.util.ArrayList;

import com.tournafond.raphael.spinit.model.Liste;
import com.tournafond.raphael.spinit.model.User;
import com.tournafond.raphael.spinit.model.ViewHolder;


public class ChoixAdapterHolder extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<ListItem> listItems = new ArrayList<>();
    private String prefixe;

    private static final int NB_OF_ITEMS = 3;

    public ChoixAdapterHolder(Context context, User utilisateur) {
        ArrayList<String> init = new ArrayList<>();
        init.add("Casino");
        init.add("Boîte de nuit");
        init.add("Netflix");
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ListItem listItem;
        Liste liste = utilisateur.getListeParDefaut();

        this.prefixe = utilisateur.getPrefixe();

        // Rempli la vue avec trois elements vides
        if (liste.estVide()) {
            for (int i = 0; i < NB_OF_ITEMS; i++) {
                listItem = new ListItem();
                listItem.caption = prefixe + " " + (i + 1);
                listItem.text = init.get(i);
                listItems.add(listItem);
            }
        } else {
            int taille = liste.getListeMots().size();
            for (int i = 0; i < taille; i++) {
                listItem = new ListItem();
                listItem.caption = prefixe + " " + (i + 1);
                listItem.text = liste.getListeMots().get(i);
                listItems.add(listItem);
            }
        }
        notifyDataSetChanged();
    }

    public void setPrefixe(String prefixe) {
        this.prefixe = prefixe;
        ListItem listItem;
        int taille = listItems.size();
        for (int i = 0; i < taille; i++) {
            listItems.get(i).caption = this.prefixe + " " + (i + 1);
        }
        notifyDataSetChanged();
    }

    public void addNewItem() {
        ListItem listItem = new ListItem();
        listItem.caption = prefixe + " " + (this.getCount()+1);
        listItem.text = "";
        listItems.add(listItem);
        notifyDataSetChanged();
    }

    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) { return listItems.get(position); }

    public long getItemId(int position) { return position; }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item, null);
            holder.caption = convertView
                    .findViewById(R.id.ItemCaption);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //Fill EditText with the value you have in data source
        ListItem li = listItems.get(position);
        holder.caption.setText(li.text);
        holder.caption.setHint(li.caption);
        holder.caption.setId(position);

        //we need to update adapter once we finish with editing
        holder.caption.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    final int position = v.getId();
                    final EditText Caption = (EditText) v;
                    ListItem li = listItems.get(position);
                    li.text = Caption.getText().toString();
                    li.caption = prefixe + " " + (position + 1);
                }
            }
        });
        return convertView;
    }
}
