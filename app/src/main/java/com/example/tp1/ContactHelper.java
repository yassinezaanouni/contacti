package com.example.tp1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ContactHelper extends SQLiteOpenHelper {
    // le nom et les champs de table dans la BD
    public static final String table_contact = "contacts";
    public static final String col_id = "id";
    public static final String col_nom = "nom";
    public static final String col_pseudo = "pseudo";
    public static final String col_num = "num";

    public String requete = "create table "+table_contact+" ("+col_id+" Integer not null primary key autoincrement,"+col_nom+" text not null,"+col_pseudo+" text not null,"+col_num+" text not null)";
    public ContactHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(requete);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table "+table_contact);
        onCreate(db);
    }
}
