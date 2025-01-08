package com.example.javarendu.dao;

import com.example.javarendu.model.Utilisateur;
import com.example.javarendu.utils.DBconnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {

    // Add a new utilisateur
    public void add(Utilisateur utilisateur) {
        String query = "INSERT INTO utilisateurs (nom, prenom, email, type) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBconnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, utilisateur.getNom());
            stmt.setString(2, utilisateur.getPrenom());
            stmt.setString(3, utilisateur.getEmail());
            stmt.setString(4, utilisateur.getType());

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    utilisateur.setIdUtilisateur(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error while adding utilisateur: " + e.getMessage());
        }
    }

    // Get a single utilisateur by ID
    public Utilisateur get(int idUtilisateur) {
        String query = "SELECT * FROM utilisateurs WHERE id_utilisateur = ?";
        try (Connection conn = DBconnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idUtilisateur);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Utilisateur utilisateur = new Utilisateur(
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("type")
                );
                utilisateur.setIdUtilisateur(rs.getInt("id_utilisateur"));
                return utilisateur;
            }
        } catch (SQLException e) {
            System.err.println("Error while retrieving utilisateur: " + e.getMessage());
        }
        return null;
    }

    // Get all utilisateurs
    public List<Utilisateur> getAll() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String query = "SELECT * FROM utilisateurs";
        try (Connection conn = DBconnection.getConn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Utilisateur utilisateur = new Utilisateur(
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("type")
                );
                utilisateur.setIdUtilisateur(rs.getInt("id_utilisateur"));
                utilisateurs.add(utilisateur);
            }
        } catch (SQLException e) {
            System.err.println("Error while retrieving all utilisateurs: " + e.getMessage());
        }
        return utilisateurs;
    }

    // Delete a utilisateur by ID
    public void delete(int idUtilisateur) {
        String query = "DELETE FROM utilisateurs WHERE id_utilisateur = ?";
        try (Connection conn = DBconnection.getConn();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idUtilisateur);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error while deleting utilisateur: " + e.getMessage());
        }
    }
}
