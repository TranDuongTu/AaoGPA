package com.tutran.aaogpa.data.local.jpa;

import com.tutran.aaogpa.data.local.GenericDAO;
import com.tutran.aaogpa.data.models.DomainObject;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class GenericDAOJpa<T extends DomainObject> implements GenericDAO<T> {
    private Class<T> type;
    private EntityManager entityManager;

    public Class<T> getType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        return entityManager.createQuery(
                "SELECT o FROM " + type.getName() + " o"
        ).getResultList();
    }

    @Override
    public T get(long id) {
        return entityManager.find(type, id);
    }

    @Override
    public void save(T object) {
        entityManager.persist(object);
    }

    @Override
    public void delete(T object) {
        entityManager.remove(object);
    }

    @Override
    public void deleteAll() {
        entityManager.createQuery(
                "DELETE FROM " + type.getName()
        ).executeUpdate();
    }

    @Override
    public int size() {
        return 0;
    }
}
