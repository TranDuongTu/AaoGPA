package com.tutran.aaogpa.data.local.hibernate;

import com.tutran.aaogpa.data.local.GenericDAO;
import com.tutran.aaogpa.data.models.DomainObject;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GenericDAOHibernate<T extends DomainObject>
        implements GenericDAO<T> {

    private Class<T> type;
    private SessionFactory sessionFactory;

    public GenericDAOHibernate(Class<T> type) {
        this.type = type;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Class<T> getType() {
        return type;
    }

    public void setType(Class<T> type) {
        this.type = type;
    }

    @Override
    public void saveOrUpdate(T object) {
        sessionFactory.getCurrentSession().saveOrUpdate(object);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(long id) {
        return (T) sessionFactory.getCurrentSession().get(type, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("From " + type.getName())
                .list();
    }

    @Override
    public void delete(T object) {
        sessionFactory.getCurrentSession().delete(object);
    }

    @Override
    public void deleteAll() {
        sessionFactory.getCurrentSession().
                createQuery("DELETE FROM " + type.getName()).executeUpdate();
    }

    @Override
    public int count() {
        List list = sessionFactory.getCurrentSession()
                .createQuery("SELECT count(*) FROM " + type.getName())
                .list();
        return list != null && list.size() == 1 ?
                ((Long) list.get(0)).intValue() : 0;
    }
}
