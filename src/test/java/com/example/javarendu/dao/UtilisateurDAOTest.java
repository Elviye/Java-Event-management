package com.example.javarendu.dao;

import com.example.javarendu.model.Utilisateur;
import com.example.javarendu.utils.DBconnection;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UtilisateurDAOTest {

    private static UtilisateurDAO utilisateurDAO;

    @BeforeAll
    public static void setUp() {
        utilisateurDAO = new UtilisateurDAO();
    }

    @Test
    @Order(1)
    public void testAddUtilisateur() {
        Utilisateur utilisateur = new Utilisateur("Ahmed", "tobi", "ahmed@example.com", "ETUDIANT");
        utilisateurDAO.add(utilisateur);
        Utilisateur fetchedUtilisateur = utilisateurDAO.get(utilisateur.getIdUtilisateur());
        assertNotNull(fetchedUtilisateur, "Utilisateur should be found after being added");
        assertEquals("Ahmed", fetchedUtilisateur.getNom(), "The name should match the added utilisateur");
    }

    @Test
    @Order(2)
    public void testGetAllUtilisateurs() {
        List<Utilisateur> utilisateurs = utilisateurDAO.getAll();
        assertTrue(utilisateurs.size() > 0, "There should be at least one utilisateur in the database");
    }

    @Test
    @Order(3)
    public void testDeleteUtilisateur() {
        Utilisateur utilisateur = new Utilisateur("Alice", "Smith", "alice@example.com", "ETUDIANT");
        utilisateurDAO.add(utilisateur);
        int id = utilisateur.getIdUtilisateur();
        utilisateurDAO.delete(id);
        assertNull(utilisateurDAO.get(id), "Utilisateur should be null after deletion");
    }
}
