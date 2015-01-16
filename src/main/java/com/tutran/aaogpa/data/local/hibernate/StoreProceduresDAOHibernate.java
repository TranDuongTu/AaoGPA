package com.tutran.aaogpa.data.local.hibernate;

import com.tutran.aaogpa.data.local.StoreProceduresDAO;
import com.tutran.aaogpa.data.models.DomainObject;
import org.hibernate.Criteria;
import org.hibernate.Query;

import java.util.List;
import java.util.Map;

public class StoreProceduresDAOHibernate
        extends GenericDAOHibernate<DomainObject>
        implements StoreProceduresDAO {

    public StoreProceduresDAOHibernate() {
        super(DomainObject.class);
    }

    @Override
    public void saveOrUpdate(DomainObject object) {
        // Do nothing
    }

    @Override
    public DomainObject get(long id) {
        return null;
    }

    @Override
    public List<DomainObject> getAll() {
        return null;
    }

    @Override
    public void delete(DomainObject object) {
        // Do nothing
    }

    @Override
    public void deleteAll() {
        // Do nothing
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Map> callStoreProcedure(
            String spName, Map<String, ?> params) {
        StringBuilder paramNames = new StringBuilder("(");
        for (String param : params.keySet()) {
            paramNames.append(":").append(param).append(",");
        }
        paramNames.replace(paramNames.length() - 1, paramNames.length(), ")");


        Query query = getSessionFactory().getCurrentSession()
                .createSQLQuery("CALL " + spName + paramNames)
                .setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
        for (String param : params.keySet()) {
            query.setParameter(param, params.get(param));
        }

        return query.list();
    }
}
