package data;

import com.wrapper.spotify.model_objects.specification.Playlist;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.stream.Stream;

public class PlaylistDAO implements DAOInterface<Playlist> {

    @Override
    public List getList() {
        return null;
    }

    @Override
    public Playlist get() {
        return null;
    }

    @Override
    public int insert(Playlist object) {
        return 0;
    }

    @Override
    public int delete() {
        return 0;
    }

    @Override
    public Playlist update() {
        return null;
    }
}
