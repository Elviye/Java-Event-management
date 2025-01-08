package com.example.javarendu.dao;

import com.example.javarendu.model.Terrain;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TerrainDAO implements GenericDAO<Terrain> {
    private Connection connection;

    public TerrainDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void add(Terrain terrain) throws SQLException {
        String sql = "INSERT INTO terrains " +
                "(nom, description, type, capacite, est_couverte, est_disponible) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, terrain.getNom());
            pstmt.setString(2, terrain.getDescription());
            pstmt.setString(3, terrain.getType().name());
            pstmt.setInt(4, terrain.getCapacite());
            pstmt.setBoolean(5, terrain.isEstCouverte());
            pstmt.setBoolean(6, terrain.isEstDisponible());

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    terrain.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public Terrain get(int id) throws SQLException {
        String sql = "SELECT * FROM terrains WHERE id_terrain = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTerrain(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Terrain> getAll() throws SQLException {
        List<Terrain> terrains = new ArrayList<>();
        String sql = "SELECT * FROM terrains";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                terrains.add(mapResultSetToTerrain(rs));
            }
        }

        return terrains;
    }

    @Override
    public void update(Terrain terrain) throws SQLException {
        String sql = "UPDATE terrains SET " +
                "nom = ?, description = ?, type = ?, " +
                "capacite = ?, est_couverte = ?, est_disponible = ? " +
                "WHERE id_terrain = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, terrain.getNom());
            pstmt.setString(2, terrain.getDescription());
            pstmt.setString(3, terrain.getType().name());
            pstmt.setInt(4, terrain.getCapacite());
            pstmt.setBoolean(5, terrain.isEstCouverte());
            pstmt.setBoolean(6, terrain.isEstDisponible());
            pstmt.setInt(7, terrain.getId());

            pstmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM terrains WHERE id_terrain = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    /**
     * Méthode utilitaire pour mapper un ResultSet à un objet Terrain
     */
    private Terrain mapResultSetToTerrain(ResultSet rs) throws SQLException {
        Terrain terrain = new Terrain();

        terrain.setId(rs.getInt("id_terrain"));
        terrain.setNom(rs.getString("nom"));
        terrain.setDescription(rs.getString("description"));

        // Conversion du type de terrain
        terrain.setType(Terrain.TypeTerrain.valueOf(rs.getString("type")));

        terrain.setCapacite(rs.getInt("capacite"));
        terrain.setEstCouverte(rs.getBoolean("est_couverte"));
        terrain.setEstDisponible(rs.getBoolean("est_disponible"));

        return terrain;
    }

    /**
     * Recherche des terrains par type
     * @param type Le type de terrain à rechercher
     * @return Liste des terrains du type spécifié
     * @throws SQLException en cas d'erreur de requête
     */
    public List<Terrain> getTerrainsByType(Terrain.TypeTerrain type) throws SQLException {
        List<Terrain> terrains = new ArrayList<>();
        String sql = "SELECT * FROM terrains WHERE type = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, type.name());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    terrains.add(mapResultSetToTerrain(rs));
                }
            }
        }

        return terrains;
    }
    public Terrain getByName(String name) throws SQLException {
        String sql = "SELECT * FROM terrains WHERE nom = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTerrain(rs);
                }
            }
        }
        return null;
    }
    /**
     * Recherche des terrains disponibles avec une capacité minimale
     * @param capaciteMinimale Capacité minimale requise
     * @return Liste des terrains disponibles et suffisamment grands
     * @throws SQLException en cas d'erreur de requête
     */
    public List<Terrain> getTerrainsDisponibles(int capaciteMinimale) throws SQLException {
        List<Terrain> terrains = new ArrayList<>();
        String sql = "SELECT * FROM terrains WHERE est_disponible = true AND capacite >= ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, capaciteMinimale);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    terrains.add(mapResultSetToTerrain(rs));
                }
            }
        }

        return terrains;
    }

    /**
     * Recherche des terrains par type et statut de couverture
     * @param type Type de terrain
     * @param estCouverte Indique si le terrain doit être couvert ou non
     * @return Liste des terrains correspondant aux critères
     * @throws SQLException en cas d'erreur de requête
     */
    public List<Terrain> getTerrainsByTypeEtCouverture(
            Terrain.TypeTerrain type, boolean estCouverte) throws SQLException {
        List<Terrain> terrains = new ArrayList<>();
        String sql = "SELECT * FROM terrains WHERE type = ? AND est_couverte = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, type.name());
            pstmt.setBoolean(2, estCouverte);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    terrains.add(mapResultSetToTerrain(rs));
                }
            }
        }

        return terrains;
    }
}