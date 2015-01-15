package com.tutran.aaogpa.data.local.hibernate;

import com.tutran.aaogpa.data.local.UpdateStatusDAO;
import com.tutran.aaogpa.data.models.UpdateStatus;

import java.util.List;

public class UpdateStatusDAOHibernate extends GenericDAOHibernate<UpdateStatus>
        implements UpdateStatusDAO {

    public UpdateStatusDAOHibernate() {
        super(UpdateStatus.class);
    }

    @Override
    public UpdateStatus getLastUpdate() {
        List result = getSessionFactory().getCurrentSession()
                .createQuery("FROM " + getType().getName() + " US "
                                + "ORDER BY US.updateDate DESC")
                .setMaxResults(1)
                .list();
        return result != null && result.size() == 1 ?
                (UpdateStatus) result.get(0) : null;
    }
}
