package org.reina.userservice.restController;

import org.reina.userservice.dto.UserDTO;
import org.reina.userservice.entity.User;
import org.reina.userservice.service.MyUserDetail;
import org.reina.userservice.service.MyUserDetailService;
import org.reina.userservice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    UserService userService;

    @Autowired
    MyUserDetailService myUserDetailService;

    @GetMapping("/")
    public String getHome(){
        return "Welcome to ERA-Commerce !!";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getUserHome(){
        return "Welcome to the ERA-Commerce user home !!";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getAdminHome(){
        return "Welcome to the  ERA-Commerce admin home !!";
    }

    @PostMapping("/addUser")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User createdUser = userService.addUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    public UserDTO authenticateUser(@RequestBody User user) {
        log.info("Authentication user {}",user);
        String username = user.getUserName();
        MyUserDetail userDetails = (MyUserDetail) myUserDetailService.loadUserByUsername(username);
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName(userDetails.getUsername());
        userDTO.setPassword(userDetails.getPassword());
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority) // Convert GrantedAuthority to String
                .collect(Collectors.toList());
        userDTO.setRoles(roles);
        return userDTO;
    }
}
