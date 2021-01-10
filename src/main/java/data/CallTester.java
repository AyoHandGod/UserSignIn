package data;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Basic class for testing out spotify API calls and getting an idea of what we'll need to create the spotify app.
 * @author Dante Anthony
 * @version 1.0
 */
public class CallTester {
    public static void main(String[] args) throws URISyntaxException {

        //This process builds the link we need to grant authorization to the app, and returns it in the console.
        // Meanwhile, the servlet that handles the call is currently printing the returned code to the page.
        // Working at this point.
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId("c59275ebbf74401fa69f5936e6c43cca")
                .setClientSecret("35667913882d4a5ca3207923ddc6723c")
                .setRedirectUri(SpotifyHttpManager.makeUri("http://localhost:8080/UserSignIn_war_exploded/home"))
                .build();

        AuthorizationCodeUriRequest authCodeRequest = spotifyApi.authorizationCodeUri().build();
        URI uri = authCodeRequest.execute();
        String accessToken = spotifyApi.getAccessToken();
        System.out.println("URI: " + uri.toString());
    }
}