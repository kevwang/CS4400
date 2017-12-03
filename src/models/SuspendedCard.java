package models;

import java.util.Date;

/**
 * Created by Kevin on 12/2/2017.
 */
public class SuspendedCard {
    private String breezecardNumber;
    private String previousOwner;
    private String newOwner;
    private Date dateSuspended;

    public SuspendedCard(String breezecardNumber, String previousOwner, String newOwner, Date dateSuspended) {
        this.breezecardNumber = breezecardNumber;
        this.previousOwner = previousOwner;
        this.newOwner = newOwner;
        this.dateSuspended = dateSuspended;
    }

    public String getBreezecardNumber() {
        return breezecardNumber;
    }

    public void setBreezecardNumber(String breezecardNumber) {
        this.breezecardNumber = breezecardNumber;
    }

    public String getPreviousOwner() {
        return previousOwner;
    }

    public void setPreviousOwner(String previousOwner) {
        this.previousOwner = previousOwner;
    }

    public String getNewOwner() {
        return newOwner;
    }

    public void setNewOwner(String newOwner) {
        this.newOwner = newOwner;
    }

    public Date getDateSuspended() {
        return dateSuspended;
    }

    public void setDateSuspended(Date dateSuspended) {
        this.dateSuspended = dateSuspended;
    }
}
