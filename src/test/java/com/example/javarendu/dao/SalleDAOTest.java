package com.example.javarendu.dao;

import com.example.javarendu.utils.DBconnection;
import com.example.javarendu.model.Salle;
import org.junit.jupiter.api.*;
import java.sql.SQLException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SalleDAOTest {

    private static SalleDAO salleDAO;

    @BeforeAll
    public static void setUp() {
        salleDAO = new SalleDAO();
    }

    @Test
    @Order(1)
    public void testAddSalle() throws SQLException {
        Salle salle = new Salle("Salle A", "Salle Amphitheatre", 100, Salle.TypeSalle.AMPHI);
        salleDAO.add(salle);
        assertTrue(salle.getId() > 0, "Salle should be added and have a valid ID.");
    }

    @Test
    @Order(2)
    public void testGetSalle() throws SQLException {
        Salle salle = salleDAO.get(1);
        assertNotNull(salle, "Salle with ID 1 should exist.");
    }

    @Test
    @Order(3)
    public void testGetAllSalles() throws SQLException {
        List<Salle> salles = salleDAO.getAll();
        assertFalse(salles.isEmpty(), "Salles list should not be empty.");
    }

    @Test
    @Order(4)
    public void testUpdateSalle() throws SQLException {
        Salle salle = salleDAO.get(1);
        salle.setNom("Updated Salle A");
        salleDAO.update(salle);
        Salle updatedSalle = salleDAO.get(1);
        assertEquals("Updated Salle A", updatedSalle.getNom(), "Salle name should be updated.");
    }

    @Test
    @Order(5)
    public void testDeleteSalle() throws SQLException {
        salleDAO.delete(1);
        assertNull(salleDAO.get(1), "Salle with ID 1 should be deleted.");
    }
}
