package network;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import org.apache.hc.core5.http.ParseException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.net.URI;
import java.util.Date;

/**
 * Application Server that handles client requests and responds to Spotify.
 * @author Dante Anthony
 */
@WebServlet("/home")
public class AppServer extends HttpServlet {

    private final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId("c59275ebbf74401fa69f5936e6c43cca")
            .setClientSecret("35667913882d4a5ca3207923ddc6723c")
            .setRedirectUri(SpotifyHttpManager.makeUri("http://localhost:8080/UserSignIn_war_exploded/home"))
            .build();

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

        // Get the code query parameter and uses it to create Access Token and Refresh token
        AuthorizationCodeCredentials authorizationCodeCredentials = getAccessRefreshTokens(queryParameter);
        assert authorizationCodeCredentials != null;
        resp.getWriter().write("Expires in: " + authorizationCodeCredentials.getExpiresIn());
    }

    private AuthorizationCodeCredentials getAccessRefreshTokens(String authCode) {
        // We use the Auth Code that we retrieved previously in order to obtain the access and refresh
        // tokens.
        // Start Access and Refresh Token Process
        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi
                .authorizationCode(authCode).build();

        try {
            AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

            return authorizationCodeCredentials;

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
        // End Access and Refresh Token Process
    }

    public static void getSpotifyAuthCode() {
        //This process builds the link we need to grant authorization to the app, and returns it in the console.
        // Meanwhile, the servlet that handles the call is currently printing the returned code to the page.
        // Working at this point.
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId("c59275ebbf74401fa69f5936e6c43cca")
                .setClientSecret("35667913882d4a5ca3207923ddc6723c")
                .setRedirectUri(SpotifyHttpManager.makeUri("http://localhost:8080/UserSignIn_war_exploded/home"))
                .build();

        AuthorizationCodeUriRequest authCodeUriRequest = spotifyApi.authorizationCodeUri().build();
        URI uri = authCodeUriRequest.execute();
        String accessToken = spotifyApi.getAccessToken();
        System.out.println("URI: " + uri.toString());
        // End Auth Code Access Process
    }

    public static void main(String[] args) {
        //This process builds the link we need to grant authorization to the app, and returns it in the console.
        // Meanwhile, the servlet that handles the call is currently printing the returned code to the page.
        // Working at this point.
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId("c59275ebbf74401fa69f5936e6c43cca")
                .setClientSecret("35667913882d4a5ca3207923ddc6723c")
                .setRedirectUri(SpotifyHttpManager.makeUri("http://localhost:8080/UserSignIn_war_exploded/home"))
                .build();

        AuthorizationCodeUriRequest authCodeUriRequest = spotifyApi.authorizationCodeUri().build();
        URI uri = authCodeUriRequest.execute();
        String accessToken = spotifyApi.getAccessToken();
        System.out.println("URI: " + uri.toString());
        // End Auth Code Access Process

    }
}
