package models;

/**
 * Created by Kevin on 12/2/2017.
 */
public class StationFlow {
    private String stationName;
    private Integer passengersIn;
    private Integer passengersOut;
    private Integer flow;
    private Double revenue;

    public StationFlow(String stationName, Integer passengersIn, Integer passengersOut, Integer flow, Double revenue) {
        this.stationName = stationName;
        this.passengersIn = passengersIn;
        this.passengersOut = passengersOut;
        this.flow = flow;
        this.revenue = revenue;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public Integer getPassengersIn() {
        return passengersIn;
    }

    public void setPassengersIn(Integer passengersIn) {
        this.passengersIn = passengersIn;
    }

    public Integer getPassengersOut() {
        return passengersOut;
    }

    public void setPassengersOut(Integer passengersOut) {
        this.passengersOut = passengersOut;
    }

    public Integer getFlow() {
        return flow;
    }

    public void setFlow(Integer flow) {
        this.flow = flow;
    }

    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }
}
