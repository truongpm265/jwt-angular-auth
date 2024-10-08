package com.example.backend.service;

import com.example.backend.repository.RoleRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.entity.Role;
import com.example.backend.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(User user){
        Role role = roleRepository.findById("USER").get();

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRole(roles);
        user.setUserPassword(getEncodePassword(user.getUserPassword()));

        return userRepository.save(user);
    }

    public void initRolesAndUsers(){
        Role adminRole = new Role();
        adminRole.setRoleName("ADMIN");
        adminRole.setRoleDescription("Manage everything");
        roleRepository.save(adminRole);

        Role userRole = new Role();
        userRole.setRoleName("USER");
        userRole.setRoleDescription("Default Role for new user");
        roleRepository.save(userRole);

        User adminUser = new User();
        adminUser.setUserName("Admin");
        adminUser.setUserPassword(getEncodePassword("123"));
        adminUser.setUserEmail("Admin@123");
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        adminUser.setRole(adminRoles);
        userRepository.save(adminUser);


        User user = new User();
        user.setUserName("Truong");
        user.setUserPassword(getEncodePassword("123"));
        user.setUserEmail("Truong@123");
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);
        user.setRole(userRoles);
        userRepository.save(user);
    }

    public String getEncodePassword(String password){
        return passwordEncoder.encode(password);
    }
}
