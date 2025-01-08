package com.example.javarendu.dao;

import com.example.javarendu.model.Reservation;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO implements GenericDAO<Reservation> {
    private Connection connection;

    public ReservationDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void add(Reservation reservation) throws SQLException {
        // Check for conflicts before adding
        if (existeConflit(reservation)) {
            throw new SQLException("La salle est déjà réservée pour cette période");
        }

        String sql = "INSERT INTO reservations " +
                "(id_user, id_event, id_salle, id_terrain, date_reservation, date_reservation_end) " + // Added date_reservation_end
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, reservation.getId_user());
            pstmt.setInt(2, reservation.getId_event());
            pstmt.setInt(3, reservation.getId_salle());
            pstmt.setInt(4, reservation.getId_terrain());
            pstmt.setTimestamp(5, Timestamp.valueOf(reservation.getDate_reservation()));  // Converted LocalDateTime to Timestamp
            pstmt.setTimestamp(6, Timestamp.valueOf(reservation.getDate_reservation_end())); // Set the end date

            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    reservation.setId_reservation(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public Reservation get(int id) throws SQLException {
        String sql = "SELECT * FROM reservations WHERE id_reservation = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToReservation(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Reservation> getAll() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservations";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
        }

        return reservations;
    }

    @Override
    public void update(Reservation reservation) throws SQLException {
        // Check for conflicts before updating
        if (existeConflit(reservation)) {
            throw new SQLException("La salle est déjà réservée pour cette période");
        }

        String sql = "UPDATE reservations SET " +
                "id_user = ?, id_event = ?, id_salle = ?, id_terrain = ?, " +
                "date_reservation = ?, date_reservation_end = ? " +  // Updated to include date_reservation_end
                "WHERE id_reservation = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, reservation.getId_user());
            pstmt.setInt(2, reservation.getId_event());
            pstmt.setInt(3, reservation.getId_salle());
            pstmt.setInt(4, reservation.getId_terrain());
            pstmt.setTimestamp(5, Timestamp.valueOf(reservation.getDate_reservation()));  // Converted LocalDateTime to Timestamp
            pstmt.setTimestamp(6, Timestamp.valueOf(reservation.getDate_reservation_end()));  // Converted LocalDateTime to Timestamp
            pstmt.setInt(7, reservation.getId_reservation());

            pstmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM reservations WHERE id_reservation = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    private Reservation mapResultSetToReservation(ResultSet rs) throws SQLException {
        Reservation reservation = new Reservation(LocalDateTime.now(), LocalDateTime.now()); // Updated to include end date

        reservation.setId_reservation(rs.getInt("id_reservation"));
        reservation.setId_user(rs.getInt("id_user"));
        reservation.setId_event(rs.getInt("id_event"));
        reservation.setId_salle(rs.getInt("id_salle"));
        reservation.setId_terrain(rs.getInt("id_terrain"));

        Timestamp dateReservationTimestamp = rs.getTimestamp("date_reservation");
        reservation.setDate_reservation(dateReservationTimestamp.toLocalDateTime());  // Convert Timestamp to LocalDateTime

        Timestamp dateReservationEndTimestamp = rs.getTimestamp("date_reservation_end"); // Added end date
        reservation.setDate_reservation_end(dateReservationEndTimestamp.toLocalDateTime()); // Convert Timestamp to LocalDateTime

        return reservation;
    }

    public boolean existeConflit(Reservation reservation) throws SQLException {
        String sql = "SELECT COUNT(*) FROM reservations " +
                "WHERE id_salle = ? " +
                "AND ((date_reservation < ? AND date_reservation_end > ?) " +  // Overlaps at the start
                "OR (date_reservation >= ? AND date_reservation < ?)) " +     // Overlaps in the middle
                "AND id_reservation != ?"; // Exclude the current reservation ID

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, reservation.getId_salle());
            pstmt.setTimestamp(2, Timestamp.valueOf(reservation.getDate_reservation_end()));  // End of the new reservation
            pstmt.setTimestamp(3, Timestamp.valueOf(reservation.getDate_reservation()));        // Start of the new reservation
            pstmt.setTimestamp(4, Timestamp.valueOf(reservation.getDate_reservation()));        // Start of the new reservation
            pstmt.setTimestamp(5, Timestamp.valueOf(reservation.getDate_reservation_end()));  // End of the new reservation
            pstmt.setInt(6, reservation.getId_reservation()); // Exclude current reservation from the conflict check

            try (ResultSet rs = pstmt.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }
}
