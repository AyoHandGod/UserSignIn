package network;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.util.Date;

/**
 * Application Server that handles client requests and responds to Spotify.
 * @author Dante Anthony
 */
@WebServlet("/home")
public class AppServer extends HttpServlet {

    private String connstr;

    public void init() {

    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // process the request
        String strHost = req.getHeader("Host");
        String strContentType = req.getContentType();
        String queryParameter = req.getParameter("code");

        // generate the response
        resp.setContentType("text/html");
        resp.setHeader("X-Custom-Header", String.valueOf(new Date()));
        // The next command is performing a redirect to the /otherAddress context (doesn't include the rest of the host)
        // resp.sendRedirect("/otherAddress");
        resp.getWriter().write(queryParameter);
    }
}
