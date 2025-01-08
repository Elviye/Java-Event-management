package com.example.javarendu.model;

import java.time.LocalDateTime;

public class Reservation {
    private int id_reservation;
    private int id_user;
    private int id_event;
    private int id_salle;
    private int id_terrain;
    private LocalDateTime date_reservation;  // Updated to LocalDateTime
    private LocalDateTime date_reservation_end; // Added field for end date

    // Constructor
    public Reservation(LocalDateTime date_reservation, LocalDateTime date_reservation_end) {
        this.date_reservation = date_reservation;
        this.date_reservation_end = date_reservation_end;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id_reservation=" + id_reservation +
                ", id_user=" + id_user +
                ", id_event=" + id_event +
                ", id_salle=" + id_salle +
                ", id_terrain=" + id_terrain +
                ", date_reservation=" + date_reservation +
                ", date_reservation_end=" + date_reservation_end + // Add end time to string
                '}';
    }

    // Getters and setters
    public int getId_reservation() {
        return id_reservation;
    }

    public void setId_reservation(int id_reservation) {
        this.id_reservation = id_reservation;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getId_event() {
        return id_event;
    }

    public void setId_event(int id_event) {
        this.id_event = id_event;
    }

    public int getId_salle() {
        return id_salle;
    }

    public void setId_salle(int id_salle) {
        this.id_salle = id_salle;
    }

    public int getId_terrain() {
        return id_terrain;
    }

    public void setId_terrain(int id_terrain) {
        this.id_terrain = id_terrain;
    }

    public LocalDateTime getDate_reservation() {
        return date_reservation;
    }

    public void setDate_reservation(LocalDateTime date_reservation) {
        this.date_reservation = date_reservation;
    }

    public LocalDateTime getDate_reservation_end() {
        return date_reservation_end;
    }

    public void setDate_reservation_end(LocalDateTime date_reservation_end) {
        this.date_reservation_end = date_reservation_end;
    }
}
