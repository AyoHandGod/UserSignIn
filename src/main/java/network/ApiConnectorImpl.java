package network;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.SavedAlbum;
import com.wrapper.spotify.model_objects.specification.User;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.data.library.GetCurrentUsersSavedAlbumsRequest;
import com.wrapper.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApiConnectorImpl implements ApiConnector {

    private final Logger logger = Logger.getLogger(AppServer.class.getName());
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    private SpotifyApi spotifyApi;
    private AuthorizationCodeCredentials authorizationCodeCredentials;

    public ApiConnectorImpl() {
        try (InputStream inputStream = ApiConnector.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            FileHandler fileHandler = new FileHandler("ApiConnectorLog.log");
            logger.addHandler(fileHandler);

            Properties prop = new Properties();

            if (inputStream == null) {
                System.out.println("Unable to locate the config file");
            }

            prop.load(inputStream);
            this.spotifyApi = SpotifyApi.builder()
                    .setRedirectUri(SpotifyHttpManager.makeUri(prop.getProperty("redirect.URI")))
                    .setClientSecret(prop.getProperty("client.secret"))
                    .setClientId(prop.getProperty("client.id"))
                    .build();

        } catch (IOException ex) {
            logger.log(Level.SEVERE, ex.getMessage() + " failed at: " + dtf.format(LocalTime.now()));
        }
    }

    @Override
    public void setAuthorizationCredentials(String code) {
        // Get the code query parameter and uses it to create Access Token and Refresh token
        logger.log(Level.INFO, "Getting Authorization Credentials with provided code");
        AuthorizationCodeCredentials authorizationCodeCredentials = null;
        try {
            authorizationCodeCredentials = getAccessRefreshTokens(code);
        } catch (ParseException | SpotifyWebApiException | IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        assert authorizationCodeCredentials != null;

        this.spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
        this.spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
        // End access token process

    }

    /**
     * Generate the Authorization Code Credentials using the Auth Code
     * @param authCode String value code provided in Spotify request as query parameter
     * @return AuthorizationCodeCredentials object if code valid, null otherwise.
     */
    private AuthorizationCodeCredentials getAccessRefreshTokens(String authCode)
            throws ParseException, SpotifyWebApiException, IOException {
        // We use the Auth Code that we retrieved previously in order to obtain the access and refresh
        // tokens.
        // Start Access and Refresh Token Process
        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi
                .authorizationCode(authCode).build();

        this.authorizationCodeCredentials = authorizationCodeRequest.execute();
        spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
        spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

        logger.log(Level.CONFIG, "Auth Code Expiration time: " + authorizationCodeCredentials.getExpiresIn());

        return authorizationCodeCredentials;
        // End Access and Refresh Token Process
    }

    @Override
    public User getUserData() throws SpotifyWebApiException, IOException, ParseException {
        GetCurrentUsersProfileRequest getCurrentUsersProfileRequest = spotifyApi.getCurrentUsersProfile().build();
        return getCurrentUsersProfileRequest.execute();
    }

    @Override
    public ArrayList<SavedAlbum> getUsersSavedAlbums(int limit, int offset) throws ParseException, SpotifyWebApiException, IOException {
        logger.log(Level.INFO, "Get Users Saved Albums Request received at: " + dtf.format(LocalDateTime.now()));

        GetCurrentUsersSavedAlbumsRequest getCurrentUsersSavedAlbumsRequest = spotifyApi.getCurrentUsersSavedAlbums()
                .limit(10).offset(0).build();

        Paging<SavedAlbum> savedAlbumPaging = getCurrentUsersSavedAlbumsRequest.execute();
        ArrayList<SavedAlbum> savedAlbums = new ArrayList<>();
        for(SavedAlbum album: savedAlbumPaging.getItems()) {
            savedAlbums.add(album);
        }
        return savedAlbums;
    }


    @Override
    public Playlist[] getUsersPlaylists() {
        return new Playlist[0];
    }

    @Override
    public SpotifyApi getSpotifyApi() {
        return spotifyApi;
    }

    @Override
    public AuthorizationCodeCredentials getAuthCredentials() {
        return null;
    }
}
