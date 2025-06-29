package it.polimi.tiw.controllers;

import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;



import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;

@WebServlet("/Login")
public class Login extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Connessione al tuo DB
    private final String DB_URL = "jdbc:mysql://localhost:3306/registro?serverTimezone=UTC";
    private final String DB_USER = "root";
    private final String DB_PASS = "Ennio15anita22!"; // inserisci la tua password se c'è


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                String sql = "SELECT id_utente FROM utente WHERE email = ? AND password = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, email);
                    stmt.setString(2, password);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            int idUtente = rs.getInt("id_utente");
                            String ruolo = null;

                            // Controlla se è studente
                            try (PreparedStatement checkStud = conn.prepareStatement(
                                    "SELECT * FROM studente WHERE id_studente = ?")) {
                                checkStud.setInt(1, idUtente);
                                try (ResultSet rsStud = checkStud.executeQuery()) {
                                    if (rsStud.next()) {
                                        ruolo = "studente";
                                    }
                                }
                            }

                            // Altrimenti controlla se è docente
                            if (ruolo == null) {
                                try (PreparedStatement checkDoc = conn.prepareStatement(
                                        "SELECT * FROM docente WHERE id_docente = ?")) {
                                    checkDoc.setInt(1, idUtente);
                                    try (ResultSet rsDoc = checkDoc.executeQuery()) {
                                        if (rsDoc.next()) {
                                            ruolo = "docente";
                                        }
                                    }
                                }
                            }

                            if (ruolo != null) {
                                HttpSession session = request.getSession();
                                session.setAttribute("id_utente", idUtente);
                                session.setAttribute("ruolo", ruolo);

                                if (ruolo.equals("studente")) {
                                    response.sendRedirect("HomeStudente");
                                } else {
                                    response.sendRedirect("HomeDocente");
                                }
                                return;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Se arrivi qui -> login fallito
        response.sendRedirect("index.html"); // puoi anche aggiungere un errore in query string
    }
    
}

