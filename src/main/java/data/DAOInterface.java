package data;

import java.util.List;

public interface DAOInterface<T> {


    List<T> getList();

    T get();

    int insert(T object);

    int delete();

    T update();


}
