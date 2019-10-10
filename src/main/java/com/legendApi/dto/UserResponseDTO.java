package com.legendApi.dto;

import com.legendApi.models.User;
import com.legendApi.models.entities.UserEntity;

public class UserResponseDTO {
    public UserResponseDTO() { }

    public UserResponseDTO(UserEntity user) {
        setUsername(user.getUsername());
        setFirstName(user.getFirstName());
        setLastName(user.getLastName());
        setEmailAddress(user.getEmailAddress());
        setRoles(user.getStringRoles());
        setIsActive(user.getIsActive());
    }

    private String username;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String[] stringRoles;
    private boolean isActive;

    public String getUsername() {
        return username;
    }
    public void setUsername(String value) {username = value;}

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

    public String[] getRoles() {return stringRoles;}
    public void setRoles(String[] value) {stringRoles = value;}

    public boolean getIsActive() {return  isActive;}
    public void  setIsActive(boolean value) {isActive = value;}
}
