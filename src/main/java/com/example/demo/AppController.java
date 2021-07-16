package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AppController {

    @Autowired
    private UserService service;

    @Autowired
    private UserRepository repo;

    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("/register")
    public String showSignUpForm(Model model) {
        model.addAttribute("user",new User());
        return "signup_form";
    }

    @PostMapping("/process_register")
    public String processRegistration(User user){
        service.saveUserWithDefaultRole(user);
        return "register_success";
    }

    @GetMapping("/list_users")
    public String viewUsersList(Model model) {
        List<User> listUsers = service.listAll();
        model.addAttribute("listUsers",listUsers);

        return "users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model, RedirectAttributes ra) {
        try{
            User user = service.get(id);
            List<Role> listRoles = service.getRoles();
            model.addAttribute("user",user);
            model.addAttribute("listRoles",listRoles);
            model.addAttribute("pageTitle","Edit User (ID: "+id +")");
            return "user_form";
        } catch (UserNotFoundException e) {
            ra.addFlashAttribute("message", "The user has benn saved successfully.");
            e.printStackTrace();
            return "redirect:/list_users";
        }
    }

    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes ra) {
          service.save(user);
          ra.addFlashAttribute("message","The user has benn saved successfully.");
          return "redirect:/list_users";
    }

    @GetMapping("/users/new")
    public String showNewForm(Model model) {
        model.addAttribute("user",new User());
        List<Role> listRoles = service.getRoles();
        model.addAttribute("listRoles",listRoles);
        model.addAttribute("pageTitle","Add New User");
          return "new_user_form";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes ra) {
        try{
            service.delete(id);
            ra.addFlashAttribute("message", "The user ID" + id + "has been deleted");
        } catch (UserNotFoundException e) {
            ra.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/list_users";
    }
}
