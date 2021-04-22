package network;

import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.miscellaneous.PlaylistTracksInformation;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.model_objects.specification.SavedAlbum;
import com.wrapper.spotify.model_objects.specification.User;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import org.apache.hc.core5.http.ParseException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Application Servlet that handles client requests and responds to Spotify.
 * @author Dante Anthony
 */
@WebServlet("/home")
public class AppServer extends HttpServlet {

    private final ApiConnectorInterface apiConnectorInterface = ApiConnectorInterfaceImpl.create();
    private final Logger logger = Logger.getLogger(AppServer.class.getName());
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // process the request
        String strHost = req.getHeader("Host");
        String strContentType = req.getContentType();
        String queryParameter = req.getParameter("code");
        String session = req.getSession().getId();
        String contextPath = req.getContextPath();
        String encodedUrl = resp.encodeURL(contextPath + "/Party/" + session);

        logger.log(Level.INFO, dtf.format(LocalDateTime.now()) + " Request from: " +
                strHost + " " + strContentType + ". Routing to: " + encodedUrl);

        PrintWriter writer = resp.getWriter();

        // generate the response
        resp.setContentType("text/html");
        resp.setHeader("X-Custom-Header", String.valueOf(new Date()));
        // The next command is performing a redirect to the /otherAddress context (doesn't include the rest of the host)
        // resp.sendRedirect("/otherAddress");

        // Get the code query parameter and uses it to create Access Token and Refresh token
        apiConnectorInterface.setAuthorizationCredentials(queryParameter);
        // End access token process

        // test out getting users Data
        User user = null;
        try {
            user = apiConnectorInterface.getUserData();
        } catch (SpotifyWebApiException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        assert user != null;
        writer.write("<p> " + user.getDisplayName() + "</p>\n");
        writer.write("<ul>");
        req.getSession().setAttribute("userName", user.getDisplayName());

        // test out getting users Saved Albums and playlists from user
        try(PrintWriter filewriter = new PrintWriter(
                new BufferedWriter(new FileWriter(System.getProperty("user.dir") + "TextFile.txt")))) {
            for (SavedAlbum savedAlbum: apiConnectorInterface.getUsersSavedAlbums(10, 0)) {
                writer.write("<li>" + savedAlbum.getAlbum().getName() + "</li>");
            }
            writer.write("</ul>");
            writer.write("<p>" + session + "</p>");
            writer.write("<a href=party/" + session + ">link to session party</a>");

            writer.write("<h3>User Data</h3>");
            writer.write("<ul1>");
            writer.write("<li>Birthday: " + user.getBirthdate() +  "</li>");
            writer.write("<li>Display name: " + user.getDisplayName() + "</li>");
            writer.write("<li> User email:" + user.getEmail() + "</li>");
            writer.write("<li> :" + user.getHref() + "<li>");
            writer.write("</ul>");

            writer.write("<ul>");
            for(PlaylistSimplified playlistSimplified: apiConnectorInterface.getUsersPlaylists(10, 0)) {
                writer.write("<li>" + playlistSimplified.getExternalUrls() + "</li>");
                PlaylistTracksInformation plistInfo = playlistSimplified.getTracks();
                writer.write(playlistSimplified.getTracks().toString());
            }
            writer.write("</ul>");



        } catch (ParseException | SpotifyWebApiException | IOException e) {
            e.printStackTrace();
        }
        // req.getRequestDispatcher("party/" + session).forward(req, resp);
    }

    // TODO: Add a Timer to manager Refreshing Access token


    public static void main(String[] args) {
        //This process builds the link we need to grant authorization to the app, and returns it in the console.
        // Meanwhile, the servlet that handles the call is currently printing the returned code to the page.
        // Working at this point.
        ApiConnectorInterface apiConnectorInterface = ApiConnectorInterfaceImpl.create();

        AuthorizationCodeUriRequest authCodeUriRequest = apiConnectorInterface.getSpotifyApi().authorizationCodeUri()
                .scope("user-library-read user-read-private").build();
        URI uri = authCodeUriRequest.execute();
        System.out.println("URI: " + uri.toString());
        // End Auth Code Access Process

    }
}
