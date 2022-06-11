package com.ensah.core.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin")
public class ExceptionRedirectController {

    @GetMapping("/error")
    public  String  redirectErrorInscription(){
        return  "redirect:error";
    }
}
