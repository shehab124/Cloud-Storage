package com.example.Cloud.Storage.controller;

import com.example.Cloud.Storage.model.SignupForm;
import com.example.Cloud.Storage.model.UserModel;
import com.example.Cloud.Storage.service.UserService;
import org.apache.catalina.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignupController {

    private final UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getSignup(SignupForm signupForm)
    {
        return "signup";
    }

    @PostMapping
    public String signup(Model model, SignupForm signupForm)
    {
        if(userService.isUsernameAvailable(signupForm.getUsername()))
        {
            userService.createUser(new UserModel(null, signupForm.getUsername(), null, signupForm.getPassword(), signupForm.getFirstName(), signupForm.getLastName()));
            model.addAttribute("success", true);
        }
        else
        {
            model.addAttribute("usernameTaken", true);
        }
        return "signup";
    }

}
