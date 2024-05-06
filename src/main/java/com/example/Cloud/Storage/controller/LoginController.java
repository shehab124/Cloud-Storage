package com.example.Cloud.Storage.controller;

import com.example.Cloud.Storage.model.LoginForm;
import com.example.Cloud.Storage.service.AuthenticationService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/login")
public class LoginController {

    private AuthenticationService authenticationService;

    public LoginController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public String getLogin(Model model, LoginForm loginForm,
                           @RequestParam(value = "invalid", required = false) String invalid,
                           @RequestParam(value = "logout", required = false) String logout
                           )
    {
        if(logout != null)
            model.addAttribute("loggedOut", true);
        if(invalid != null)
            model.addAttribute("invalid", true);
        return "login";
    }

//    @PostMapping
//    public String login(Model model, @ModelAttribute("loginForm") LoginForm loginForm)
//    {
//        Authentication authentication = new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword());
//        try
//        {
//            Authentication auth = authenticationService.authenticate(authentication);
//            if(auth == null)
//            {
//                model.addAttribute("invalid", true);
//                return "login";
//            }
//            else
//                return "home";
//        }
//        catch (AuthenticationException e)
//        {
//            return "login";
//        }
//    }
}
