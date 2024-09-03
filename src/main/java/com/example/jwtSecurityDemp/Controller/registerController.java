package com.example.jwtSecurityDemp.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class registerController {

    @GetMapping("/userregistration")
    public String userregistration() {
        return "adduser";
    }

    @GetMapping("/adminregistration")
    public String adminregistration() {
        return "adminregister";
    }

    @GetMapping("/login")
    public String adminlogin() {
        return "login";
    }
    @GetMapping("/GlobalResource")
    public String display()
    {
        return "userResource";
    }

    @GetMapping("/adminResource")
    public String display1()
    {
        return "adminResource";
    }
    @GetMapping("/home")
    public String home()
    {
        return "home";
    }
    @GetMapping("/display")
    public String display3()
    {
        return "ar";
    }
    @GetMapping ("/display_any")
    public String display4()
    {
        return "ur";
    }
}
