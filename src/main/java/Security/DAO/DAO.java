package Security.DAO;

import Config.HibernateConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class DAO<T, E> implements IDAO <T, E> {
    protected EntityManagerFactory emf;

    @Override
    public List<E> getAll(Class<E> eClass) {
        try (EntityManager em = emf.createEntityManager()){
            return em.createQuery("SELECT t FROM " + eClass.getSimpleName() + " t", eClass).getResultList();
        }
    }

    @Override
    public E getById(int id, Class<E> eClass) {
        try (EntityManager em = emf.createEntityManager()){
            return em.find(eClass, id);
        }
    }

    @Override
    public E getByName(String name, Class<E> eClass) {
        try (EntityManager em = emf.createEntityManager()){
            return em.createQuery("SELECT t FROM " + eClass.getSimpleName() + " t WHERE t.name = :name", eClass).setParameter("name", name).getSingleResult();
        }
    }

    @Override
    public E create(T t) {
        try (EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.persist(t);
            em.getTransaction().commit();
            return (E) t;
        }
    }

    @Override
    public E update(T t) {
        try (EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.merge(t);
            em.getTransaction().commit();
            return (E) t;
        }
    }

    @Override
    public boolean delete(int id, Class<E> eClass) {
        try (EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            E e = em.find(eClass, id);
            em.remove(e);
            em.getTransaction().commit();
            return true;
        }
    }

}
