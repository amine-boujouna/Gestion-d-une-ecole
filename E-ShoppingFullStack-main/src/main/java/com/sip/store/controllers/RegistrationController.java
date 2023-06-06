package com.sip.store.controllers;

import com.sip.store.entities.Role;
import com.sip.store.entities.User;
import com.sip.store.repositories.RoleRepository;
import com.sip.store.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;

@RestController
@CrossOrigin(origins = "*")
public class RegistrationController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @RequestMapping(value = "/Registration", method = RequestMethod.POST)
    public User createnewUser(@RequestBody User user) {


        Role userRole = roleRepository.findByRole(user.getTemp());
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));

        userService.saveuser(user);

        return user;
    }

}
