package network;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.SavedAlbum;
import com.wrapper.spotify.model_objects.specification.User;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.ArrayList;

public interface ApiConnector {

    public User getUserData() throws SpotifyWebApiException, IOException, ParseException;

    public ArrayList<SavedAlbum> getUsersSavedAlbums(int limit, int offset) throws ParseException, SpotifyWebApiException,
            IOException;

    public Playlist[] getUsersPlaylists();

    public void setAuthorizationCredentials(String code);

    public SpotifyApi getSpotifyApi();

    public AuthorizationCodeCredentials getAuthCredentials();

}
