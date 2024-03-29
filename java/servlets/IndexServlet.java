package servlets;

import model.User;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class IndexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<User> users = UserService.getInstance().getAllUsers();
            req.setAttribute("users", users);
            getServletContext().getRequestDispatcher("/index.jsp").forward(req,resp);

        } catch (SQLException e) {
            resp.setStatus(501);
        }
    }
}
