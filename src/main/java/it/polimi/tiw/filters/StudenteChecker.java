package it.polimi.tiw.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Filter per l'autorizzazione degli studenti
 * Controlla che l'utente autenticato sia uno studente prima di accedere alle pagine degli studenti
 */
@WebFilter({"/home-studente", "/esito"})
public class StudentAuthorizationFilter implements Filter {

    /**
     * Default constructor.
     */
    public StudentAuthorizationFilter() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see Filter#destroy()
     */
    public void destroy() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        System.out.println("Student authorization filter executing...");

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        
        // Controlla se l'utente è autenticato
        if (session == null || session.getAttribute("user") == null) {
            System.out.println("User not authenticated, redirecting to login");
            res.sendRedirect(req.getContextPath() + "/index.html");
            return;
        }
        
        // Controlla se l'utente è uno studente
        String userType = (String) session.getAttribute("userType");
        if (userType == null || !"STUDENTE".equals(userType)) {
            System.out.println("User is not a student, access denied");
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Accesso negato. Solo gli studenti possono accedere a questa pagina.");
            return;
        }
        
        // Utente è uno studente autenticato, continua con la richiesta
        System.out.println("Student authorized: " + session.getAttribute("user"));
        chain.doFilter(request, response);
    }

    /**
     * @see Filter#init(FilterConfig)
     */
    public void init(FilterConfig fConfig) throws ServletException {
        // TODO Auto-generated constructor stub
    }
} 