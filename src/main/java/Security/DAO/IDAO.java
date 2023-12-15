package Security.DAO;

import java.util.List;

public interface IDAO <T, E> {
    public abstract List<E> getAll(Class<E> eClass);
    public abstract E getById(int id, Class<E> eClass);
    public abstract E getByName(String name, Class<E> eClass);
    public abstract E create(T t);
    public abstract E update(T t);
    public abstract boolean delete(int id, Class<E> eClass);
}
