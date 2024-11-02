package com.example.tp1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;


public class ContactManager {
    SQLiteDatabase db =null;

    public ContactManager(Context context) {
        this.context = context;
    }

    Context context;
    public void ouvrir(){
        ContactHelper helper = new ContactHelper(context,"mabase.db",null,2);
        db=helper.getWritableDatabase();
    }
    public Long ajouter(String nom, String pseudo, String num){
        Long a = null;
        ContentValues values = new ContentValues();
        values.put(ContactHelper.col_nom,nom);
        values.put(ContactHelper.col_pseudo,pseudo);
        values.put(ContactHelper.col_num,num);
        a=db.insert(ContactHelper.table_contact,null,values);
        return a;
    }

    public ArrayList<Contact> getAllContacts(){
        ArrayList<Contact> l = new ArrayList<Contact>();
//        Cursor cr = db.query(ContactHelper.table_contact, new String[]{ContactHelper.col_num, ContactHelper.col_pseudo, ContactHelper.col_num}, null, null, null);
        Cursor cr = db.query(ContactHelper.table_contact,
                new String[]{ContactHelper.col_nom,ContactHelper.col_pseudo,ContactHelper.col_num},
                null,
                null,
                null,
                null,
                null);
        cr.moveToFirst();
        while (!cr.isAfterLast()){
            String i1 = cr.getString(0);
            String i2 = cr.getString(1);
            String i3 = cr.getString(2);
            l.add(new Contact(i1,i2,i3));
            cr.moveToNext();
        }
        return l;


    }

    public void supprimer(String num, String pseudo, String nom) {
        db.delete(ContactHelper.table_contact, ContactHelper.col_num + " = ? and "+ContactHelper.col_pseudo+" = ? and "+ContactHelper.col_nom+" = ?", new String[]{num,pseudo,nom});
    }
    public void modifier(String nom, String nouveauPseudo, String nouveauNum) {
        ContentValues values = new ContentValues();
        values.put(ContactHelper.col_pseudo, nouveauPseudo);
        values.put(ContactHelper.col_num, nouveauNum);

        db.update(ContactHelper.table_contact, values, ContactHelper.col_nom + " = ?", new String[]{nom});
    }

    public void modifier(String oldNom, String newNom, String newPseudo, String newNum) {
        ContentValues values = new ContentValues();
        values.put(ContactHelper.col_nom, newNom);
        values.put(ContactHelper.col_pseudo, newPseudo);
        values.put(ContactHelper.col_num, newNum);
        db.update(ContactHelper.table_contact, values, ContactHelper.col_nom + "=?", new String[]{oldNom});
    }

    public void supprimer(String nom) {
        db.delete(ContactHelper.table_contact, ContactHelper.col_nom + "=?", new String[]{nom});
    }

}
