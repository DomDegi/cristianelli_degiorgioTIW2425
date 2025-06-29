package it.polimi.tiw.controllers;
import java.util.Map;
import java.util.HashMap;

import it.polimi.tiw.beans.Corso;
import it.polimi.tiw.beans.Appello;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@WebServlet("/HomeStudente")
public class HomeStudente extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final String DB_URL = "jdbc:mysql://localhost:3306/registro?serverTimezone=UTC";
    private final String DB_USER = "root";
    private final String DB_PASS = "Ennio15anita22!";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        System.out.println("sono qui!");
        if (session == null || session.getAttribute("id_utente") == null) {
            response.sendRedirect("index.html");
            return;
        }

        int idStudente = (int) session.getAttribute("id_utente");

        List<Corso> corsi = new ArrayList<>();
        Map<Integer, List<Appello>> appelliPerCorso = new HashMap<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                // 1. Recupero corsi
                String query = """
                    SELECT c.id_corso, c.nome, c.cfu
                    FROM corso c
                    JOIN iscrizione_corso ic ON c.id_corso = ic.id_corso
                    WHERE ic.id_studente = ?
                """;

                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setInt(1, idStudente);
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            Corso corso = new Corso(rs.getInt("id_corso"), rs.getString("nome"), rs.getInt("cfu"));
                            corsi.add(corso);
                            appelliPerCorso.put(corso.id_corso, new ArrayList<>());
                        }
                    }
                }

                // 2. Recupero appelli
                String sqlAppelli = "SELECT id_appello, data FROM appello WHERE id_corso = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sqlAppelli)) {
                    for (Corso corso : corsi) {
                        stmt.setInt(1, corso.id_corso);
                        try (ResultSet rs = stmt.executeQuery()) {
                            while (rs.next()) {
                                Appello appello = new Appello(rs.getInt("id_appello"), rs.getDate("data"));
                                appelliPerCorso.get(corso.id_corso).add(appello);
                            }
                        }
                    }
                }

                // ✅ STAMPA DI DEBUG
                System.out.println("CORSI E APPELLI RECUPERATI:");
                for (Corso corso : corsi) {
                    System.out.println("Corso: " + corso.nome + " (" + corso.id_corso + ")");
                    List<Appello> appelli = appelliPerCorso.get(corso.id_corso);
                    if (appelli != null) {
                        for (Appello a : appelli) {
                            System.out.println("    Appello: " + a.data);
                        }
                    }
                }

                ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
                templateResolver.setPrefix("/WEB-INF/");  // o "/templates/" se stai usando questa struttura
                templateResolver.setSuffix(".html");
                templateResolver.setTemplateMode("HTML");
                templateResolver.setCharacterEncoding("UTF-8");
                TemplateEngine engine = new TemplateEngine();
                engine.setTemplateResolver(templateResolver);

                WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
                ctx.setVariable("corsi", corsi);
                ctx.setVariable("appelliPerCorso", appelliPerCorso);

                engine.process("homeStudente", ctx, response.getWriter());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

    /*
    public List<Appello> getAppelliStudente(int idStudente, Connection conn) throws SQLException {
        List<Appello> appelli = new ArrayList<>();

        String query = "SELECT a.id_appello, a.data " +
                       "FROM appello a " +
                       "JOIN iscrizione_corso ic ON a.id_corso = ic.id_corso " +
                       "WHERE ic.id_studente = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idStudente);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id_appello");
                    Date data = rs.getDate("data");
                    appelli.add(new Appello(id, data));
                }
            }
        }

        return appelli;
    }*/

