package ru.kata.spring.boot_security.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;

@Controller
@Slf4j
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String adminPanel(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("listOfUsers", userService.getAllUsers());
        model.addAttribute("admin", user);
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("personalRoles", user.getRolesAsString());
        return "adminHome";
    }

    @GetMapping("/info")
    public String adminInfo(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("admin", user);
        model.addAttribute("personalRoles", user.getRolesAsString());
        return "adminInfo";
    }

    @GetMapping("/new")
    public String addUserForm(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("admin", user);
        model.addAttribute("personalRoles", user.getRolesAsString());
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("user", new User());
        return "adminNewUser";
    }

    @PostMapping("/newUser")
    public String addUser(@ModelAttribute("user") User user) {
        userService.createUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/id/editUser")
    public String updateUser(@ModelAttribute("user") User user,
                             @RequestParam("id") Long id) {
        userService.updateUser(id, user);
        return "redirect:/admin";
    }

    @PostMapping("/id/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}