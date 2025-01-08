package com.example.javarendu.model;

import java.time.LocalDateTime;

public class Evenement {
    // Types possibles d'événements
    public enum TypeEvenement {
        ACADEMIQUE, SPORTIF, CULTUREL, CONFERENCE, SPORT, MEETING, SOCIAL;

        // Method for case-insensitive conversion from string to enum
        public static TypeEvenement fromString(String type) {
            for (TypeEvenement t : TypeEvenement.values()) {
                if (t.name().equalsIgnoreCase(type)) {
                    return t;
                }
            }
            throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    // Attributs de l'événement
    private int id;
    private int idUtilisateur; // L'utilisateur qui crée l'événement
    private String nom;
    private String description;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private TypeEvenement type;

    // Constructeurs
    public Evenement() {}

    public Evenement(int idUtilisateur, String nom, String description,
                     LocalDateTime dateDebut, LocalDateTime dateFin, TypeEvenement type) {
        this.idUtilisateur = idUtilisateur;
        this.nom = nom;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.type = type;
    }

    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) {
        this.id = id;
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
    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }
    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }
    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public TypeEvenement getType() {
        return type;
    }
    public void setType(TypeEvenement type) { this.type = type; }

    @Override
    public String toString() {
        return "Evenement{" +
                "id=" + id +
                ", idUtilisateur=" + idUtilisateur +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", type=" + type +
                '}';
    }
}
