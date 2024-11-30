package servlets;

import datasets.UData;
import service.AccService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SingUpServlet extends HttpServlet {
    private final AccService accountService;

    public SingUpServlet(AccService accountService) {
        this.accountService = accountService;
    }

    //sign up
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String pass = request.getParameter("password");

        if (login == null || pass == null) {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        final UData user = new UData(login, pass);
        accountService.addNewUser(user);
    }
}
