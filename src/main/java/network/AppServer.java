package network;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.SavedAlbum;
import com.wrapper.spotify.model_objects.specification.User;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.data.library.GetCurrentUsersSavedAlbumsRequest;
import com.wrapper.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;
import org.apache.hc.core5.http.ParseException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Application Server that handles client requests and responds to Spotify.
 * @author Dante Anthony
 */
@WebServlet("/home")
public class AppServer extends HttpServlet {

    private final ApiConnector apiConnector = new ApiConnectorImpl();
    private final Logger logger = Logger.getLogger(AppServer.class.getName());
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // process the request
        String strHost = req.getHeader("Host");
        String strContentType = req.getContentType();
        String queryParameter = req.getParameter("code");

        PrintWriter writer = resp.getWriter();

        // generate the response
        resp.setContentType("text/html");
        resp.setHeader("X-Custom-Header", String.valueOf(new Date()));
        // The next command is performing a redirect to the /otherAddress context (doesn't include the rest of the host)
        // resp.sendRedirect("/otherAddress");

        // Get the code query parameter and uses it to create Access Token and Refresh token
        apiConnector.setAuthorizationCredentials(queryParameter);
        // End access token process

        // test out getting users Data
        User user = null;
        try {
            user = apiConnector.getUserData();
        } catch (SpotifyWebApiException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        writer.write(user.getDisplayName() + "\n");

        // test out getting users Saved Albums
        try {
            for (SavedAlbum savedAlbum: apiConnector.getUsersSavedAlbums(10, 0)) {
                writer.write(savedAlbum.getAlbum().getName());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SpotifyWebApiException e) {
            e.printStackTrace();
        }
    }

    // TODO: Add a Timer to manager Refreshing Access token


    public static void main(String[] args) {
        //This process builds the link we need to grant authorization to the app, and returns it in the console.
        // Meanwhile, the servlet that handles the call is currently printing the returned code to the page.
        // Working at this point.
        ApiConnector apiConnector = new ApiConnectorImpl();

        AuthorizationCodeUriRequest authCodeUriRequest = apiConnector.getSpotifyApi().authorizationCodeUri()
                .scope("user-library-read").build();
        URI uri = authCodeUriRequest.execute();
        System.out.println("URI: " + uri.toString());
        // End Auth Code Access Process

    }
}
