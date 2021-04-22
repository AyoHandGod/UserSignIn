package data;

import java.util.UUID;

/**
 * User Model Class. Represents a single users information
 * @author Dante
 * @version 1.0.2
 */
public class User {

    private int id;
    private String name;
    private String email;
    private String password;

    public User() {}

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public User(UserBuilder userBuilder) {
        this.password = userBuilder.password;
        this.email = userBuilder.email;
        this.name = userBuilder.name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static class UserBuilder {
        private int id;
        private String name;
        private String email;
        private String password;

        UserBuilder setName(String name) {
            this.name = name;
            return this;
        }

        UserBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        UserBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        User build() {
            return new User(this);
        }
    }
}
