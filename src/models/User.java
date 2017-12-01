package models;

/**
 * Created by Kevin on 12/1/2017.
 */
public class User {
    private String username;
    private Boolean isAdmin;
    private String email;
    private static User currentUser = null;

    public User(String username, Boolean isAdmin, String email) {
        this.username = username;
        this.isAdmin = isAdmin;
        this.email = email;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
