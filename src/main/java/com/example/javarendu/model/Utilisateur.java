package com.example.javarendu.model;

public class Utilisateur {
    private int idUtilisateur;
    private String nom;
    private String prenom;
    private String email;
    private String type;

    public Utilisateur(String nom, String prenom, String email, String type) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.type = type;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(int idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }
}
