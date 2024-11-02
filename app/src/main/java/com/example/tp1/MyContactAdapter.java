package com.example.tp1;


import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public  class MyContactAdapter extends BaseAdapter {
    Context context;
    ArrayList<Contact> data;
    public MyContactAdapter(Context context, ArrayList<Contact> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contact contact = data.get(position);
        // Check if convertView is null, reuse it if it's not null
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.view_contact, parent, false);
        }
        //creation d'un view
        LayoutInflater inflater =LayoutInflater.from(context);
        View v= inflater.inflate(R.layout.view_contact,null);

        //recuperation des holders:
        TextView tvnom = v.findViewById(R.id.tv_nom_contact);
        TextView tvpseudo = v.findViewById(R.id.tv_pseudo_contact);
        TextView tvnum = v.findViewById(R.id.tv_num_contact);

        //affectation des holders
        tvnom.setText(contact.nom);
        tvpseudo.setText(contact.pseudo);
        tvnum.setText(contact.numero);
        return v;
    }
}
