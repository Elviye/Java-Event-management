package com.example.javarendu.dao;

import com.example.javarendu.utils.DBconnection;
import com.example.javarendu.model.Salle;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalleDAO implements GenericDAO<Salle> {

    @Override
    public void add(Salle salle) throws SQLException {
        String sql = "INSERT INTO salles (nom, nom_complet, capacite, type, est_disponible) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBconnection.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, salle.getNom());
            pstmt.setString(2, salle.getNomComplet());
            pstmt.setInt(3, salle.getCapacite());
            pstmt.setString(4, salle.getType().name());
            pstmt.setBoolean(5, salle.isEstDisponible());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    salle.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public Salle get(int id) throws SQLException {
        String sql = "SELECT * FROM salles WHERE id_salle = ?";
        try (Connection conn = DBconnection.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSalle(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Salle> getAll() throws SQLException {
        List<Salle> salles = new ArrayList<>();
        String sql = "SELECT * FROM salles";
        try (Connection conn = DBconnection.getConn();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                salles.add(mapResultSetToSalle(rs));
            }
        }
        return salles;
    }

    @Override
    public void update(Salle salle) throws SQLException {
        String sql = "UPDATE salles SET nom = ?, nom_complet = ?, capacite = ?, " +
                "type = ?, est_disponible = ? WHERE id_salle = ?";
        try (Connection conn = DBconnection.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, salle.getNom());
            pstmt.setString(2, salle.getNomComplet());
            pstmt.setInt(3, salle.getCapacite());
            pstmt.setString(4, salle.getType().name());
            pstmt.setBoolean(5, salle.isEstDisponible());
            pstmt.setInt(6, salle.getId());

            pstmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM salles WHERE id_salle = ?";
        try (Connection conn = DBconnection.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    private Salle mapResultSetToSalle(ResultSet rs) throws SQLException {
        Salle salle = new Salle();
        salle.setId(rs.getInt("id_salle"));
        salle.setNom(rs.getString("nom"));
        salle.setNomComplet(rs.getString("nom_complet"));
        salle.setCapacite(rs.getInt("capacite"));
        salle.setType(Salle.TypeSalle.valueOf(rs.getString("type")));
        salle.setEstDisponible(rs.getBoolean("est_disponible"));
        return salle;
    }
}
