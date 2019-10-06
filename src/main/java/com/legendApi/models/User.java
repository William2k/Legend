package com.legendApi.models;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String password;
    private boolean isActive;

    public User() {}

    public User(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }
    public void setId(int value) {id = value;}

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String value) {firstName = value;}

    public String getEmailAddress() {
        return emailAddress;
    }
    public void setEmailAddress(String value) {emailAddress = value;}

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String value) {lastName = value;}

    public String getPassword() {return  password;}
    public void setPassword(String value) {password = value;}

    public boolean getIsActive() {return  isActive;}
    public void  setIsActive(boolean value) {isActive = value;}

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
