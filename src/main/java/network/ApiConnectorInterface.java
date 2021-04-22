package network;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.model_objects.specification.SavedAlbum;
import com.wrapper.spotify.model_objects.specification.User;
import org.apache.hc.core5.http.ParseException;

import java.io.IOException;
import java.util.List;

public interface ApiConnectorInterface {

    public User getUserData() throws SpotifyWebApiException, IOException, ParseException;

    public List<SavedAlbum> getUsersSavedAlbums(int limit, int offset) throws ParseException, SpotifyWebApiException,
            IOException;

    public List<PlaylistSimplified> getUsersPlaylists(int limit, int offset) throws ParseException, SpotifyWebApiException, IOException;

    public void setAuthorizationCredentials(String code);

    public SpotifyApi getSpotifyApi();

}
