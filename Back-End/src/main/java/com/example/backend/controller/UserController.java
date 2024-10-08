package com.example.backend.controller;

import com.example.backend.entity.User;
import com.example.backend.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserService userService;


    @PostConstruct
    public void initRolesAndUsers(){
        userService.initRolesAndUsers();
    }

    @PostMapping("/register")
    public User register(@RequestBody User user){
        return userService.register(user);
    }

    @GetMapping({"/admin"})
    @PreAuthorize("hasRole('ADMIN')")
    public String forAdmin(){
        return "This url for admin";
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public String forUser(){
        return "This url for user";
    }
}
