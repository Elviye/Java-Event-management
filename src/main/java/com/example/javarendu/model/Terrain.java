package com.example.javarendu.model;

public class Terrain {
    // Types de terrains possibles
    public enum TypeTerrain {
        FOOTBALL,       // Terrain de football
        BASKET,         // Terrain de basketball
        TENNIS,         // Court de tennis
        VOLLEYBALL,     // Terrain de volleyball
        ATHLETISME,     // Piste d'athlétisme
        POLYVALENT      // Terrain multifonctionnel
    }

    // Attributs du terrain
    private int id;
    private String nom;
    private String description;
    private TypeTerrain type;
    private int capacite;  // Nombre de personnes pouvant utiliser le terrain
    private boolean estCouverte;  // Terrain couvert ou en extérieur
    private boolean estDisponible;

    // Constructeurs
    public Terrain() {}

    public Terrain(String nom, String description, TypeTerrain type,
                   int capacite, boolean estCouverte) {
        this.nom = nom;
        this.description = description;
        this.type = type;
        this.capacite = capacite;
        this.estCouverte = estCouverte;
        this.estDisponible = true;
    }

    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public TypeTerrain getType() { return type; }
    public void setType(TypeTerrain type) { this.type = type; }

    public int getCapacite() { return capacite; }
    public void setCapacite(int capacite) { this.capacite = capacite; }

    public boolean isEstCouverte() { return estCouverte; }
    public void setEstCouverte(boolean estCouverte) { this.estCouverte = estCouverte; }

    public boolean isEstDisponible() { return estDisponible; }
    public void setEstDisponible(boolean estDisponible) { this.estDisponible = estDisponible; }

    @Override
    public String toString() {
        return "Terrain{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", capacite=" + capacite +
                ", estCouverte=" + estCouverte +
                ", estDisponible=" + estDisponible +
                '}';
    }

    /**
     * Vérifie si le terrain peut accueillir un certain nombre de personnes
     * @param nombrePersonnes Nombre de personnes souhaitant utiliser le terrain
     * @return true si le terrain est suffisamment grand, false sinon
     */
    public boolean peutAccueillir(int nombrePersonnes) {
        return this.capacite >= nombrePersonnes;
    }

    /**
     * Détermine si le terrain est adapté à un type d'activité spécifique
     * @param typeDemande Type d'activité recherché
     * @return true si le terrain correspond, false sinon
     */
    public boolean estAdapte(TypeTerrain typeDemande) {
        return this.type == typeDemande;
    }
}