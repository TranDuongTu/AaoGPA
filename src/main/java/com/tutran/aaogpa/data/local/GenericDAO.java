package com.tutran.aaogpa.data.local;

import com.tutran.aaogpa.data.models.DomainObject;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface GenericDAO<T extends DomainObject> {
    void save(T object);
    void delete(T object);
    T get(long id);
    List<T> getAll();
    void deleteAll();
    int size();
}
