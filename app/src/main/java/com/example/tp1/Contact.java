package com.example.tp1;

public class Contact {
    String nom;
    String pseudo;
    String numero;

    public Contact(String nom, String pseudo, String numero) {
        this.nom = nom;
        this.pseudo = pseudo;
        this.numero = numero;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
