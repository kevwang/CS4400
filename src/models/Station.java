package models;

/**
 * Created by Kevin on 12/2/2017.
 */
public class Station {
    private String stopId;
    private String name;
    private Double fare;
    private Boolean closed;
    private Boolean isTrain;

    public Station(String s, String n, Double f, Boolean c, Boolean i) {
        stopId = s;
        name = n;
        fare = f;
        closed = c;
        isTrain = i;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getFare() {
        return fare;
    }

    public void setFare(Double fare) {
        this.fare = fare;
    }

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    public Boolean getTrain() {
        return isTrain;
    }

    public void setTrain(Boolean train) {
        isTrain = train;
    }
}