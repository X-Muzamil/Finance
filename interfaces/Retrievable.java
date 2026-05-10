package Finance.interfaces;

import java.util.List;


public interface Retrievable<T> {
    List<T> getAll(int userId);
    T getById(int id);
}
