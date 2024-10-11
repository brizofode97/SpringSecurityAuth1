package com.Opencassrooms.SpringSecurityAuth1.recueil;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @GetMapping(path = "/user")
    public String getUser(){
        return "Welcome, User";
    }

    @GetMapping(path = "/admin")
    public String getAdmin(){
        return "Welcome, Admin";
    }

}
