package models;

import java.util.Date;

/**
 * Models a trip...!
 *
 * Created by Kevin on 12/2/2017.
 */
public class Trip {
    private Double tripFare;
    private Date startTime;
    private String cardNum;
    private String startsAt;
    private String endsAt;

    public Trip(Double tripFare, Date startTime, String cardNum, String startsAt, String endsAt) {
        this.tripFare = tripFare;
        this.startTime = startTime;
        this.cardNum = cardNum;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
    }

    public Double getTripFare() {
        return tripFare;
    }

    public void setTripFare(Double tripFare) {
        this.tripFare = tripFare;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(String startsAt) {
        this.startsAt = startsAt;
    }

    public String getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(String endsAt) {
        this.endsAt = endsAt;
    }
}
