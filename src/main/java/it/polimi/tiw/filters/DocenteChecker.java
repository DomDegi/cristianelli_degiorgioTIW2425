package it.polimi.tiw.filters;

import java.io.IOException;

import it.polimi.tiw.beans.UtenteBean;
import it.polimi.tiw.beans.DocenteBean;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class DocenteChecker implements Filter {

    public DocenteChecker() {}

    public void destroy() {}

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        if (session == null) {
            res.sendRedirect(req.getContextPath() + "/index.html");
            return;
        }

        UtenteBean utente = (UtenteBean) session.getAttribute("utente");
        if (utente == null) {
            res.sendRedirect(req.getContextPath() + "/index.html");
            return;
        }
        if (!"docente".equals(utente.getRuolo())) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("Utente non autorizzato");
            return;
        }
        System.out.println("Docente autenticato: " + utente.getEmail());
        chain.doFilter(request, response);
    }

    public void init(FilterConfig fConfig) throws ServletException {}
} 