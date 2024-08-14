package me.emma.paymentservice.pojo.dto;

import lombok.Data;

import java.util.List;

@Data
public class AuthenticateDTO {
    public String userName;
    public String password;
    public List<String> roles;
}