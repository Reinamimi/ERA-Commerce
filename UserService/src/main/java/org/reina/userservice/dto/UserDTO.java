package org.reina.userservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    public String userName;
    public String password;
    public List<String> roles;
}
