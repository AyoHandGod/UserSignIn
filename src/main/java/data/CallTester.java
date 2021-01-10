package data;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
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

        AuthorizationCodeUriRequest authCodeUriRequest = spotifyApi.authorizationCodeUri().build();
        URI uri = authCodeUriRequest.execute();
        String accessToken = spotifyApi.getAccessToken();
        System.out.println("URI: " + uri.toString());
        // End Auth Code Access Process

        // We use the Auth Code that we retrieved previously in order to obtain the access and refresh
        // tokens.
        // Start Access and Refresh Token Process
        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi
                .authorizationCode("AQDN-fmL3IUcOw-9ap0Z1kLblz_UMvqjrpVCFr1Nrc81ivZXzRVbtHibjv89KTwkydFYb" +
                        "0hQDOa-CrcC_1yJSKGnxLLtyqsYQZvLc88cvbIv7DhJj5eLe5w_l2yGx6VI6cAeB5eJUigoBi9S8UI5" +
                        "AuQABRZqtHdHXFMVSMasED_c_M2FeVeP1XwrAR0hLeqHNP2qMYaF").build();

        /**
        try {
            AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

            System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        // End Access and Refresh Token Process */
    }
}
