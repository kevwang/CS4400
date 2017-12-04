package models;

/**
 * Model for breezecard, user is a string
 * Created by Kevin on 12/2/2017.
 */
public class Breezecard {
    private String cardNumber;
    private Double value;
    private String user;
    private boolean isSuspended;
    private String suspendedString;

    /**
     * New instance of breezecard
     *
     * @param cardNumber
     * @param value
     * @param user
     */
    public Breezecard(String cardNumber, Double value, String user) {
        this.cardNumber = cardNumber;
        this.value = value;
        this.user = user;
    }

    @Override
    public String toString() {
        String c = this.cardNumber;
        return c.substring(0, 4) + " " +
            c.substring(4, 8) + " " +
            c.substring(8, 12) + " " +
            c.substring(12, 16);
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean isSuspended() {
        return isSuspended;
    }

    public void setSuspended(boolean suspended) {
        isSuspended = suspended;
    }

    public String getSuspendedString() {
        return suspendedString;
    }

    public void setSuspendedString(String suspendedString) {
        this.suspendedString = suspendedString;
    }
}
