package com.tournafond.raphael.spinit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import com.tournafond.raphael.spinit.ListItem;
import com.tournafond.raphael.spinit.R;
import java.util.ArrayList;
import com.tournafond.raphael.spinit.ViewHolder;


public class ChoixAdapterHolder extends BaseAdapter {
    private LayoutInflater mInflater;
    public ArrayList listItems = new ArrayList();
    private ArrayList<String> init = new ArrayList<String>();

    private static final String PREFIXE = "Choix";
    private static final int NB_OF_ITEMS = 3;

    public ChoixAdapterHolder(Context context) {
        init.add("Casino");
        init.add("Boîte de nuit");
        init.add("Netflix");
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < NB_OF_ITEMS; i++) {
            ListItem listItem = new ListItem();
            listItem.caption = PREFIXE + " " + (i + 1);
            listItem.text = init.get(i);
            listItems.add(listItem);
        }
        notifyDataSetChanged();
    }

    public void addNewItem() {
        ListItem listItem = new ListItem();
        listItem.caption = PREFIXE + " " + (this.getCount()+1);
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
            holder.caption = (EditText) convertView
                    .findViewById(R.id.ItemCaption);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //Fill EditText with the value you have in data source
        ListItem li = (ListItem) listItems.get(position);
        holder.caption.setText(li.text);
        holder.caption.setHint(li.caption);
        holder.caption.setId(position);

        //we need to update adapter once we finish with editing
        holder.caption.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    final int position = v.getId();
                    final EditText Caption = (EditText) v;
                    ListItem li = (ListItem) listItems.get(position);
                    li.text = Caption.getText().toString();
                    li.caption = PREFIXE + " " + (position + 1);
                }
            }
        });
        return convertView;
    }
}
