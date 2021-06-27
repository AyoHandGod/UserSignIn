package network;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSessionBindingEvent;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * PartyServlet is responsible for managing party sessions. Should be where users are joined together once a party
 * is created.
 * @author Dante Anthony
 * @version 1.3.0
 */
@WebServlet("/party/*")
public class PartyServlet extends HttpServlet {

    static List<String> users = new ArrayList<>();
    HttpSessionBindingEvent sessionBindingEvent;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        sessionBindingEvent =
                new HttpSessionBindingEvent(req.getSession(), req.getSession().getId());
        PrintWriter writer = resp.getWriter();
        
        writer.println("<p>Here is the session ID: " + req.getSession().getId());
        users.add(req.getSession().getId());

        writer.println("<p> Current user count:" + users.size());

        String name = (String) req.getSession().getAttribute("userName");
        writer.println("<p>" + name + "</p>");

    }

    @Override
    public void destroy() {
        users.remove(sessionBindingEvent.getSession().getId());
    }

}
