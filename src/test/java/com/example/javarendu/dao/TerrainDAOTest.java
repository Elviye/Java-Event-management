package com.example.javarendu.dao;

import com.example.javarendu.model.Terrain;
import com.example.javarendu.model.Terrain.TypeTerrain;
import com.example.javarendu.utils.DBconnection;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TerrainDAOTest {

    private TerrainDAO terrainDAO;
    private Connection connection;

    @BeforeAll
    public void setUp() {
        try {
            connection = DBconnection.getConn();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        terrainDAO = new TerrainDAO(connection);
    }

    @Test
    public void testAddAndGetTerrain() throws SQLException {
        Terrain terrain = new Terrain("Stadium", "Outdoor football field", TypeTerrain.FOOTBALL, 5000, false);
        terrainDAO.add(terrain);

        Terrain retrieved = terrainDAO.get(terrain.getId());
        assertNotNull(retrieved);
        assertEquals("Stadium", retrieved.getNom());
        assertEquals(TypeTerrain.FOOTBALL, retrieved.getType());
    }

    @Test
    public void testUpdateTerrain() throws SQLException {
        Terrain terrain = new Terrain("Court", "Indoor basketball court", TypeTerrain.BASKET, 300, true);
        terrainDAO.add(terrain);

        terrain.setNom("Updated Court");
        terrain.setDescription("Updated description");
        terrainDAO.update(terrain);

        Terrain updated = terrainDAO.get(terrain.getId());
        assertEquals("Updated Court", updated.getNom());
        assertEquals("Updated description", updated.getDescription());
    }

    @Test
    public void testDeleteTerrain() throws SQLException {
        Terrain terrain = new Terrain("Temporary", "Temporary use", TypeTerrain.TENNIS, 100, false);
        terrainDAO.add(terrain);

        int id = terrain.getId();
        terrainDAO.delete(id);

        assertNull(terrainDAO.get(id));
    }

    @Test
    public void testGetAllTerrains() throws SQLException {
        Terrain terrain1 = new Terrain("Field1", "Description1", TypeTerrain.FOOTBALL, 2000, false);
        Terrain terrain2 = new Terrain("Field2", "Description2", TypeTerrain.VOLLEYBALL, 1500, true);
        terrainDAO.add(terrain1);
        terrainDAO.add(terrain2);

        List<Terrain> terrains = terrainDAO.getAll();
        assertTrue(terrains.size() >= 2);
    }

    @Test
    public void testGetTerrainsByType() throws SQLException {
        Terrain terrain = new Terrain("Basket Court", "Basketball terrain", TypeTerrain.BASKET, 400, true);
        terrainDAO.add(terrain);

        List<Terrain> terrains = terrainDAO.getTerrainsByType(TypeTerrain.BASKET);
        assertTrue(terrains.stream().anyMatch(t -> t.getNom().equals("Basket Court")));
    }

    @Test
    public void testGetTerrainsDisponibles() throws SQLException {
        Terrain terrain = new Terrain("Available Field", "Available for use", TypeTerrain.POLYVALENT, 500, false);
        terrain.setEstDisponible(true);
        terrainDAO.add(terrain);

        List<Terrain> terrains = terrainDAO.getTerrainsDisponibles(100);
        assertTrue(terrains.stream().anyMatch(t -> t.getNom().equals("Available Field")));
    }

    @AfterAll
    public void tearDown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
