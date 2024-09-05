package org.library.model;

public class User {
    private Long id;
    private String name;
    private String email;
    private String userType;

    // Constructors
    public User() {
        // No-argument constructor
    }

    public User(String name, String email, String userType) {
        this.name = name;
        this.email = email;
        this.userType = userType;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }
}
