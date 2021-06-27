package network;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.specification.*;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.data.library.GetCurrentUsersSavedAlbumsRequest;
import com.wrapper.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;
import com.wrapper.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;
import org.apache.hc.core5.http.ParseException;

import java.io.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ApiConnectorInterfaceImpl implements ApiConnectorInterface {

    private final Logger logger = Logger.getLogger(AppServer.class.getName());
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    private SpotifyApi spotifyApi;

    private ApiConnectorInterfaceImpl() {
        try (InputStream inputStream = ApiConnectorInterface.class.getClassLoader()
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
            this.spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            this.spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
        } catch (ParseException | SpotifyWebApiException | IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        assert authorizationCodeCredentials != null;
        // End access token process

    }

    /**
     * Generate the Authorization Code Credentials using the Authentication Code
     * @param authCode String value code sent to Callback URL from Spotify as query parameter in request
     * @return AuthorizationCodeCredentials object if code valid, null otherwise.
     */
    private AuthorizationCodeCredentials getAccessRefreshTokens(String authCode)
            throws ParseException, SpotifyWebApiException, IOException {
        // We use the Auth Code that we retrieved previously in order to obtain the access and refresh
        // tokens.
        // Start Access and Refresh Token Process
        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi
                .authorizationCode(authCode).build();

        AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();
        spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
        spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

        logger.log(Level.CONFIG, "Auth Code Expiration time: " + authorizationCodeCredentials.getExpiresIn());

        return authorizationCodeCredentials;
        // End Access and Refresh Token Process
    }

    @Override
    public User getUserData() throws SpotifyWebApiException, IOException, ParseException {
        logger.log(Level.INFO, "Grabbing user data");
        GetCurrentUsersProfileRequest getCurrentUsersProfileRequest = spotifyApi.getCurrentUsersProfile().build();
        return getCurrentUsersProfileRequest.execute();
    }

    @Override
    public List<SavedAlbum> getUsersSavedAlbums(int limit, int offset)
            throws ParseException, SpotifyWebApiException, IOException {
        logger.log(Level.INFO, "Get Users Saved Albums Request received at: " + dtf.format(LocalDateTime.now()));

        GetCurrentUsersSavedAlbumsRequest getCurrentUsersSavedAlbumsRequest = spotifyApi.getCurrentUsersSavedAlbums()
                .limit(limit).offset(offset).build();

        Paging<SavedAlbum> savedAlbumPaging = getCurrentUsersSavedAlbumsRequest.execute();
        return Arrays.stream(savedAlbumPaging.getItems()).collect(Collectors.toList());
    }

    @Override
    public List<PlaylistSimplified> getUsersPlaylists(int limit, int offset)
            throws ParseException, SpotifyWebApiException, IOException {
        logger.log(Level.INFO, "Getting the users playlists");

        GetListOfCurrentUsersPlaylistsRequest playlistRequest = spotifyApi.getListOfCurrentUsersPlaylists()
                .limit(limit).offset(offset).build();
        Paging<PlaylistSimplified> playlistPaging = playlistRequest.execute();

        return Arrays.stream(playlistPaging.getItems()).collect(Collectors.toList());
    }

    @Override
    public SpotifyApi getSpotifyApi() {
        return spotifyApi;
    }

    public static ApiConnectorInterfaceImpl create() {
        return new ApiConnectorInterfaceImpl();
    }

    public static void main(String[] args) {
        try(PrintWriter printWriter = new PrintWriter(
                new BufferedWriter
                        (new FileWriter("Textfile.txt", true)))) {
            printWriter.append("Some words");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
