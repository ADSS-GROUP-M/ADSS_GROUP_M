package DataAccessLayer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

public abstract class DAO<DTO extends Serializable> {
    private Class<DTO> clazz;

    @PersistenceContext(unitName = "entityManagerFactory")
    private EntityManager entityManager;

    public final void setClazz(final Class<DTO> clazzToSet) {
        this.clazz = clazzToSet;entityManager.
    }

    public T findOne(final long id) {
        return entityManager.find(clazz, id);
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return entityManager.createQuery("from " + clazz.getName()).getResultList();
    }

    public T create(final T entity) {
        entityManager.persist(entity);
        return entity;
    }

    public T update(final T entity) {
        return entityManager.merge(entity);
    }

    public void delete(final T entity) {
        entityManager.remove(entity);
    }

    public void deleteById(final long entityId) {
        final T entity = findOne(entityId);
        delete(entity);
    }
}
//credit to baeldung https://www.baeldung.com/simplifying-the-data-access-layer-with-spring-and-java-generics