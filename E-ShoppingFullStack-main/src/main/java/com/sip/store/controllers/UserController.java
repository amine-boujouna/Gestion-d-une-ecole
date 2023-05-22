package com.sip.store.controllers;

import com.sip.store.entities.Departement;
import com.sip.store.entities.Niveau;
import com.sip.store.entities.Role;
import com.sip.store.entities.User;
import com.sip.store.repositories.DepartementRepository;
import com.sip.store.repositories.RoleRepository;
import com.sip.store.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/users/")
public class UserController {

    private final UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private RoleRepository roleRepository;

    @Autowired
    public UserController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder=bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
    }



    @GetMapping("list")
    //@ResponseBody
    public String listUser(Model model) {


        model.addAttribute("user", userRepository.findAll());

        return "users/listUser";

    }

    @GetMapping("add")
    public String showAddUserForm(Model model) {
        User user = new User();// object dont la valeur des attributs par defaut
        model.addAttribute("user", user);
        return "users/addUser";
    }

    @PostMapping("add")
    public String addUser(@Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return "users/addUser";
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(0);
        Role userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        userRepository.save(user);
        return "redirect:list";
    }


    @Transactional
    @GetMapping("delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
/*
        User user = userRepository.findById(id).
                orElseThrow(()-> new IllegalArgumentException("Invalid user Id:" + id));

        userRepository.delete(user);*/
        userRepository.deleteuser(id);
        return "redirect:../list";
    }


    @GetMapping("edit/{id}")
    public String showUserFormToUpdate(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Invalid user Id:" + id));

        model.addAttribute("user", user);

        return "users/updateUser";
    }





    @PostMapping("update/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            user.setId(id);
            return "users/updateUser";
        }
        userRepository.save(user);
        return "redirect:../list";

    }


    @GetMapping("show/{id}")
    public String showUser(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        return "users/showUser";
    }

}

