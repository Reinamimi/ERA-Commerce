package me.emma.orderservice.pojo.dto;


import lombok.Data;

@Data
public class UserDTO {
    private int id;
    private String userName;
    private String email;
    private String password;
    private String role;
}
