package id.ac.ui.cs.advprog.b14.pandacare.authentication.dto;

import id.ac.ui.cs.advprog.b14.pandacare.authentication.model.UserType;

public class UserDTO {
    private String id;
    private String email;
    private String name;
    private UserType type;
    private String address;
    private String phone;

    public UserDTO() {
    }

    public UserDTO(String id, String email, String name, UserType type, String address, String phone) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.type = type;
        this.address = address;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
