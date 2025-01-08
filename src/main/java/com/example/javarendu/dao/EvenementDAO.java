package com.example.javarendu.dao;

import com.example.javarendu.model.Evenement;
import com.example.javarendu.utils.DBconnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EvenementDAO implements GenericDAO<Evenement> {
    private Connection connection;

    public EvenementDAO() throws SQLException {
        // Initialize connection using DBconnection utility class
        this.connection = DBconnection.getConn();
    }

    @Override
    public void add(Evenement evenement) throws SQLException {
        String sql = "INSERT INTO evenements " +
                "(id_utilisateur, nom, description, date_debut, date_fin, type) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, evenement.getIdUtilisateur());
            pstmt.setString(2, evenement.getNom());
            pstmt.setString(3, evenement.getDescription());
            pstmt.setTimestamp(4, Timestamp.valueOf(evenement.getDateDebut()));
            pstmt.setTimestamp(5, Timestamp.valueOf(evenement.getDateFin()));
            pstmt.setString(6, evenement.getType().name());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    evenement.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public Evenement get(int id) throws SQLException {
        String sql = "SELECT * FROM evenements WHERE id_event = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEvenement(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Evenement> getAll() throws SQLException {
        List<Evenement> evenements = new ArrayList<>();
        String sql = "SELECT * FROM evenements";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                evenements.add(mapResultSetToEvenement(rs));
            }
        }

        return evenements;
    }

    @Override
    public void update(Evenement evenement) throws SQLException {
        String sql = "UPDATE evenements SET " +
                "id_utilisateur = ?, nom = ?, description = ?, " +
                "date_debut = ?, date_fin = ?, type = ? " +
                "WHERE id_event = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, evenement.getIdUtilisateur());
            pstmt.setString(2, evenement.getNom());
            pstmt.setString(3, evenement.getDescription());
            pstmt.setTimestamp(4, Timestamp.valueOf(evenement.getDateDebut()));
            pstmt.setTimestamp(5, Timestamp.valueOf(evenement.getDateFin()));
            pstmt.setString(6, evenement.getType().name());
            pstmt.setInt(7, evenement.getId());

            pstmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM evenements WHERE id_event = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    private Evenement mapResultSetToEvenement(ResultSet rs) throws SQLException {
        Evenement evenement = new Evenement();

        evenement.setId(rs.getInt("id_event"));
        evenement.setIdUtilisateur(rs.getInt("id_utilisateur"));
        evenement.setNom(rs.getString("nom"));
        evenement.setDescription(rs.getString("description"));

        Timestamp dateDebutTimestamp = rs.getTimestamp("date_debut");
        Timestamp dateFinTimestamp = rs.getTimestamp("date_fin");

        evenement.setDateDebut(dateDebutTimestamp != null ? dateDebutTimestamp.toLocalDateTime() : null);
        evenement.setDateFin(dateFinTimestamp != null ? dateFinTimestamp.toLocalDateTime() : null);

        evenement.setType(Evenement.TypeEvenement.valueOf(rs.getString("type").toUpperCase()));


        return evenement;
    }

    public List<Evenement> getEvenementsByType(Evenement.TypeEvenement type) throws SQLException {
        List<Evenement> evenements = new ArrayList<>();
        String sql = "SELECT * FROM evenements WHERE type = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, type.name());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    evenements.add(mapResultSetToEvenement(rs));
                }
            }
        }

        return evenements;
    }
}
