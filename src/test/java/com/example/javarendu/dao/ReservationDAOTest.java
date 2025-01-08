package com.example.javarendu.dao;

import com.example.javarendu.model.Reservation;
import org.junit.jupiter.api.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReservationDAOTest {

    private static Connection connection;
    private static ReservationDAO reservationDAO;

    @BeforeAll
    static void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/RenduDB", "postgres", "anass123");
        reservationDAO = new ReservationDAO(connection);

        // Create the table with the date_reservation_end field
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS reservations (
                id_reservation SERIAL PRIMARY KEY,
                id_user INT NOT NULL,
                id_event INT NOT NULL,
                id_salle INT NOT NULL,
                id_terrain INT NOT NULL,
                date_reservation TIMESTAMP NOT NULL,
                date_reservation_end TIMESTAMP NOT NULL
            )
        """;
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        }
    }

    @AfterAll
    static void tearDown() throws SQLException {
        String dropTableSQL = "DROP TABLE IF EXISTS reservations";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(dropTableSQL);
        }
        connection.close();
    }

    @Test
    @Order(1)
    void testAddReservation() throws SQLException {
        // Reservation with start and end times
        Reservation reservation = new Reservation(LocalDateTime.of(2025, 1, 8, 10, 30), LocalDateTime.of(2025, 1, 8, 12, 30));
        reservation.setId_user(1);
        reservation.setId_event(1);
        reservation.setId_salle(1);
        reservation.setId_terrain(1);

        reservationDAO.add(reservation);
        assertTrue(reservation.getId_reservation() > 0, "Reservation ID should be generated and greater than 0");
    }

    @Test
    @Order(2)
    void testGetReservation() throws SQLException {
        Reservation reservation = reservationDAO.get(1);
        assertNotNull(reservation, "Reservation should be found");
        assertEquals(1, reservation.getId_user(), "User ID should be 1");
    }

    @Test
    @Order(3)
    void testGetAllReservations() throws SQLException {
        List<Reservation> reservations = reservationDAO.getAll();
        assertFalse(reservations.isEmpty(), "Reservations list should not be empty");
    }

    @Test
    @Order(4)
    void testUpdateReservation() throws SQLException {
        Reservation reservation = reservationDAO.get(1);
        assertNotNull(reservation, "Reservation should be found before updating");

        // Updating with new start and end times
        reservation.setId_salle(2);
        reservation.setDate_reservation(LocalDateTime.of(2025, 1, 9, 10, 0));
        reservation.setDate_reservation_end(LocalDateTime.of(2025, 1, 9, 12, 0));

        reservationDAO.update(reservation);

        Reservation updatedReservation = reservationDAO.get(1);
        assertEquals(2, updatedReservation.getId_salle(), "Salle ID should be updated to 2");
        assertEquals(LocalDateTime.of(2025, 1, 9, 10, 0), updatedReservation.getDate_reservation(), "Start date should be updated");
    }

    @Test
    @Order(5)
    void testExisteConflit() throws SQLException {
        // Inserting a reservation to create conflict
        Reservation reservation = new Reservation(LocalDateTime.of(2025, 1, 8, 10, 30), LocalDateTime.of(2025, 1, 8, 12, 30));
        reservation.setId_user(1);
        reservation.setId_event(1);
        reservation.setId_salle(1); // Same salle, same time as existing reservation
        reservation.setId_terrain(1);
        reservationDAO.add(reservation);

        // Trying to create a conflicting reservation (same salle and overlapping time)
        Reservation conflictingReservation = new Reservation(LocalDateTime.of(2025, 1, 8, 11, 0), LocalDateTime.of(2025, 1, 8, 13, 0));
        conflictingReservation.setId_user(2);
        conflictingReservation.setId_event(2);
        conflictingReservation.setId_salle(1); // Same salle, conflicting time
        conflictingReservation.setId_terrain(2);

        boolean conflictExists = reservationDAO.existeConflit(conflictingReservation);
        assertTrue(conflictExists, "Conflict should be detected for the same salle and overlapping time");
    }

    @Test
    @Order(6)
    void testDeleteReservation() throws SQLException {
        reservationDAO.delete(1);
        Reservation deletedReservation = reservationDAO.get(1);
        assertNull(deletedReservation, "Reservation should be null after deletion");
    }
}
