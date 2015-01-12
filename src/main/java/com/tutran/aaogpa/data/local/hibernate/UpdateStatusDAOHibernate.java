package com.tutran.aaogpa.data.local.hibernate;

import com.tutran.aaogpa.data.local.UpdateStatusDAO;
import com.tutran.aaogpa.data.models.UpdateStatus;
import org.hibernate.Query;

import java.util.List;

public class UpdateStatusDAOHibernate extends GenericDAOHibernate<UpdateStatus>
        implements UpdateStatusDAO {

    public UpdateStatusDAOHibernate() {
        super(UpdateStatus.class);
    }

    @Override
    public UpdateStatus getLastUpdate() {
        Query query = getSessionFactory().getCurrentSession()
                .createQuery(
                    "FROM UpdateStatus US ORDER BY US.updateDate DESC"
                ).setMaxResults(1);
        List result = query.list();
        if (result == null || result.size() == 0)
            return null;
        else
            return (UpdateStatus) result.get(0);
    }
}
