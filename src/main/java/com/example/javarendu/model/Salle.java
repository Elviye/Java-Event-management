package com.example.javarendu.model;

public class Salle {
    public enum TypeSalle {
        AMPHI, CLASSE, REUNION, CONFERENCE, LABORATOIRE
    }

    private int id;
    private String nom;
    private String nomComplet;
    private int capacite;
    private TypeSalle type;
    private boolean estDisponible;

    public Salle() {}

    public Salle(String nom, String nomComplet, int capacite, TypeSalle type) {
        this.nom = nom;
        this.nomComplet = nomComplet;
        this.capacite = capacite;
        this.type = type;
        this.estDisponible = true;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getNomComplet() { return nomComplet; }
    public void setNomComplet(String nomComplet) { this.nomComplet = nomComplet; }

    public int getCapacite() { return capacite; }
    public void setCapacite(int capacite) { this.capacite = capacite; }

    public TypeSalle getType() { return type; }
    public void setType(TypeSalle type) { this.type = type; }

    public boolean isEstDisponible() { return estDisponible; }
    public void setEstDisponible(boolean estDisponible) { this.estDisponible = estDisponible; }

    @Override
    public String toString() {
        return "Salle{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", nomComplet='" + nomComplet + '\'' +
                ", capacite=" + capacite +
                ", type=" + type +
                ", estDisponible=" + estDisponible +
                '}';
    }
}
