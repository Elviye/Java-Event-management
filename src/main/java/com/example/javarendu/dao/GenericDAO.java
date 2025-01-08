package com.example.javarendu.dao;

import java.util.List;
import java.sql.SQLException;

public interface GenericDAO<T> {
    /**
     * Ajoute une nouvelle entité à la base de données
     * @param entity L'entité à ajouter
     * @throws SQLException en cas d'erreur lors de l'ajout
     */
    void add(T entity) throws SQLException;

    /**
     * Récupère une entité par son identifiant
     * @param id L'identifiant de l'entité
     * @return L'entité correspondante
     * @throws SQLException en cas d'erreur lors de la récupération
     */
    T get(int id) throws SQLException;

    /**
     * Récupère toutes les entités
     * @return Une liste de toutes les entités
     * @throws SQLException en cas d'erreur lors de la récupération
     */
    List<T> getAll() throws SQLException;

    /**
     * Met à jour une entité existante
     * @param entity L'entité à mettre à jour
     * @throws SQLException en cas d'erreur lors de la mise à jour
     */
    void update(T entity) throws SQLException;

    /**
     * Supprime une entité par son identifiant
     * @param id L'identifiant de l'entité à supprimer
     * @throws SQLException en cas d'erreur lors de la suppression
     */
    void delete(int id) throws SQLException;
}