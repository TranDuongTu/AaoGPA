package com.tutran.aaogpa.data.local;

import com.tutran.aaogpa.data.models.DomainObject;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface StoreProceduresDAO extends GenericDAO<DomainObject> {
    List<Map> callStoreProcedure(String spName, Map<String, ?> params);
}
