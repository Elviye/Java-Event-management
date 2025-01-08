package com.example.javarendu.dao;

import com.example.javarendu.model.Evenement;
import com.example.javarendu.model.Evenement.TypeEvenement;
import com.example.javarendu.utils.DBconnection;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EvenementDAOTest {

    private Connection connection;
    private EvenementDAO evenementDAO;

    @BeforeAll
    public void setup() throws SQLException {
        connection = DBconnection.getConn();
        evenementDAO = new EvenementDAO();
    }

    @Test
    public void testAddAndGet() throws SQLException {
        // Create a new event
        Evenement evenement = new Evenement();
        evenement.setIdUtilisateur(1);
        evenement.setNom("Test Event");
        evenement.setDescription("This is a test event");
        evenement.setDateDebut(LocalDateTime.now());
        evenement.setDateFin(LocalDateTime.now().plusDays(1));
        evenement.setType(TypeEvenement.CONFERENCE);

        // Add the event to the database
        evenementDAO.add(evenement);

        // Retrieve the event by ID
        Evenement retrievedEvent = evenementDAO.get(evenement.getId());

        // Assert that the retrieved event matches the original
        assertNotNull(retrievedEvent);
        assertEquals(evenement.getNom(), retrievedEvent.getNom());
        assertEquals(evenement.getDescription(), retrievedEvent.getDescription());
    }

    @Test
    public void testGetAll() throws SQLException {
        List<Evenement> evenements = evenementDAO.getAll();
        assertNotNull(evenements);
        assertTrue(evenements.size() > 0);
    }

    @Test
    public void testUpdate() throws SQLException {
        // Create a new event
        Evenement evenement = new Evenement();
        evenement.setIdUtilisateur(1);
        evenement.setNom("Event to Update");
        evenement.setDescription("Initial description");
        evenement.setDateDebut(LocalDateTime.now());
        evenement.setDateFin(LocalDateTime.now().plusDays(1));
        evenement.setType(TypeEvenement.CONFERENCE);

        evenementDAO.add(evenement);

        // Update the event
        evenement.setNom("Updated Event");
        evenement.setDescription("Updated description");
        evenementDAO.update(evenement);

        // Retrieve the updated event
        Evenement updatedEvent = evenementDAO.get(evenement.getId());

        // Assert that the changes were saved
        assertEquals("Updated Event", updatedEvent.getNom());
        assertEquals("Updated description", updatedEvent.getDescription());
    }

    @Test
    public void testDelete() throws SQLException {
        // Create a new event
        Evenement evenement = new Evenement();
        evenement.setIdUtilisateur(1);
        evenement.setNom("Event to Delete");
        evenement.setDescription("This event will be deleted");
        evenement.setDateDebut(LocalDateTime.now());
        evenement.setDateFin(LocalDateTime.now().plusDays(1));
        evenement.setType(TypeEvenement.CONFERENCE);

        evenementDAO.add(evenement);

        // Delete the event
        evenementDAO.delete(evenement.getId());

        // Try to retrieve the deleted event
        Evenement deletedEvent = evenementDAO.get(evenement.getId());

        // Assert that the event no longer exists
        assertNull(deletedEvent);
    }

    @AfterAll
    public void tearDown() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
