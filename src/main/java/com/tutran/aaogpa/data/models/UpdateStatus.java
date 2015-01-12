package com.tutran.aaogpa.data.models;

public class UpdateStatus extends DomainObject {
    private long updateDate;
    private boolean status;

    public long getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(long updateDate) {
        this.updateDate = updateDate;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
