package com.tournafond.raphael.spinit.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import com.tournafond.raphael.spinit.view.holder.ListItem;
import com.tournafond.raphael.spinit.R;
import java.util.ArrayList;

import com.tournafond.raphael.spinit.model.Liste;
import com.tournafond.raphael.spinit.model.User;
import com.tournafond.raphael.spinit.view.holder.ViewHolder;

// Adapter de la listview d'ajout d'elements
public class MainListeAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<ListItem> listItems = new ArrayList<>();
    private String prefixe;

    private static final int NB_OF_ITEMS = 3;
    public static final int ACTION = 0;
    public static final int PARTICIPANT = 1;

    public MainListeAdapter(Context context, User utilisateur, int type) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ListItem listItem;
        //Liste liste = utilisateur.getListeParDefaut(); (ajout d'une liste par defaut plus tard)
        Liste liste = new Liste();

        this.prefixe = utilisateur.getPrefixe();

        // Rempli la vue avec trois elements vides
        if (liste.estVide()) {
            for (int i = 0; i < NB_OF_ITEMS; i++) {
                listItem = new ListItem();
                listItem.caption = prefixe + " " + (i + 1);
                listItem.text = "";
                listItems.add(listItem);
            }
        } else {
            ArrayList<String> listeType;
            if (type == PARTICIPANT) {
                listeType = liste.getParticipant();
            } else {
                listeType = liste.getAction();
            }
            int taille = listeType.size();
            for (int i = 0; i < taille; i++) {
                listItem = new ListItem();
                listItem.caption = prefixe + " " + (i + 1);
                listItem.text = listeType.get(i);
                listItems.add(listItem);
            }
        }
        notifyDataSetChanged();
    }

    public MainListeAdapter(Context context, String prefixe, ArrayList<String> list) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ListItem listItem;
        this.prefixe = prefixe;

        listItems = new ArrayList<>();

        if (list.isEmpty()) {
            for (int i = 0; i < NB_OF_ITEMS; i++) {
                listItem = new ListItem();
                listItem.caption = prefixe + " " + (i + 1);
                listItem.text = "";
                listItems.add(listItem);
            }
        } else {
            int taille = list.size();
            String current;
            for (int i = 0; i < taille; i++) {
                current = list.get(i);
                if (!current.isEmpty()) {
                    listItem = new ListItem();
                    listItem.caption = prefixe + " " + (i + 1);
                    listItem.text = current;
                    listItems.add(listItem);
                }
            }
        }
        notifyDataSetChanged();
    }

    // ecrit la caption en fonction du prefixe courant (option, action, participant)
    public void setPrefixe(String prefixe) {
        this.prefixe = prefixe;
        int taille = listItems.size();
        for (int i = 0; i < taille; i++) {
            listItems.get(i).caption = this.prefixe + " " + (i + 1);
        }
        notifyDataSetChanged();
    }

    // ajout d'un item dans la liste
    public void addNewItem() {
        ListItem listItem = new ListItem();
        listItem.caption = prefixe + " " + (this.getCount()+1);
        listItem.text = "";
        listItems.add(listItem);
        notifyDataSetChanged();
    }

    // nombre d'items
    public int getCount() {
        return listItems.size();
    }

    // recuperation de l'item courant
    @Override
    public Object getItem(int position) { return listItems.get(position); }

    // position de l'item
    public long getItemId(int position) { return position; }

    // lien avec la vue
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item, null);
            holder.caption = convertView
                    .findViewById(R.id.itemCaption);
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
        //ici le probleme est que l'update n'est pas effectue apres chaque entre clavier
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

    public ArrayList<ListItem> getItemList() {
        return listItems;
    }

    public ArrayList<String> getList() {
        ArrayList<String> list = new ArrayList<>();
        int taille = getItemList().size();
        String current;
        for (int i = 0; i < taille; i++) {
            current = getItemList().get(i).toString();
            if (!current.isEmpty()) {
                list.add(getItemList().get(i).toString());
            }
        }
        return list;
    }
}
