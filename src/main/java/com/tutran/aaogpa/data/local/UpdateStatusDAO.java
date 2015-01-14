package com.tutran.aaogpa.data.local;

import com.tutran.aaogpa.data.models.UpdateStatus;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UpdateStatusDAO extends GenericDAO<UpdateStatus> {
    UpdateStatus getLastUpdate();
}
