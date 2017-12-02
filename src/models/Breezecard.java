package models;

/**
 * Model for breezecard, user is a string
 * Created by Kevin on 12/2/2017.
 */
public class Breezecard {
    private String cardNumber;
    private Double value;
    private String user;

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
        return this.getCardNumber();
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
}
